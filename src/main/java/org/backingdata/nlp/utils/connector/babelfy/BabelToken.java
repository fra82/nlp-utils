package org.backingdata.nlp.utils.connector.babelfy;

import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.jlt.util.Language;

public class BabelToken {
	
	private Integer id;
	private String word;
	private String lemma;
	private PosTag pos;
	private Language lang;
	
	
	// Constructor
	public BabelToken(Integer id, String word, String lemma, PosTag pos, Language lang) {
		super();
		this.id = id;
		this.word = word;
		this.lemma = lemma;
		this.pos = pos;
		this.lang = lang;
	}
	
	
	// Getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public PosTag getPos() {
		return pos;
	}

	public void setPos(PosTag pos) {
		this.pos = pos;
	}	

	public Language getLang() {
		return lang;
	}
	
	public void setLang(Language lang) {
		this.lang = lang;
	}
	
	
	
}
