

/* First created by JCasGen Fri Dec 23 17:28:33 CET 2016 */
package com.ctapweb.feature.type;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/**
 * Updated by JCasGen Mon Jun 29 11:20:35 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/NMorphType.xml
 * @generated */
public class NMorph extends ComplexityFeatureBase {
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.NMorph";

  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(NMorph.class);
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int type = typeIndexID;

  /**
   * @return index of the type
   * @generated
   */
  @Override
  public int getTypeIndexID() {
    return typeIndexID;
  }


  /**
   * Never called.  Disable default constructor
   *
   * @generated
   */
  protected NMorph() {/* intentionally empty block */}

  /**
   * Internal - constructor used by generator
   *
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type    the type of this Feature Structure
   * @generated
   */
  public NMorph(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }

  /**
   * @param jcas JCas to which this Feature Structure belongs
   * @generated
   */
  public NMorph(JCas jcas) {
    super(jcas);
    readObject();
  }


  /**
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  private void readObject() {/*default - does nothing empty block */}
}
