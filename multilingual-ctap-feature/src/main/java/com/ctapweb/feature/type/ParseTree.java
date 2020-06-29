

/* First created by JCasGen Tue Dec 06 13:17:37 CET 2016 */
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


/** the parse tree of a sentence
 * Updated by JCasGen Mon Jun 29 11:24:02 CEST 2020
 * XML source: /home/rziai/git/ctap-multilingual/multilingual-ctap-feature/src/main/resources/descriptor/type_system/feature_type/SyntacticComplexityType.xml
 * @generated */
public class ParseTree extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "com.ctapweb.feature.type.ParseTree";
  
	/** @generated
	 * @ordered 
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = JCasRegistry.register(ParseTree.class);
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
   
  public final static String _FeatName_parseTree = "parseTree";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_parseTree = TypeSystemImpl.createCallSite(ParseTree.class, "parseTree");
  private final static MethodHandle _FH_parseTree = _FC_parseTree.dynamicInvoker();

   
	/** Never called.  Disable default constructor
	 * @generated */
	protected ParseTree() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public ParseTree(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
	/** @generated
	 * @param jcas JCas to which this Feature Structure belongs 
	 */
	public ParseTree(JCas jcas) {
    super(jcas);
    readObject();   
  } 


	/** @generated
	 * @param jcas JCas to which this Feature Structure belongs
	 * @param begin offset to the begin spot in the SofA
	 * @param end offset to the end spot in the SofA 
	 */  
	public ParseTree(JCas jcas, int begin, int end) {
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
  //* Feature: parseTree

  /** getter for parseTree - gets the parse tree as a String
   * @generated
   * @return value of the feature 
   */
  public String getParseTree() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_parseTree));
  }
    
  /** setter for parseTree - sets the parse tree as a String 
   * @generated
   * @param v value to set into the feature 
   */
  public void setParseTree(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_parseTree), v);
  }    
    
  }

