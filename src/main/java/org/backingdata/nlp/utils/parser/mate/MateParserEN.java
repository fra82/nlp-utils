package org.backingdata.nlp.utils.parser.mate;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.backingdata.nlp.utils.Manage;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import se.lth.cs.srl.Parse;
import se.lth.cs.srl.SemanticRoleLabeler;
import se.lth.cs.srl.corpus.Predicate;
import se.lth.cs.srl.corpus.Sentence;
import se.lth.cs.srl.corpus.Word;
import se.lth.cs.srl.languages.Language;
import se.lth.cs.srl.options.CompletePipelineCMDLineOptions;
import se.lth.cs.srl.pipeline.Pipeline;
import se.lth.cs.srl.pipeline.Reranker;
import se.lth.cs.srl.pipeline.Step;
import se.lth.cs.srl.preprocessor.Preprocessor;


/**
 * English Mate-tools parser (pos tagger, lemmatizer, dep parser and semantic role labeller)
 * REF: https://code.google.com/p/mate-tools/<br/>
 * 
 * This class provides methods to analyze an English sentence (as a string or list of tokens).<br/>
 * 
 * NB: you need at least 3Gb of Java memory to execute the parser (-Xmx3000m).
 * 
 * @author Francesco Ronzano
 */
public class MateParserEN {

	private static Logger logger = Logger.getLogger(MateParserEN.class);

	private static Integer maxSentenceLengthTokens = 250;

	private static Preprocessor pp = null;

	private static SemanticRoleLabeler srl = null;

	private static Tokenizer tok = null;

