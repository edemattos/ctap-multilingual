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
import com.ctapweb.feature.type.NMorph;
import com.ctapweb.feature.type.MorphologicalTag;
import com.ctapweb.feature.type.NSyntacticConstituent;
import com.ctapweb.feature.util.MorphologicalCategories;
import com.ctapweb.feature.util.UDMorphologicalCategories;

/**
 * Counts the number of morphological phenomena in a text
 * @author edemattos
 *
 */
public class NMorphAE extends JCasAnnotator_ImplBase {

    //the analysis engine's id from the database
    //this value needs to be set when initiating the analysis engine
    public static final String PARAM_AEID = "aeID";
    public static final String PARAM_FEATURE = "feature";
    public static final String PARAM_DENOMINATOR = "denominator";
    public static final String PARAM_LANGUAGE_CODE = "LanguageCode";

    private int aeID;

    private static final Logger logger = LogManager.getLogger();
    private static final AEType aeType = AEType.FEATURE_EXTRACTOR;
    private static final String aeName = "Number of Morphological Features Feature Extractor";

    private String feature;
    private MorphologicalCategories morphTags;
    private String denominatorStr;

    @Override
    public void initialize(UimaContext aContext)
            throws ResourceInitializationException {
        logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
        super.initialize(aContext);

        // get the mandatory parameter
        if (aContext.getConfigParameterValue(PARAM_FEATURE) == null) {
            ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
                    new Object[]{PARAM_FEATURE});
            logger.throwing(e);
            throw e;
        } else {
            feature = (String) aContext.getConfigParameterValue(PARAM_FEATURE);
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
            switch (lCode) {
                case "PT":
                    morphTags = new UDMorphologicalCategories();
                    break;
            }
        }

        //get the parameter value of analysis id
        if (aContext.getConfigParameterValue(PARAM_AEID) == null) {
            ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
                    new Object[]{PARAM_AEID});
            logger.throwing(e);
            throw e;
        } else {
            aeID = (Integer) aContext.getConfigParameterValue(PARAM_AEID);
        }

        // get denominator
		if (aContext.getConfigParameterValue(PARAM_DENOMINATOR) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
					new Object[] {PARAM_DENOMINATOR});
			logger.throwing(e);
			throw e;
		} else {
			denominatorStr = (String) aContext.getConfigParameterValue(PARAM_DENOMINATOR);
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

        int nMorph = 0;

        // get annotation indexes and iterator
        Iterator morphIter = aJCas.getAnnotationIndex(MorphologicalTag.type).iterator();

        while (morphIter.hasNext()) {
            MorphologicalTag m = (MorphologicalTag) morphIter.next();
            if (m.getMorphologicalTag() == null) continue;
            String[] morphs = m.getMorphologicalTag().split("\\|");
            for (String s : morphs) {
                switch (feature) {
                    case "preterite":
                        if (morphTags.isPastTenseVerb(s)) {
                            nMorph++;
                            break;
                        }
                    case "imperfect":
                        if (morphTags.isImperfectVerb(s)) {
                            nMorph++;
                            break;
                        }
                        break;
                    case "pluperfect":
                        if (morphTags.isPluperfectVerb(s)) {
                            nMorph++;
                            break;
                        }
                        break;
                    case "subjunctive":
                        if (morphTags.isSubjunctiveVerb(s)) {
                            nMorph++;
                            break;
                        }
                        break;
                    case "conditional":
                        if (morphTags.isConditionalVerb(s)) {
                            nMorph++;
                            break;
                        }
                        break;
                    case "inflinf":
                        if (morphTags.isInfinitiveVerb(s) && morphs.length > 1) { // probably should be changed
                            nMorph++;
                            break;
                        }
                        break;
                }
            }
        }

        double denominator = 0;

        // get unit by which to divide
        Iterator it = aJCas.getAllIndexedFS(NSyntacticConstituent.class);
		while (it.hasNext()) {
			NSyntacticConstituent synConst = (NSyntacticConstituent) it.next();
			String type = "n" + synConst.getContituentType();
			if (type.equals(denominatorStr)) { denominator = synConst.getValue(); }
		}

		double rval = denominator > 0 ? (nMorph / denominator) : 0;

        NMorph annotation = new NMorph(aJCas);
        annotation.setId(aeID);
        annotation.setValue(rval);
        annotation.addToIndexes();

        logger.info(new PopulatedFeatureValueMessage(aeID, rval));
    }

    @Override
    public void destroy() {
        logger.trace(LogMarker.UIMA_MARKER, new DestroyingAEMessage(aeType, aeName));

        super.destroy();

        logger.trace(LogMarker.UIMA_MARKER, new DestroyAECompleteMessage(aeType, aeName));
    }
}
