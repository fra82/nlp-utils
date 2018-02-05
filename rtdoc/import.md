**How to import NLP-utils in your code**

**Resources**:
+ <a href="http://backingdata.org/nlp-utils/doc/" target="_blank">NLP-utils Javadoc</a>  
+ <a href="http://backingdata.org/nlp-utils/NLPutils-resources-1.0.tar.gz" target="_blank">Download NLP-utils resources folder (file: NLPutils-resources-1.0.tar.gz)</a>  
  
  
  
## Import NLP-utils java library

+ **OPTION 1**: in your code with Maven:  
  
Add to the POM of your Maven project the following repository:  

```  
<repositories>
	<repository>
		<id>backingdata-repo</id>
		<name>Backingdata repository</name>
		<url>http://backingdata.org/mavenRepo/</url>
	</repository>
</repositories>
```  
  
  and the following dependency:  
  
```  
<dependency>
	<groupId>org.backingdata.nlp</groupId>
	<artifactId>nlp-util</artifactId>
	<version>0.1</version>
</dependency>
```  
  
  
  
+ **OPTION 2**:

Download the JAR / dependencies Download the jar of the library together the libraries it depends on (lib folder).  
  
  
  

## Use the library
  
+ Download (link on the top of this page) and uncompress the resource folder to your local drive;  
+ Improt the library in your java project as explained before (by maven of JARs);  
+ Set the path to the resoruce folder in your java code by means of the method 'static boolean setResourceFolder(java.lang.String resFolder)' of the <a href="http://backingdata.org/nlputils/doc/org/backingdata/nlp/utils/Manage.html" target="_blank">org.backingdata.nlp.utils.Manage</a>;  
+ Access to the the utility class and methods exposed by the library to support and ease the analysis of textual resources by browisng the <a href="doc" target="_blank">Javadoc</a> or the <a href="https://github.com/fra82/nlp-utils/" target="_blank">source code (on GitHub)</a>.  
  
