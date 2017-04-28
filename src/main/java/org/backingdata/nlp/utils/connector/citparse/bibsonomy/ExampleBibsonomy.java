package org.backingdata.nlp.utils.connector.citparse.bibsonomy;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.connector.citparse.bibsonomy.model.BibTexWrap;




/**
 * Example of bibliographic entry search by Bibsonomy
 * REF: http://www.bibsonomy.org/help/doc/api.html
 * 
 * @author Francesco Ronzano
 *
 */
public class ExampleBibsonomy {
	
	private static Logger logger = Logger.getLogger(ExampleBibsonomy.class.getName());

	public static void main(String[] args) {
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		List<BibTexWrap> list = BibsonomyStandaloneConn.getBibTexWrap("Neural machine translation by jointly learning to align and translate. "
				+ "arXiv preprint arXiv:1409.0473", "francesco82", "de8ce60b98de517ef533df6e860dedd6", 15);
				
		for (BibTexWrap post : list) {
			System.out.println(post.getBibtexKey());
			System.out.println(post.getTitle());
			System.out.println(post.getAuthorList());
			System.out.println(post.getInstitution());
		}
	}
}
