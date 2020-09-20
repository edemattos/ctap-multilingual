/**
 *
 */
package com.ctapweb.feature.annotator;

import java.util.*;

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
import com.ctapweb.feature.type.Sentence;
import com.ctapweb.feature.type.SurfaceForm;
import com.ctapweb.feature.util.SupportedLanguages;

/**
 * Annotates text with surface forms for each sentence in the input text
 * Requires the following annotations: sentences (see SurfaceFormAnnotatorTAE.xml)
 *
 * Tokenization is done using the CTAPTokenizer interface.
 * To add a new tokenizer, make sure to implement the CTAPTokenizer interface.
 *
 * @author edemattos
 */
public class SurfaceFormAnnotator extends JCasAnnotator_ImplBase {

	private CTAPTokenizer tokenizer;

	private static final String PARAM_LANGUAGE_CODE = "LanguageCode";
	private static final Logger logger = LogManager.getLogger();

	private static final AEType aeType = AEType.ANNOTATOR;
	private static final String aeName = "SurfaceForm Annotator";

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
		super.initialize(aContext);

		// define the model to be loaded based on the mandatory LanguageCode config parameter
		String lCode = "";
		if(aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
					new Object[] {PARAM_LANGUAGE_CODE});
			logger.throwing(e);
			throw e;
		} else {
			lCode = ((String) aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE)).toUpperCase();
		}

        switch (lCode) {
            case SupportedLanguages.PORTUGUESE:
                tokenizer = new StanzaTokenizer();
                break;
        }

		logger.trace(LogMarker.UIMA_MARKER, new InitializeAECompleteMessage(aeType, aeName));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.
	 * apache.uima.jcas.JCas)
	 */
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		logger.trace(LogMarker.UIMA_MARKER,
				new ProcessingDocumentMessage(aeType, aeName, aJCas.getDocumentText()));

		// Get document text
		String docText = aJCas.getDocumentText();

		// get annotation indexes and iterator
		Iterator sentIter = aJCas.getAnnotationIndex(Sentence.type).iterator();

        while (sentIter.hasNext()) {
			Sentence sent = (Sentence) sentIter.next();

			SF[] surfaceForms = tokenizer.tokenize(sent, docText);

			//populate the CAS
			for (int i = 0; i < surfaceForms.length; i++) {
				SurfaceForm annotation = new SurfaceForm(aJCas);
				annotation.setBegin(surfaceForms[i].getBegin());
				annotation.setEnd(surfaceForms[i].getEnd());
				annotation.setSurfaceForm(surfaceForms[i].getForm());
				annotation.addToIndexes();
			}
		}
	}

	@Override
	public void destroy() {
		logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));
		super.destroy();
		logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
	}

	/**
	 * Interface for tokenizer; acts as wrapper for any tokenizer that may be
	 * added to support new languages or to change existing parsing components.
	 * @author zweiss
	 */
	interface CTAPTokenizer {
		abstract SF[] tokenize(Sentence sent, String doc);
	}

	/**
	 * Wrapper for Stanza by Stanford NLP (https://stanfordnlp.github.io/stanza/)
	 * For each text, an HTTP request is made to a containerized Python web service
	 * API: https://github.com/lingmod-tue/stanza-api
	 * Java implementation: https://github.com/lingmod-tue/stanza-java
	 *
	 * @author edemattos, rziai
	 */
	private class StanzaTokenizer implements CTAPTokenizer {

		@Override
		public SF[] tokenize(Sentence sent, String doc) {

			List<SF> sf = new ArrayList<>();
			for (String s : doc.split("\n\n")[6].trim().split("\t")) {
				String[] split = s.split("//");
				if ((Integer.parseInt(split[0]) >= sent.getBegin()) && (Integer.parseInt(split[1]) <= sent.getEnd())) {
					sf.add(new SF(Integer.parseInt(split[0]), Integer.parseInt(split[1]), split[2]));
				}
			}
			return Arrays.copyOf(sf.toArray(), sf.size(), SF[].class);
		}
	}

	/**
	 * Simple SurfaceForm object
	 *
	 * @author edemattos
	 */
	private class SF {

		private int b;
		private int e;
		private String form;

		public SF (int b, int e, String form) {
			this.b = b;
			this.e = e;
			this.form = form;
		}

		public int getBegin() { return b; }
		public int getEnd() {return e; }
		public String getForm() { return form; }
	}
}
