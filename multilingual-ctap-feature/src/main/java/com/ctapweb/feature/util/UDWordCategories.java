package com.ctapweb.feature.util;

/**
 * Definition of word categories for the Universal Dependencies tagset
 * @author Tanja
 *
 */
public abstract class UDWordCategories extends WordCategories {
    public UDWordCategories() {
        lexical = new String[]{
                "ADJ","ADV","NOUN","PROPN","VERB","AUX"
        };
        adjective = new String[]{"ADJ"};
        noun = new String[]{"NOUN", "PROPN"};
        adverb = new String[]{"ADV"};
        verb = new String[]{
                "VERB", "AUX"
        };
        lexicalVerb = new String[]{"VERB"};
        functional = new String[] {
                "INTJ","ADP","CCONJ","DET","NUM","PART","PRON","SCONJ", "SYM"
        };
        pronouns = new String[] {"PRON"};
        punctuation = new String[] {"PUNCT"};
        proposition = new String[] {
                "CCONJ",                                    // coordinating conjunction
                "NUM",                                      // cardinal numeral
                "DET",                                      // determiner
                "ADP", 										// preposition
                "SCONJ",                             		// subordinating conj.
                "ADJ",  			                        // adjective
                // possessive pronoun
                "ADV", 				                        // adverbs
                "VERB", 								    // verbs
                // interrogatives/relatives
        };
        symbol = new String[] {"SYM"};
        number = new String[] {"NUM"};
        auxiliary = new String[] {"AUX"};
    }

    protected String[] relativePronouns;
    protected String[] interrogativePronouns;
    protected String[] possessivePronouns;
    protected String[] articleForms;
    protected String[] cliticForms;

    @Override
    public boolean isRelativePronoun(String form, String tag) {
        if(!tag.equals("PRON")) {
            return false;
        }
        for (String token : relativePronouns) {
            if (token.equalsIgnoreCase(form)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInterrogativePronoun(String form, String tag) {
        if(!tag.equals("PRON")) {
            return false;
        }
        for (String token : interrogativePronouns) {
            if (token.equalsIgnoreCase(form)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossessivePronoun(String form, String tag) {
        if(!tag.equals("PRON")) {
            return false;
        }
        for (String token : possessivePronouns) {
            if (token.equalsIgnoreCase(form)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPreposition(String tag) {
        return tag.equals("ADP");
    }

    @Override
    public boolean isProposition(String form, String tag) {
        for (String pTag : proposition) {
            if (tag.equals(pTag)) {
                return true;
            }
        }
        return isRelativePronoun(form, tag) || isInterrogativePronoun(form, tag) || isPossessivePronoun(form, tag);
    }

    @Override
    public boolean isArticle(String form, String tag) {
        if(!tag.equals("DET")) {
            return false;
        }
        for (String token : articleForms) {
            if (token.equalsIgnoreCase(form)) {
                return true;
            }
        }
        return false;
    }
}
