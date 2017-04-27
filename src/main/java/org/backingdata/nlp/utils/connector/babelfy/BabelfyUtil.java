package org.backingdata.nlp.utils.connector.babelfy;

import java.io.File;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.Manage;
import org.backingdata.nlp.utils.parser.mate.MateParserEN;
import org.backingdata.nlp.utils.parser.mate.ParsedSentence;

import it.uniroma1.lcl.babelfy.commons.BabelfyConfiguration;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyToken;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;


/**
 * Collection of utility methods to access Babelfy Disambiguation service.<br/>
 * Implements an in-memory cache of disambiguated texts.<br/><br/>
 * 
 * NB: when using the MateParser class to parse a sentence and idnetify tokens/POS, 
 * you need at least 3Gb of Java memory to execute the parser (-Xmx3000m).
 * 
 * @author Francesco Ronzano
 *
 */
public class BabelfyUtil {

	private static Map<String, List<SemanticAnnotation>> textCache = new HashMap<String, List<SemanticAnnotation>>();
	private static Map<String, List<SemanticAnnotation>> tokenCache = new HashMap<String, List<SemanticAnnotation>>();

	private static Logger logger = Logger.getLogger(BabelfyUtil.class);

	/**
	 * Disambiguate a list of sentences, where each sentence is constituted by a list of tokens of type
	 * {@link edu.upf.taln.common.babelnet.BabelToken BabelToken}.<br/>
	 * The language can be specified at method level by means of the 'lang' parameter or at token level.
	 * Token level language specification has precedence over method level language specification.
	 * 
	 * @param apiKey
	 * @param sentTokenList
	 * @param lang
	 * @return
	 */
	public static List<SemanticAnnotation> disambiguateTokenSentList(String apiKey, List<List<BabelToken>> sentTokenList, Language lang) {

		List<SemanticAnnotation> retList = new ArrayList<SemanticAnnotation>();

		if(StringUtils.isBlank(apiKey)) {
			logger.error("API key empty.");
			return retList;
		}
		else if(sentTokenList == null || sentTokenList.size() == 0) {
			logger.error("Sentence list empty.");
			return retList;
		}

		BabelfyConfiguration bconfig = it.uniroma1.lcl.babelfy.commons.BabelfyConfiguration.getInstance();
		bconfig.setRFkey(apiKey);
		try {
			bconfig.setConfigurationFile( new File(retList.getClass().getResource("/babelconf/babelfy.properties").toURI()) );
		} catch (URISyntaxException e) {
			logger.error("Impossible to set the proper configuration file of BabelNet");
		}

		// Instantiate BabelNet connection class
		BabelfyParameters bParam = new BabelfyParameters();
		bParam.setMultiTokenExpression(true);
		Babelfy bfy = new Babelfy(bParam);

		// Prepare token list
		List<BabelfyToken> tkList = new ArrayList<BabelfyToken>();

		String tokenCacheString = "";
		for(List<BabelToken> sentTokens : sentTokenList) {
			if(sentTokens != null && sentTokens.size() > 0) {
				for(BabelToken btk : sentTokens) {
					if(btk != null) {
						Language btk_language = (btk.getLang() != null) ? btk.getLang() : lang;
						BabelfyToken bt = new BabelfyToken(btk.getWord(), btk.getLemma(), btk.getPos(), btk_language);
						tkList.add(bt);
						tokenCacheString += " " + btk.getWord() + 
								"_" + ((btk.getLemma() != null) ? btk.getLemma() : "NOLEMMA") +
								"_" + ((btk.getPos() != null) ? btk.getPos() : "NOPOS") +
								"_" + ((btk_language != null) ? btk_language : "NOLANG");
					}
					else {
						logger.warn("Null sentence token!");
					}
				}
				tkList.add(BabelfyToken.EOS);
				tokenCacheString += " EOS";
			}
			else {
				logger.warn("Null sentence!");
			}
		}

		if(tokenCache.containsKey(computeStringDigest(tokenCacheString.getBytes()))) {
			logger.info("BabelNet info retrieved from cache (token cache size: " + tokenCache.size() + ")");
			return tokenCache.get(computeStringDigest(tokenCacheString.getBytes()));
		}

		List<SemanticAnnotation> bfyAnnotations = disambiguateTokenList(apiKey, tkList);

		tokenCache.put(computeStringDigest(tokenCacheString.getBytes()), bfyAnnotations);

		return bfyAnnotations;
	}

