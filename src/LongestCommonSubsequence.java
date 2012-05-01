/*
 * Reference on Longest common subsequence problem: http://en.wikipedia.org/wiki/Longest_common_subsequence_problem
LCS(Xi, Yj) = 
(0) null 								if i = 0 or j = 0
(1) (LCS(Xi-1, Yj-1), xi)    			if xi = yj
(2) max(LCS(Xi, Yj-1), LCS(Xx-1, Yj)) 	if xi != yj
*/

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Comparator;

class LongestCommonSubsequence {
	public static void main(String args[]) {
		final String s1 = "AGCAT";
		final String s2 = "GAC";
		System.out.println("s1: " + s1 + ", s2: " + s2);
		
		List<int[]> LCSIndexArrayList = calculateLCS(s1, s2);
		
		// Create a list of LCS
		List<String> strLCSList = new ArrayList<String>(LCSIndexArrayList.size());
		for (int[] LCSIndexArray : LCSIndexArrayList) {
			StringBuilder strLCS = new StringBuilder(LCSIndexArray.length);
			for (int LCSIndex : LCSIndexArray) {
				strLCS.append(s1.charAt(LCSIndex));
			}
			strLCSList.add(strLCS.toString());
		}
		
		// Create a list of s1 index array for each LCS
		List<int[]> resultIndexArrayList = new ArrayList<int[]>(LCSIndexArrayList.size());
		for (int[] LCSIndexArray : LCSIndexArrayList) {
			int[] resultLCSIndexArray = new int[s1.length()];
			int i = 1;
			for (int index = 0; index < LCSIndexArray.length; index++) {
				resultLCSIndexArray[LCSIndexArray[index]] = i++;
			}
			resultIndexArrayList.add(resultLCSIndexArray);
		}
		
		// Print the list of LCS
		System.out.println();
		System.out.println("Result: (Each LCS and corresponding s1 indices as the order of charcter in LCS");
		for (int i = 0; i < strLCSList.size(); i++) {
			System.out.println(strLCSList.get(i) + "  " + Arrays.toString(resultIndexArrayList.get(i)));
		}
	}
	
