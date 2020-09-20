package com.ctapweb.feature.util;

/**
 *
 * @author zweiss, edemattos
 *
 */
public class PortugueseDependencyLabels extends UDDependencyLabels {

    public PortugueseDependencyLabels() {
//        subordinationgConjunctions = new String[] {};
        conjuncts = new String[] {"conj"};
        arguments = new String[] {"nsubj", "nsubj:pass", "obj", "iobj", "csubj", "ccomp", "xcomp"};
        relativeClauses = new String[] {"acl:relcl"};
    }

}