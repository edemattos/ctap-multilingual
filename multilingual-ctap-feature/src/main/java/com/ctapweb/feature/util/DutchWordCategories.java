package com.ctapweb.feature.util;

/**
 * Definition of word categories for Dutch
 * @author zweiss (in collaboration with Rachel Rubin)
 *
 * TODO these POS categories are currently still predominantly German  
 */
public class DutchWordCategories extends WordCategories {
	
	// TODO check with Rachel if this is ok 
	private static final String[] relativePronounForms = new String[] {"dat", "die", "wat", "wie", "wiens", "wier"};
	
	public DutchWordCategories() {
		lexical = new String[]{ 
				"Adj", "V", "N", "Adv"
		};
		adjective = new String[]{"Adj"};
		noun = new String[]{"N"}; 
		adverb = new String[]{"Adv"};
		verb = new String[]{
				"V"
		};
		lexicalVerb = new String[]{
				"V"
		};
		finVerb = new String[]{
				"V"
		};  // TODO change this, finite verb cannot be required by word categories
		functional = new String[] { 
				"Prep", "Art", "Conj", "Pron"
		}; 
		pronouns = new String[] {
				"Pron"};
		punctuation = new String[] {"Punc"};
	}
	
	@Override
	public boolean isRelativePronoun(String form, String tag) {
		// TODO ask Rachel if this makes sense
		if (!tag.equals("Pron")) {
			return false;
		}
		for (String relPron : relativePronounForms) {
			if (form.equalsIgnoreCase(relPron)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean isPreposition(String tag) {
		return tag.startsWith("Prep");
	}
}
