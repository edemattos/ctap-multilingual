package com.ctapweb.feature.util;

/**
 * Interface that allows to define common word categories that are shared between POS based AEs
 * @author zweiss
 *
 */
public abstract class MorphologicalCategories {

    protected String[] infVerb;
    protected String[] finVerb;
    protected String[] subjunctiveVerb;
    protected String[] conditionalVerb;
    protected String[] imperfectVerb;
    protected String[] pluperfectVerb;
    protected String[] pastTenseVerb;

    public String[] getInfiniteVerbs() {
        return infVerb;
    }
    public String[] getFiniteVerbs() {
        return finVerb;
    }
    public String[] getSubjunctiveVerbs() {
        return subjunctiveVerb;
    }
    public String[] getConditionalVerb() {
        return conditionalVerb;
    }
    public String[] getImperfectVerb() {
        return imperfectVerb;
    }
    public String[] getPluperfectVerb() {
        return pluperfectVerb;
    }
    public String[] getPastTenseVerb() { return pastTenseVerb; }

    public boolean isInfinitiveVerb(String tag) { return check(tag, infVerb); }
    public boolean isFiniteVerb(String tag) { return check(tag, finVerb); }
    public boolean isSubjunctiveVerb(String tag) { return check(tag, subjunctiveVerb); }
    public boolean isConditionalVerb(String tag) { return check(tag, conditionalVerb); }
    public boolean isImperfectVerb(String tag) { return check(tag, imperfectVerb); }
    public boolean isPluperfectVerb(String tag) { return check(tag, pluperfectVerb); }
    public boolean isPastTenseVerb(String tag) { return check(tag, pastTenseVerb); }

    private boolean check(String tag, String[] category) {
        for (String c : category)
            if (tag.equals(c))
                return true;
        return false;
    }
}
