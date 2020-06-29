

/* First created by JCasGen Mon Jan 28 10:39:46 CET 2019 */
package com.ctapweb.feature.type;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** the lemma of a token
 * Updated by JCasGen Mon Jun 29 11:17:07 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/linguistic_type/LemmaType.xml
 * @generated */
public class Lemma extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.Lemma";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Lemma.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_lemma = "lemma";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_lemma = TypeSystemImpl.createCallSite(Lemma.class, "lemma");
  private final static MethodHandle _FH_lemma = _FC_lemma.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected Lemma() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Lemma(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Lemma(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Lemma(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
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
     
 
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets the lemma as a String
   * @generated
   * @return value of the feature 
   */
  public String getLemma() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_lemma));
  }
    
  /** setter for lemma - sets the lemma as a String 
   * @generated
   * @param v value to set into the feature 
   */
  public void setLemma(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_lemma), v);
  }    
    
  }

    