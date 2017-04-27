package org.backingdata.nlp.utils.connector.citparse.freecite.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * FreeCite search result - data model
 * 
 * @author Francesco Ronzano
 *
 */
public class FreeCiteResult {
	
	private String title = "";
	private List<String> authorNames = new ArrayList<String>();
	private String year = "";
	private String journal = "";
	private String pages = "";
	private String rawString = "";
	
	// Getters and setters
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getAuthorNames() {
		return authorNames;
	}
	
	public void setAuthorNames(List<String> authorNames) {
		this.authorNames = authorNames;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getJournal() {
		return journal;
	}
	
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	public String getPages() {
		return pages;
	}
	
	public void setPages(String pages) {
		this.pages = pages;
	}
	
	public String getRawString() {
		return rawString;
	}

	public void setRawString(String rawString) {
		this.rawString = rawString;
	}
	

	@Override
	public String toString() {
		return "FreeCiteResult [title=" + StringUtils.defaultIfEmpty(title, "NULL") 
				+ ", authorNames=" + (!CollectionUtils.isEmpty(authorNames) ? authorNames : "EMPTY") 
				+ ", year=" + StringUtils.defaultIfEmpty(year, "NULL") 
				+ ", journal=" + StringUtils.defaultIfEmpty(journal, "NULL")
				+ ", pages=" + StringUtils.defaultIfEmpty(pages, "NULL")
				+ ", raw_string=" + StringUtils.defaultIfEmpty(rawString, "NULL") + "]";
	}
	
}
