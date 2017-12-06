package com.whollyframework.util;

import java.io.Closeable;

public class IOUtils {

	public static void closeQuietly(Closeable io) {
		try {
			if (io != null)
				io.close();
		} catch (Exception e) {
		}
	}
}
