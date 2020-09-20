package com.ctapweb.feature.util;

/**
 *
 * @author edemattos
 *
 */
public class UDMorphologicalCategories extends MorphologicalCategories {
    public UDMorphologicalCategories() {
        infVerb = new String[] { "VerbForm=Inf" };
        finVerb = new String[] { "VerbForm=Fin" };
        subjunctiveVerb = new String[] { "Mood=Sub" };
        conditionalVerb = new String[] { "Mood=Cnd" };
        imperfectVerb = new String[] { "Tense=Imp" };
        pluperfectVerb = new String[] { "Tense=Pqp" };
        pastTenseVerb = new String[] { "Tense=Pst" };
    }
}
