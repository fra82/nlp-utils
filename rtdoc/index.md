**NLP-utils**

**Latest version: 1.0 (released on 5/2/2018)**

NLP-utils is a Java library that enables users to easlily perform basic NLP analyses and to access to a set of NLP resources and Web Service. Each NLP fuinctionality supported by the NLP-utils library is implemented by means of a self-conained set of classes / collections of static methods.  

This page provides an overview of the capabilities of the NLP-utils library. The page ['How to use NLP-utils'](import.md') explains how to import and set-up the NLP-utils Java library in your program.  

The resources / models needed to execute this code as well as the explanation of how to use this library in your (Maven) Java project can be found at [How to import NLP-utils in your code](Import.md). Here also the [NLP-utils Javadoc](http://backingdata.org/nlp-utils/doc/) is available.  

You can find the source code at: <a href="https://github.com/fra82/nlp-utils" target="_blank">https://github.com/fra82/nlp-utils</a>: consider that the code in the repo is not optimized nor extensively tested. For any issue please send an email to fr.ronzano AT gmail.com.  
  
  
  
## Dependency parsing (EN/ES) by MATE  
**package**: org.backingdata.nlp.utils.parser.mate  

Perform dependency parsing and semantic role labelling of sentences by means of the [MATE pareser](https://code.google.com/archive/p/mate-tools/ "MATE parser").  

Example code (from class: org.backingdata.nlp.utils.parser.mate.MateParserEN):  
  
```  
Manage.setResourceFolder("/hlocal/path/to/resource/folder");
String sentenceToParse = "This is an English test sentence to parse by means of the MATE dependency parser.";
ParsedSentence parsedSent = parseSentence(sentenceToParse);
System.out.println(parsedSent.toString());
```

Execution output:  

```  
ParsedSentence [sentenceText=
 token    =[This, is, a, test, sentence, that, needs, to, be, parsed, by, means, of, this, dependency, parser, .]
 lemma    =[this, be, a, test, sentence, that, need, to, be, parse, by, means, of, this, dependency, parser, .]
 pos      =[DT, VBZ, DT, NN, NN, WDT, VBZ, TO, VB, VBN, IN, NNS, IN, DT, NN, NN, .]
 depFunct =[SBJ, ROOT, NMOD, NMOD, PRD, SBJ, NMOD, OPRD, IM, VC, LGS, PMOD, NMOD, NMOD, NMOD, PMOD, P]
 depTarget=[1, null, 4, 4, 1, 6, 4, 6, 7, 8, 9, 10, 11, 15, 15, 12, 1]
 SRLtag   =[null, null, null, null, A1, R-A1, null, A1, null, null, A0, null, A1, null, null, null, null]
 SRLrootID=[null, null, null, null, 9, 9, null, 6, null, null, 9, null, 11, null, null, null, null]
 SRLsense =[null, null, null, null, parse.01, parse.01, null, need.01, parse.01, null, mean.01, null, mean.01, null, null, null, null]]
```


## Word Sense Disambiguation and Entity Linking by Babelfy
**package**: org.backingdata.nlp.utils.connector.babelfy  

Connect to the [Babelfy](http://babelfy.org/ "Babelfy") Word Sense Disambiguation and Named Entity Linking service to process and thus semantically annotate a text excerpt.  

Example code (from class: org.backingdata.nlp.utils.connector.babelfy.BabelfyUtil):  


```  
// Set resource folder
Manage.setResourceFolder("/hlocal/path/to/resource/folder");
// Disambiguate a text by Babelfy
String inputText = "The Australian, 55, succeeds Stuart Lancaster after England's poor show at "
+ "the World Cup and starts in December. In his first news conference, it was claimed he was called the 'devil' "
+ "for how hard he worked former team Japan.";

List<SemanticAnnotation> bfyAnnotations = BabelfyUtil.disambiguateText("PUT_YOUR_BABELFY_API_KEY", inputText, Language.EN);
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
```

Execution output (first lines):  
 
```  
*******************************************
BabelNet URL: http://babelnet.org/rdf/s00007228n
SynsetID: bn:00007228n
DBpedia URL: http://dbpedia.org/resource/Australia
Global score: 0.24746027611357124
Coherence score: 0.7916666666666666
Score: 0.9824198552223371
Source: BABELFY
Char offset fragment: (4, 13)
Token offset fragment: (1, 1)
*******************************************


*******************************************
BabelNet URL: http://babelnet.org/rdf/s00084086v
SynsetID: bn:00084086v
DBpedia URL: NULL
Global score: 2.6048450117218026E-4
Coherence score: 0.041666666666666664
Score: 1.0
Source: BABELFY
Char offset fragment: (20, 27)
Token offset fragment: (5, 5)
*******************************************

.......
```


## DBpedia Entity Spotting by DBpedia Spotlight
**package**: org.backingdata.nlp.utils.connector.spotlight)  

Exploit [DBpedia Spotlight](https://github.com/dbpedia-spotlight/dbpedia-spotlight "DBpedia Spotlight") service to process and thus semantically annotate a text excerpt.  

Example code (from class: org.backingdata.nlp.utils.connector.spotlight.SpotlightUtil):  

```  
Map<String, String> surfaceURImap = SpotlightUtil.spotEntitiesByType("Theoretical Computer Science, TU Dresden, Germany", 15, SpotlightUtil.ORGANIZATIONtype);
for(Map.Entry<String, String> entry : surfaceURImap.entrySet()) {
	System.out.println("ORGANIZATION: " + entry.getKey() + "  -  " + entry.getValue());
}

surfaceURImap = SpotlightUtil.spotEntitiesByType("Theoretical Computer Science, TU Dresden, Germany", 15, SpotlightUtil.LOCATIONtype);
for(Map.Entry<String, String> entry : surfaceURImap.entrySet()) {
	System.out.println("LOCATION: " + entry.getKey() + "  -  " + entry.getValue());
}
	
surfaceURImap = SpotlightUtil.spotEntitiesByType("Theoretical Computer Science, TU Dresden, Germany", 15, SpotlightUtil.STATEtype);
for(Map.Entry<String, String> entry : surfaceURImap.entrySet()) {
	System.out.println("STATE: " + entry.getKey() + "  -  " + entry.getValue());
}
```

Execution output:  

```  
ORGANIZATION: TU Dresden  -  http://dbpedia.org/resource/Dresden_University_of_Technology
LOCATION: Germany  -  http://dbpedia.org/resource/Germany
STATE: Germany  -  http://dbpedia.org/resource/Germany
```


## Bibliographic Entry Parsing by Bibsonomy, CrossRef and FreeCite
**package** org.backingdata.nlp.utils.connector.citparse  

Utilities to parse / retrieve bibliographic entry metadata by analyzing the text of each bibliographic entries from the list of references at the end of a paper. Such utilities rely on the following Web Services: [Bibsonomy](https://bitbucket.org/bibsonomy/bibsonomy/wiki/documentation/api/REST%20API "Bibsonomy API"), [CrossRef](http://search.crossref.org/help/api "CrossRef API") and [FreeCite](http://freecite.library.brown.edu/ "FreeCite API").  

Example code (Freecite  - from class: org.backingdata.nlp.utils.connector.citparse.freecite.ExampleFreeCite):  

```  
// Freecite
List<String> citations = new ArrayList<String>();
citations.add("[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3");
List<FreeCiteResult> parsingResults = FreeCiteConn.parseCitations(citations, 15);
parsingResults.stream().forEach((res) -> System.out.println("Parsed citations:\n " + res.toString()));
```

Execution output :  

```  
Parsed citations:  
 FreeCiteResult [  
 title=M.: Single view modeling of free-form scenes,  
 authorNames=[L ZHANG, G DUGAS-PHOCION, J-S SAMSON, S SEITZ, David Gunning, Vinay K Chaudhri, Peter E Clark, Ken Barker, Shaw-Yi Chaw, Mark Greaves, Benjamin Grosof, Alice Leung, David D McDonald, Sunil Mishra, John Pacheco, Bruce Porter, Aaron Spaulding, Dan Tecuci, Jing Tien, L ZHANG, G DUGAS-PHOCION, J-S SAMSON, S SEITZ, David Gunning, Vinay K Chaudhri, Peter E Clark, Ken Barker, Shaw-Yi Chaw, Mark Greaves, Benjamin Grosof, Alice Leung, David D McDonald, Sunil Mishra, John Pacheco, Bruce Porter, Aaron Spaulding, Dan Tecuci, Jing Tien],  
 year=2001,  
 journal=Project Halo update: Progress toward Digital Aristotle. AI Magazine,  
 pages=NULL,  
 raw_string=[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3]  

```

Example code (CrossRef - from class: org.backingdata.nlp.utils.connector.citparse.crossref.ExampleCrossRef):  
  

```  
// CrossRef  
String biblioEntry = "[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3";  
Pair<String, CrossRefResult> parsingResults = CrossRefConn.parseCitations(biblioEntry, 15);  
System.out.println("Parsed citations:\n " + ((parsingResults != null && parsingResults.getLeft() != null) ? parsingResults.getLeft().toString() : "NULL") +   
"\n" + ((parsingResults != null && parsingResults.getRight() != null) ? parsingResults.getRight().toString() : "NULL"));  
```

Execution output:  

```  
CrossRefResult [  
originalText=[ZDPSS01] ZHANG L., DUGAS-PHOCION G., SAMSON J.-S.,SEITZ S. M.: Single view modeling of free-form scenes. CVPR1 (2001), 990. 3",
score=17.687973,  
normalizedScore=100,  
doi=http://dx.doi.org/10.1109/cvpr.2001.990638, 
title=Single view modeling of free-form scenes,  
year=NULL,  
coins=ctx_ver=Z39.88-2004&amp;rft_id=info%3Adoi%2Fhttp%3A%2F%2Fdx.doi.org%2F10.1109%2Fcvpr.2001.990638&amp;rfr_id=info%3Asid%2Fcrossref.org%3Asearch&amp;rft.atitle=Single+view+modeling+of+free-form+scenes&amp;rft.jtitle=Proceedings+of+the+2001+IEEE+Computer+Society+Conference+on+Computer+Vision+and+Pattern+Recognition.+CVPR+2001&amp;rft.aulast=Li+Zhang&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Ajournal&amp;rft.genre=proceeding&amp;rft.au=+Li+Zhang&amp;rft.au=+G.+Dugas-Phocion&amp;rft.au=+J.-S.+Samson&amp;rft.au=+S.M.+Seitzt,  
fullCitation= Li Zhang, G. Dugas-Phocion, J.-S. Samson, S.M. Seitzt, 'Single view modeling of free-form scenes', <i>Proceedings of the 2001 IEEE Computer Society Conference on Computer Vision and Pattern Recognition. CVPR 2001</i>]

```

## Retrieve synset polarity information from SentiWordnet 3.0
**package**: org.backingdata.nlp.utils.langres.sentiwordnet  

Static methods to retrieve polarity information of Wordnet synsets from [SentiWordnet 3.0](http://sentiwordnet.isti.cnr.it/ "SentiWordnet 3.0").  
  
Example code (from class: org.backingdata.nlp.utils.langres.sentiwordnet.SentiwordnetParser):  
  
```  
SentiwordnetParser.initSentiwordnetParser();  
// Get sentiment scores of synset  
String id = "02772202-v";  
SentiWordnetElem swElem = SentiwordnetParser.getSentimentInfoFromENSynsetID(id);  
		  
System.out.println(id + " <-- synset id | pos score --> " + swElem.getPosScore());  
System.out.println(id + " <-- synset id | neg score --> " + swElem.getNegScore());  
System.out.println(id + " <-- synset id | obj score --> " + swElem.getObjScore());  
System.out.println(id + " <-- synset id | gloss --> " + swElem.getGloss());  
System.out.println(id + " <-- synset id | terms --> " + swElem.getTerms());  
```

Execution output:  

```  
Initializing SentiWordnet...  
Loading SentiWordnet dictionary data from: /home/ronzano/Downloads/NLPutils-resources-1.0/SentiWordNet-3.0/SentiWordNet_3.0.0_20130122.txt  
Loaded Sentiwordnet  
Entries: 117659  
Exception count: 0  
Data loaded in 401 milliseconds.  
*******************************  
02772202-v <-- synset id | pos score --> 0.125  
02772202-v <-- synset id | neg score --> 0.25  
02772202-v <-- synset id | obj score --> 0.625  
02772202-v <-- synset id | gloss --> null  
02772202-v <-- synset id | terms --> haze#1  
```
  
  
## Retrieve word frequency from English, Spanish and Catalan Wikipedia
**package**: org.backingdata.nlp.utils.langres.wikifreq  

Static methods to retrieve word frequencies - overall and by Part of Speech - from Wikipedia (English, Spanish and Catalan - 2015 dumps).  

Example code (from class: org.backingdata.nlp.utils.langres.wikifreq.WikipediaLemmaTermFrequency):  

```  
// Get the total number of occurrences of the word COCHE in Spanish
System.out.println("Word coche - total occurrences (Spanish Wikipedia): " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.Spanish, "coche"));
	
// Get the total number of occurrences of the word COCHE in Spanish
System.out.println("Word cotxe - total occurrences (Catalan Wikipedia): " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.Catalan, "cotxe"));

// Get the total number of occurrences of the word CAR in English
System.out.println("Word car - total occurrences: " + WikipediaLemmaTermFrequency.getLemmaOccurrencesCount(LangENUM.English, "car"));
			
```

Execution output:  

```  
Loading term frequency file: /home/ronzano/Downloads/NLPutils-resources-1.0/frequencies/wikipedia/eswiki_lemma_POS_TF_DF.dat...  
Word coche - total occurrences (Spanish Wikipedia): 21370  
Loading term frequency file: /home/ronzano/Downloads/NLPutils-resources-1.0/frequencies/wikipedia/cawiki_lemma_POS_TF_DF.dat...  
Word cotxe - total occurrences (Catalan Wikipedia): 5738  
Loading term frequency file: /home/ronzano/Downloads/NLPutils-resources-1.0/frequencies/wikipedia/enwiki_lemma_POS_TF_DF.dat...  
Word car - total occurrences: 329776  
```
  
  
  
## Exploit Mallet to perform LDA (topic modeling)
**package**: org.backingdata.nlp.utils.topiclda  

Utility method to learn LDA topics from documents, load/store/user learned models by relying on [Mallet](http://mallet.cs.umass.edu/ "Mallet").


Example code (from class: org.backingdata.nlp.utils.topiclda.Lda):  

```  
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
```

