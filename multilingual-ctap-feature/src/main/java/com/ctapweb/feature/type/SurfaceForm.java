

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


/** the surface form of a (potentially multi-word) token
 * Updated by JCasGen Mon Jun 29 11:17:07 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/linguistic_type/SurfaceFormType.xml
 * @generated */
public class SurfaceForm extends Annotation {
  /** @generated
   * @ordered
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.SurfaceForm";

  /** @generated
   * @ordered
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SurfaceForm.class);
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

  public final static String _FeatName_surfaceform = "surfaceform";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_surfaceform = TypeSystemImpl.createCallSite(SurfaceForm.class, "surfaceform");
  private final static MethodHandle _FH_surfaceform = _FC_surfaceform.dynamicInvoker();


  /** Never called.  Disable default constructor
   * @generated */
  protected SurfaceForm() {/* intentionally empty block */}

  /** Internal - constructor used by generator
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure
   */
  public SurfaceForm(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   */
  public SurfaceForm(JCas jcas) {
    super(jcas);
    readObject();
  }


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA
  */
  public SurfaceForm(JCas jcas, int begin, int end) {
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
  //* Feature: surfaceform

  /** getter for surface form - gets the surface form as a String
   * @generated
   * @return value of the feature
   */
  public String getSurfaceForm() { return _getStringValueNc(wrapGetIntCatchException(_FH_surfaceform)); }

  /** setter for surface form - sets the surface form as a String
   * @generated
   * @param v value to set into the feature
   */
  public void setSurfaceForm(String v) { _setStringValueNfc(wrapGetIntCatchException(_FH_surfaceform), v); }

  }