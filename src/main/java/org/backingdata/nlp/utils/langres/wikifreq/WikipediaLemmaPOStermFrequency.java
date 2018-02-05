package org.backingdata.nlp.utils.langres.wikifreq;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.backingdata.nlp.utils.Manage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;


/**
 * Wikipedia lemma document frequency Utility Class
 * 
 * @author Francesco Ronzano
 *
 */
public class WikipediaLemmaPOStermFrequency {

	private static final Logger logger = LoggerFactory.getLogger(WikipediaLemmaPOStermFrequency.class);

	private static TObjectIntHashMap<String> wordIndexes_EN = null;
	private static TObjectIntHashMap<String> wordIndexes_ES = null;
	private static TObjectIntHashMap<String> wordIndexes_CA = null;

	private static List<WordOccurrenceCounter> occurrenceCounter_EN = null;
	private static List<WordOccurrenceCounter> occurrenceCounter_ES = null;
	private static List<WordOccurrenceCounter> occurrenceCounter_CA = null;


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

			TObjectIntHashMap<String> currentWordIndex = new TObjectIntHashMap<String>();
			List<WordOccurrenceCounter> currentOccurrenceCounter = new ArrayList<WordOccurrenceCounter>();

			switch(lang) {
			case English:
				if(wordIndexes_EN != null && occurrenceCounter_EN != null) {
					return;
				}
				tfidfFileName += "enwiki_lemma_POS_TF_DF.dat";
				wordIndexes_EN = currentWordIndex;
				occurrenceCounter_EN = currentOccurrenceCounter;
				break;
			case Spanish:
				if(wordIndexes_ES != null && occurrenceCounter_ES != null) {
					return;
				}
				tfidfFileName += "eswiki_lemma_POS_TF_DF.dat";
				wordIndexes_ES = currentWordIndex;
				occurrenceCounter_ES = currentOccurrenceCounter;
				break;
			case Catalan:
				if(wordIndexes_CA != null && occurrenceCounter_CA != null) {
					return;
				}
				tfidfFileName += "cawiki_lemma_POS_TF_DF.dat";
				wordIndexes_CA = currentWordIndex;
				occurrenceCounter_CA = currentOccurrenceCounter;
				break;
			default:
				if(wordIndexes_EN != null && occurrenceCounter_EN != null) {
					return;
				}
				tfidfFileName += "enwiki_lemma_POS_TF_DF.dat";
				wordIndexes_EN = currentWordIndex;
				occurrenceCounter_EN = currentOccurrenceCounter;
			}

			System.gc();

			File tfidfFile = new File(tfidfFileName);

