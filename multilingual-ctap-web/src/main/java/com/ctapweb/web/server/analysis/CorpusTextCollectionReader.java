/**
 * 
 */
package com.ctapweb.web.server.analysis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import com.ctapweb.web.server.DBConnectionManager;
import com.ctapweb.web.server.analysis.type.CorpusTextInfo;
import com.ctapweb.web.server.logging.LogMarker;
import com.ctapweb.web.shared.CorpusText;

import de.lingmod.stanza.client.StanzaAPIClient;
import de.lingmod.stanza.data.TokenOrWord;
import de.lingmod.stanza.util.AnnotationUtils;

/**
 * A collection reader that reads texts from database corpus table.
 *
 * When initialized, the UIMA framework passes in the 'corpusID' as a parameter.
 * The reader query the database for all the texts in the corpus whose id equals 'corpusID'.
 * The framework then iterates through all the texts the reader obtained by calling the 'hasNext()'
 * and 'getNext()' functions.
 *
 * A collection reader is like any other AEs. It works on the CAS. As a result, this collection reader
 * outputs the CorpusTextInfo type, which has a feature of 'id' designating the id of the text in the
 * 'text' table of the system database. This id will be used
 *
 * @author xiaobin
 *
 */
public class CorpusTextCollectionReader extends CollectionReader_ImplBase {
	//a connection to the database
	Connection dbConnection = DBConnectionManager.getDbConnection();
	Logger logger = LogManager.getLogger();

	//name of the parameter from the collection reader descriptor.
	public static final String PARAM_ANALYSISID = "analysisID";

	//the list of corpus texts
	private List<CorpusText> texts = new ArrayList<>();
	private int currentIndex;

	@Override
	public void initialize() throws ResourceInitializationException {
		logger.info(LogMarker.CTAP_SERVER_MARKER, "Initiating Corpus Text Collection Reader...");
		super.initialize();
		currentIndex = 0;

		//get the analysisID parameter
		// This needs to be set in the descriptor or when initiating the cpe.
		long analysisID = (int) getConfigParameterValue(PARAM_ANALYSISID);

		//get analysis texts from DB
		try {
			logger.info(LogMarker.CTAP_SERVER_MARKER, "Getting analysis texts from DB...");
			texts = AnalysisUtils.getAnalysisTexts(analysisID);

			/*
			 * hardcoded for Stanza/Portuguese. breaks all other languages. possible to get language code here?
			 */

			// rewrite document to include all information from Stanza at once, to be populated in each annotator
			StanzaAPIClient client = new StanzaAPIClient("http://localhost:8000/analyze");

			int count = 0;

			for (CorpusText c : texts) {

				System.out.println("Analyzing text " + ++count + "/" + texts.size());

				List<List<TokenOrWord>> result = client.analyze(c.getContent().trim(), "pt"); // VERY slow
				JCas jcas = JCasFactory.createJCas();
				AnnotationUtils.annotateDkPro(c.getContent().trim(), "pt", result, jcas);

				StringJoiner newDoc = new StringJoiner("\n\n");

				// expand multi-word tokens and get resulting sentence segmentation boundaries
				StringBuilder expanded = new StringBuilder();
				StringJoiner sentence_spans = new StringJoiner("\t");
				int offset = 0;
				for (de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence.class)) {

					while (offset < x.getBegin()) { // in case of new lines or rogue whitespace in original text
						offset++;
						expanded.append(" ");
					}
					expanded.append(x.getCoveredText().trim());
					sentence_spans.add(x.getBegin() + "-" + x.getEnd());
					offset = x.getEnd();
				}
				newDoc.add(expanded.toString());
				newDoc.add(sentence_spans.toString());

				// get token boundaries
				StringJoiner tokens = new StringJoiner("\t");
				for (de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token.class)) {
					tokens.add(x.getBegin() + "-" + x.getEnd());
				}
				newDoc.add(tokens.toString());

				// get POS annotations
				StringJoiner pos = new StringJoiner("\t");
				for (de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS.class)) {
					pos.add(x.getCoarseValue());
				}
				newDoc.add(pos.toString());

				// get lemmas
				StringJoiner lemmas = new StringJoiner("\t");
				for (de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma.class)) {
					lemmas.add(x.getValue());
				}
				newDoc.add(lemmas.toString());

				// get morphological features
				StringJoiner morph = new StringJoiner("\t");
				for (de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.morph.MorphologicalFeatures x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.morph.MorphologicalFeatures.class)) {
					morph.add(x.getBegin() + "//" + x.getEnd() + "//" + x.getValue());
				}
				newDoc.add(morph.toString());

				// get surface forms features
				StringJoiner surface = new StringJoiner("\t");
				for (de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.SurfaceForm x :
						JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.SurfaceForm.class)) {
					surface.add(x.getBegin() + "//" + x.getEnd() + "//" + x.getValue());
				}
				newDoc.add(surface.toString());

				// get dependency parses
				StringBuilder sb = new StringBuilder();
		        for (List sentence : result) {
		            for (Object obj : sentence) {
						TokenOrWord tok = (TokenOrWord) obj;

						sb.append(tok.getId() != null ? tok.getId() : "_");
						sb.append("\t");
						sb.append(tok.getText() != null ? tok.getText() : "_");
						sb.append("\t");
						sb.append(tok.getLemma() != null ? tok.getLemma() : "_");
						sb.append("\t");
						sb.append(tok.getUpos() != null ? tok.getUpos() : "_");
						sb.append("\t");
						sb.append(tok.getUpos() != null ? tok.getUpos() : "_"); // should be Xpos?
						sb.append("\t");
						sb.append(tok.getFeats() != null ? tok.getFeats() : "_");
						sb.append("\t");
						sb.append(tok.getHead() >= 0 ? Integer.toString(tok.getHead()) : "_");
						sb.append("\t");
						sb.append(tok.getDeprel() != null ? tok.getDeprel() : "_");
						sb.append("\t");
						sb.append(tok.getHead() >= 0 ? Integer.toString(tok.getHead()) : "_"); // duplicate?
						sb.append("\t");
						sb.append(tok.getDeprel() != null ? tok.getDeprel() : "_"); // duplicate?
						sb.append(System.getProperty("line.separator"));
					}
					sb.append(System.getProperty("line.separator"));
				}
				newDoc.add(sb.toString());
				c.setContent(newDoc.toString()); // overwrite existing (raw) text
			}
			logger.info(LogMarker.CTAP_SERVER_MARKER, "Obtained {} texts from DB.", texts.size());
		} catch (SQLException | ResourceInitializationException | CASException e) {
			throw logger.throwing(new ResourceInitializationException(e));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
	 */
	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		//create a jcas
		JCas jcas;
		try {
			jcas = aCAS.getJCas();
		} catch (CASException e) {
			throw logger.throwing(new CollectionException(e));
		}

		//put text in the cas
		CorpusText currentText = texts.get(currentIndex++);
		jcas.setDocumentText(currentText.getContent());

		//store text info in the cas, the cas consumer will need this information
		CorpusTextInfo corpusTextInfo = new CorpusTextInfo(jcas);
		corpusTextInfo.setId(currentText.getId());
		corpusTextInfo.addToIndexes();
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
	 */
	@Override
	public void close() throws IOException {

	}

	/* (non-Javadoc)
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
	 */
	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(currentIndex, texts.size(), Progress.ENTITIES) };
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
	 */
	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return currentIndex < texts.size();
	}

}
