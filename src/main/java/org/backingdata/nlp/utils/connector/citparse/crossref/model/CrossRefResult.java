package org.backingdata.nlp.utils.connector.citparse.crossref.model;

import org.apache.commons.lang.StringUtils;


/**
 * CrossRef search result - data model
 * 
 * @author Francesco Ronzano
 *
 */
public class CrossRefResult {
	
	private String originalText = "";
	private String score = "";
	private String normalizedScore = "";
	
	private String doi = "";
	private String title = "";
	private String year = "";
	private String fullCitation = "";
	private String coins = "";
	
	
	// Getters and setters
	public String getOriginalText() {
		return originalText;
	}
	
	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}
	
	public String getScore() {
		return score;
	}
	
	public void setScore(String score) {
		this.score = score;
	}
	
	public String getNormalizedScore() {
		return normalizedScore;
	}
	
	public void setNormalizedScore(String normalizedScore) {
		this.normalizedScore = normalizedScore;
	}
	
	public String getDoi() {
		return doi;
	}
	
	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getFullCitation() {
		return fullCitation;
	}
	
	public void setFullCitation(String fullCitation) {
		this.fullCitation = fullCitation;
	}
	
	public String getCoins() {
		return coins;
	}

	public void setCoins(String coins) {
		this.coins = coins;
	}
	

	@Override
	public String toString() {
		return "CrossRefResult [originalText=" + StringUtils.defaultIfEmpty(originalText, "NULL")  
				+ ", score=" + StringUtils.defaultIfEmpty(score, "NULL")  
				+ ", normalizedScore=" + StringUtils.defaultIfEmpty(normalizedScore, "NULL")  
				+ ", doi=" + StringUtils.defaultIfEmpty(doi, "NULL")  
				+ ", title=" + StringUtils.defaultIfEmpty(title, "NULL")  
				+ ", year=" + StringUtils.defaultIfEmpty(year, "NULL") 
				+ ", coins=" + StringUtils.defaultIfEmpty(coins, "NULL") 
				+ ", fullCitation=" + StringUtils.defaultIfEmpty(fullCitation, "NULL")  + "]";
	}
	
}
