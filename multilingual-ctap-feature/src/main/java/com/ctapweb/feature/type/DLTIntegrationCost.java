

/* First created by JCasGen Tue Jan 29 16:16:36 CET 2019 */
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
 * Updated by JCasGen Mon Jun 29 11:20:17 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/DLTIntegrationCostType.xml
 * @generated */
public class DLTIntegrationCost extends ComplexityFeatureBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.DLTIntegrationCost";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DLTIntegrationCost.class);
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
   
  public final static String _FeatName_costCalculationConfiguration = "costCalculationConfiguration";
  public final static String _FeatName_integrationCostType = "integrationCostType";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_costCalculationConfiguration = TypeSystemImpl.createCallSite(DLTIntegrationCost.class, "costCalculationConfiguration");
  private final static MethodHandle _FH_costCalculationConfiguration = _FC_costCalculationConfiguration.dynamicInvoker();
  private final static CallSite _FC_integrationCostType = TypeSystemImpl.createCallSite(DLTIntegrationCost.class, "integrationCostType");
  private final static MethodHandle _FH_integrationCostType = _FC_integrationCostType.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected DLTIntegrationCost() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public DLTIntegrationCost(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DLTIntegrationCost(JCas jcas) {
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
  //* Feature: costCalculationConfiguration

  /** getter for costCalculationConfiguration - gets the configuration should be one of the following: 
o		original DLT IC cost calculation
v		additional verb weight
m		ignore modifier weights
c		reduced coordination weights
cv		reduce coordination weight, add verb weight
cm		reduce coordination weight, ignore modifier weight
mv		ignore modifier weight, add verb weight
cmv		reduce coordination weight, ignore modifier weight, add verb weight
   * @generated
   * @return value of the feature 
   */
  public String getCostCalculationConfiguration() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_costCalculationConfiguration));
  }
    
  /** setter for costCalculationConfiguration - sets the configuration should be one of the following: 
o		original DLT IC cost calculation
v		additional verb weight
m		ignore modifier weights
c		reduced coordination weights
cv		reduce coordination weight, add verb weight
cm		reduce coordination weight, ignore modifier weight
mv		ignore modifier weight, add verb weight
cmv		reduce coordination weight, ignore modifier weight, add verb weight 
   * @generated
   * @param v value to set into the feature 
   */
  public void setCostCalculationConfiguration(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_costCalculationConfiguration), v);
  }    
    
   
    
  //*--------------*
  //* Feature: integrationCostType

  /** getter for integrationCostType - gets the integration cost should be one of the following: 
totalIC		average total integration cost
maxIC		average maximal integration cost
highAdjacentIC		average high adjacent integration cost
   * @generated
   * @return value of the feature 
   */
  public String getIntegrationCostType() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_integrationCostType));
  }
    
  /** setter for integrationCostType - sets the integration cost should be one of the following: 
totalIC		average total integration cost
maxIC		average maximal integration cost
highAdjacentIC		average high adjacent integration cost 
   * @generated
   * @param v value to set into the feature 
   */
  public void setIntegrationCostType(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_integrationCostType), v);
  }    
    
  }

    