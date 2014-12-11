package com.whollyframework.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The security
 */
public class Security {
	public final static String ENCRYPTION_BASE64 = "base64";
	private static Blowfish cipher = null;

	/**
	 * Encrypt the string with the MD5 arithmetic
	 * 
	 * @param s
	 *            Normal message that you want to convert.
	 * @return The Encrypt string.
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static String encodeToMD5(String s) throws NoSuchAlgorithmException {
		if (s == null)
			return null;
		StringBuffer digstr = new StringBuffer();
		MessageDigest MD = MessageDigest.getInstance("MD5");

		byte[] oldbyte = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			oldbyte[i] = (byte) s.charAt(i);
		}
		MD.update(oldbyte);
		byte[] newbyte = MD.digest(oldbyte);
		for (int i = 0; i < newbyte.length; i++) {
			digstr.append(newbyte[i]);
		}

		return digstr.toString();
	}

	/**
	 * BASE64 编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeToBASE64(String s) {
		if (s == null)
			return null;
		try {
			return new String(Base64.encode(s.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new String(Base64.encode(s.getBytes()));
		}
	}

	/**
	 * BASE64 解码
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeBASE64(String s) {
		if (s == null)
			return null;
		byte[] b = null;
		try {
			b = Base64.decode(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return new String(b);
		}
	}
	
	public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        Blowfish cipher = getCipher();
        if (cipher == null) {
            throw new UnsupportedOperationException();
        }
        return cipher.encryptString(password);
    }

    /**
     * Returns a decrypted version of the encrypted password. Encryption is performed
     * using the Blowfish algorithm. The encryption key is stored as the Jive property
     * "passwordKey". If the key is not present, it will be automatically generated.
     *
     * @param encryptedPassword the encrypted password.
     * @return the encrypted password.
     * @throws UnsupportedOperationException if encryption/decryption is not possible;
     *      for example, during setup mode.
     */
    public static String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        Blowfish cipher = getCipher();
        if (cipher == null) {
            throw new UnsupportedOperationException();
        }
        return cipher.decryptString(encryptedPassword);
    }
    
    /**
     * Returns a Blowfish cipher that can be used for encrypting and decrypting passwords.
     * The encryption key is stored as the Jive property "passwordKey". If it's not present,
     * it will be automatically generated.
     *
     * @return the Blowfish cipher, or <tt>null</tt> if Openfire is not able to create a Cipher;
     *      for example, during setup mode.
     */
    private static synchronized Blowfish getCipher() {
        if (cipher != null) {
            return cipher;
        }
        // Get the password key, stored as a database property. Obviously,
        // protecting your database is critical for making the
        // encryption fully secure.
        String keyString;
        try {
            keyString = "teabo";
//            if (keyString == null) {
                // Check to make sure that setting the property worked. It won't work,
                // for example, when in setup mode.
//            }
            cipher = new Blowfish(keyString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cipher;
    }

	public static void main(String[] args) {
		try {
			String password = Security.encryptPassword("123");
			System.out.println("encrypt: " + password);
			
			password = Security.decryptPassword(password);
			System.out.println("decrypt: " + password);
			
			String data = "中华人民共和国";
			System.out.println(data);
			String s = Security.encodeToBASE64(data);
			System.out.println(s);
			System.out.println(Security.decodeBASE64(s));
			// String pw = encodeToMD5("123");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
