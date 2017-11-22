package com.whollyframework.util.security.encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import com.whollyframework.util.IOUtils;
import com.whollyframework.util.file.FileOperator;
import com.whollyframework.util.sequence.Sequence;


public class FileEnCrypter {

	public void encrypt(String inFile, String outFile) throws Exception {
		encrypt(new File(inFile), new File(outFile));
	}
	
	public void encrypt(String inFile, String outFile, Key publicKey) throws Exception {
		encrypt(new File(inFile), new File(outFile), publicKey);
	}
	
	public void encrypt(File inFile, File outFile) throws Exception {
		encrypt(inFile, outFile, null);
	}

	public void encrypt(File inFile, File outFile, Key publicKey) throws Exception {
		String sKey = Sequence.getRandomSequence();
		encryptDES(inFile, outFile, sKey);

		RSAEncoder RSAEncoder = new RSAEncoder(publicKey);
		
		FileOperator.appendContent(outFile, RSAEncoder.encryptString(sKey));
	}

	/**
	 * 加密函数 输入： 要加密的文件，密码（由0-F组成，共48个字符，表示3个8位的密码）如：
	 * AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746 其中： AD67EA2F3BE6E5AD
	 * DES密码一 D368DFE03120B5DF DES密码二 92A8FD8FEC2F0746 DES密码三 输出：
	 * 对输入的文件加密后，保存到同一文件夹下增加了".tdes"扩展名的文件中。
	 */
	private void encryptDES(File inFile, File outFile, String sKey) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (sKey.length() == 48) {
				fis = new FileInputStream(inFile);
				fos = new FileOutputStream(outFile);
				
				DESEncoder.encrypt(fis, fos, sKey);
//				byte[] bytIn = new byte[(int) inFile.length()];
//				for (int i = 0; i < inFile.length(); i++) {
//					bytIn[i] = (byte) fis.read();
//				}
//				String bytK1 = sKey.substring(0, 16);
//				String bytK2 = sKey.substring(16, 32);
//				String bytK3 = sKey.substring(32, 48);
//				// 加密
//				byte[] bytOut = DESEncoder.encrypt(DESEncoder.encrypt(DESEncoder.encrypt(bytIn, bytK1), bytK2), bytK3);
//				for (int i = 0; i < bytOut.length; i++) {
//					fos.write((int) bytOut[i]);
//				}

			} else
				;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}

	

	public static void main(String args[]) {
		FileEnCrypter da = new FileEnCrypter();
		try {
			long start = System.currentTimeMillis();
			// String srcFile = "C:\\Users\\Chris Hsu\\Desktop\\RSAwithAES.rar";
			String srcFile = "F:\\资料.rar";
			da.encrypt(new File(srcFile), new File("C:\\Users\\Chris Hsu\\Desktop\\1.rar.tdes"));
			
			System.out.println("Cost Time: " + (System.currentTimeMillis()-start) + "(ms)");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
