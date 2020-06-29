

/* First created by JCasGen Tue Aug 16 14:30:54 CEST 2016 */
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


/** The token type.
 * Updated by JCasGen Mon Jun 29 11:24:17 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/TypeTokenRatioType.xml
 * @generated */
public class TokenType extends AnnotationBase {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.TokenType";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TokenType.class);
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
   
  public final static String _FeatName_wordString = "wordString";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_wordString = TypeSystemImpl.createCallSite(TokenType.class, "wordString");
  private final static MethodHandle _FH_wordString = _FC_wordString.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  protected TokenType() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public TokenType(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TokenType(JCas jcas) {
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
  //* Feature: wordString

  /** getter for wordString - gets The spelling of the word type.
   * @generated
   * @return value of the feature 
   */
  public String getWordString() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_wordString));
  }
    
  /** setter for wordString - sets The spelling of the word type. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setWordString(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_wordString), v);
  }    
    
  }

    