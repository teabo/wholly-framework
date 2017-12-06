package com.whollyframework.util.security.encoder;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.whollyframework.util.Base64;
import com.whollyframework.util.security.CertificateUtil;


public class RSAEncoder {

	public static final String X509 = "X.509";
	private Key PublicKey;
	
	public RSAEncoder(){
		this.PublicKey = getPublicKey("/keystore/YGNETPub.key");
	}
	
	public RSAEncoder(Key publicKey){
		if (publicKey!=null)
			this.PublicKey = publicKey;
		else
			this.PublicKey = getPublicKey("/keystore/YGNETPub.key");
	}
	
	public RSAEncoder(String certificateFile){
		this(CertificateUtil.getPublicKey(certificateFile));
	}
	
	private Key getPublicKey(String fileName){
		// 公钥类所对应的类
		Key key = null;
		try {
			ObjectInputStream ooi = new ObjectInputStream(RSAEncoder.class.getResource(fileName).openStream());
			key = (PublicKey) ooi.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	
	public String encryptString(String data) throws Exception {
		byte[] datas = encrypt(data);
		
		return Base64.encode2String(datas);
	}
	
	public byte[] encrypt(String data) throws Exception {
		return encrypt(this.PublicKey, data.getBytes());
	}
	
	private byte[] encrypt(Key pk, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(data.length);
		int leavedSize = data.length % blockSize;
		int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
		byte[] raw = new byte[outputSize * blocksSize];
		int i = 0;
		while (data.length - i * blockSize > 0) {
			if (data.length - i * blockSize > blockSize) {
				cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
			} else {
				cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
			}
			i++;
		}
		return raw;
	}
	
	public static void main(String[] args) {
		try {
			String eString = "123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf123ssdfsdfsdfsdfsdfsdf";
			System.out.println("length: " +  eString.length() + " 明文： " + eString);
			RSAEncoder encoder = new RSAEncoder();
			String baseString = encoder.encryptString(eString);
			System.out.println("加密結果：" +  baseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
