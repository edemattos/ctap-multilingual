

/* First created by JCasGen Fri Sep 09 21:52:52 CEST 2016 */
package com.ctapweb.web.server.analysis.type;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.AnnotationBase;
import org.apache.uima.jcas.cas.TOP_Type;


/** 
 * Updated by JCasGen Mon Jul 06 12:51:32 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-web/src/main/java/com/ctapweb/web/server/analysis/descriptor/CorpusTextCollectionReader.xml
 * @generated */
public class CorpusTextInfo extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.web.server.analysis.type.CorpusTextInfo";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CorpusTextInfo.class);
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


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_id = TypeSystemImpl.createCallSite(CorpusTextInfo.class, "id");
  private final static MethodHandle _FH_id = _FC_id.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected CorpusTextInfo() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public CorpusTextInfo(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CorpusTextInfo(JCas jcas) {
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

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public long getId() { 
    return _getLongValueNc(wrapGetIntCatchException(_FH_id));
  }
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(long v) {
    _setLongValueNfc(wrapGetIntCatchException(_FH_id), v);
  }    
    
  }

    