package org.chon.cms.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {
	public static String getMd5Digest(String pInput) {
		try {
			MessageDigest lDigest = MessageDigest.getInstance("MD5");
			lDigest.update(pInput.getBytes());
			BigInteger lHashInt = new BigInteger(1, lDigest.digest());
			return String.format("%1$032x", lHashInt).toLowerCase();
		} catch (NoSuchAlgorithmException lException) {
			throw new RuntimeException(lException);
		}
	}
	
	
	private static String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-_=+[{]};:'\"\\|,<.>/?";
	
	
	public static void main(String[] args) {
//		String md5 = getMd5Digest("goma#112");
//		System.out.println(md5);
//		md5 = getMd5Digest("joco");
//		System.out.println(md5);
//		md5 = getMd5Digest("abu");
//		System.out.println(md5);
//		if(true) {
//			return;
//		}
		//findMd5ki(new String [] {"9fe42613e44a05802bb1768e5a3c8c90", "05bc335133b3863abf22d27622a9071b", "34d71b97c471931334f385b4e7b7379c"});
		
		findMd5ki(new String [] {"9fe42613e44a05802bb1768e5a3c8c90", "05bc335133b3863abf22d27622a9071b", "34d71b97c471931334f385b4e7b7379c"});
	}
	
	private static class MyCallback implements Callback {
		private int cycle;
		private int count = 0;
		private String[] strings;
		private char[] chars;
		public MyCallback(int cycle, String [] strtings, char [] chars) {
			this.cycle = cycle;
			this.strings = strtings;
			this.chars = chars;
		}
		
		@Override
		public boolean next(String str) {
			//System.out.println(str);
			String md5 = getMd5Digest(str);
			for(String s : strings) {
				if(md5.equals(s)) {
					System.out.printf("md5(%s) is for     %s \n", md5, str);
				}
			}
			count++;
			return count < (Math.pow(chars.length, cycle)*0.7);
		}
	}
	
	private static void findMd5ki(final String[] strings) {
		char [] chars = string.toCharArray();
		for(int i=1; i<10; i++) {
			System.out.println("Processing " + i);
			Callback cb = new MyCallback(i, strings, chars);
			randcomb(chars, i, cb);
		}
	}

	interface Callback {
		public boolean next(String str);
	}
	
	public static void randcomb(char[] chars, int M, Callback cb) {
		Random rand = new Random();
		
		int [] a = new int[M];
		
		while(true) {
			
			StringBuffer buf = new StringBuffer();
			for(int i=0; i<M; i++) {
				a[i] = Math.abs(rand.nextInt())%chars.length;
				buf.append(chars[a[i]]);
			}
			//a[M-1]++;
			//System.out.println(buf.toString());
			if(cb.next(buf.toString()) == false) return;
//			for(int i=M-1; i>0; i--) {
//				if(a[i]>=chars.length) {
//					a[i]=0; a[i-1]++;
//				}
//			}
		}
	}
	
	public static void comb(char[] chars, int M, Callback cb) {
		int [] a = new int[M];
		while(a[0]<chars.length) {
			StringBuffer buf = new StringBuffer();
			for(int i=0; i<M; i++) {
				buf.append(chars[a[i]]);
			}
			a[M-1]++;
			//System.out.println(buf.toString());
			if(cb.next(buf.toString()) == false) return;
			for(int i=M-1; i>0; i--) {
				if(a[i]>=chars.length) {
					a[i]=0; a[i-1]++;
				}
			}
		}
	}
}
