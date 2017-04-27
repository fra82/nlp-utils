# nlp-utils

This repository gather some utility class to access and execute to a set of NLP resources and Web Service.
The code in the repo is not optimized nor extensively tested.

Every package contains a self-conained set of classes useful to:
* **org.backingdata.nlp.utils.parser.mate**: perform dependency parsing and semantic role labelling of sentences by means of the [MATE pareser](https://code.google.com/archive/p/mate-tools/ "MATE parser")
* **org.backingdata.nlp.utils.connector.babelfy**: connect to the [Babelfy](http://babelfy.org/ "Babelfy") Word Sense Disambiguation and Named Entity Linking servie to process and thus semantically annotate a text excerpt
* **org.backingdata.nlp.utils.connector.spotlight**: exploit [DBpedia Spotlight](https://github.com/dbpedia-spotlight/dbpedia-spotlight "DBpedia Spotlight") servie to process and thus semantically annotate a text excerpt;
* **org.backingdata.nlp.utils.connector.citparse**: parse / retrieve bibliographic entry metadata (from the list of references at the end of a paper) by querying [Bibsonomy](https://bitbucket.org/bibsonomy/bibsonomy/wiki/documentation/api/REST%20API "Bibsonomy API"), [CrossRef](http://search.crossref.org/help/api "CrossRef API") or [FreeCite](http://freecite.library.brown.edu/ "FreeCite API")
* **org.backingdata.nlp.utils.langres.sentiwordnet**: static methods to retrieve synset polarity from [SentiWordnet 3.0](http://sentiwordnet.isti.cnr.it/ "SentiWordnet 3.0")
* **org.backingdata.nlp.utils.langres.wikifreq**: static methods to retrieve [word frequency in Wikipedia](http://www.monlp.com/2012/04/16/calculating-word-and-n-gram-statistics-from-a-wikipedia-corpora "word frequency in Wikipedia")

All the static files and models needed together with the explanation on how to use this code (in your Maven project) can be accessed at: [http://backingdata.org/nlputil/](http://backingdata.org/nlputil/).