	/**
	 * Disambiguate a list of tokens belonging to the same or different sentences. Each sentence is constituted by a list of tokens of type
	 * {@link it.uniroma1.lcl.babelfy.commons.BabelfyToken BabelfyToken}.<br/>
	 * 
	 * @param apiKey
	 * @param sentTokens
	 * @return
	 */
	public static List<SemanticAnnotation> disambiguateTokenList(String apiKey, List<BabelfyToken> sentTokens) {

		List<SemanticAnnotation> retList = new ArrayList<SemanticAnnotation>();

		if(StringUtils.isBlank(apiKey)) {
			logger.error("API key empty.");
			return retList;
		}
		else if(sentTokens == null || sentTokens.size() == 0) {
			logger.error("Sentence list empty.");
			return retList;
		}

		BabelfyConfiguration bconfig = it.uniroma1.lcl.babelfy.commons.BabelfyConfiguration.getInstance();

		bconfig.setRFkey(apiKey);
		try {
			bconfig.setConfigurationFile( new File(retList.getClass().getResource("/babelconf/babelfy.properties").toURI()) );
		} catch (URISyntaxException e) {
			logger.error("Impossible to set the proper configuration file of BabelNet");
		}

		// Instantiate BabelNet connection class
		BabelfyParameters bParam = new BabelfyParameters();
		bParam.setMultiTokenExpression(true);
		Babelfy bfy = new Babelfy(bParam);

		for(int i = 0; i < sentTokens.size(); i++) {
			System.out.println("Token " + i + " " + sentTokens.get(i).getWord());
		}

		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(sentTokens, Language.EN);

		return bfyAnnotations;
	}

	/**
	 * Disambiguate a text, given an api key and the language parameter.
	 * 
	 * @param apiKey
	 * @param text
	 * @param lang
	 * @return
	 */
	public static List<SemanticAnnotation> disambiguateText(String apiKey, String text, Language lang) {

		List<SemanticAnnotation> retList = new ArrayList<SemanticAnnotation>();

		if(StringUtils.isBlank(apiKey)) {
			logger.error("API key empty.");
			return retList;
		}
		else if(StringUtils.isBlank(text)) {
			logger.error("Text to disambiguate empty.");
			return retList;
		}

		if(textCache.containsKey(computeStringDigest(text.getBytes()))) {
			logger.info("BabelNet info retrieved from cache (text cache size: " + textCache.size() + ")");
			return textCache.get(computeStringDigest(text.getBytes()));
		}

		BabelfyConfiguration bconfig = it.uniroma1.lcl.babelfy.commons.BabelfyConfiguration.getInstance();

		bconfig.setRFkey(apiKey);
		try {
			bconfig.setConfigurationFile( new File(retList.getClass().getResource("/babelconf/babelfy.properties").toURI()) );
		} catch (URISyntaxException e) {
			logger.error("Impossible to set the proper configuration file of BabelNet");
		}

		// Instantiate BabelNet connection class
		BabelfyParameters bParam = new BabelfyParameters();
		bParam.setMultiTokenExpression(true);
		Babelfy bfy = new Babelfy(bParam);


		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(text, Language.EN);

		textCache.put(computeStringDigest(text.getBytes()), bfyAnnotations);

		return bfyAnnotations;
	}

	/**
	 * Clean both in-memory token-list and sentence caches
	 * 
	 */
	public static void cleanCache() {
		textCache = new HashMap<String, List<SemanticAnnotation>>();
		tokenCache = new HashMap<String, List<SemanticAnnotation>>();
	}

	private static String computeStringDigest(byte[] byteArrayInput) {
		StringBuffer hexString = new StringBuffer();

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(byteArrayInput);
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				hexString.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("Digest computation error - " + e.getMessage());
			e.printStackTrace();
		}