			if(tfidfFile != null && tfidfFile.exists() && tfidfFile.isFile()) {

				System.out.println("Loading term frequency file: " + tfidfFile.getAbsolutePath() + "...");

				try(BufferedReader br = new BufferedReader(new FileReader(tfidfFile))) {
					int lineCount = 0;

					for(String line; (line = br.readLine()) != null; ) {
						line = line.trim();
						try {
							if(line.length() > 0) {
								String[] splitLine = line.split(" ");
								if(splitLine.length == 4) {
									String lemma = splitLine[0];
									String POS = splitLine[1];
									String termFrequ = splitLine[2];
									// String docFrequ = splitLine[3];

									if(lemma != null && lemma.trim().length() > 0 && 
											POS != null && POS.trim().length() > 0 &&
											termFrequ != null && termFrequ.trim().length() > 0) {

										lemma = lemma.toLowerCase().trim();

										WordOccurrenceCounter woc = null;
										if(currentWordIndex.containsKey(lemma)) {
											woc = currentOccurrenceCounter.get(currentWordIndex.get(lemma));
										}
										else {
											woc = new WordOccurrenceCounter();
											int positionToAdd = currentOccurrenceCounter.size();
											currentOccurrenceCounter.add(positionToAdd, woc);
											currentWordIndex.put(lemma, positionToAdd);
										}

										if(woc.getByPOS().containsKey(POS.trim().substring(0, 1))) {
											woc.getByPOS().put(POS.trim().substring(0, 1), woc.getByPOS().get(POS.trim().substring(0, 1)) + Integer.valueOf(termFrequ));
										}
										else {
											woc.getByPOS().put(POS.trim().substring(0, 1), Integer.valueOf(termFrequ));
										}

										if(++lineCount % 500000 == 0) {
											System.out.println("Lines processed: " + lineCount + " words. ");
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

				for(WordOccurrenceCounter woc : currentOccurrenceCounter) {
					woc.updateTotal();
				}

				logger.info("Loaded word frequencies of " + lang + " with: " + currentOccurrenceCounter.size() + " words.");

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
		wordIndexes_EN = null;
		wordIndexes_ES = null;
		wordIndexes_CA = null;

		occurrenceCounter_EN = null;
		occurrenceCounter_ES = null;
		occurrenceCounter_CA = null;

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
	 * Get the frequency of a lemma in a specific language (optionally by POS)
	 * 
	 * @param lang
	 * @param lemma
	 * @param POS if not null, only lemma with the specified POS will be considered
	 * @return
	 * @throws Exception
	 */
	public static Integer getLemmaOccurrencesCount(LangENUM lang, String lemma, String POS) throws Exception {

		if(lang == null) {
			throw new Exception("Please, specify a language");
		}
		
		if(lemma != null) {
			lemma = lemma.trim().toLowerCase().replace(" ", "_");

			loadLemmaPOStermFrequencyMap(lang);
			
			TObjectIntHashMap<String> currentWordIndex = new TObjectIntHashMap<String>();
			List<WordOccurrenceCounter> currentOccurrenceCounter = new ArrayList<WordOccurrenceCounter>();

			switch(lang) {
			case English:
				currentWordIndex = wordIndexes_EN;
				currentOccurrenceCounter = occurrenceCounter_EN;
				break;
			case Spanish:
				currentWordIndex = wordIndexes_ES;
				currentOccurrenceCounter = occurrenceCounter_ES;
				break;
			case Catalan:
				currentWordIndex = wordIndexes_CA;
				currentOccurrenceCounter = occurrenceCounter_CA;
				break;
			default:
				currentWordIndex = wordIndexes_EN;
				currentOccurrenceCounter = occurrenceCounter_EN;
			}

			if(currentWordIndex.containsKey(lemma) && currentWordIndex.get(lemma) < currentOccurrenceCounter.size() &&
					currentOccurrenceCounter.get(currentWordIndex.get(lemma)) != null) {

				if(POS != null && POS.trim().length() > 0) {
					TObjectIntHashMap<String> POSmap = currentOccurrenceCounter.get(currentWordIndex.get(lemma)).getByPOS();
					if(POSmap.contains(POS.trim().substring(0, 1))) {
						return POSmap.get(POS.trim().substring(0, 1));
					}
					else {
						return 0;
					}
				}
				else {
					return currentOccurrenceCounter.get(currentWordIndex.get(lemma)).getTotal();
				}
			}

		}

		return 0;
	}


	public static Map<String, Integer> getLemmaOccurrencesCountByPOS(LangENUM lang, String lemma) throws Exception {

		if(lang == null) {
			throw new Exception("Please, specify a language");
		}
		
		Map<String, Integer> retMap = new HashMap<String, Integer>();

		if(lemma != null) {
			lemma = lemma.trim().toLowerCase().replace(" ", "_");

			loadLemmaPOStermFrequencyMap(lang);
			
			TObjectIntHashMap<String> currentWordIndex = new TObjectIntHashMap<String>();
			List<WordOccurrenceCounter> currentOccurrenceCounter = new ArrayList<WordOccurrenceCounter>();

			switch(lang) {
			case English:
				currentWordIndex = wordIndexes_EN;
				currentOccurrenceCounter = occurrenceCounter_EN;
				break;
			case Spanish:
				currentWordIndex = wordIndexes_ES;
				currentOccurrenceCounter = occurrenceCounter_ES;
				break;
			case Catalan:
				currentWordIndex = wordIndexes_CA;
				currentOccurrenceCounter = occurrenceCounter_CA;
				break;
			default:
				currentWordIndex = wordIndexes_EN;
				currentOccurrenceCounter = occurrenceCounter_EN;
			}

			if(currentWordIndex.containsKey(lemma) && currentWordIndex.get(lemma) < currentOccurrenceCounter.size() &&
					currentOccurrenceCounter.get(currentWordIndex.get(lemma)) != null) {

				TObjectIntHashMap<String> POSmap = currentOccurrenceCounter.get(currentWordIndex.get(lemma)).getByPOS();
				if(POSmap != null && POSmap.size() > 0) {
					TObjectIntIterator<String> it = POSmap.iterator();
					while(it.hasNext()){
						it.advance();
						retMap.put(it.key(), it.value());
					}
				}
			}

		}

		return retMap;
	}


	public static void main(String[] args) {
		Manage.setResourceFolder("/home/ronzano/Downloads/NLPutils-resources-1.0");

		try {

			// Get lemma POS map of the word COCHE in Spanish
			System.out.println("Word coche - POS distribution: " + WikipediaLemmaPOStermFrequency.getLemmaOccurrencesCountByPOS(LangENUM.Spanish, "coche"));

			// Get the total number of occurrences of the word COCHE in Spanish
			System.out.println("Word coche - total occurrences: " + WikipediaLemmaPOStermFrequency.getLemmaOccurrencesCount(LangENUM.Spanish, "coche", null));

			// Get lemma POS map of the word CAR in English
			System.out.println("Word car - POS distribution: " + WikipediaLemmaPOStermFrequency.getLemmaOccurrencesCountByPOS(LangENUM.English, "car"));

			// Get the total number of occurrences of the word CAR in English
			System.out.println("Word car - total occurrences: " + WikipediaLemmaPOStermFrequency.getLemmaOccurrencesCount(LangENUM.English, "car", null));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}