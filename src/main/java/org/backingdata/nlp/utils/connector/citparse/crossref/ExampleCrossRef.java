package org.backingdata.nlp.utils.connector.citparse.crossref;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.connector.citparse.crossref.model.CrossRefResult;

/**
 * CrossRef connector example
 * 
 * @author Francesco Ronzano
 *
 */
public class ExampleCrossRef {
	
	public static void main(String[] args) {
		
		Logger.getRootLogger().setLevel(Level.ERROR);
		
		String biblioEntry = "[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3";
		Pair<String, CrossRefResult> parsingResults = CrossRefConn.parseCitations(biblioEntry, 15);
		System.out.println(((parsingResults != null && parsingResults.getLeft() != null) ? parsingResults.getLeft().toString() : "NULL") + 
				"\n" + ((parsingResults != null && parsingResults.getRight() != null) ? parsingResults.getRight().toString() : "NULL"));
		
		biblioEntry = "21. David Gunning, Vinay K. Chaudhri, Peter E. Clark, Ken Barker, Shaw-Yi Chaw, Mark Greaves, Benjamin Grosof, Alice Leung, David D. McDonald, Sunil Mishra, John Pacheco, Bruce Porter, Aaron Spaulding, Dan Tecuci, and Jing Tien. Project Halo update: Progress toward Digital Aristotle. AI Magazine, 31(3):33–58, 2010.";
		parsingResults = CrossRefConn.parseCitations(biblioEntry, 15);
		System.out.println(((parsingResults != null && parsingResults.getLeft() != null) ? parsingResults.getLeft().toString() : "NULL") + 
				"\n" + ((parsingResults != null && parsingResults.getRight() != null) ? parsingResults.getRight().toString() : "NULL"));
		
	}

}
