package org.backingdata.nlp.utils.langres.sentiwordnet;

public class SentiWordnetElem {

	private String sid;
	private Double posScore;
	private Double negScore;
	private Double objScore;
	private String terms;
	private String gloss;
	
	
	// Constructors
	public SentiWordnetElem(String sid, Double posScore, Double negScore, Double objScor) {
		super();
		this.sid = sid;
		this.posScore = posScore;
		this.negScore = negScore;
		this.objScore = objScor;
		this.terms = "";
		this.gloss = "";
	}
	
	public SentiWordnetElem(String sid, Double posScore, Double negScore, Double objScore, String terms, String gloss) {
		super();
		this.sid = sid;
		this.posScore = posScore;
		this.negScore = negScore;
		this.objScore = objScore;
		this.terms = terms;
		this.gloss = gloss;
	}

	// Getters and setters
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Double getPosScore() {
		return posScore;
	}

	public void setPosScore(Double posScore) {
		this.posScore = posScore;
	}

	public Double getNegScore() {
		return negScore;
	}

	public void setNegScore(Double negScore) {
		this.negScore = negScore;
	}

	public Double getObjScore() {
		return objScore;
	}

	public void setObjScore(Double objScore) {
		this.objScore = objScore;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	
}
