

/* First created by JCasGen Tue Jan 29 11:05:21 CET 2019 */
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


/** the dependency parse
 of a sentence
 * Updated by JCasGen Mon Jun 29 11:20:17 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/DLTIntegrationCostType.xml
 * @generated */
public class DependencyParse extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.DependencyParse";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DependencyParse.class);
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
   
  public final static String _FeatName_dependencyParse = "dependencyParse";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_dependencyParse = TypeSystemImpl.createCallSite(DependencyParse.class, "dependencyParse");
  private final static MethodHandle _FH_dependencyParse = _FC_dependencyParse.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected DependencyParse() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public DependencyParse(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DependencyParse(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public DependencyParse(JCas jcas, int begin, int end) {
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
  //* Feature: dependencyParse

  /** getter for dependencyParse - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDependencyParse() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_dependencyParse));
  }
    
  /** setter for dependencyParse - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDependencyParse(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_dependencyParse), v);
  }    
    
  }

    