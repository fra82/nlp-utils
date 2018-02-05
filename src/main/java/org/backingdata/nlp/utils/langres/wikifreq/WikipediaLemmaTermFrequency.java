package org.backingdata.nlp.utils.langres.wikifreq;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.backingdata.nlp.utils.Manage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.TObjectIntHashMap;


/**
 * Wikipedia lemma document frequency Utility Class
 * 
 * @author Francesco Ronzano
 *
 */
public class WikipediaLemmaTermFrequency {

	private static final Logger logger = LoggerFactory.getLogger(WikipediaLemmaTermFrequency.class);

	private static TObjectIntHashMap<String> wordCounter_EN = null;
	private static TObjectIntHashMap<String> wordCounter_ES = null;
	private static TObjectIntHashMap<String> wordCounter_CA = null;
	
	/**
	 * Load data from Wikipedia
	 * 
	 * @param lang
	 * @param addVariants
	 * @throws Exception
	 */
	private static void loadLemmaPOStermFrequencyMap(LangENUM lang) throws Exception {


		if(lang != null) {

			String tfidfFileName = Manage.getResourceFolder() + File.separator + "frequencies/wikipedia/";

			TObjectIntHashMap<String> currentWordCounter = new TObjectIntHashMap<String>();

			switch(lang) {
			case English:
				if(wordCounter_EN != null) {
					return;
				}
				tfidfFileName += "enwiki_lemma_POS_TF_DF.dat";
				wordCounter_EN = currentWordCounter;
				break;
			case Spanish:
				if(wordCounter_ES != null) {
					return;
				}
				tfidfFileName += "eswiki_lemma_POS_TF_DF.dat";
				wordCounter_ES = currentWordCounter;
				break;
			case Catalan:
				if(wordCounter_CA != null) {
					return;
				}
				tfidfFileName += "cawiki_lemma_POS_TF_DF.dat";
				wordCounter_CA = currentWordCounter;
				break;
			default:
				if(wordCounter_EN != null) {
					return;
				}
				tfidfFileName += "enwiki_lemma_POS_TF_DF.dat";
				wordCounter_EN = currentWordCounter;
			}

			System.gc();

			File tfidfFile = new File(tfidfFileName);

			if(tfidfFile != null && tfidfFile.exists() && tfidfFile.isFile()) {

				System.out.println("Loading term frequency file: " + tfidfFile.getAbsolutePath() + "...");

				try(BufferedReader br = new BufferedReader(new FileReader(tfidfFile))) {
					
					for(String line; (line = br.readLine()) != null; ) {
						line = line.trim();
						try {
							if(line.length() > 0) {
								String[] splitLine = line.split(" ");
								if(splitLine.length == 4) {
									String lemma = splitLine[0];
									// String POS = splitLine[1];
									String termFrequ = splitLine[2];
									// String docFrequ = splitLine[3];

									if(lemma != null && lemma.trim().length() > 0 && 
											termFrequ != null && termFrequ.trim().length() > 0) {

										lemma = lemma.toLowerCase().trim();
										
										if(currentWordCounter.containsKey(lemma)) {
											currentWordCounter.put(lemma, currentWordCounter.get(lemma) +  Integer.valueOf(termFrequ));
										}
										else {
											currentWordCounter.put(lemma, Integer.valueOf(termFrequ));
										}
									}

								}
							}
						}
						catch(Exception e) {
							/* Do nothing */
						}
					}
				} catch (IOException e) {
					throw new Exception("Impossible to read tfidf list for " + lang + " from file: '" +
							((tfidfFileName != null) ? tfidfFileName : "NULL")+ "' - " + e.getMessage());
				}
				
				logger.info("Loaded word frequencies of " + lang + " with: " + currentWordCounter.size() + " words.");

			}
			else {
				throw new Exception("Impossible to read TFIDF for " + lang + " from file: '" +
						((tfidfFileName != null) ? tfidfFileName : "NULL")+ "'");
			}
		}
		else {
			throw new Exception("Specify a language to load a tfidf word list.");
		}

		return;
	}

	/**
	 * Free the memory from word frequencies.
	 * 
	 */
	public static void freeAll() {
		wordCounter_EN = null;
		wordCounter_ES = null;
		wordCounter_CA = null;

		System.gc();
	}

	/*
	private static Integer getTotNumDoc(LangENUM lang) throws Exception {

		if(lang == null) {
			throw new Exception("Please, specify a language");
		}

		switch(lang) {
		case English:
			return 4487682;
		case Spanish:
			return 1061535;
		case Catalan:
			return 450885;
		default:
			return null;
		}
	}
	 */

	/**
	 * Get the frequency of a lemma in a specific language
	 * 
	 * @param lang
	 * @param lemma
	 * @return
	 * @throws Exception
	 */
	public static Integer getLemmaOccurrencesCount(LangENUM lang, String lemma) throws Exception {

		if(lang == null) {
			throw new Exception("Please, specify a language");
		}
		
		if(lemma != null) {
			lemma = lemma.trim().toLowerCase().replace(" ", "_");

			loadLemmaPOStermFrequencyMap(lang);
			
			TObjectIntHashMap<String> currentWordCounter = new TObjectIntHashMap<String>();

			switch(lang) {
			case English:
				currentWordCounter = wordCounter_EN;
				break;
			case Spanish:
				currentWordCounter = wordCounter_ES;
				break;
			case Catalan:
				currentWordCounter = wordCounter_CA;
				break;
			default:
				currentWordCounter = wordCounter_EN;
			}

			if(currentWordCounter.containsKey(lemma)) {
				return currentWordCounter.get(lemma);
			}

		}

		return 0;
	}
	

	public static void main(String[] args) {
		Manage.setResourceFolder("/home/ronzano/Downloads/NLPutils-resources-1.0");

		try {

			// Get the total number of occurrences of the word COCHE in Spanish
			System.out.println("Word coche - total occurrences (Spanish Wikipedia): " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.Spanish, "coche"));
			
			// Get the total number of occurrences of the word COCHE in Spanish
			System.out.println("Word cotxe - total occurrences (Catalan Wikipedia): " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.Catalan, "cotxe"));
			
			// Get the total number of occurrences of the word CAR in English
			System.out.println("Word car - total occurrences: " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.English, "car"));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}