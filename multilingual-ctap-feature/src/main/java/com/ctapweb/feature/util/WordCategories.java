package com.ctapweb.feature.util;

/**
 * Interface that allows to define common word categories that are shared between POS based AEs
 * @author zweiss
 *
 */
public abstract class WordCategories {

		protected String[] lexical;
		protected String[] functional;
		protected String[] noun;
		protected String[] adjective;
		protected String[] adverb;
		protected String[] verb;
		protected String[] finVerb;
		protected String[] lexicalVerb;
		protected String[] pronouns;
		protected String[] punctuation;
		protected String[] proposition;
		protected String[] symbol;
		protected String[] article;
		protected String[] number;
		protected String[] auxiliary;

		public String[] getLexicalWords() {
			return lexical;
		}

		public String[] getNouns() {
			return noun;
		}

		public String[] getVerbs() {
			return verb;
		}		
		public String[] getFiniteVerbs() {
			return finVerb;
		}
		public String[] getLexicalVerbs() {
			return lexicalVerb;
		}

		public String[] getAdjectives() {
			return adjective;
		}

		public String[] getAdverbs() {
			return adverb;
		}

		public String[] getFunctionalWords() {
			return functional;
		}

		public String[] getPronouns() {
			return pronouns;
		}

		public String[] getPunctuation() {
			return punctuation;
		}

		public boolean isNoun(String tag) {
			for (String nTag : noun) {
				if (tag.equals(nTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isPunctuation(String tag) {
			for (String pTag : punctuation) {
				if (tag.equals(pTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isVerb(String tag) {
			for (String vTag : verb) {
				if (tag.equals(vTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isFiniteVerb(String tag) {
			for (String vTag : finVerb) {
				if (tag.equals(vTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isProposition(String form, String tag) {
			for (String pTag : proposition) {
				if (tag.equals(pTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isArticle(String form, String tag) {
			for (String pTag : article) {
				if (tag.equals(pTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isSymbol(String tag) {
			for (String sTag : symbol) {
				if (tag.equals(sTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isNumber(String tag) {
			for (String nTag : number) {
				if (tag.equals(nTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean isAuxiliary(String form, String tag) {
			for (String aTag : auxiliary) {
				if (tag.equals(aTag)) {
					return true;
				}
			}
			return false;
		}

		public boolean supportsFiniteVerbs() {
			return finVerb != null;
		}

		public abstract boolean isRelativePronoun(String form, String tag);
		public abstract boolean isPreposition(String tag);

		public boolean isClitic(String form, String tag, String headPOS, String[] feats) { return false; };
		public boolean isMesoclitic(String form) { return false; };
}
