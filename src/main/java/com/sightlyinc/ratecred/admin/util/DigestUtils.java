package com.sightlyinc.ratecred.admin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DigestUtils {

	public static String makeDigest(byte[] buffer)
			throws NoSuchAlgorithmException {

		// Initialize SecureRandom
		// This is a lengthy operation, to be done only upon
		// initialization of the application
		SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

		// generate a random number
		String randomNum = new Integer(prng.nextInt()).toString();

		// get its digest
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] result = sha.digest(buffer);
		return new String(result);

	}

	/**
	 * The byte[] returned by MessageDigest does not have a nice textual
	 * representation, so some form of encoding is usually performed.
	 * 
	 * This implementation follows the example of David Flanagan's book
	 * "Java In A Nutshell", and converts a byte array into a String of hex
	 * characters.
	 * 
	 * Another popular alternative is to use a "Base64" encoding.
	 */
	private static String hexEncode(byte[] aInput) {
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

}
