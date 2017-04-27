package org.backingdata.nlp.utils.connector.citparse.bibsonomy.model;

import org.bibsonomy.model.BibTex;

/**
 * Extension of class that represent a BibTeX entry
 * 
 * @author Francesco Ronzano
 *
 */
public class BibTexWrap extends BibTex {
	
	private static final long serialVersionUID = 1L;
	
	private String authorList = "";
	private String editorList = "";
	
	// Constructor
	public BibTexWrap() {
		super();
		this.authorList = null;
		this.editorList = null;
	}
	
	public BibTexWrap(String authorString, String editorString) {
		super();
		this.authorList = authorString;
		this.editorList = editorString;
	}

	// Setters and getters
	public String getAuthorList() {
		return authorList;
	}

	public void setAuthorList(String authorString) {
		this.authorList = authorString;
	}

	public String getEditorList() {
		return editorList;
	}

	public void setEditorList(String editorString) {
		this.editorList = editorString;
	}
	
}
