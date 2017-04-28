package org.backingdata.nlp.utils.topiclda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;

/**
 * This class collects a set of static methods useful to perform LDA topic modeling by relying on <a href="http://mallet.cs.umass.edu/">Mallet</a>.<br/><br/>
 * 
 * This class relies on the Mallet simple parallel threaded implementation of LDA (cc.mallet.topics.ParallelTopicModel), 
 * following Newman, Asuncion, Smyth and Welling, Distributed Algorithms for Topic Models JMLR (2009), with SparseLDA sampling scheme 
 * and data structure from Yao, Mimno and McCallum, Efficient Methods for Topic Model Inference on Streaming Document Collections, KDD (2009).<br/><br/>
 * 
 * Refer to the main method implementation to get an example of use.<br/><br/>
 * 
 * 
 * @author Francesco Ronzano
 *
 */
public class Lda {

	/**
	 * Read file to string
	 * 
	 * @param fileAbsolutePathName
	 * @return
	 * @throws IOException
	 */
	private static String readFile(String fileAbsolutePathName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileAbsolutePathName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	/**
	 * Read all the .txt files in the dir basePath and returns a list of strings, one per file
	 * 
	 * @param basePath
	 * @return
	 */
	private static Map<String, String> readDocuments(String basePath) {
		Map<String, String> retMap = new TreeMap<String, String>();

		File baseDir = new File(basePath);

		File[] filesInDir = baseDir.listFiles();
		for(File fileInDir : filesInDir) {
			if(fileInDir != null && fileInDir.exists() && fileInDir.isFile() && fileInDir.length() > 0l && fileInDir.getName().endsWith(".txt")) {
				try {
					String fileContent = readFile(fileInDir.getAbsolutePath());
					if(fileContent != null && !fileContent.trim().equals("")) {
						retMap.put(fileInDir.getName(), fileContent);
					}
				}
				catch(Exception e) {
					/* Do nothing */
				}
			}
		}

		return retMap;
	}

	/**
	 * Get text processing pipe
	 * 
	 * @return
	 */
	private static ArrayList<Pipe> pipes = null;
	public static ArrayList<Pipe> getPipes() {
		if(pipes != null) {
			return pipes;
		}

		pipes = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipes.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipes.add(new TokenSequenceLowercase());
		pipes.add(new TokenSequenceRemoveStopwords(new File("/home/francesco/Downloads/SEPLN/TOPIC_ANALYSIS_v3/res/stopwords/stopwords_ES.list"), "UTF-8", false, false, false));
		pipes.add(new TokenSequence2FeatureSequence());

		return pipes;
	}

	/**
	 * Process file textual contents and returns InstanceList to train LDA
	 * 
	 * @param texts
	 * @return
	 * @throws IOException
	 */
	private static InstanceList createInstanceList(List<String> texts) throws IOException {
		ArrayList<Pipe> pipes = getPipes();

		InstanceList instanceList = new InstanceList(new SerialPipes(pipes));
		instanceList.addThruPipe(new ArrayIterator(texts));
		return instanceList;
	}
	
	/**
	 * Create and train an LDA model that is returned, given a collection of documents
	 * 
	 * @param textsMap map of fileID (key) ---> file contents
	 * @param numTopics number of topics
	 * @param numThreads number of threads to use
	 * @param numIterations number of LDA iterations
	 * @return
	 * @throws IOException
	 */
	public static ParallelTopicModel createNewModel(Map<String, String> textsMap, int numTopics, int numThreads, int numIterations) throws IOException {
		// STEP 1: read documents and create instance of InstanceList
		List<String> textsList = new ArrayList<String>(textsMap.values());

		int count = 0;
		for(Entry<String, String> textsMapEntry : textsMap.entrySet()) {
			if(!textsMapEntry.getValue().equals(textsList.get(count))) {
				System.out.println("ERROR: not preserved order!");
			}
			count++;
		}

		InstanceList instanceList = createInstanceList(textsList);

		// STEP 2: create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		//  > Note that the first parameter is passed as the sum over topics, while
		//    the second is the parameter for a single dimension of the Dirichlet prior.
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		// STEP 3: define the set of document instances to use to build the model
		model.addInstances(instanceList);

		// STEP 4: use two parallel samplers, which each look at one half the corpus and combine
		// statistics after every iteration.
		model.setNumThreads(numThreads);

		// STEP 5: run the model for 50 iterations and stop (this is for testing only, 
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(numIterations);

		model.estimate();

		return model;
	}

	/**
	 * Store a model to a file
	 * 
	 * @param model
	 * @param fullPathToStoreModel
	 */
	public static void storeModelToFile(ParallelTopicModel model, String fullPathToDir, String modelName) {
		File file = new File(fullPathToDir, modelName);
		if(file.exists()) {
			file.delete();
		}

		model.write(file);
	}

	/**
	 * Read a model from a file
	 * 
	 * @param fullPathToStoreModel
	 * @return
	 * @throws Exception 
	 */
	public static ParallelTopicModel readModelFromFile(String fullPathToDir, String modelName) throws Exception {
		File file = new File(fullPathToDir, modelName);
		if(file.exists()) {
			return ParallelTopicModel.read(file);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Given a model, get a list of the most frequent words for each topic
	 * @param model
	 * @param numTopWords topN-words per topic to retrieve
	 * @param printOut if true print topN-word per topic on standard output
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getSortedListOfWordForEachTopic(ParallelTopicModel model, int numTopWords, boolean printOut) throws Exception {
		List<List<String>> retList = new ArrayList<List<String>>();

		int topicID = 0;
		for(Object[] topWords : model.getTopWords(numTopWords)) {
			List<String> topicWordList = new ArrayList<String>();
			if(printOut) System.out.print("\nTOPIC " + topicID++ + ": ");
			for (Object s : topWords) {
				String sStr = (String) s;
				if(printOut) System.out.print(sStr + ", ");
				topicWordList.add(sStr);
			}
			retList.add(topicWordList);
		}

		return retList;
	}
	
	/**
	 * Given a model and a text, infer topic distribution
	 * @param model
	 * @param text
	 * @param printOut if true print topic distribution on standard output
	 * @return map with key topicID and value the probability (ordered by decreasing values)
	 */
	public static Map<Integer, Double> getTopicDistribution(ParallelTopicModel model, String text, boolean printOut) {
		ArrayList<Pipe> pipes = getPipes();

		// Create a new instance named "test instance" with empty target and source fields.
		InstanceList instanceList = new InstanceList(new SerialPipes(pipes));
		List<String> textList = new ArrayList<String>();
		textList.add(text);
		instanceList.addThruPipe(new ArrayIterator(textList));


		TopicInferencer inferencer = model.getInferencer();
		double[] topicProbs = inferencer.getSampledDistribution(instanceList.get(0), 1, 10, 5); // 100, 10, 10);

		Map<Integer, Double> retMap = new HashMap<Integer, Double>();
		for(int i = 0; i < topicProbs.length; i++) {
			double topicProb = topicProbs[i];
			retMap.put(i, topicProb);
		}

		retMap = sortByValueDec(retMap);

		if(printOut) {
			List<List<String>> topicWords = new ArrayList<List<String>>();
			try {
				topicWords = getSortedListOfWordForEachTopic(model, 5, false);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for(Entry<Integer, Double> topicElement : retMap.entrySet()) {
				System.out.print("\nTOPIC " + topicElement.getKey() + " > " + topicElement.getValue() + " --- " + ((topicWords != null && topicWords.size() > topicElement.getKey() && topicWords.get(topicElement.getKey()) != null) ? topicWords.get(topicElement.getKey()) : "NULL"));
			}
		}

		return retMap;
	}

	/**
	 * Sort a map by decreasing value
	 * 
	 * @param map
	 * @return
	 */
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDec( Map<K, V> map ) {
		Map<K,V> result = new LinkedHashMap<>();
		Stream <Entry<K,V>> st = map.entrySet().stream();
		st.sorted(new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		}).forEach(e ->result.put(e.getKey(),e.getValue()));
		return result;
	}

	
	public static void main(String[] args) throws Exception {
		// SET PARAM: directory where the there is the collection of documents to learn topics from - one textual document per file (UTF-8) 
		String baseDirOfDocs = "/local/path/to/document/folder";
		// SET PARAM: directory where learned topic models should be stored
		String baseDirOfModels = "/local/path/to/store/models";
		
		// SET PARAM: ID common to all model names
		String modelID = "MODEL_V1";
		// SET PARAM: number of topics
		int numTopics = 50;
		// SET PARAM: number of iterations
		int numIterations = 1400;
		// SET PARAM: number of threads
		int numThreads = 2;
		
		
		// Load text files and create new LDA model
		Map<String, String> textsMap = readDocuments(baseDirOfDocs);
		ParallelTopicModel topicModelInstance = Lda.createNewModel(textsMap, numTopics, numThreads, numIterations);
		
		// *** How to persist models? ***
		// Store LDA model to file
		Lda.storeModelToFile(topicModelInstance, baseDirOfModels, modelID + "_" + numTopics + "topics_" + numIterations + "iters.model");
		
		// Read stored model from file
		// ParallelTopicModel topicModelInstanceRead = Lda.readModelFromFile(baseDirOfModels, modelID + "_" + numTopics + "topics_" + numIterations + "iters.model");

		// *** How to use models? ***
		// Topic distribution of new doc
		String newDoc = "Our intention is generating the right mixture of audio, speech and language technologies with big data ones. Some audio, speech and language automatic technologies are available or gaining enough degree of maturity as to be able to help to this objective: automatic speech transcription, query by spoken example, spoken information retrieval, natural language processing, unstructured multimedia contents transcription and description, multimedia files summarization, spoken emotion detection and sentiment analysis, speech and text understanding, etc. They seem to be worthwhile to be joined and put at work on automatically captured data streams coming from several sources of information like YouTube, Facebook, Twitter, online newspapers, web search engines, etc. to automatically generate reports that include both scientific based scores and subjective but relevant summarized statements on the tendency analysis and the perceived satisfaction of a product, a company or another entity by the general population.";
		Map<Integer, Double> topicIDdistValueMap = Lda.getTopicDistribution(topicModelInstance, newDoc, true);
		
		// Get top-10 words for each topic
		List<List<String>> topWords = Lda.getSortedListOfWordForEachTopic(topicModelInstance, 10, true);
		
	}

}