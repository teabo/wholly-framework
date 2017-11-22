package com.whollyframework.util.security;

import com.whollyframework.util.security.decoder.DESDecoder;
import com.whollyframework.util.security.encoder.DESEncoder;
import com.whollyframework.util.sequence.Sequence;

/**
 * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现 。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DES {
	
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
		return DESEncoder.encrypt(datasource, password);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) throws Exception {
		return DESDecoder.decrypt(src, password);
	}

	// 测试
	public static void main(String args[]) throws Exception {
		// 待加密内容
		String str = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容";
		// 密码，长度要是8的倍数
		String sKey = Sequence.getRandomSequence();
		String bytK1 = sKey.substring(0, 16);
		String bytK2 = sKey.substring(16, 32);
		String bytK3 = sKey.substring(32, 48);
		
		byte[] sbytes = str.getBytes();
		byte[] result = new byte[0];
		byte[] buf = new byte[24];
		for (int i = 0; i < 4; i++) {
			System.arraycopy(sbytes, i*24 , buf, 0, 24);
			
			// 加密
			byte[] result1 = DES.encrypt(DES.encrypt(DES.encrypt(buf, bytK1), bytK2), bytK3);
			
			byte[] tmp = new byte[result.length + result1.length];
			
			System.arraycopy(result1, 0, tmp, result.length, result1.length);
			System.arraycopy(result, 0, tmp, 0, result.length);
			result = tmp;
		}
		System.out.println("加密字节长度：" +  result.length);
		
		byte[] decryResult = new byte[0];
		buf = new byte[48];
		for (int i = 0; i < 4; i++) {
			System.arraycopy(result, i*48 , buf, 0, 48);
			
			// 加密
			byte[] result1 = DES.decrypt(DES.decrypt(DES.decrypt(buf, bytK3), bytK2), bytK1);
			
			byte[] tmp = new byte[decryResult.length + result1.length];
			
			System.arraycopy(result1, 0, tmp, decryResult.length, result1.length);
			System.arraycopy(decryResult, 0, tmp, 0, decryResult.length);
			decryResult = tmp;
		}
		System.out.println("解密字节长度：" +  decryResult.length);
		System.out.println("解密后：" + new String(decryResult));  

		// 直接将如上内容解密
		

	}
}