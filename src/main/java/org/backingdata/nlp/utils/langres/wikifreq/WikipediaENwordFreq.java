package org.backingdata.nlp.utils.langres.wikifreq;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.Manage;

import au.com.bytecode.opencsv.CSVReader;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * File with word frequency lists of wikipedia extracted from the following web:<P>
 * Calculating Word and N-Gram Statistics from a Wikipedia Corpora By Richard Marsden, on April 16th, 2012<P>
 * http://www.monlp.com/2012/04/16/calculating-word-and-n-gram-statistics-from-a-wikipedia-corpora/<P>
 * 
 * This class provides the possibility to get the frequency of a word in the English Wikipedia.<P>
 * All words are trimmed and lowercased.<P>
 * 
 * Word frequencies are loaded in memory the first time the method {@link #getWordFrequency(String) getWordFrequency} is called.<P>
 * 
 * @author Francesco Ronzano
 *
 */
public class WikipediaENwordFreq {

	private static Logger logger = Logger.getLogger(Manage.class);

	// private static Map<String, Integer> wordCount = new HashMap<String, Integer>();
	private static TObjectIntHashMap<String> wordCount = new TObjectIntHashMap<String>();
	private static Map<Integer, Integer> wordFreqNumWordsMap = new HashMap<Integer, Integer>();
	
	private static final String fileRelPath = "frequencies" + File.separator + "wikipedia" + File.separator + "wikipedia_wordfreq.txt";

	/**
	 * Force the loading of word frequencies into memory.
	 * 
	 * @return
	 */
	public static boolean init() {

		free();

		logger.info("Loading Wikipedia dictionary data from: " + Manage.getResourceFolder() + File.separator + fileRelPath);

		Integer lineCount = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(Manage.getResourceFolder() + File.separator + fileRelPath), '\t');

			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				try  {
					lineCount++;
					if(nextLine.length == 2) {
						String key = nextLine[0];
						Integer value = Integer.valueOf(nextLine[1]);

						wordCount.put(key, value);
						
						if(!wordFreqNumWordsMap.containsKey(value)) {
							wordFreqNumWordsMap.put(value, 1);
						}
						else {
							wordFreqNumWordsMap.put(value, wordFreqNumWordsMap.get(value) + 1);
						}
					}
					else {
						logger.error("Impossible to read Wikipedia dictionary line number (length != 2): " + lineCount);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					logger.error("Impossible to read Wikipedia dictionary line number: " + lineCount);
				}
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Impossible to read the Wikipedia dictionary file!");
		}

		logger.info("Loaded " + wordCount.size() + " lowerased/trimmed wikipedia tokens.");

		return false;
	}

	/**
	 * Free the memory from word frequencies.
	 * 
	 */
	public static void free() {
		wordCount = new TObjectIntHashMap<String>();
		wordFreqNumWordsMap = new HashMap<Integer, Integer>();
		System.gc();
	}

	/**
	 * Get word frequency (word is trimmed and lowercased)
	 * 
	 * @param word
	 * @return
	 */
	public static Integer getWordFrequency(String word) {
		if(wordCount == null || wordCount.size() == 0) {
			init();
		}

		if(word != null && !word.equals("") && wordCount.containsKey(word.trim().toLowerCase())) {
			return wordCount.get(word.trim().toLowerCase());
		}
		else {
			return 0;
		}
	}

	/**
	 * Get the number of words with a frequency lower or equal than the current one
	 * 
	 * @param word
	 * @return if word is null or does not exist, the total number of words is returned
	 */
	public static Integer getNumLessFreqWords(String word) {
		if(wordCount == null || wordCount.size() == 0) {
			init();
		}

		if(word != null && !word.equals("")) {
			int currentWordFreq = wordCount.get(word.trim().toLowerCase());
			
			int totalCountWordLesseqFreq = 0;
			for(Entry<Integer, Integer> wordFreqNumWordsEntry : wordFreqNumWordsMap.entrySet()) {
				if(wordFreqNumWordsEntry.getKey() <= currentWordFreq) {
					totalCountWordLesseqFreq += wordFreqNumWordsEntry.getValue();
				}
			}
			
			return totalCountWordLesseqFreq;
		}
		else {
			return wordCount.size();
		}
	}

	public static void main(String[] args) {
		// Set resource folder
		Manage.setResourceFolder("/hlocal/path/to/resource/folder");
		
		// Init: load data in memory
		WikipediaENwordFreq.init();
		
		// Query word frequency
		System.out.println("Word frequency: " + WikipediaENwordFreq.getWordFrequency("car"));
		
	}

}
