

/* First created by JCasGen Tue Dec 06 13:51:53 CET 2016 */
package com.ctapweb.feature.type;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon Jun 29 11:24:02 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/SyntacticComplexityType.xml
 * @generated */
public class NSyntacticConstituent extends ComplexityFeatureBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.NSyntacticConstituent";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NSyntacticConstituent.class);
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
   
  public final static String _FeatName_contituentType = "contituentType";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_contituentType = TypeSystemImpl.createCallSite(NSyntacticConstituent.class, "contituentType");
  private final static MethodHandle _FH_contituentType = _FC_contituentType.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected NSyntacticConstituent() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public NSyntacticConstituent(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public NSyntacticConstituent(JCas jcas) {
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
     
  //*--------------*
  //* Feature: contituentType

  /** getter for contituentType - gets the constituent type, should be one of the following:
            VP Verb Phrases
            C Clauses
            T T-units
            DC Dependent Clauses
            CT Complex T-unit
            CP Coordinate Phrases
            CN Complex nominal
            FC Fragment Clauses
            FT Frgment T-units
   * @generated
   * @return value of the feature 
   */
  public String getContituentType() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_contituentType));
  }
    
  /** setter for contituentType - sets the constituent type, should be one of the following:
            VP Verb Phrases
            C Clauses
            T T-units
            DC Dependent Clauses
            CT Complex T-unit
            CP Coordinate Phrases
            CN Complex nominal
            FC Fragment Clauses
            FT Frgment T-units 
   * @generated
   * @param v value to set into the feature 
   */
  public void setContituentType(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_contituentType), v);
  }    
    
  }

    