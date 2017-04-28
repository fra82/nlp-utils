package org.backingdata.nlp.utils.connector.citparse.freecite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.connector.citparse.freecite.model.FreeCiteResult;

/**
 * FreeCite connector example
 * 
 * @author Francesco Ronzano
 *
 */
public class ExampleFreeCite {

	private static Logger logger = Logger.getLogger(ExampleFreeCite.class.getName());
	
	public static void main(String[] args) {
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		List<String> citations = new ArrayList<String>();
		
		citations.add(null);
		citations.add("[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3");
		citations.add("21. David Gunning, Vinay K. Chaudhri, Peter E. Clark, Ken Barker, Shaw-Yi Chaw, Mark Greaves, Benjamin Grosof, Alice Leung, David D. McDonald, Sunil Mishra, John Pacheco, Bruce Porter, Aaron Spaulding, Dan Tecuci, and Jing Tien. Project Halo update: Progress toward Digital Aristotle. AI Magazine, 31(3):33–58, 2010.");
		
		List<FreeCiteResult> parsingResults = FreeCiteConn.parseCitations(citations, 15);
		
		parsingResults.stream().forEach((res) -> System.out.println("Parsed citations:\n " + res.toString()));
		
	}

}
