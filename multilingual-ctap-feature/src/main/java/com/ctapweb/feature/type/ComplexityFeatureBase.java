

/* First created by JCasGen Tue Aug 16 14:32:11 CEST 2016 */
package com.ctapweb.feature.type;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.AnnotationBase;
import org.apache.uima.jcas.cas.TOP_Type;


/** All complexity features extend this base type.
 * Updated by JCasGen Mon Jun 29 11:24:30 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/Word2OrMoreSyllablesType.xml
 * @generated */
public class ComplexityFeatureBase extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.ComplexityFeatureBase";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ComplexityFeatureBase.class);
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
   
  public final static String _FeatName_id = "id";
  public final static String _FeatName_value = "value";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_id = TypeSystemImpl.createCallSite(ComplexityFeatureBase.class, "id");
  private final static MethodHandle _FH_id = _FC_id.dynamicInvoker();
  private final static CallSite _FC_value = TypeSystemImpl.createCallSite(ComplexityFeatureBase.class, "value");
  private final static MethodHandle _FH_value = _FC_value.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected ComplexityFeatureBase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public ComplexityFeatureBase(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public ComplexityFeatureBase(JCas jcas) {
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
  //* Feature: id

  /** getter for id - gets the ID of the current analysis engine
   * @generated
   * @return value of the feature 
   */
  public int getId() { 
    return _getIntValueNc(wrapGetIntCatchException(_FH_id));
  }
    
  /** setter for id - sets the ID of the current analysis engine 
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(int v) {
    _setIntValueNfc(wrapGetIntCatchException(_FH_id), v);
  }    
    
   
    
  //*--------------*
  //* Feature: value

  /** getter for value - gets The value of the feature.
   * @generated
   * @return value of the feature 
   */
  public double getValue() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_value));
  }
    
  /** setter for value - sets The value of the feature. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_value), v);
  }    
    
  }

    