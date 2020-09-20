package com.ctapweb.feature.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import com.ctapweb.feature.type.Lemma;
import com.ctapweb.feature.type.Sentence;
import com.ctapweb.feature.type.Token;
import com.ctapweb.feature.util.SupportedLanguages;

import is2.data.SentenceData09;
import is2.lemmatizer.Lemmatizer;

/**
 * Annotates text with lemmas for each word in the input text
 * Requires the following annotations: sentences, tokens (see LemmaAnnotatorTAE.xml)
 * 
 * Lemmatization is done using the CTAPLemmatizer interface. 
 * To add a new lemmatizer, make sure to implement the CTAPLemmatizer interface.
 * 
 * @author zweiss
 *
 */
public class LemmaAnnotator extends JCasAnnotator_ImplBase {

	//for pos tagger
	private CTAPLemmatizer lemmatizer;
	public static String LEMMA_RESOURCE_KEY = "LemmaModel";

	private static final String PARAM_LANGUAGE_CODE = "LanguageCode";
	private static final Logger logger = LogManager.getLogger();
	private static final AEType aeType = AEType.ANNOTATOR;
	private static final String aeName = "Lemma Annotator";

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
		super.initialize(aContext);

		String lemmaModelFilePath = null;
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

		//init lemmatizer
		String languageSpecificResourceKey = LEMMA_RESOURCE_KEY+lCode;
		try {
			lemmaModelFilePath = getContext().getResourceFilePath(languageSpecificResourceKey);

			// Stanza models are currently being hosted by the web service
			if (lemmaModelFilePath != null) {
				logger.trace(LogMarker.UIMA_MARKER,
						new LoadLangModelMessage(languageSpecificResourceKey, lemmaModelFilePath));
			}

			switch (lCode) {
				case SupportedLanguages.PORTUGUESE:
					lemmatizer = new StanzaLemmatizer();
					break;
				case SupportedLanguages.GERMAN: // German as default
					lemmatizer = new MateLemmatizer(lemmaModelFilePath);
					break;
			}

		} catch (ResourceAccessException e) {
			logger.throwing(e);
			throw new ResourceInitializationException("could_not_access_data",
					new Object[] {lemmaModelFilePath}, e);
		} catch (IOException e) {
			logger.throwing(e);
			throw new ResourceInitializationException(CTAPException.EXCEPTION_DIGEST, 
					"file_io_error",
					new Object[] {lemmaModelFilePath}, e);
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

		//iterate through all sentences
		Iterator sentIter = aJCas.getAnnotationIndex(Sentence.type).iterator();
		while (sentIter.hasNext()) {
			Sentence sent = (Sentence) sentIter.next();

			//iterate through all tokens
			List<Token> sentTokens = new ArrayList<>();
			Iterator tokenIter = aJCas.getAnnotationIndex(Token.type).iterator(false);
			while(tokenIter.hasNext()) {
				Token token = (Token) tokenIter.next();
				if(token.getBegin() >= sent.getBegin() && token.getEnd() <= sent.getEnd()) {
					sentTokens.add(token);
				}
			}

			//get lemmas
			String[] lemmas = lemmatizer.lemmatize(sentTokens, docText);

			//populate the CAS
			for(int i = 0; i < sentTokens.size(); i++) {
				Token token = sentTokens.get(i);
				if (token.getBegin() >= sent.getBegin() && token.getEnd() <= sent.getEnd()) {
					Lemma annotation = new Lemma(aJCas);
					//logger.trace(LogMarker.UIMA_MARKER, "Adding Lemma: "+token.getCoveredText()+" "+lemmas[i]);  // debugging
					annotation.setBegin(token.getBegin());
					annotation.setEnd(token.getEnd());
					annotation.setLemma(lemmas[i]);
					annotation.addToIndexes();
				}
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
	 * Interface for lemmatizer; acts as wrapper for any lemmatizer that may be 
	 * added to support new languages or to change existing lemmatizing components.
	 * @author zweiss
	 */
	interface CTAPLemmatizer {
		abstract String[] lemmatize(List<Token> tokenizedSentence, String sentence);
	}

	/**
	 * Wrapper for use of Mate lemmatizer
	 * which is part of the Mate tools (https://www.ims.uni-stuttgart.de/forschung/ressourcen/werkzeuge/matetools.en.html)
	 * @author zweiss
	 */
	private class MateLemmatizer implements CTAPLemmatizer {
		
		private Lemmatizer mateLemmatizer;
		
		public MateLemmatizer(String modelInFile) throws IOException {
//			is2.lemmatizer.Options optsLemmatizer = new is2.lemmatizer.Options(new String[]{"-model", modelInFile}); 
//			mateLemmatizer = new Lemmatizer(optsLemmatizer);
			mateLemmatizer = new Lemmatizer(modelInFile);
		}
		
		@Override
		public String[] lemmatize(List<Token> sentTokens, String sentence) {
			String[] tokens = new String[sentTokens.size()+1];
			tokens[0] = "<root>";  // Mate tools expect root token
			for (int i = 0; i < sentTokens.size(); i++) {
				tokens[i+1] = sentTokens.get(i).getCoveredText();
			}
			return lemmatize(tokens);
		}

		/**
		 * Takes input with root element at initial position
		 */
		public String[] lemmatize(String[] tokens) {
			SentenceData09 inputSentenceData = new SentenceData09();
			inputSentenceData.init(tokens);
			return lemmatize(inputSentenceData);
		}

		public String[] lemmatize(SentenceData09 inputSentenceData) {
			SentenceData09 lemmatizedSentence = mateLemmatizer.apply(inputSentenceData);
			return lemmatizedSentence.plemmas;
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
	private class StanzaLemmatizer implements CTAPLemmatizer {

		@Override
		public String[] lemmatize(List<Token> sentTokens, String doc) {

			List<String> l = new ArrayList<>();
			String[] spans = doc.split("\n\n")[2].trim().split("\t");
			String[] lemmas = doc.split("\n\n")[4].trim().split("\t");

			int startIndex = 0;
			for (Token t : sentTokens) {
				for (int i = startIndex; i < spans.length; i++){
					if ((Integer.parseInt(spans[i].split("-")[0]) == t.getBegin()) &&
							(Integer.parseInt(spans[i].split("-")[1]) == t.getEnd())) {
						l.add(lemmas[i]);
						startIndex = i;
					}
				}
			}
			return Arrays.copyOf(l.toArray(), l.size(), String[].class);
		}
	}
}
