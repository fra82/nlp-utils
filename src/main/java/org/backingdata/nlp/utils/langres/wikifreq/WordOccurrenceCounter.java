package org.backingdata.nlp.utils.langres.wikifreq;

import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;

public class WordOccurrenceCounter {
	
	private int total;
	private TObjectIntHashMap<String> byPOS;
	
	
	// Consructor
	public WordOccurrenceCounter() {
		super();
		this.total = 0;
		this.byPOS = new TObjectIntHashMap<String>();
	}
	
	public WordOccurrenceCounter(int total, TObjectIntHashMap<String> byPOS) {
		super();
		this.total = total;
		this.byPOS = byPOS;
	}
	
	// Getters and setters
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public TObjectIntHashMap<String> getByPOS() {
		return byPOS;
	}

	public void setByPOS(TObjectIntHashMap<String> byPOS) {
		this.byPOS = byPOS;
	}
	
	// Other
	public void updateTotal() {
		total = 0;
		
		TObjectIntIterator<String> it = byPOS.iterator();
		while(it.hasNext()){
			it.advance();
			total += it.value();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((byPOS == null) ? 0 : byPOS.hashCode());
		result = prime * result + total;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordOccurrenceCounter other = (WordOccurrenceCounter) obj;
		if (byPOS == null) {
			if (other.byPOS != null)
				return false;
		} else if (!byPOS.equals(other.byPOS))
			return false;
		if (total != other.total)
			return false;
		return true;
	}
	
	
}
