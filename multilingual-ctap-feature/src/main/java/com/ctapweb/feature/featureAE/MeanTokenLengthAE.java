/**
 * 
 */
package com.ctapweb.feature.featureAE;

import java.util.Iterator;

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
import com.ctapweb.feature.logging.message.PopulatedFeatureValueMessage;
import com.ctapweb.feature.logging.message.ProcessingDocumentMessage;
import com.ctapweb.feature.type.ComplexityFeatureBase;
import com.ctapweb.feature.type.MeanTokenLength;
import com.ctapweb.feature.type.NLetter;
import com.ctapweb.feature.type.NSurfaceForm;
import com.ctapweb.feature.type.NSyllable;
import com.ctapweb.feature.type.NToken;

/**
 * @author xiaobin
 * Calculates the average word length in syllables/letters, depending on the parameter setting.
 * Paramerter: 
 *  - unit: "syllable" or "letter"
 *
 * edemattos 30/07/20 - add surface forms for syllables
 */
public class MeanTokenLengthAE extends JCasAnnotator_ImplBase {

	//the analysis engine's id from the database
	//this value needs to be set when initiating the analysis engine
	public static final String PARAM_AEID = "aeID";
	public static final String PARAM_UNIT = "unit"; //counting unit: token/syllable/letter
	private int aeID;
	private String unit = null;

	private static final Logger logger = LogManager.getLogger();

	private static final AEType aeType = AEType.FEATURE_EXTRACTOR;
	private static final String aeName = "MeanTokenLength Feature Extractor";

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));

		super.initialize(aContext);

		//get the parameter value of analysis id
		if(aContext.getConfigParameterValue(PARAM_AEID) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing", 
					new Object[] {PARAM_AEID});
			logger.throwing(e);
			throw e;
		} else {
			aeID = (Integer) aContext.getConfigParameterValue(PARAM_AEID);
		}

		//get the unit parameter value
		if(aContext.getConfigParameterValue(PARAM_UNIT) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing", 
					new Object[] {PARAM_UNIT});
			logger.throwing(e);
			throw e;
		} else {
			unit = (String) aContext.getConfigParameterValue(PARAM_UNIT);

			//check if unit value is correct
			switch(unit) {
			case "syllable":
			case "letter":
				break;
			default: 
				throw new ResourceInitializationException("annotator_parameter_not_valid", 
						new Object[] {unit, PARAM_UNIT});
			}
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
		
		double nToken = 0; //number of tokens or surface forms
		double entityCount = 0; //number of syllables/letters

		Iterator it;

		switch (unit) {
			case "syllable":
				it = aJCas.getAllIndexedFS(NSurfaceForm.class);
				nToken = it.hasNext() ? ((ComplexityFeatureBase) it.next()).getValue() : 0;
				it = aJCas.getAllIndexedFS(NSyllable.class);
				entityCount = it.hasNext() ? ((ComplexityFeatureBase) it.next()).getValue() : 0;
				break;
			case "letter":
				it = aJCas.getAllIndexedFS(NToken.class);
				nToken = it.hasNext() ? ((ComplexityFeatureBase) it.next()).getValue() : 0;
				it = aJCas.getAllIndexedFS(NLetter.class);
				entityCount = it.hasNext() ? ((ComplexityFeatureBase) it.next()).getValue() : 0;
				break;
		}

		double meanTokenLength = nToken != 0 ? entityCount / nToken : 0;
		MeanTokenLength annotation = new MeanTokenLength(aJCas);

		annotation.setId(aeID);
		annotation.setValue(meanTokenLength);
		annotation.addToIndexes();

		logger.info(new PopulatedFeatureValueMessage(aeID, meanTokenLength));
	}
	
	@Override
	public void destroy() {
		logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));

		super.destroy();

		logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
	}
}
