package org.chon.core.common.util;

public class CyrToLat {

	private static final String cyr = "АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш";
	private static final String[] lat = { "A", "B", "V", "G", "D", "Gj", "E", "Zh",
			"Z", "Dz", "I", "J", "K", "L", "Lj", "M", "N", "Nj", "O", "P", "R",
			"S", "T", "Kj", "U", "F", "H", "C", "Ch", "Dzh", "Sh", "a", "b",
			"v", "g", "d", "gj", "e", "zh", "z", "dz", "i", "j", "k", "l",
			"lj", "m", "n", "nj", "o", "p", "r", "s", "t", "kj", "u", "f", "h",
			"c", "ch", "dzh", "sh" };
	
	private static final String validChars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	/**
	 * Same as calling cyrlicToLat(txt, true, null, '-')
	 * @param txt
	 * @return
	 */
	public static String cyrlicToUrlFriendly(String txt) {
		return cyrlicToLat(txt, true, null, '-'); 
	}
	
	/**
	 * Convert any String to latin 
	 * Original validChars = ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789
	 *   Cyrillic letters recognized and converted to latin
	 *   All other letters are replaced with replacer
	 *   
	 * @param txt
	 * @param lowerCase
	 * @param aditionalValidChars
	 * @param replacer
	 * @return
	 */
	public static String cyrlicToLat(String txt, boolean lowerCase, String aditionalValidChars, char replacer) {
		if (txt == null) {
			return null;
		}
		
		String allValidChars = validChars;
		if(aditionalValidChars != null) {
			allValidChars += aditionalValidChars;
		}
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < txt.length(); i++) {
			char c = txt.charAt(i);
			int index = cyr.indexOf(c);
			if (index != -1) {
				buf.append(lat[index]);
			} else {
				if (allValidChars.indexOf(c) != -1) {
					buf.append(c);
				} else {
					if(buf.length()>0 && buf.charAt(buf.length()-1) != replacer) {
						buf.append('-');
					}
				}
			}
		}
		
		String r = buf.toString();
		if(r.length()>0) {
			while(r.charAt(r.length()-1)== replacer) {
				r = r.substring(0, r.length()-1);
			}
		}
		
		if(lowerCase) {
			r = r.toLowerCase();
		}
		return r;
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println(cyrlicToUrlFriendly("  $ Како „наводник“ ќе работи  "));
//	}

}