		return hexString.toString();
	}

	/**
	 * Example of how to disambiguate three sentences by Babelfy.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Set resource folder
		Manage.setResourceFolder("/hlocal/path/to/resource/folder");

		// ***************************************
		// Disambiguate a text by Babelfy
		// ***************************************

		String inputText = "The Australian, 55, succeeds Stuart Lancaster after England's poor show at "
				+ "the World Cup and starts in December. In his first news conference, it was claimed he was called the 'devil' "
				+ "for how hard he worked former team Japan. Jones said: \"Every side is different. You can be a devil one day "
				+ "and an angel the next day. We don't know what I'm going to be. "
				+ "Following his four-year appointment as England's first foreign coach, Jones said there was \"extreme talent\" "
				+ "in the squad despite the team's worst performance at a World Cup. He added that he wanted his "
				+ "team to make Twickenham \"buzz\" again by tapping into a \"bulldog spirit\" to take on the world's best sides."
				+ "One thing you have to do is create your own unique style of play, the former Australia coach said. " 
				+ "\"We won't be copying the All Blacks. We will make our own style and I want the players to believe that 100%. "
				+ "\"We want the All Blacks to be watching how England play. That would be nice, wouldn't it?\""
				+ "Hart said that it was possible that a public memorial could be held at Auckland's 50,000-capacity Eden Park "
				+ "stadium, but added that the family were still in discussions with central and local government in New Zealand "
				+ "about what was most appropriate. \"I am delighted with the tremendous support we are getting from "
				+ "government and local government to celebrate Jonah's life,\" Hart added. \"We have agreed that there will be a "
				+ "public memorial service and that will be followed by a private family church service.\"";

		List<SemanticAnnotation> bfyAnnotations = BabelfyUtil.disambiguateText("72f9a677-054b-4d6c-a58b-8ab74988a615", inputText, Language.EN);

		for(SemanticAnnotation semAnnBabelnet : bfyAnnotations) {
			System.out.println("\n*******************************************");
			System.out.println("BabelNet URL: " + ((semAnnBabelnet.getBabelNetURL() != null) ? semAnnBabelnet.getBabelNetURL() : "NULL"));
			System.out.println("SynsetID: " + ((semAnnBabelnet.getBabelSynsetID() != null) ? semAnnBabelnet.getBabelSynsetID() : "NULL"));
			System.out.println("DBpedia URL: " + ((semAnnBabelnet.getDBpediaURL() != null) ? semAnnBabelnet.getDBpediaURL() : "NULL"));
			System.out.println("Global score: " + semAnnBabelnet.getGlobalScore());
			System.out.println("Coherence score: " + semAnnBabelnet.getCoherenceScore());
			System.out.println("Score: " + semAnnBabelnet.getScore());
			System.out.println("Source: " + ((semAnnBabelnet.getSource() != null) ? semAnnBabelnet.getSource() : "NULL"));
			System.out.println("Char offset fragment: " + ((semAnnBabelnet.getCharOffsetFragment() != null) ? semAnnBabelnet.getCharOffsetFragment() : "NULL"));
			System.out.println("Token offset fragment: " + ((semAnnBabelnet.getTokenOffsetFragment() != null) ? semAnnBabelnet.getTokenOffsetFragment() : "NULL"));
			System.out.println("*******************************************\n");
		}


		// ***************************************
		// Disambiguate a set of sentences by Babelfy by providing tokens/POS
		// ***************************************

		// Populate list of sentences to disambiguate
		List<List<BabelToken>> listSentences = new ArrayList<List<BabelToken>>();

		// Parse by Mate the first sentence to retrieve token-POS.
		ParsedSentence ps = MateParserEN.parseSentence("The new algorithm has been proposed by the first author.");
		List<BabelToken> listToken = new ArrayList<BabelToken>();
		for(int i = 0; i < ps.getToken().size(); i++) {
			String tokenPOS = ps.getPOSatIndex(i);
			PosTag pt = null;
			if(tokenPOS.startsWith("N")) {
				pt = PosTag.NOUN;
			}
			else if(tokenPOS.startsWith("J")) {
				pt = PosTag.ADJECTIVE;
			}
			else if(tokenPOS.startsWith("V")) {
				pt = PosTag.VERB;
			}
			else if(tokenPOS.startsWith("RB")) {
				pt = PosTag.ADVERB;
			}
			else {
				pt = PosTag.OTHER;
			}

			if(pt != null) {
				BabelToken bt = new BabelToken(i, ps.getTokenAtIndex(i), ps.getLemmaAtIndex(i), pt, Language.EN);
				listToken.add(bt);
			}
		}
		listSentences.add(listToken);

		// Parse by Mate the second sentence to retrieve token-POS.
		ps = MateParserEN.parseSentence("The big bang theory is based on ideas of Edwin Hubble.");
		listToken = new ArrayList<BabelToken>();
		for(int i = 0; i < ps.getToken().size(); i++) {
			String tokenPOS = ps.getPOSatIndex(i);
			PosTag pt = null;
			if(tokenPOS.startsWith("N")) {
				pt = PosTag.NOUN;
			}
			else if(tokenPOS.startsWith("J")) {
				pt = PosTag.ADJECTIVE;
			}
			else if(tokenPOS.startsWith("V")) {
				pt = PosTag.VERB;
			}
			else if(tokenPOS.startsWith("RB")) {
				pt = PosTag.ADVERB;
			}
			else {
				pt = PosTag.OTHER;
			}

			if(pt != null) {
				BabelToken bt = new BabelToken(i, ps.getTokenAtIndex(i), ps.getLemmaAtIndex(i), pt, Language.EN);
				listToken.add(bt);
			}
		}
		listSentences.add(listToken);

		// Parse by Mate the first sentence to retrieve token-POS.
		ps = MateParserEN.parseSentence("We have collected a lot of different butterflies.");
		listToken = new ArrayList<BabelToken>();
		for(int i = 0; i < ps.getToken().size(); i++) {
			String tokenPOS = ps.getPOSatIndex(i);
			PosTag pt = null;
			if(tokenPOS.startsWith("N")) {
				pt = PosTag.NOUN;
			}
			else if(tokenPOS.startsWith("J")) {
				pt = PosTag.ADJECTIVE;
			}
			else if(tokenPOS.startsWith("V")) {
				pt = PosTag.VERB;
			}
			else if(tokenPOS.startsWith("RB")) {
				pt = PosTag.ADVERB;
			}
			else {
				pt = PosTag.OTHER;
			}

			if(pt != null) {
				BabelToken bt = new BabelToken(i, ps.getTokenAtIndex(i), ps.getLemmaAtIndex(i), pt, Language.EN);
				listToken.add(bt);
			}
		}
		listSentences.add(listToken);

		List<SemanticAnnotation> semAnnList = disambiguateTokenSentList("72f9a677-054b-4d6c-a58b-8ab74988a615", listSentences, Language.EN);
		for(SemanticAnnotation semAnn : semAnnList) {
			System.out.println("\n*******************************************");
			System.out.println("BabelNet URL: " + ((semAnn.getBabelNetURL() != null) ? semAnn.getBabelNetURL() : "NULL"));
			System.out.println("SynsetID: " + ((semAnn.getBabelSynsetID() != null) ? semAnn.getBabelSynsetID() : "NULL"));
			System.out.println("DBpedia URL: " + ((semAnn.getDBpediaURL() != null) ? semAnn.getDBpediaURL() : "NULL"));
			System.out.println("Global score: " + semAnn.getGlobalScore());
			System.out.println("Coherence score: " + semAnn.getCoherenceScore());
			System.out.println("Score: " + semAnn.getScore());
			System.out.println("Source: " + ((semAnn.getSource() != null) ? semAnn.getSource() : "NULL"));
			System.out.println("Char offset fragment: " + ((semAnn.getCharOffsetFragment() != null) ? semAnn.getCharOffsetFragment() : "NULL"));
			System.out.println("Token offset fragment: " + ((semAnn.getTokenOffsetFragment() != null) ? semAnn.getTokenOffsetFragment() : "NULL"));
			System.out.println("*******************************************\n");
		}

	}

}
