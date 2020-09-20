package com.ctapweb.feature.util;

/**
 *
 * @author Tanja
 * TODO subordinating conjunctions not supported.
 *
 */
public class EnglishDependencyLabels extends UDDependencyLabels {
    public EnglishDependencyLabels() {
        relativeClauses = new String[] {"acl:relcl"};
        conjuncts = new String[] {"conj", "cc", "cc:preconj"};
    }
}
