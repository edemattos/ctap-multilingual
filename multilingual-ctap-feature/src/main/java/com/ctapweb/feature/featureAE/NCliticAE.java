/**
 *
 */
package com.ctapweb.feature.featureAE;

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
import com.ctapweb.feature.logging.message.PopulatedFeatureValueMessage;
import com.ctapweb.feature.logging.message.ProcessingDocumentMessage;
import com.ctapweb.feature.type.DependencyParse;
import com.ctapweb.feature.type.NClitic;
import com.ctapweb.feature.type.NSyntacticConstituent;
import com.ctapweb.feature.util.DependencyTree;
import com.ctapweb.feature.util.WordCategories;
import com.ctapweb.feature.util.PortugueseWordCategories;

/**
 * Counts the number of clitics per unit in a text
 * @author edemattos
 *
 */
public class NCliticAE extends JCasAnnotator_ImplBase {

    //the analysis engine's id from the database
    //this value needs to be set when initiating the analysis engine
    public static final String PARAM_AEID = "aeID";
    public static final String PARAM_CONNECTIVE_SCOPE = "Scope";
    public static final String PARAM_DENOMINATOR = "denominator";
    public static final String PARAM_LANGUAGE_CODE = "LanguageCode";

    private int aeID;

    private static final Logger logger = LogManager.getLogger();
    private static final AEType aeType = AEType.FEATURE_EXTRACTOR;
    private static final String aeName = "Clitic Cluster Feature Extractor";

    private WordCategories posMapping;
    private String scope;
    private String denominatorStr;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        logger.trace(LogMarker.UIMA_MARKER, new InitializingAEMessage(aeType, aeName));
        super.initialize(aContext);

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
                    posMapping = new PortugueseWordCategories();
                    break;
                // add new language here
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

        // get the type of clitics
		if (aContext.getConfigParameterValue(PARAM_CONNECTIVE_SCOPE) == null) {
			ResourceInitializationException e = new ResourceInitializationException("mandatory_value_missing",
					new Object[] {PARAM_CONNECTIVE_SCOPE});
			logger.throwing(e);
			throw e;
		} else {
			scope = (String) aContext.getConfigParameterValue(PARAM_CONNECTIVE_SCOPE);
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
        logger.trace(LogMarker.UIMA_MARKER, new ProcessingDocumentMessage(aeType, aeName, aJCas.getDocumentText()));

        int nClitics = 0;
        int nProclitics = 0;
        int nEnclitics = 0;
        int nMesoclitics = 0;

        Iterator depIter = aJCas.getAnnotationIndex(DependencyParse.type).iterator();

        while (depIter.hasNext()) {
            DependencyTree depTree = DependencyTree.valueOf(((DependencyParse) depIter.next()).getDependencyParse());

            // map head information with CoNLL ID to avoid indexing issues (better to define in DependencyTree?)
            Map<String, Map<String, String>> deps = new HashMap<>();
            for (int i = 0; i < depTree.id.length; i++) {
                String id = depTree.id[i];
                deps.put(id, new HashMap<>());
                deps.get(id).put("pos", depTree.ppos[i]);
                deps.get(id).put("head", Integer.toString(depTree.pheads[i]));
                deps.get(id).put("feats", depTree.pfeats[i]);
            }

            Set<String> skip = new HashSet<>();

            for (int i = 0; i < depTree.id.length; i++) {

                String conllID = depTree.id[i];
                if (skip.contains(conllID)) { continue; } // skip subtokens if multi-word token was already accepted

                if (posMapping.isMesoclitic(depTree.forms[i])) { // special case for Portuguese

                    // skip over subtokens in case we've already determined it's a mesoclitic
                    if (conllID.contains("-")) {
                        int begin = Integer.parseInt(conllID.split("-")[0]);
                        int end = Integer.parseInt(conllID.split("-")[1]);
                        for (int j = begin; j <= end; j++) { skip.add(Integer.toString(j)); }
                    }
                    nMesoclitics++;
                    nClitics++;
                    continue;
                }

                if (conllID.contains("-")) { continue; } // skip multi-word tokens (no feature information)

                int headID = Integer.parseInt(deps.get(conllID).get("head"));
                if (headID == 0) { continue; } // current token is root (no host, therefore cannot be a clitic)

                String form = depTree.forms[i];
                String pos = depTree.ppos[i];
                String headPOS = deps.get(deps.get(conllID).get("head")).get("pos");
                String[] feats = deps.get(conllID).get("feats").split("\\|");

                if (posMapping.isClitic(form, pos, headPOS, feats)) {
                    int distance = Integer.parseInt(conllID) - headID;
                    if (distance < 0) {
                        nProclitics++;
                    } else if (distance > 0) {
                        nEnclitics++;
                    }
                    nClitics++;
                }
            }
        }

        double rval = 0;
        double denominator = 0;

        // get unit by which to divide number of clitics
        Iterator it = aJCas.getAllIndexedFS(NSyntacticConstituent.class);
		while (it.hasNext()) {
			NSyntacticConstituent synConst = (NSyntacticConstituent) it.next();
			String type = "n" + synConst.getContituentType();
			if (type.equals(denominatorStr)) { denominator = synConst.getValue(); }
		}

        switch (scope) {
            case "ALL":
                rval = denominator > 0 ? nClitics / denominator : 0;
                break;
            case "PROCLISIS":
                rval = denominator > 0 ? nProclitics / denominator : 0;
                break;
            case "ENCLISIS":
                rval = denominator > 0 ? nEnclitics / denominator : 0;
                break;
            case "MESOCLISIS":
                rval = denominator > 0 ? nMesoclitics / denominator : 0;
                break;
        }

        NClitic annotation = new NClitic(aJCas);
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
