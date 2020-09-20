package com.ctapweb.feature.util;

/**
 *
 * @author Tanja
 *
 */
public class UDDependencyLabels extends DependencyLabelCategories {
    public UDDependencyLabels() {
        conjuncts = new String[] {"conj", "cc"};
        arguments = new String[] {"nsubj", "csubj", "obj", "iobj"};
    }
}
