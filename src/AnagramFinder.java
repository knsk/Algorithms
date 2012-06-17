import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;


public class AnagramFinder {
	static Map<String, List<String>> signatureWordListMap = new HashMap<String, List<String>>();
	
	public static void main(String args[]) {
		String dictionaryFilePath = "/usr/share/dict/words"; // Words file on UNIX 
		String delimiter = System.getProperty("line.separator");
		buildDictionaryIndex(dictionaryFilePath, delimiter);
		
		String word = "asterin";
		List<String> anagrams = findAnagrams(word);
		if (anagrams.isEmpty()) {
			System.out.println("No anagram of '" + word + "' found in the dictionary.");
		} else {
			System.out.println(anagrams.size() + " anagrams found for '" + word + "': ");
			for (String anagram : anagrams) {
				System.out.println(anagram);
			}
		}
	}
	
	/**
	 * Find anagrams of the word in the dictionary
	 * @param word  Word to find anagrams 
	 * @return      List of anagrams found in the dictionary
	 */
	static List<String> findAnagrams(String word) {
		String signature = computeSignature(word);
		List<String> anagrams = signatureWordListMap.get(signature);
		anagrams.remove(word); // Remove the word itself
		return anagrams;
	}
	
	/**
	 * Build a signature based index of dictionary 
	 * @param dictionaryFilePath  Path of the dictionary file 
	 * @param delimiter  Delimiter of words in dictionary 
	 */
	static void buildDictionaryIndex(String dictionaryFilePath, String delimiter) {
		try {
			String dictionary = FileUtils.readFileToString(new File(dictionaryFilePath));
			String[] words = dictionary.split(delimiter);
			System.out.println(words.length + " words in the dictionary.");
			List<String> wordList;
			for (String word : words) {
				String signature = computeSignature(word);
				wordList = signatureWordListMap.get(signature);
				if (wordList == null) {
					wordList = new ArrayList<String>();
				}
				wordList.add(word);
				signatureWordListMap.put(signature, wordList);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	/**
	 * Compute a signature of a word. Define the signature as alphabetically sorted characters of the word.
	 * @param word  A word to compute the signature
	 * @return      The signature of the word computed
	 */
	static String computeSignature(String word) {
		char[] charWord = word.toCharArray();
		Arrays.sort(charWord);
		return new String(charWord);
	}
	
}
