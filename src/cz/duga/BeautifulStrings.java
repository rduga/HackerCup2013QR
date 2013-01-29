package cz.duga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * BeautifulString implementation
 * 
 * @author Radovan Duga
 * 26.01.2013
 */
public class BeautifulStrings {

	/**
	 * @param args inputFile
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("USAGE: " + BeautifulStrings.class.getName() + " inputFile");
			return;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(args[0])));

			// char->count
			HashMap<Integer, Integer> char2CountMap = new HashMap<Integer, Integer>();
			
			int lines = Integer.parseInt(reader.readLine());
			for (int curr = 0; curr < lines; ++curr) {
				char2CountMap.clear();
				String line = reader.readLine();
				
				// each line
				for (int i = 0; i < line.length(); ++i) {
					int currChar = line.charAt(i);
					
					// ignore when it is not letter
					if (!Character.isLetter(currChar)) {
//						System.err.print((char) currChar);
						continue;
					}
					
					// to lower case
					currChar = Character.toLowerCase(currChar);
//					System.out.println("Char: " + new Character((char)currChar) + " " + currChar);
					
					Integer val = char2CountMap.get(currChar);
					
					if (val == null) {
						char2CountMap.put(currChar, 1);
					} else {
//						System.out.println("Val: " + (val + 1));
						char2CountMap.put(currChar, val + 1);
					}
				}
				
//				System.out.println();
				
				int charCount = char2CountMap.size();
				// we count beauty from bottom to top because of using sort which sorts by natural order
				int currBeauty = 26 - charCount + 1;
				
				if (charCount > 26) {
					throw new RuntimeException(String.format("Case #%d: char count exceeded max size 26", curr));
				}
				
				List<Integer> sortedCounts = new ArrayList<Integer>(char2CountMap.values());
				// ascending sorted counts of characters
				Collections.sort(sortedCounts);
				
				int beautyForLine = 0;
				
				for (Integer currCount : sortedCounts) {
					beautyForLine += currCount * currBeauty;
					// increase beauty of next character (-> up to 26)
					++currBeauty;
				}

//				System.out.println("map:" + char2CountMap);
				System.out.println(String.format("Case #%d: %d", curr + 1, beautyForLine));
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader!= null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