	public static List<int[]> calculateLCS(String s1, String s2) {
		// Define the matrix of LCS indices set as Object[][] to work around the generic array creation for Set<List<Integer>>
		// Each cell of the matrix contains a set of LCS index list
		@SuppressWarnings("unchecked") // To use multidimensional array instead of ArrayList of ArrayList for convenience
		SortedSet<List<Integer>>[][] LCSMatrix = new TreeSet[s2.length()+1][s1.length()+1]; // (0) LCSMatrix[j][i] = null if i = 0 or j = 0
		
		for (int j = 1; j < s2.length() + 1; j++) { // Loop over rows of LCSMatrix
			for (int i = 1; i < s1.length() + 1; i++) { // Loop over columns of LCSMatrix
				SortedSet<List<Integer>> LCSIndexListSet = new TreeSet<List<Integer>>(new Comparator<List<Integer>>() { // Set of LCSIndexList for LCSMatrix[j][i]	
					@Override
					public int compare(List<Integer> iList1, List<Integer> iList2) {
						Set<Integer> iSet1 = new HashSet<Integer>(iList1);
						Set<Integer> iSet2 = new HashSet<Integer>(iList2);
						if (iSet1.equals(iSet2)) { 
							return 0; // Define to be equal if iList1 contains all elements in iList2 and vice versa
						} 
						// Create new sorted lists using copy constructor in order to avoid modifying the original lists 
						List<Integer> iSortedList1 = new ArrayList<Integer>(iList1);
						Collections.sort(iSortedList1);
						List<Integer> iSortedList2 = new ArrayList<Integer>(iList2);
						Collections.sort(iSortedList2);
						
						int index = 0;
						// Compare each integer in iList1 and iList2 in ascending order, and the list is 
						// define to be less if the integer is less, and greater if the integer is greater
						while (index < Math.min(iSortedList1.size(), iSortedList2.size())) {
							if (iSortedList1.get(index) < iSortedList2.get(index)) {
								return -1;  
							} else if (iSortedList1.get(index) > iSortedList2.get(index)) {
								return 1;
							}
							index++;
						}
						
						return 0; // Not supposed to reach here
					}
				}); 
				SortedSet<List<Integer>> leftLCSIndexListSet = (TreeSet<List<Integer>>) LCSMatrix[j][i-1];
				SortedSet<List<Integer>> aboveLCSIndexListSet = (TreeSet<List<Integer>>) LCSMatrix[j-1][i];
				
				// (1) LCS(Xi, Yj) = (LCS(Xi-1, Yj-1), xi) for s1[i] == s2[j]
				if (s1.charAt(i-1) == s2.charAt(j-1)) { 	
					int LCSLength = 0;
					// 1) Extend LCS from left above cell of LCSMatrix
					
					SortedSet<List<Integer>> leftAboveLCSIndexListSet = (TreeSet<List<Integer>>) LCSMatrix[j-1][i-1];
					if (leftAboveLCSIndexListSet != null) {
						// Extend the left above  LCS with s1[i-1]
						for (List<Integer> leftAboveLCSIndexList : leftAboveLCSIndexListSet) {
							List<Integer> LCSIndexList = new ArrayList<Integer>(); // List of s1 indices to represent a LCS
							LCSIndexList.addAll(leftAboveLCSIndexList); // Add all the indices from one of the leftAboveLCSIndexList
							LCSIndexList.add(i-1); // Extend the LCS with the (i-1)th character of s1
							LCSLength = LCSIndexList.size();
							LCSIndexListSet.add(LCSIndexList);
						}
					} else {
						// New LCS starting with the (i-1)th character of s1
						List<Integer> LCSIndexList = new ArrayList<Integer>(); // List of s1 indices to represent a LCS
						LCSIndexList.add(i-1);
						LCSIndexListSet.add(LCSIndexList);
					}	
					// 2) Keep existing LCS from left cell of LCSMatrix if LCSLength matches
					if (leftLCSIndexListSet != null && leftLCSIndexListSet.first().size() == LCSLength) {
						for (List<Integer> leftLCSIndexList : leftLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(leftLCSIndexList)); // Create a new ArrayList by copy constructor
						}
					}
					// 3) Keep existing LCS from above cell of LCSMatrix if LCSLength matches
					if (aboveLCSIndexListSet != null && aboveLCSIndexListSet.first().size() == LCSLength) {
						for (List<Integer> aboveLCSIndexList : aboveLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(aboveLCSIndexList)); // Create a new ArrayList by copy constructor
						}
					}
					LCSMatrix[j][i] = LCSIndexListSet; // Set LCSIndexListSet to LCSMatrix
				// (2) LCS(Xi, Yj) = max(LCS(Xi, Yj-1), LCS(Xi-1, Yj)) for s1[i] != s2[j]
				} else { 
					int leftLCSLength = (leftLCSIndexListSet == null) ? 0 : leftLCSIndexListSet.first().size();
					int aboveLCSLength = (aboveLCSIndexListSet == null) ? 0 : aboveLCSIndexListSet.first().size();
					if (leftLCSLength != 0 && leftLCSLength == aboveLCSLength) {
						// Keep both left and above
						for (List<Integer> leftLCSIndexList : leftLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(leftLCSIndexList)); // Create a new ArrayList by copy constructor
						}
						for (List<Integer> aboveLCSIndexList : aboveLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(aboveLCSIndexList)); // Create a new ArrayList by copy constructor
						}
					} else if (leftLCSLength > aboveLCSLength) {
						// Keep left LCS
						for (List<Integer> leftLCSIndexList : leftLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(leftLCSIndexList)); // Create a new ArrayList by copy constructor
						}
					} else if (aboveLCSLength > leftLCSLength) {
						// Keep above LCS
						for (List<Integer> aboveLCSIndexList : aboveLCSIndexListSet) {
							LCSIndexListSet.add(new ArrayList<Integer>(aboveLCSIndexList)); // Create a new ArrayList by copy constructor
						}
					} else {
						LCSIndexListSet = null;
					}
					LCSMatrix[j][i] = LCSIndexListSet; // Set LCSIndexListSet to LCSMatrix
				}
			}
		}
		
		// Convert the result to desired format of List<int[]>
		SortedSet<List<Integer>> finalLCSIndexListSet = (SortedSet<List<Integer>>) LCSMatrix[s2.length()][s1.length()];
		List<int[]> LCSIndexArrayList = new ArrayList<int[]>(finalLCSIndexListSet.size());		
		for (List<Integer> LCSIndexList : finalLCSIndexListSet) {
			int[] LCSIndexArray = new int[LCSIndexList.size()];
			for (int i = 0; i < LCSIndexList.size(); i++) {
				LCSIndexArray[i] = LCSIndexList.get(i);
			}
			LCSIndexArrayList.add(LCSIndexArray);
		}

		return LCSIndexArrayList;
	}
}
