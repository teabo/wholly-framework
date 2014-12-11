package com.whollyframework.util.sequence;

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
		return uuid.toString();
	}

	public static synchronized String getTimeSequence() throws SequenceException {
		long result = System.currentTimeMillis();
		if (result == millis) {
			old++;
			if (old >= base)
				throw new SequenceException(
						"It had exceed the maxium sequence in this moment.");
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

	public static void main(String[] args) throws Exception {
//		System.out.println(Sequence.getGuid());
//		System.out.println(Sequence.getSequence());
		System.out.println(Sequence.getTimeSequence());
		System.out.println(Sequence.getSequenceTimes());// 125292303662500000

	}
}
