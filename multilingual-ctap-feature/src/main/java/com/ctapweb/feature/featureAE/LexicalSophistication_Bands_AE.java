package com.ctapweb.feature.featureAE;

import java.util.ArrayList;
import java.util.HashSet;
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

import com.ctapweb.feature.logging.LogMarker;
import com.ctapweb.feature.logging.message.AEType;
import com.ctapweb.feature.logging.message.DestroyAECompleteMessage;
import com.ctapweb.feature.logging.message.DestroyingAEMessage;
import com.ctapweb.feature.logging.message.InitializeAECompleteMessage;
import com.ctapweb.feature.logging.message.InitializingAEMessage;
import com.ctapweb.feature.logging.message.PopulatedFeatureValueMessage;
import com.ctapweb.feature.logging.message.ProcessingDocumentMessage;
import com.ctapweb.feature.type.LexicalSophistication_Bands;
import com.ctapweb.feature.type.NSurfaceForm;
import com.ctapweb.feature.type.SurfaceForm;
import com.ctapweb.feature.util.LookUpListResource;

/**
 * Counts the number of words in a particular frequency band
 *
 * @author edemattos
 */
public class LexicalSophistication_Bands_AE extends JCasAnnotator_ImplBase {

    //the analysis engine's id from the database
    //this value needs to be set when initiating the analysis engine
    public static final String PARAM_AEID = "aeID";
    public static final String RESOURCE_KEY = "lookUpList";
    public static final String PARAM_LANGUAGE_CODE = "LanguageCode";

    private int aeID;
    private LookUpListResource freqList;

    private static final Logger logger = LogManager.getLogger();

    private static final AEType aeType = AEType.FEATURE_EXTRACTOR;
    private static final String aeName = "Frequency Band Feature Extractor";

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
        super.initialize(aContext);

        //get the parameter value of analysis id
        if (aContext.getConfigParameterValue(PARAM_AEID) == null) {
            ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
                    new Object[]{PARAM_AEID});
            logger.throwing(e);
            throw e;
        } else {
            aeID = (Integer) aContext.getConfigParameterValue(PARAM_AEID);
        }

        // obtain mandatory language parameter and access language dependent resources
        String lCode = "";
        if (aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE) == null) {
            ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
                    new Object[]{PARAM_LANGUAGE_CODE});
            logger.throwing(e);
            throw e;
        } else {
            lCode = ((String) aContext.getConfigParameterValue(PARAM_LANGUAGE_CODE)).toUpperCase();
        }
        String languageSpecificResourceKey = RESOURCE_KEY + lCode;

        try {
            freqList = (LookUpListResource) aContext.getResourceObject(languageSpecificResourceKey);
        } catch (ResourceAccessException e) {
            logger.throwing(e);
            throw new ResourceInitializationException(e);
        }

        logger.trace(LogMarker.UIMA_MARKER, new InitializeAECompleteMessage(aeType, aeName));
    }

    /* (non-Javadoc)
     * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
     */
    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        logger.trace(LogMarker.UIMA_MARKER, new ProcessingDocumentMessage(aeType, aeName, aJCas.getDocumentText()));

        double occurrence = 0.0;

        Iterator tokenIter = aJCas.getAnnotationIndex(SurfaceForm.type).iterator(false);
        List<String> sentTokens = new ArrayList<>();
        while (tokenIter.hasNext()) {
            sentTokens.add(((SurfaceForm) tokenIter.next()).getSurfaceForm().trim().toLowerCase());
        }

        HashSet<String> freqWords = new HashSet<>(freqList.getKeys());
        for (String token : sentTokens) {
            if (freqWords.contains(token)) {
                occurrence++;
            }
        }

        occurrence = getNTokens(aJCas) != 0 ? occurrence / getNTokens(aJCas) : 0;

        LexicalSophistication_Bands annotation = new LexicalSophistication_Bands(aJCas);
        annotation.setId(aeID);
        annotation.setValue(occurrence);
        annotation.addToIndexes();
        logger.info(new PopulatedFeatureValueMessage(aeID, occurrence));
    }

    /**
     * Helper method to get the document's number of tokens from the CAS
     */
    private int getNTokens(JCas aJCas) {
        Iterator posIter = aJCas.getAllIndexedFS(NSurfaceForm.class);
        if (posIter.hasNext()) {
            NSurfaceForm nToken = (NSurfaceForm) posIter.next();
            return (int) nToken.getValue();
        }
        return 0;
    }

    @Override
    public void destroy() {
        logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));
        super.destroy();
        logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
    }
}
