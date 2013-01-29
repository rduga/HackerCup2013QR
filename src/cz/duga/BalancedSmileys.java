package cz.duga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * BalancedSmileys implementation
 * 
 * @author Radovan Duga
 * 27.01.2013
 */
public class BalancedSmileys {
	
	/**
	 * Cache of balanced and unbalanced string map (compounded of patentheses).
	 */
	public static HashMap<String, Boolean> balancedParMap = new HashMap<String, Boolean>(150);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("USAGE: " + BalancedSmileys.class.getName() + " inputFile");
			return;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(args[0])));
			
			int lines = Integer.parseInt(reader.readLine());
			for (int currLine = 0; currLine < lines; ++currLine) {
				String line = reader.readLine();
				
				boolean isBalanced = false;
				
				// converted string consisting of:
				// standard parentheses: '(', ')'
				// smileys which can be parentheses:
				//   '{' - can be '(' or empty char
				//   '}' - can be '(' or empty char
				StringBuffer chLineBuff = new StringBuffer();
				
//				System.out.println(line);
				
				for (int i = 0; i < line.length(); ++i) {
					int currChar = line.charAt(i);
					
					if (currChar == ':') {
						int nextChar = i < line.length() - 1 ? line.charAt(i + 1) : 'x';
						
						if (nextChar == ')') {
							chLineBuff.append('}');
							++i;
						} else if (nextChar == '(') {
							chLineBuff.append('{');
							++i;
						} else {
							// ignore all other characters after ':'
						}
						
					} else if (currChar == '(') {
						chLineBuff.append('(');
					} else if  (currChar == ')') {
						chLineBuff.append(')');
					} else {
						
					}
				}
				
				String chLine = chLineBuff.toString();
//				System.out.println("chline: " + chLine);
				
				isBalanced = generParString(chLine, 0);
				
				System.out.println(String.format("Case #%d: %s", currLine + 1, isBalanced ? "YES" : "NO"));
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

	/**
	 * @param chLine converted string consisting of:
	 * 	- standard parentheses: '(', ')'
	 * 	- braces (as a replacement for smileys):
	 * 		'{' - can be in function of '(' or of empty char
	 * 		'}' - can be in function of '(' or of empty char
	 * @param pos position since which we generate
	 * 		suffix of string by replacing braces by
	 * 		emtpy string or by normal parentheses
	 */
	private static boolean generParString(String chLine, int pos) {
		
		if (pos >= chLine.length()) {
			return isBalancedString(chLine);
		}		
		
		int currChar = chLine.charAt(pos); 
		
		// we find closest bracket
		while (currChar != '{' && currChar != '}') {
			++pos;
			if (pos >= chLine.length()) {
				return isBalancedString(chLine);
			}
			currChar = chLine.charAt(pos);
		}
			
		int generTimes = 1;
		int j = pos;
		
		// we find the next brackets which are the same as the first found bracket
		while (j < chLine.length()) {
			int nextChar = chLine.charAt(j);
			
			if (nextChar != currChar) {
				break;
			}
			
			++j;
			generTimes++;
		}
		
		String start = chLine.substring(0, pos);
		String end = chLine.substring(j, chLine.length());
		
		StringBuffer genered = new StringBuffer();
		
		int currCharReplacement = 0;
		if (currChar == '{') {
			currCharReplacement = '(';
		} else if (currChar == '}') {
			currCharReplacement = ')';
		} else {
			System.err.println("unexpected char: " + ((char) currChar) + " at position: " + pos + " of string");
			return false;
		}
		
		// We generate the string by replacing substring of brackets 
		// from 0 to generTimes with standard parentheses.
		for (int k = 0; k < generTimes; ++k) {
			String compound = start + genered.toString() + end;
			
			if (generParString(compound, pos + k) == true) {
//				System.out.format("Substring %s is balanced\n", compound);
				return true;
			}
			
			genered.append((char) currCharReplacement);
		}

		return false;
	}

	/**
	 * We test the string consisted of only '(', ')' if it has ballanced parentheses
	 */
	private static boolean isBalancedString(String chLine) {
//		System.out.println("testing balancing of string: " + chLine);
		
		Boolean isBalanced = balancedParMap.get(chLine);
		
		if (isBalanced != null) {
			return isBalanced;
		}
		
		int balance = 0;
		
		for (int i = 0; i < chLine.length(); ++i) {
			int currChar = chLine.charAt(i);
			
			if (currChar == '(') {
				++balance;
			} else if (currChar == ')') {
				--balance;
			} else {
				System.err.format("String %s doesnt consist only '(', ')' parentheses", chLine);
			}
			
			if (balance < 0) {
				balancedParMap.put(chLine, false);
				return false;
			}
		}
		
		if (balance == 0) {
			balancedParMap.put(chLine, true);
			return true;
		}
		
		balancedParMap.put(chLine, false);
		return false;
	}

}