	/**
	 * Force the loading of the parser into memory.
	 * 
	 * @param force
	 * @return
	 */
	public static boolean init(boolean force) {
		try {

			if(pp != null && tok != null && !force && srl != null) {
				return true;
			}

			free();

			TokenizerModel model = new TokenizerModel(new File(Manage.getResourceFolder() + File.separator + "openNLP_models" + File.separator + "en-token.bin"));
			tok = new TokenizerME(model);

			List<String> argumentList = new ArrayList<String>();
			argumentList.add("eng");

			argumentList.add("-lemma");
			argumentList.add(Manage.getResourceFolder() + File.separator + "mate_models/" + "CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model");
			argumentList.add("-tagger");
			argumentList.add(Manage.getResourceFolder() + File.separator + "mate_models/" + "CoNLL2009-ST-English-ALL.anna-3.3.postagger.model");
			argumentList.add("-parser");
			argumentList.add(Manage.getResourceFolder() + File.separator + "mate_models/" + "CoNLL2009-ST-English-ALL.anna-3.3.parser.model");
			argumentList.add("-srl");
			argumentList.add(Manage.getResourceFolder() + File.separator + "mate_models/" + "CoNLL2009-ST-English-ALL.anna-3.3.srl-4.1.srl.model");

			// Set options
			String[] arguments = argumentList.toArray(new String[argumentList.size()]);

			CompletePipelineCMDLineOptions options = new CompletePipelineCMDLineOptions();
			options.parseCmdLineArgs(arguments);

			pp = Language.getLanguage().getPreprocessor(options);
			Parse.parseOptions = options.getParseOptions();

			if (options.reranker) {
				srl = new Reranker(Parse.parseOptions);
			} else {
				ZipFile zipFile = new ZipFile(Parse.parseOptions.modelFile);
				if (Parse.parseOptions.skipPI) {
					srl = Pipeline.fromZipFile(zipFile, new Step[] { Step.pd, Step.ai, Step.ac });
				} else {
					srl = Pipeline.fromZipFile(zipFile);
				}
				zipFile.close();
			}

		} catch (Exception e) {
			logger.error("Error initializing Mate parser: " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Free the memory from the parser.
	 * 
	 */
	public static void free() {
		pp = null;
		tok = null;
		srl = null;
		System.gc();
	}

	/**
	 * Parse a sentence string - should be terminated with a full stop or a question/exclamation mark, 
	 * if not a full stop is added at the end of the sentence.
	 * The sentence is tokenized by OpenNLP English tokenizer.
	 * 
	 * @param sentence
	 * @return
	 */
	public static ParsedSentence parseSentence(String sentence) {

		init(false);

		ParsedSentence retSentence = null;

		if(sentence != null && !sentence.equals("")) {
			sentence = sentence.trim();
			if(sentence.charAt(sentence.length() - 1) != '.' &&
					sentence.charAt(sentence.length() - 1) != '!' &&
					sentence.charAt(sentence.length() - 1) != '?') {
				sentence = sentence + ".";
			}

			// Tokenize
			String[] tokens = tok.tokenize(sentence);

			List<String> tokenList = new ArrayList<String>();
			tokenList.addAll(Arrays.asList(tokens));

			retSentence = parseSentenceTokens(Arrays.asList(tokens));

		}

		return retSentence;
	}

	/**
	 * Parse a sentence as a list of tokens - should be terminated with a full stop or a question/exclamation mark if not a full stop is addedç
	 * at the end of the sentence.
	 * 
	 * @param sentenceTokens
	 * @return
	 */
	public static ParsedSentence parseSentenceTokens(List<String> sentenceTokens) {

		init(false);

		ParsedSentence retSentence = null;

		if(sentenceTokens != null && sentenceTokens.size() > 0) {
			if(!sentenceTokens.get(sentenceTokens.size() - 1).equals(".") &&
					!sentenceTokens.get(sentenceTokens.size() - 1).equals("!") &&
					!sentenceTokens.get(sentenceTokens.size() - 1).equals("?")) {
				sentenceTokens.add(".");
			}

			List<String> tokensToProcess = new ArrayList<String>();
			tokensToProcess.add("<root>");
			for (int i = 0; i < sentenceTokens.size(); i++) {
				tokensToProcess.add(String.valueOf(sentenceTokens.get(i)));
			}

			// **********************************************
			// PROCESS:
			Sentence s = null;

			if(tokensToProcess.size() > 0 && tokensToProcess.size() <= maxSentenceLengthTokens) {
				s = new Sentence(pp.preprocess(tokensToProcess.toArray(new String[tokensToProcess.size()])));
				srl.parseSentence(s);
			}
			else {
				logger.debug("Impossible to parse the sentence " + tokensToProcess.toString() + "(token size is " + tokensToProcess.size() + ", greater than " + maxSentenceLengthTokens + ")");
			}

			if(s != null) {
				retSentence = new ParsedSentence(tokensToProcess.size() - 1);
				for(int w = 1; w < tokensToProcess.size(); w++) {
					String token = tokensToProcess.get(w);
					Integer actualIndex = w - 1;
					retSentence.setToken(actualIndex, token);

					Word word = s.get(w);
					retSentence.setLemma(actualIndex, word.getLemma());
					retSentence.setPos(actualIndex, word.getPOS());
					if(word.getDeprel() != null) {
						retSentence.setDepFunct(actualIndex, word.getDeprel());
					}
					if(word.getHeadId() > 0) {
						retSentence.setDepTarget(actualIndex, word.getHeadId() - 1);
					}

					List<Predicate> predicates = s.getPredicates();
					for (int j = 0; j < predicates.size(); ++j) {
						Predicate pred = predicates.get(j);
						String tag = pred.getArgumentTag(word);
						if (StringUtils.isNotBlank(tag)) {
							// Is SRL predicate
							retSentence.setSRLtag(actualIndex, StringUtils.defaultString(tag, ""));
							retSentence.setSRLrootID(actualIndex, pred.getIdx() - 1);
							retSentence.setSRLsense(actualIndex, StringUtils.defaultString(pred.getSense(), ""));
							
						}
						else if(pred.getIdx() == w+1) {
							// Is SRL root token
							retSentence.setSRLsense(actualIndex, StringUtils.defaultString(pred.getSense(), ""));
						}
					}

				}
			}
		}

		return retSentence;
	}
	
	
	public static void main(String args[]) {
		
		Manage.setResourceFolder("/hlocal/path/to/resource/folder");
		
		String sentenceToParse = "This is a test sentence that needs to be parsed by means of this dependency parser.";
		
		ParsedSentence parsedSent = parseSentence(sentenceToParse);
		
		System.out.println(parsedSent.toString());
	}
}
