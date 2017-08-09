package com.huacai.util;

import java.security.MessageDigest;

/**
 * �ַ�MD5ǩ��
 * 
 */
public class MD5Util {
		
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	private final static String[] hexDigitsBig = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	/**
	 * ���ֽ�����ת��Ϊʮ����Ƶ��ַ�
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return ʮ����Ƶ��ַ�
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * ���ֽ�����ת��Ϊʮ����Ƶ��ַ�
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return ʮ����Ƶ��ַ�
	 */
	private static String byteArrayToHexBigString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexBigString(b[i]));
		}
		return resultSb.toString();
	}
	
	/**
	 * ��һ���ֽ�ת��Ϊһ��ʮ����Ƶ��ַ�
	 * 
	 * @param b
	 *            �ֽ�
	 * @return ʮ����Ƶ��ַ�
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * ��һ���ֽ�ת��Ϊһ��ʮ����Ƶ��ַ�
	 * 
	 * @param b
	 *            �ֽ�
	 * @return ʮ����Ƶ��ַ�
	 */
	private static String byteToHexBigString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigitsBig[d1] + hexDigitsBig[d2];
	}
	
	/**
	 * ���ַ���MD5�ķ�ʽ����
	 * 
	 * @param origin
	 *            ��Ҫ���ܵ��ַ�
	 * @return ���ܺ���ַ�
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * ���ַ��Դ�дMD5�ķ�ʽ����
	 * 
	 * @param origin
	 *            ��Ҫ���ܵ��ַ�
	 * @return ���ܺ���ַ�
	 */
	public static String MD5EncodeBig(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexBigString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	
	/**
	 * ���ַ��Դ�дMD5�ķ�ʽ����
	 * 
	 * @param origin
	 *            ��Ҫ���ܵ��ַ�
	 * @return ���ܺ���ַ�
	 */
	public static String MD5EncodeBig(String origin,String charset) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexBigString(md.digest(resultString.getBytes(charset)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	
	public static String MD5EncodeBigSequence() {
		String resultString = null;
		try {
			resultString = System.currentTimeMillis()+""+(Math.random()*100000);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexBigString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
		
	}
	
	public static void main(String[] args)
	{
		System.out.println(MD5Util.MD5Encode("admin123"));
	}
}