package org.backingdata.nlp.utils.parser.mate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Results of a sentence parsed by the Mate parser.<P>
 * Each token in the sentence has an id (Integer) that is the equal to the position of the token in that sentence.<P>
 * The first token of a sentence has id equal to 0.<P>
 * For instance the sentence "The apple is red." has the following five tokens:<P>
 *  - 'the' with id 0<P>
 *  - 'apple' with id 1<P>
 *  - 'is' with id 2<P>
 *  - 'red' with id 3<P>
 *  - '.' with id 4.<P>
 * 
 * Each token has an associated lemma (String) and Part-Of-Speech (String).<P>
 * To get the token in position i we can use the method {@link #getTokenAtIndex(int) getTokenAtIndex}.<P>
 * To get the lemma of a token in position i we can use the method {@link #getLemmaAtIndex(int) getLemmaAtIndex}.<P>
 * To get the POS of a token in position i we can use the method {@link #getPOSatIndex(int) getLemmaAtIndex}.<P>
 * 
 * The methods {@link #getParentId(int) getParentId}, {@link #getChildrenIds(int) getChildrenIds} and {@link #getChildParentDep(int,int) getChildParentDep} are
 * useful to browse the dependendy tree and get the names of the dependency relations among each pair of children/parent nodes.<P>
 * 
 * NB: Each token can be related to one parent one by means of one dependency function.<P>
 * 
 * @author Francesco Ronzano
 *
 */
public class ParsedSentence {

	private String sentenceText;
	private String[] token;
	private String[] lemma;
	private String[] pos;
	private String[] depFunct;
	private Integer[] depTarget;
	private String[] SRLtag;
	private Integer[] SRLrootID;
	private String[] SRLsense;

	public ParsedSentence(Integer tokenNumber) {
		super();
		this.sentenceText = "";
		this.token = new String[tokenNumber];
		this.lemma = new String[tokenNumber];
		this.pos = new String[tokenNumber];
		this.depFunct = new String[tokenNumber];
		this.depTarget = new Integer[tokenNumber];
		this.SRLtag = new String[tokenNumber];
		this.SRLrootID = new Integer[tokenNumber];
		this.SRLsense = new String[tokenNumber];
	}

	public String getSentenceText() {
		return new String(sentenceText);
	}

	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}

	public List<String> getToken() {
		return Collections.unmodifiableList(Arrays.asList(token));
	}

	public List<String> getLemma() {
		return Collections.unmodifiableList(Arrays.asList(lemma));
	}

	public List<String> getPos() {
		return Collections.unmodifiableList(Arrays.asList(pos));
	}

	public List<String> getDepFunct() {
		return Collections.unmodifiableList(Arrays.asList(depFunct));
	}

	public List<Integer> getDepTarget() {
		return Collections.unmodifiableList(Arrays.asList(depTarget));
	}

	public List<String> getSRLtag() {
		return Collections.unmodifiableList(Arrays.asList(SRLtag));
	}

	public List<Integer> getSRLrootID() {
		return Collections.unmodifiableList(Arrays.asList(SRLrootID));
	}

	public List<String> getSRLsense() {
		return Collections.unmodifiableList(Arrays.asList(SRLsense));
	}


	// Other methods
	public boolean setToken(int index, String tk) {
		if(index >= 0 && index < token.length && tk != null) {
			token[index] = new String(tk);
			return true;
		}
		return false;
	}

	public boolean setLemma(int index, String lm) {
		if(index >= 0 && index < lemma.length && lm != null) {
			lemma[index] = new String(lm);
			return true;
		}
		return false;
	}

	public boolean setPos(int index, String ps) {
		if(index >= 0 && index < pos.length && ps != null) {
			pos[index] = new String(ps);
			return true;
		}
		return false;
	}

	public boolean setDepFunct(int index, String df) {
		if(index >= 0 && index < depFunct.length && df != null) {
			depFunct[index] = new String(df);
			return true;
		}
		return false;
	}

	public boolean setDepTarget(int index, Integer dt) {
		if(index >= 0 && index < depTarget.length && dt != null && dt >= 0 && dt < depTarget.length) {
			depTarget[index] = new Integer(dt);
			return true;
		}
		return false;
	}

	public boolean setSRLtag(int index, String st) {
		if(index >= 0 && index < SRLtag.length && st != null) {
			SRLtag[index] = new String(st);
			return true;
		}
		return false;
	}

	public boolean setSRLrootID(int index, Integer sroot) {
		if(index >= 0 && index < SRLrootID.length && sroot != null && sroot >= 0 && sroot < SRLrootID.length) {
			SRLrootID[index] = new Integer(sroot);
			return true;
		}
		return false;
	}

	public boolean setSRLsense(int index, String sSense) {
		if(index >= 0 && index < SRLsense.length && sSense != null) {
			SRLsense[index] = new String(sSense);
			return true;
		}
		return false;
	}	


	// Utility methods

	/**
	 * Get the token of the sentence at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getTokenAtIndex(int index) {
		String retToken = null;
		if(index >= 0 && index < this.getToken().size()) {
			retToken = this.getToken().get(index);
		}
		return retToken;
	}

	/**
	 * Get the lemma of a sentence token at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getLemmaAtIndex(int index) {
		String retLemma = null;
		if(index >= 0 && index < this.getToken().size() && this.getLemma() != null) {
			retLemma = this.getLemma().get(index);
		}
		return retLemma;
	}

	/**
	 * Get the POS of the a sentence token at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getPOSatIndex(int index) {
		String retPOS = null;
		if(index >= 0 && index < this.getToken().size() && this.getPos() != null) {
			retPOS = this.getPos().get(index);
		}
		return retPOS;
	}

	/**
	 * Get the SRL tag of the a sentence token at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getSRLtagatIndex(int index) {
		String retSRLtag = null;
		if(index >= 0 && index < this.getToken().size() && this.getSRLtag() != null) {
			retSRLtag = this.getSRLtag().get(index);
		}
		return retSRLtag;
	}

	/**
	 * Get the SRL sense of the a sentence token at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getSRLsenseatIndex(int index) {
		String retSRLsense = null;
		if(index >= 0 && index < this.getToken().size() && this.getSRLsense() != null) {
			retSRLsense = this.getSRLsense().get(index);
		}
		return retSRLsense;
	}

	/**
	 * Get the SRL sense of the a sentence token at a given index.
	 * 
	 * @param index
	 * @return
	 */
	public Integer getSRLrootIDatIndex(int index) {
		Integer retSRLrootID = null;
		if(index >= 0 && index < this.getToken().size() && this.getSRLrootID() != null) {
			retSRLrootID = this.getSRLrootID().get(index);
		}
		return retSRLrootID;
	}

	/**
	 * Check if the token at the given index is a predicate of a semantic frame.
	 * 
	 * @param index
	 * @return
	 */
	public Boolean isSRLpredicate(int index) {
		return (getSRLtagatIndex(index) != null && !getSRLtagatIndex(index).equals("")) ? true : false;
	}

	/**
	 * Check if the token at the given index is a root of a semantic frame.
	 * 
	 * @param index
	 * @return
	 */
	public Boolean isSRLroot(int index) {
		return ( (getSRLtagatIndex(index) == null || getSRLtagatIndex(index).equals("")) &&
				(getSRLsenseatIndex(index) != null && !getSRLsenseatIndex(index).equals(""))) ? true : false;
	}

	/**
	 * Get the depth in the dependency tree of the token at a given index.
	 * The root token has depth 0.
	 * 
	 * @param index
	 */
	public Integer getDepthOfToken_depTree(int index) {
		Integer depthValue = 0;

		Integer currentIndex = new Integer(index);
		String currentWord = new String(this.getToken().get(currentIndex));
		// System.out.println("Determining depth of word: " + currentWord + " at index: " + currentIndex);
		if(index >= 0 && index < this.getToken().size()) {
			List<Integer> targetIdList = this.getDepTarget();
			List<String> depFunctList = this.getDepFunct();
			while(targetIdList.get(currentIndex) != null && targetIdList.get(currentIndex) >= 0) {
				depthValue++;
				// System.out.println("Word: " + currentWord + " at index: " + currentIndex + " with function: " + depFunctList.get(currentIndex));
				currentIndex = targetIdList.get(currentIndex);
				currentWord = new String(this.getToken().get(currentIndex));
				// System.out.println("Word: " + currentWord + " at index: " + currentIndex + " with function: " + depFunctList.get(currentIndex));
			}
		}
		else {
			depthValue = -1;
		}
		return depthValue;
	}

	/**
	 * Get the id of the parent dependency tree node / token, given the id of a token.
	 * 
	 * @param index
	 * @return -1 if no parent node exists
	 */
	public Integer getParentId_depTree(int index) {
		Integer retVal = -1;

		if(index < this.getDepTarget().size() && this.getDepTarget().get(index) != null &&
				this.getDepTarget().get(index) >= 0) {
			retVal = new Integer(this.getDepTarget().get(index));
		}

		return retVal;
	}

	/**
	 * Get the list of dependency tree nodes/token ids that are children of a dependency tree node/token.
	 * 
	 * @param index
	 * @return empty list if no children nodes exist
	 */
	public List<Integer> getChildrenIds_depTree(int index) {
		List<Integer> retChildrenList = new ArrayList<Integer>();

		if(this.getDepTarget().size() > 0) {
			for(int i = 0; i < this.getToken().size(); i++) {
				if(this.getDepTarget().get(i) != null && this.getDepTarget().get(i).equals(index) ) {
					retChildrenList.add(new Integer(i));
				}
			}
		}

		return retChildrenList;
	}

	/**
	 * Get the dependency function of a child token (id) that depends on a parent token (id).
	 * For instance in the sentence 'The dog eats the apple.'
	 * if we ask the dependency function of the child token 'dog' (1) towards its parent token 'eats' (2) we can
	 * invoke this method getChildParentDep(1,2) and it will return "SBJ" since 'dog' is subject of 'eats'.
	 * 
	 * @param childIndex the id of the child token
	 * @param parentIndex the id of the parent token
	 * @return null if no dependency function links the child token to the parent token.
	 */
	public String getChildParentDep_depTree(int childIndex, int parentIndex) {
		String retDependency = null;
		if(childIndex >= 0 && childIndex < this.getToken().size() && parentIndex >= 0 && parentIndex < this.getToken().size()) {
			int parentId = this.getParentId_depTree(childIndex);
			if(parentId != -1 && this.getDepFunct() != null) {
				retDependency = this.getDepFunct().get(childIndex);
			}
		}
		return retDependency;
	}

	@Override
	public String toString() {
		return "ParsedSentence [sentenceText=" + sentenceText + "\n"
				+ " token    =" + Arrays.toString(token) + "\n"
				+ " lemma    =" + Arrays.toString(lemma) + "\n"
				+ " pos      =" + Arrays.toString(pos) + "\n"
				+ " depFunct =" + Arrays.toString(depFunct) + "\n"
				+ " depTarget=" + Arrays.toString(depTarget) + "\n"
				+ " SRLtag   =" + Arrays.toString(SRLtag) + "\n"
				+ " SRLrootID=" + Arrays.toString(SRLrootID) + "\n"
				+ " SRLsense =" + Arrays.toString(SRLsense) + "]";
	}

}
