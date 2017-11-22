package com.whollyframework.util.sequence;

import java.util.Random;
import java.util.UUID;

/**
 * The system sequence utility.
 */

public class Sequence {

	private static final int base = 100000;

	private static long millis = 0;

	private static long counter = 0;

	private static long old = 0;

	/**
	 * Get the base Sequence.
	 * 
	 * @return the Sequence
	 * @throws SequenceException
	 */
	public static synchronized String getSequence() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static synchronized String getGuid() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}

	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static synchronized String getTimeSequence() {
		long result = System.currentTimeMillis();
		if (result == millis) {
			old++;
			if (old >= base)
				throw new RuntimeException("It had exceed the maxium sequence in this moment.");
			result = millis * base + old;
		} else {
			millis = result;
			result *= base;
			old = 0;
		}
		return result + "";
	}

	@SuppressWarnings("static-access")
	public static synchronized long getSequenceTimes() {
		try {
			long rtn = System.currentTimeMillis();
			while (rtn == counter) {
				Thread.currentThread().sleep(2);
				rtn = System.currentTimeMillis();
			}
			counter = rtn;
			return rtn;
		} catch (InterruptedException ie) {
			return System.currentTimeMillis();
		}

	}
	
	public static synchronized String getRandomSequence() {
		return Sequence.getRandomString(48);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Sequence.getGuid());
		System.out.println(Sequence.getSequence());
		System.out.println(Sequence.getTimeSequence());// 125292303662500000
		System.out.println(Sequence.getSequenceTimes());
		System.out.println(Sequence.getRandomString(19));
		System.out.println(Sequence.getRandomSequence());
	}
}
