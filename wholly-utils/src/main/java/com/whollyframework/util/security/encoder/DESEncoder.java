package com.whollyframework.util.security.encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现 。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DESEncoder {
	
	public static String ALGORITHM_DES="DES";       //加密算法的名称 

	/**
	 * 加密
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			SecretKey securekey = getKey(password);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void encrypt(InputStream fis, OutputStream out, String password){
		CipherInputStream cis = null;
        try {
        	Cipher cipher = Cipher.getInstance("DES");  
			cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
			cis = new CipherInputStream(fis, cipher);  
	        byte[] buffer = new byte[1024];  
	        int r;
	        while ((r = cis.read(buffer)) > 0) {  
	            out.write(buffer, 0, r);  
	        }  
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			if (cis!=null){
				try {
					cis.close();
				} catch (IOException e) {
				}
			}
		}
        
	}
	
	private static SecretKey getKey(String password) throws Exception{
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
		return keyFactory.generateSecret(desKey);
	}

	// 测试
	public static void main(String args[]) {
		// 待加密内容
		String str = "测试内容";
		// 密码，长度要是8的倍数
		String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";

		byte[] result = DESEncoder.encrypt(str.getBytes(), password);
		System.out.println("加密后：" + new String(result));

	}
}