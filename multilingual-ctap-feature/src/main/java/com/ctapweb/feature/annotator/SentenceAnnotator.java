/**
 * 
 */
package com.ctapweb.feature.annotator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import com.ctapweb.feature.exception.CTAPException;
import com.ctapweb.feature.logging.LogMarker;
import com.ctapweb.feature.logging.message.AEType;
import com.ctapweb.feature.logging.message.DestroyAECompleteMessage;
import com.ctapweb.feature.logging.message.DestroyingAEMessage;
import com.ctapweb.feature.logging.message.InitializeAECompleteMessage;
import com.ctapweb.feature.logging.message.InitializingAEMessage;
import com.ctapweb.feature.logging.message.LoadLangModelMessage;
import com.ctapweb.feature.logging.message.ProcessingDocumentMessage;
import com.ctapweb.feature.type.Sentence;
import com.ctapweb.feature.util.SupportedLanguages;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * Annotates text with POS tags for each token in the input text
 * Requires the following annotations: sentences, tokens (see POSAnnotatorTAE.xml)
 * 
 * POS tagging is done using the POSTagger interface. 
 * To add a new POS tagger, make sure to implement the POSTagger interface.
 * 
 * @author xiaobin
 * 
 * Change log:
 * zweiss 18/12/18:	switch between resource keys based on UimaContext information on language
 */
public class SentenceAnnotator extends JCasAnnotator_ImplBase {

	private SentenceSegmenter segmenter;

	private static final String PARAM_LANGUAGE_CODE = "LanguageCode";
	private static final String RESOURCE_KEY = "SentenceSegmenterModel";
	private static final Logger logger = LogManager.getLogger();

	private static final AEType aeType = AEType.ANNOTATOR;
	private static final String aeName = "Sentence Annotator";
	/**
	 * Loads the appropriate sentence detector model.
	 */
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));

		super.initialize(aContext);
		String modelFilePath = null;

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

		// gets the model resource, which is declared in the annotator xml
		String languageSpecificResourceKey = RESOURCE_KEY+lCode;
		try {
			modelFilePath = getContext().getResourceFilePath(languageSpecificResourceKey);

			// Stanza models are currently being hosted by the web service
			if (modelFilePath != null) {
				logger.trace(LogMarker.UIMA_MARKER,
						new LoadLangModelMessage(languageSpecificResourceKey, modelFilePath));
			}

			switch (lCode) {
				case SupportedLanguages.PORTUGUESE:
					segmenter = new StanzaSentenceSegmenter();
					break;
				case SupportedLanguages.GERMAN:
				case SupportedLanguages.DUTCH:
				case SupportedLanguages.ENGLISH:
					segmenter = new OpenNLPSentenceSegmenter(modelFilePath);
					break;
			}
		} catch (ResourceAccessException e) {
			logger.throwing(e);
			throw new ResourceInitializationException("could_not_access_data",
					new Object[] {modelFilePath}, e);
		} catch (InvalidFormatException e) {
			logger.throwing(e);
			throw new ResourceInitializationException(CTAPException.EXCEPTION_DIGEST, 
					"incorrect_lang_model_format",
					new Object[] {modelFilePath}, e);
		} catch (IOException e) {
			logger.throwing(e);
			throw new ResourceInitializationException(CTAPException.EXCEPTION_DIGEST, 
					"file_io_error",
					new Object[] {modelFilePath}, e);
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
		// Get document text
		String docText = aJCas.getDocumentText();

		logger.trace(LogMarker.UIMA_MARKER, 
				new ProcessingDocumentMessage(aeType, aeName, docText));

		// Detect sentence spans
		Span[] spans = segmenter.segment(docText);

		for (Span span : spans) {
			Sentence annotation = new Sentence(aJCas);
			// System.out.println("begin: " + span.getStart() +" end: " +
			// span.getEnd());
			annotation.setBegin(span.getStart());
			annotation.setEnd(span.getEnd());
			annotation.addToIndexes();
			//logger.info("sentence: " + annotation.getBegin() + ", " + annotation.getEnd() + " "  + annotation.getCoveredText());
		}

	}


	@Override
	public void destroy() {
		logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));

		super.destroy();

		logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
	}

	/**
	 * Interface for sentence segmenter; acts as wrapper for any sentence segmenter that may be 
	 * added to support new languages or to change existing parsing components.
	 * @author zweiss
	 */
	interface SentenceSegmenter {
		abstract Span[] segment(String text);
	}

	/**
	 * Wrapper for use of OpenNLP sentence segmenter (https://opennlp.apache.org/)
	 * @author zweiss
	 *
	 */
	private class OpenNLPSentenceSegmenter implements SentenceSegmenter {
		
		private InputStream modelIn;
		private SentenceModel openNlpModel;
		private SentenceDetectorME openNlpSentenceDetector;
		
		public OpenNLPSentenceSegmenter(String modelInFile) throws IOException {
			modelIn = new FileInputStream(new File(modelInFile));
			openNlpModel = new SentenceModel(modelIn);
			openNlpSentenceDetector = new SentenceDetectorME(openNlpModel);
			modelIn.close();
		}

		@Override
		public Span[] segment(String text) {
			return openNlpSentenceDetector.sentPosDetect(text);
		}
	}

	/**
	 * Wrapper for Stanza by Stanford NLP (https://stanfordnlp.github.io/stanza/)
	 * For each text, an HTTP request is made to a containerized Python web service
	 * API: https://github.com/lingmod-tue/stanza-api
	 * Java implementation: https://github.com/lingmod-tue/stanza-java
	 *
	 * @author edemattos, rziai
	 */
	private class StanzaSentenceSegmenter implements SentenceSegmenter {

		@Override
		public Span[] segment(String text) {

			List<Span> spans = new ArrayList<>();
			for (String span : text.split("\n\n")[1].trim().split("\t")) {
				int b = Integer.parseInt(span.split("-")[0]);
				int e = Integer.parseInt(span.split("-")[1]);
				spans.add(new Span(b, e));
			}
			return Arrays.copyOf(spans.toArray(), spans.size(), Span[].class);
		}
	}
}
