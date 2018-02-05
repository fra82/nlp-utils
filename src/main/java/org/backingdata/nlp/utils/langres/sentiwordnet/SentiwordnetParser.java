package org.backingdata.nlp.utils.langres.sentiwordnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.backingdata.nlp.utils.Manage;


/**
 * SentiWordnet 3.0 (http://sentiwordnet.isti.cnr.it/) in-memory reader with static utility methods.
 * 
 * 
 * @author Francesco Ronzano
 *
 */
public class SentiwordnetParser {

	private static String sentiWordnet = "SentiWordNet-3.0" + File.separator + "SentiWordNet_3.0.0_20130122.txt";

	// Enable logging
	private static boolean enableLogging = false;

	// Corpus data structures
	private static Map<String, SentiWordnetElem> synMap = new HashMap<String, SentiWordnetElem>();

	public static void initSentiwordnetParser() {
		loadSentiWordnet();
	}

	private static void loadSentiWordnet() {
		System.out.println("Initializing SentiWordnet...");

		Integer entryCount = 0;
		Integer exceptionCount = 0;

		Long startTime = System.currentTimeMillis();
		String str = null;
		try {
			
			System.out.println("Loading SentiWordnet dictionary data from: " + Manage.getResourceFolder() + File.separator + sentiWordnet);
			
			BufferedReader stdInput = new BufferedReader(new FileReader(Manage.getResourceFolder() + File.separator + sentiWordnet));
			while ((str = stdInput.readLine()) != null) {
				try {
					str = str.trim();
					if(!str.startsWith("#")) {
						try {
							String[] splittedSynset = str.split("\t");
							String[] splittedSynsetNorm = Arrays.copyOf(splittedSynset, 5);

							if(splittedSynsetNorm[0] != null && !splittedSynsetNorm[0].trim().equals("") &&
									splittedSynsetNorm[1] != null && !splittedSynsetNorm[1].trim().equals("")) {

								// POS ID PosScore NegScore SynsetTerms Gloss

								String sid = splittedSynsetNorm[1].trim() + "-" + splittedSynsetNorm[0].trim();

								String posScoreStr = (splittedSynsetNorm[2] != null && !splittedSynsetNorm[2].trim().equals("")) ? splittedSynsetNorm[2].trim() : null;
								Double posScoreDouble = null;
								if(posScoreStr != null) {
									try {
										posScoreDouble = Double.valueOf(posScoreStr);
									} catch (NumberFormatException e) {
										System.out.println("Error converting to double the positivity score: " + posScoreStr);
									}
								}

								String negScoreStr = (splittedSynsetNorm[3] != null && !splittedSynsetNorm[3].trim().equals("")) ? splittedSynsetNorm[3].trim() : null;
								Double negScoreDouble = null;
								if(negScoreStr != null) {
									try {
										negScoreDouble = Double.valueOf(negScoreStr);
									} catch (NumberFormatException e) {
										System.out.println("Error converting to double the positivity score: " + negScoreStr);
									}
								}

								Double objScoreDouble = null;
								if(posScoreDouble != null && negScoreDouble != null) {
									// ObjScore = 1 - (PosScore + NegScore)
									objScoreDouble = 1 - (posScoreDouble + negScoreDouble);
								}

								String synsetTerms = (splittedSynsetNorm[4] != null && !splittedSynsetNorm[4].trim().equals("")) ? splittedSynsetNorm[4].trim() : null;

								SentiWordnetElem newElem = new SentiWordnetElem(sid, posScoreDouble, negScoreDouble, objScoreDouble, synsetTerms, null);
								synMap.put(sid, newElem);
								entryCount++;
								// logger.info("(" + sid + ", pos: " + ((posScoreDouble != null) ? posScoreDouble : "NO") + ". neg: " + ((negScoreDouble != null) ? negScoreDouble : "NO") + ")");
							}
						} catch (Exception e) {
							System.out.println("ERROR PARSING LINE: " + str);
							exceptionCount++;
						}
					}
				} catch (Exception e) {
					System.out.println("ERROR PARSING LINE: " + str);
					exceptionCount++;
				}              
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Loaded Sentiwordnet");
		System.out.println("Entries: " + entryCount);
		System.out.println("Exception count: " + exceptionCount);

		Long endTime = System.currentTimeMillis();
		System.out.println("Data loaded in " + (endTime - startTime) + " milliseconds.");

		System.out.println("*******************************");
	}

	// Utility methods
	public static SentiWordnetElem getSentimentInfoFromENSynsetID(String synID_EN) {
		if(synID_EN != null) {
			if(synMap.containsKey(synID_EN)) {
				if(enableLogging) {
					System.out.println("Retrieved SentiWordnet entry: " + synMap.get(synID_EN));
				}
				return synMap.get(synID_EN);
			} 
			else {
				if(enableLogging) {
					System.out.println("Not retrieved mapped synset id (English).");
				}
				return null;
			}
		}
		return null;
	}


	public static void main(String[] args) {
		// Set resource folder
		Manage.setResourceFolder("/home/ronzano/Downloads/NLPutils-resources-1.0");
		
		// Init: load data in memory
		SentiwordnetParser.initSentiwordnetParser();

		// Get sentiment scores of synset
		String id = "02772202-v";
		SentiWordnetElem swElem = SentiwordnetParser.getSentimentInfoFromENSynsetID(id);
		
		System.out.println(id + " <-- synset id | pos score --> " + swElem.getPosScore());
		System.out.println(id + " <-- synset id | neg score --> " + swElem.getNegScore());
		System.out.println(id + " <-- synset id | obj score --> " + swElem.getObjScore());
		System.out.println(id + " <-- synset id | gloss --> " + swElem.getGloss());
		System.out.println(id + " <-- synset id | terms --> " + swElem.getTerms());
	}


}
