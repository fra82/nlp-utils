# nlp-utils

This repository gathers some utility class / collection of static methods to easlily access to a set of NLP resources and Web Service and execute some basic NLP analyses. Please, consider that the code in the repo is not optimized nor extensively tested.

Every package of this repository contains a self-conained set of classes / collections of static methods useful to:
* **MATE Depenency Parser (EN/ES)** (package: org.backingdata.nlp.utils.parser.mate): perform dependency parsing and semantic role labelling of sentences by means of the [MATE pareser](https://code.google.com/archive/p/mate-tools/ "MATE parser")
* **Babelfy WSD** (package: org.backingdata.nlp.utils.connector.babelfy): connect to the [Babelfy](http://babelfy.org/ "Babelfy") Word Sense Disambiguation and Named Entity Linking service to process and thus semantically annotate a text excerpt
* **DBpedia Spotlight Connector** (package: org.backingdata.nlp.utils.connector.spotlight): exploit [DBpedia Spotlight](https://github.com/dbpedia-spotlight/dbpedia-spotlight "DBpedia Spotlight") service to process and thus semantically annotate a text excerpt;
* **Bibliographic Entry Parsing** (package: org.backingdata.nlp.utils.connector.citparse): utilities to parse / retrieve bibliographic entry metadata by analyzing the text of each bibliographic entries from the list of references at the end of a paper. Such utilities rely on the following Web Services: [Bibsonomy](https://bitbucket.org/bibsonomy/bibsonomy/wiki/documentation/api/REST%20API "Bibsonomy API"), [CrossRef](http://search.crossref.org/help/api "CrossRef API") and [FreeCite](http://freecite.library.brown.edu/ "FreeCite API")
* **SentiWordnet** (package: org.backingdata.nlp.utils.langres.sentiwordnet): static methods to retrieve polarity information of Wordnet synsets from [SentiWordnet 3.0](http://sentiwordnet.isti.cnr.it/ "SentiWordnet 3.0")
* **Wikipedia Word Frequencies** (package: org.backingdata.nlp.utils.langres.wikifreq): static methods to retrieve [word frequencies from Wikipedia](http://www.monlp.com/2012/04/16/calculating-word-and-n-gram-statistics-from-a-wikipedia-corpora "word frequency in Wikipedia")
* **LDA Topic Analysis with Mallet** (package: org.backingdata.nlp.utils.topiclda): utility method to learn LDA topics from documents, load/store/user learned models by relying on [Mallet](http://mallet.cs.umass.edu/ "Mallet").

The resources / models needed to execute this code as well as the explanation of how to use this library in your (Maven) Java project can be found at [http://backingdata.org/nlputil/](http://backingdata.org/nlputil/). Here also the [NLP-utils Javadoc](http://backingdata.org/nlputils/doc/) is available.
