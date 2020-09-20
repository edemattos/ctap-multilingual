/**
 *
 */
package com.ctapweb.feature.annotator;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.ctapweb.feature.logging.LogMarker;
import com.ctapweb.feature.logging.message.AEType;
import com.ctapweb.feature.logging.message.DestroyAECompleteMessage;
import com.ctapweb.feature.logging.message.DestroyingAEMessage;
import com.ctapweb.feature.logging.message.InitializeAECompleteMessage;
import com.ctapweb.feature.logging.message.InitializingAEMessage;
import com.ctapweb.feature.logging.message.ProcessingDocumentMessage;
import com.ctapweb.feature.logging.message.SelectingLanguageSpecificResource;
import com.ctapweb.feature.type.Syllable;
import com.ctapweb.feature.type.SurfaceForm;
import com.ctapweb.feature.util.SupportedLanguages;


/**
 * Annotates text with syllables for each surface form in the input text
 * Requires the following annotations: sentences, surface form (see SyllableAnnotatorTAE.xml)
 *
 * Syllable annotation is done using SyllablePatterns. 
 * To add a new syllable annotation logic, create a new SyllablePattern.
 *
 * @author xiaobin
 *
 * zweiss 20/12/18 : added new syllable structures
 * edemattos 25/07/20 : migrated from Token to SurfaceForm
 */
public class SyllableAnnotator extends JCasAnnotator_ImplBase {

	private JCas aJCas;
	private SurfaceForm surfaceForm;
	private String syllablePattern;
	private boolean considerSilentE = false;
	private static final String PARAM_LANGUAGE_CODE = "LanguageCode";
	private static final Logger logger = LogManager.getLogger();
	private static final AEType aeType = AEType.ANNOTATOR;
	private static final String aeName = "Syllable Annotator";

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
		super.initialize(aContext);

		// obtain language parameter and access language dependent resources
		String lCode = "";
		if(aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
					new Object[] {PARAM_LANGUAGE_CODE});
			logger.throwing(e);
			throw e;
		} else {
			lCode = ((String) aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE)).toUpperCase();
		}

		logger.trace(LogMarker.UIMA_MARKER, new SelectingLanguageSpecificResource(aeName, lCode));
		switch (lCode) {
		case SupportedLanguages.GERMAN:
			syllablePattern = SyllablePatterns.GERMAN;
			break;
		case SupportedLanguages.DUTCH:
			syllablePattern = SyllablePatterns.DUTCH;
			break;
		case SupportedLanguages.ENGLISH:
			syllablePattern = SyllablePatterns.ENGLISH;
			considerSilentE = true;
			break;
		case SupportedLanguages.PORTUGUESE:
			syllablePattern = SyllablePatterns.PORTUGUESE;
			break;
			// add new language here
		default: // TODO reconsider default German
			syllablePattern = SyllablePatterns.DEFAULT;
			break;
		}
		logger.trace(LogMarker.UIMA_MARKER, new InitializeAECompleteMessage(aeType, aeName));
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
	 */
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		logger.trace(LogMarker.UIMA_MARKER,
				new ProcessingDocumentMessage(aeType, aeName, aJCas.getDocumentText()));

		this.aJCas = aJCas;

		// get annotation indexes and iterator
		Iterator it = aJCas.getAnnotationIndex(SurfaceForm.type).iterator();

		//annotate syllables for each surface form
		while(it.hasNext()) {
			//get the token
			surfaceForm = (SurfaceForm)it.next();
			String surfStr = surfaceForm.getSurfaceForm().toLowerCase();

			//annotate syllables
			//solution from http://stackoverflow.com/questions/33425070/how-to-calculate-syllables-in-text-with-regex-and-java
			if (considerSilentE && surfStr.charAt(surfStr.length()-1) == 'e') {
				if (silente(surfStr)){  //silent e, so don't annotate.
					String newSurfaceForm = surfStr.substring(0, surfStr.length()-1); //deal with the rest of word
					annotateSyllables(newSurfaceForm);
				} else {
					//not silent e, annotate it as a syllable
					Syllable annotation = new Syllable(aJCas);
					annotation.setBegin(surfaceForm.getBegin());
					annotation.setEnd(surfaceForm.getEnd());
					annotation.addToIndexes();
					//logger.info("syllable: " + annotation.getBegin() + ", " + annotation.getEnd() + " "  + annotation.getCoveredText());
				}
			} else {
				annotateSyllables(surfStr);
			}
		}
	}

	/**
	 * Annotates the syllables in the token string.
	 *
	 * @param surfStr the token string to be annotated
	 * @return
	 */
	private void annotateSyllables(String surfStr) {
		Pattern splitter = Pattern.compile(syllablePattern);
		Matcher m = splitter.matcher(surfStr);

		while (m.find()) {
			//finds a syllable
			Syllable annotation = new Syllable(aJCas);
			annotation.setBegin(m.start() + surfaceForm.getBegin());
			annotation.setEnd(m.end() + surfaceForm.getBegin());
			annotation.addToIndexes();
			//logger.info("syllable: " + annotation.getBegin() + ", " + annotation.getEnd() + " \'"  + annotation.getCoveredText()+"\'");
		}
	}

	private boolean silente(String word) {
		word = word.substring(0, word.length()-1);
		Pattern yup = Pattern.compile("[aeiouy]");
		Matcher m = yup.matcher(word);
		return m.find();
	}

	@Override
	public void destroy() {
		logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));
		super.destroy();
		logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
	}

	/*
	 * Defines syllable patterns which may be chosen upon initialization based on the language parameter transported in the UimaContext
	 * @author zweiss
	 * TODO reconsider usage
	 */
	public class SyllablePatterns {
		public static final String ENGLISH = "[^aeiouy]*[aeiouy]+";  // pattern by Xiaobin Chen
		// German syllables: each vowel indicates its own syllable unless it is followed by a) itself or b) e i u y
		public static final String GERMAN = "[^aeiouöüäAEIOUÖÜÄ]*([aeiouöüäyAEIOUÖÜÄY])([eiuy]|\1)?[^aeiouöüäAEIOUÖÜÄ]*";  // pattern by Zarah Weiss
		public static final String DUTCH = "[^aeiouéèöüäëïAEIOUÖÜÄ]*([aeiouéèöüäëïyAEIOUÖÜÄY])([aeiuy]|\\1){0,2}[^aeiouéèöüäëïAEIOUÖÜÄ]*";  // pattern by Rachel Rubin
		public static final String PORTUGUESE = "(?:ã[eio]?|õe?|ôo?|u[ae]?i?|[aáâeéê][iou]?|[oó][iou]?|[ií]u?)"; // pattern by Eric DeMattos
		public static final String DEFAULT = SyllablePatterns.GERMAN;
	}
}
