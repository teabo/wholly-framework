package com.whollyframework.util.security.decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import com.whollyframework.util.IOUtils;
import com.whollyframework.util.file.FileOperator;

public class FileDeCrypter {

	public void decrypt(String inFile, String outFile) throws Exception {
		decrypt(new File(inFile), new File(outFile));
	}
	
	public void decrypt(String inFile, String outFile, Key privateKey) throws Exception {
		decrypt(new File(inFile), new File(outFile), privateKey);
	}

	public void decrypt(File inFile, File outFile) throws Exception {
		decrypt(inFile, outFile, null);
	}
	
	public void decrypt(File inFile, File outFile, Key privateKey) throws Exception {
		String dkey = FileOperator.readLastLine(inFile);
		try{
			RSADecoder RSADecoder = new RSADecoder(privateKey);
			
			String sKey = RSADecoder.decryptString(dkey);
	
			decryptDES(inFile, outFile, sKey);
		}catch(Exception e){
			FileOperator.appendContent(inFile, dkey);
			throw e;
		}
	}

	/**
	 * ���ܺ��� ���룺 Ҫ���ܵ��ļ������루��0F��ɣ���48���ַ��ʾ3��8λ�����룩�磺
	 * AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746 ���У� AD67EA2F3BE6E5AD
	 * DES����һ D368DFE03120B5DF DES����� 92A8FD8FEC2F0746 DES������ �����
	 * ��������ļ����ܺ󣬱��浽�û�ָ�����ļ��С�
	 */
	private void decryptDES(File fileIn, File outFile, String sKey) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (sKey.length() == 48) {
				fis = new FileInputStream(fileIn);
				fos = new FileOutputStream(outFile);
				
				DESDecoder.decrypt(fis, fos, sKey);
				
//				byte[] bytIn = new byte[(int) fileIn.length()];
//				for (int i = 0; i < fileIn.length(); i++) {
//					bytIn[i] = (byte) fis.read();
//				}
//				String bytK1 = sKey.substring(0, 16);
//				String bytK2 = sKey.substring(16, 32);
//				String bytK3 = sKey.substring(32, 48);
//				// ����
//				byte[] bytOut = DESDecoder.decrypt(DESDecoder.decrypt(DESDecoder.decrypt(bytIn, bytK3), bytK2), bytK1);
//				
//				for (int i = 0; i < bytOut.length; i++) {
//					fos.write((int) bytOut[i]);
//				}
			}
			IOUtils.closeQuietly(fis);
			fileIn.delete();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���ܴ���");
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}

	public static void main(String args[]) {
		FileDeCrypter da = new FileDeCrypter();
		try {
			long start = System.currentTimeMillis();
			// String srcFile = "C:\\Users\\Chris Hsu\\Desktop\\RSAwithAES.rar.tdes";
			String srcFile = "C:\\Users\\Chris Hsu\\Desktop\\1.rar.tdes";
			da.decrypt(new File(srcFile), new File("C:\\Users\\Chris Hsu\\Desktop\\1.rar"));
			
			System.out.println("Cost Time: " + (System.currentTimeMillis()-start) + "(ms)");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
