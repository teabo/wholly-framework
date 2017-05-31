package com.whollyframework.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.whollyframework.util.StringContent;

/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class FileOperator {
	// private static final Logger log = Logger.getLogger(FileOperate.class);

	private static void closeQuitely(Closeable io){
		try {
			if (io !=null)
				io.close();
		} catch (Exception e) {
		}
	}
	
	private static void writeStream(InputStream fis, OutputStream fos) throws IOException{
		byte[] buff = new byte[2048];   
		int bytesRead=0;   
		while ((bytesRead = fis.read(buff))!=-1) {   
			fos.write(buff, 0, bytesRead);   
		} 
		fos.flush();
	}
	
	public static void createFileFolder(String fileDir) {
		File dir = new File(fileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public static void copyFileToFolder(String fileName, String folderName)
			throws Exception {
		if (!folderName.endsWith(File.separator))
			folderName += File.separator;
		File file = new File(fileName);
		if (file.isFile()) {
			copyFile(file, folderName + file.getName());
		} else {
			throw new Exception(fileName
					+ " is not a file or it is not exist!");
		}
	}

	private static void copyFile(File srcFile, String destFileName)
			throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {

			fis = new FileInputStream(srcFile);
			File outFile = new File(destFileName);
			if (!outFile.getParentFile().exists()){
				outFile.getParentFile().mkdirs();
			}
			if (outFile.exists()){
				if (srcFile.length()>outFile.length()){
					fos = new FileOutputStream(outFile, true);
					fis.skip(outFile.length());
					writeStream(fis, fos);
				}
			} else {
				fos = new FileOutputStream(outFile);
				writeStream(fis, fos);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			closeQuitely(fis);
			closeQuitely(fos);
		}
	}

	public static void cutFileToFolder(String fileName, String folderName)
			throws Exception {
		try {
			File file = new File(fileName);
			if (file.isFile()) {
				copyFileToFolder(fileName, folderName);
				file.delete();
			} else {
				throw new Exception(fileName
						+ " is not a file or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static void copyFolderFilesToOtherFolder(String oldFolderName,
			String folderName) throws Exception {
		try {
			String strMsg = "";
			File file = new File(oldFolderName);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						try {
							copyFileToFolder(files[i].getPath(), folderName);
						} catch (Exception ee) {
							strMsg += "copy file<<" + files[i].getPath()
									+ ">> error,message:" + ee.getMessage()
									+ "\r\n";
						}
					}
				}
				if (!strMsg.equals("")) {
					throw new Exception(strMsg);
				}
			} else {
				throw new Exception(oldFolderName
						+ " is not a directory or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static void cutFolderFilesToOtherFolder(String oldFolderName,
			String folderName) throws Exception {
		cutFolderFilesToOtherFolder(oldFolderName, folderName, false);
	}

	public static void cutFolderFilesToOtherFolder(String oldFolderName,
			String folderName, boolean deepSub) throws Exception {
		try {
			String strMsg = "";
			File file = new File(oldFolderName);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						try {
							cutFileToFolder(files[i].getPath(), folderName);
						} catch (Exception ee) {
							strMsg += "cut file<<" + files[i].getPath()
									+ ">> error,message:" + ee.getMessage()
									+ "\r\n";
						}
					} else if (deepSub && files[i].isDirectory()){
						cutFolderFilesToOtherFolder(files[i].getAbsolutePath(), folderName, deepSub);
						files[i].delete();
					}
				}
				if (!strMsg.equals("")) {
					throw new Exception(strMsg);
				}
			} else {
				throw new Exception(oldFolderName
						+ " is not a directory or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static void deleteAllFilesInFolder(String folderName)
			throws Exception {
		try {
			// String strMsg = "";
			File file = new File(folderName);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			} else {
				throw new Exception(folderName
						+ " is not a directory or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static void writeFile(String fileFullName, String content,
			boolean overwrite) throws IOException {

		FileWriter writer = null;
		try{
			if (fileFullName != null) {
				String path = fileFullName.substring(0, fileFullName
						.lastIndexOf("/"));
				if (!(new File(path).isDirectory())) {
					new File(path).mkdirs();
				}
			}
			File file = new File(fileFullName);
			writer = new FileWriter(file);
			if (overwrite) {
				writer.write(content);
			} else {
				writer.write(content);
			}
			writer.flush();
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			closeQuitely(writer);
		}
	}

	public static void writeFileUTF(String fileFullName, String content,
			boolean overwrite) throws IOException {

		if (fileFullName != null) {
			String path = fileFullName.substring(0, fileFullName
					.lastIndexOf(File.separator));
			if (!(new File(path).isDirectory())) {
				new File(path).mkdirs();
			}
		}
		OutputStream os = null;
		OutputStreamWriter writer = null;
		try{
			if (overwrite) {
				os = new FileOutputStream(fileFullName);
			} else {
				os = new FileOutputStream(fileFullName, true);
			}
			writer = new OutputStreamWriter(os, "UTF-8");
			writer.write(content);
			writer.flush();
		}finally{
			closeQuitely(writer);
			closeQuitely(os);
		}
	}
	
	public static byte[] readFile(String fileFullName) throws IOException{
		return readFile(new File(fileFullName));
	}
	
	public static byte[] readFile(File file) throws IOException{
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			byte[] fileContent = new byte[fis.available()];
			fis.read(fileContent);
			return fileContent;
		}finally{
			closeQuitely(fis);
		}
	}
	
	public static void writeFileUTF(File file, byte[] content,
			boolean overwrite) throws IOException {
		OutputStream os = null;
		try{
			if (overwrite) {
				os = new FileOutputStream(file);
			} else {
				os = new FileOutputStream(file, true);
			}
			os.write(content);
			os.flush();
		}finally{
			closeQuitely(os);
		}
	}

	public static void writeFile(String fileFullName, InputStream in)
			throws Exception {
		if (fileFullName != null) {
			int index = fileFullName.lastIndexOf("/");
			index = index != -1 ? index : fileFullName.lastIndexOf("\\");
			// 获取文件目录，不存在则创建
			if (index != -1) {
				String path = fileFullName.substring(0, index);
				if (!(new File(path).isDirectory())) {
					new File(path).mkdirs();
				}
			}
		}

		FileOutputStream outputStream = null;
		try{
			outputStream = new FileOutputStream(fileFullName);

			writeStream(in, outputStream);
		}finally{
			closeQuitely(outputStream);
			closeQuitely(in);
		}
	}

	public static void writeFile(String fileFullName, File file)
			throws Exception {
		if (file != null) {
			FileInputStream fileIn = new FileInputStream(file);
			writeFile(fileFullName, fileIn);
		}
	}

	public static ArrayList<?> splitTxtToArray(String fileFullName)
			throws Exception {
		
		ArrayList<Object> txt = new ArrayList<Object>();
		BufferedReader in = null;
		try{
			File file = new File(fileFullName);

			in = new BufferedReader(new FileReader(file));
			String strLine = "";
			while ((strLine = in.readLine()) != null) {
				txt.add(strLine);
			}

		}finally{
			closeQuitely(in);
		}
		return txt;
	}

	public static ArrayList<?> getFolderAllFileName(String folderName)
			throws Exception {
		ArrayList<Object> fileNamelist = new ArrayList<Object>();
		try {
			// String strMsg = "";
			File file = new File(folderName);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					fileNamelist.add(files[i].getName());
				}
			} else {
				throw new Exception(folderName
						+ " is not a directory or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return fileNamelist;
	}

	public static final String getFileContentAsStringUTF(String fileFullName)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		
		BufferedReader in = null;
		try{
			File file = new File(fileFullName);

			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), "utf-8"));
			String strLine = "";
			while ((strLine = in.readLine()) != null) {
				sb.append(strLine);
				sb.append("\n");
			}
		}finally{
			closeQuitely(in);
		}
		return sb.toString();
	}

	public static final String getFileContentAsString(String fileFullName)
			throws Exception {
		File file = new File(fileFullName);
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		try{
			in = new BufferedReader(new FileReader(file));
			String strLine = "";
			while ((strLine = in.readLine()) != null) {
				sb.append(strLine);
				sb.append("\n");
			}
		}finally{
			closeQuitely(in);
		}
		return sb.toString();
	}

	public static File[] getAllFilesInFolder(String folderName) {
		return getAllFilesInFolderByExtension(folderName, "");
	}

	public static File[] getAllFilesInFolderByExtension(String folderName,
			String extension) {
		File dir = new File(folderName);
		if (dir.isDirectory()) {
			Collection<File> rtn = new ArrayList<File>();
			File[] files = dir.listFiles();

			if (extension != null && extension.trim().length() > 0) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().indexOf("." + extension) != -1) {
						rtn.add(files[i]);
					}
				}
				return (File[]) rtn.toArray(new File[rtn.size()]);
			} else {
				return files;
			}
		}

		return new File[0];
	}

	public static void convertFileEncoding(String path, String fromEncoding,
			String toEncoding) throws Exception {
		convertFileEncoding(new File(path), fromEncoding, toEncoding);
	}

	public static void convertFileEncoding(File file, String fromEncoding,
			String toEncoding) throws Exception {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				convertFileEncoding(files[i], fromEncoding, toEncoding);
			}
		} else if (file.isFile()
				&& file.getName().trim().toLowerCase().endsWith(".java")) {
			try {
				String command = "native2ascii -encoding " + fromEncoding + " "
						+ file.getAbsolutePath() + " d:/temp.java";
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor();
				command = "native2ascii -reverse -encoding " + toEncoding
						+ " d:/temp.java " + file.getAbsolutePath();
				process = Runtime.getRuntime().exec(command);
				process.waitFor();
				System.out.println(file.getAbsolutePath()
						+ " Execute Successed");
			} catch (Exception e) {
				System.out.println(file.getAbsolutePath() + " Execute Failed");
				throw e;
			} finally {
				new File("c:/temp.java").delete();
			}
		}
	}

	public static void copyFileToFolderAndRenameFile(String fileName,
			String destFolderName, String destFileName) throws Exception {
		if (!destFolderName.endsWith(File.separator))
			destFolderName += File.separator;
		File file = new File(fileName);
		if (file.isFile()) {
			copyFile(file, destFolderName + destFileName);
		} else {
			throw new Exception(fileName
					+ " is not a file or it is not exist!");
		}
	}

	public static void cutFileToFolderAndRenameFile(String fileName,
			String destFolderName, String destFileName) throws Exception {
		try {
			if (!destFolderName.endsWith(File.separator))
				destFolderName += File.separator;
			File file = new File(fileName);
			if (file.isFile()) {
				copyFile(file, destFolderName + destFileName);
				file.delete();
			} else {
				throw new Exception(fileName
						+ " is not a file or it is not exist!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendContentOfOutputStream(String fileName,
			StringContent content) {
		BufferedWriter out = null;
		FileInputStream ins = null;
		try {
			File dir = new File(fileName);
			if (!dir.exists()) {
				dir.createNewFile();
			}
			ins = new FileInputStream(dir);
			int filesize = ins.available();
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true)));
			if (filesize > 2) {
				String nextLine = System.getProperty("line.separator");
				out.write(nextLine);
			}
			out.write(content.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQuitely(ins);
			closeQuitely(out);
		}
	}

	/**
	 * 追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendContentOfWriter(String fileName,
			StringContent content) {
		FileInputStream ins = null;
		FileWriter writer = null;
		try {
			File dir = new File(fileName);
			if (!dir.exists()) {
				dir.createNewFile();
			}
			ins = new FileInputStream(dir);
			int filesize = ins.available();
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			if (filesize > 2) {
				String nextLine = System.getProperty("line.separator");
				writer.write(nextLine);
			}
			writer.write(content.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuitely(ins);
			closeQuitely(writer);
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void appendContentOfRandomAccess(String fileName, StringContent content) {
		FileInputStream ins = null;
		RandomAccessFile randomFile = null;
		try {
			File dir = new File(fileName);
			if (!dir.exists()) {
				dir.createNewFile();
			}
			ins = new FileInputStream(dir);
			int filesize = ins.available();
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			if (filesize > 2) {
				String nextLine = System.getProperty("line.separator");
				randomFile.writeBytes(nextLine);
			}
			randomFile.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuitely(ins);
			closeQuitely(randomFile);
		}
	}

	/**
	 * 根据文件头获取文件真实类型
	 * 
	 * @param file
	 *            文件
	 * @return
	 * @throws IOException
	 */
	// 定义常用文件类型
	private static final HashMap<String, String> map = new HashMap<String, String>();
	static {
		map.put("FFD8FF", "jpg");
		map.put("89504E", "png");
		map.put("474946", "gif");
		map.put("49492A", "tif");
		map.put("424D", "bmp");
		map.put("414331", "dwg");
		map.put("384250", "psd");
		map.put("7B5C72", "rtf");
		map.put("3C3F78", "xml");
		map.put("68746D", "html");
		map.put("3C3F78", "eml");
		map.put("CFAD12", "dbx");
		map.put("214244", "pst");
		map.put("D0CF11", "xls/doc"); // office文件类型
		map.put("FF5750", "mdb");
		map.put("3C3F78", "wpd");
		map.put("252150", "eps/ps");
		map.put("255044", "wpd");
		map.put("3C3F78", "pdf");
		map.put("AC9EBD", "qdf");
		map.put("E38285", "pwl");
		map.put("504B03", "zip");
		map.put("526172", "rar");
		map.put("574156", "wav");
		map.put("415649", "avi");
		map.put("2E7261", "ram");
		map.put("2E524D", "rm");
		map.put("000001", "mpg");
		map.put("6D6F6F", "mov");
		map.put("3026B2", "asf");
		map.put("4D5468", "mid");
		map.put("000000", "txt");
		map.put("706163", "java");
	}

	public static String getFileType(InputStream im) throws IOException {
		String head = getFileHead(im).toUpperCase();
		String type = "";
		if (map.get(head) != null && !map.get(head).toString().equals("")) {
			type = map.get(head).toString();
		}
		return type;
	}

	public static String getFileHead(InputStream im) throws IOException {
		InputStream is = im;
		byte[] b = new byte[3];
		is.read(b, 0, b.length);
		String t = bytesToHexString(b);

		return t;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		try {
			FileOperator.cutFolderFilesToOtherFolder("F:\\电子相册\\婚纱照\\iPhone", "F:\\电子相册\\iphone", true);
//			convertFileEncoding("F:\\workspaces\\wabacus\\zhcx\\src",
//					 "GBK",
//					 "UTF-8");
			// File dir = new File("D:/文件");
			// File[] files = dir.listFiles();
			// for (int i = 0; i < files.length; i++) {
			// System.out.println(getFileHead(new FileInputStream(files[i])));
			// }
//			String distPath = DefaultProperty.getProperty("OAInterfacePath");
//			if (!distPath.endsWith("/") && !distPath.endsWith("\\"))
//				distPath += File.separatorChar;
//			distPath += "收文" + File.separatorChar;
//			File dir = new File(distPath);
//			if (!dir.exists()) {
//				dir.mkdir();
//			}
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//			String path = format.format(System.currentTimeMillis());
//			distPath += path + File.separatorChar;
//			dir = new File(distPath);
//			if (!dir.exists()) {
//				dir.mkdir();
//			}
//			// FileOperate.copyFileToFolderAndRenameFile("D:\\Attach\\ftp\\uploads\\5.txt",
//			// "监理162号5.txt", distPath);
//			FileOperate
//					.appendContentOfRandomAccess(distPath + "records.txt",
//							new StringContent("21367443平急普通WH[2011]443关于开展水质保障工作的紧急通知3广州市水务局2011-11-07", "UTF-8", "GBK"));

		} catch (Exception e) {
			// log.info(e.getMessage());
		}
	}
}
