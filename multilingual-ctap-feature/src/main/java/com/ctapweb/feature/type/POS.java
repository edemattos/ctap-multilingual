

/* First created by JCasGen Fri Nov 25 17:05:10 CET 2016 */
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


/** The pos type.
 * Updated by JCasGen Mon Jun 29 11:22:53 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/POSDensityType.xml
 * @generated */
public class POS extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.POS";
  
	/** @generated
	 * @ordered 
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = JCasRegistry.register(POS.class);
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
   
  public final static String _FeatName_tag = "tag";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_tag = TypeSystemImpl.createCallSite(POS.class, "tag");
  private final static MethodHandle _FH_tag = _FC_tag.dynamicInvoker();

   
	/** Never called.  Disable default constructor
	 * @generated */
	protected POS() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public POS(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
	/** @generated
	 * @param jcas JCas to which this Feature Structure belongs 
	 */
	public POS(JCas jcas) {
    super(jcas);
    readObject();   
  } 


	/** @generated
	 * @param jcas JCas to which this Feature Structure belongs
	 * @param begin offset to the begin spot in the SofA
	 * @param end offset to the end spot in the SofA 
	 */  
	public POS(JCas jcas, int begin, int end) {
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
  //* Feature: tag

  /** getter for tag - gets the pos tag
   * @generated
   * @return value of the feature 
   */
	public String getTag() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_tag));
  }
    
  /** setter for tag - sets the pos tag 
   * @generated
   * @param v value to set into the feature 
   */
	public void setTag(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_tag), v);
  }    
    
  }

