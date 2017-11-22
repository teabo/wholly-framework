package com.whollyframework.util.security.decoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.whollyframework.util.Base64;
import com.whollyframework.util.security.CertificateUtil;


public class RSADecoder {
	
	private Key PrivateKey;
	
	public RSADecoder(){
		this.PrivateKey = getPrivateKey("/keystore/YGNETPri.key");
	}
	
	public RSADecoder(Key privateKey){
		if (privateKey != null)
			this.PrivateKey = privateKey;
		else
			this.PrivateKey = getPrivateKey("/keystore/YGNETPri.key");
	}
	
	public RSADecoder(String certificateFile, String password){
		this(CertificateUtil.getPrivateKey(certificateFile, password));
	}
	
	private PrivateKey getPrivateKey(String fileName) {
		PrivateKey key = null;
		try {
			ObjectInputStream ooi = new ObjectInputStream(RSADecoder.class.getResource(fileName).openStream());
			key = (PrivateKey) ooi.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	
	public byte[] decrypt(String encodeString) throws Exception {
		return decrypt(Base64.decode(encodeString));
	}
	
	public byte[] decrypt(byte[] raw) throws Exception {
		return decrypt(this.PrivateKey, raw);
	}
	
	public String decryptString(String encodeString) throws Exception {
		byte[] datas = decrypt(this.PrivateKey, Base64.decode(encodeString));
		return new String(datas);
	}
	
	private byte[] decrypt(Key pk, byte[] raw) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, pk);
		ByteArrayOutputStream bout = null;
		try {
			bout = new ByteArrayOutputStream(64);
			int j = 0;
			int blockSize = cipher.getBlockSize();
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			String baseString = "bAvtl3Fs5+E0Zu6ow7jIIuzF/ip5S2nla1Km/txYgjFe5u9zaIDhHYjLJeG"
					+ "WGDUnIt2DVh0rw2pB6ULGStXEoCU0KuBPm7PAAnWHXZHT/upq+zMKwx0q5kdna6Zt+oG1ogq"
					+ "C/TOu+BQ9jGDohNwQHXUYoRGWUWoUF8ekggW6o38DczEo0RIMthCxnsBkxIvak/qVVox/LLb1"
					+ "q0JcyBG+K3F41VFtVr1Bmcq3O6Bg57LspfGfSgxde+rG9tfU03/qOhGKtVTj1iSo4eQ8yKNswG"
					+ "vu9BselQ5PY8sIsDWOuvnZq3vqL124jiK8Sfso2fbAc4lFn1Bwm5C9kfC6ztHcSA==";
			System.out.println("密文：" +  baseString);

			RSADecoder decoder = new RSADecoder();
			System.out.println("解密結果：" +  decoder.decrypt(baseString));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
