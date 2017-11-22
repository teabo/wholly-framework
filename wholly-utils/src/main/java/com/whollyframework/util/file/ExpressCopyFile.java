package com.whollyframework.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressCopyFile {
	private static final Logger log = LoggerFactory.getLogger(ExpressCopyFile.class);
	
	public static final long BUFFER_SIZE = 1048576*100;
	// 创建可以容纳3个线程的线程池
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1500);
	
	private static ExecutorService fixedPathThreadPool = Executors.newSingleThreadExecutor();

	public static void copyFiles(String sourcePath, String desPath) {
		copyFiles(sourcePath, desPath, new ArrayList<String>());
	}
	
	public static void copyFiles(String sourcePath, final String desPath, List<String> ignorePath) {
		try {
			File file = new File(sourcePath);
			File desFile = new File(desPath);
			if (!desFile.exists()){
				desFile.mkdirs();
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					final File f = files[i];
					if (f.isFile()) {
						copyFile(f.getPath(), desPath + File.separator + f.getName());
					} else {
						if (!ignorePath.contains(f.getName())){
							fixedPathThreadPool.execute(new Runnable() {
								public void run() {
									copyFiles(f.getPath(), desPath + File.separator + f.getName());
								}
							});
						} else {
							log.warn("The source file" + f.getPath() + " is IgnorePath!");
						}
					}
				}
				log.debug("执行复制[" + sourcePath + "]文件完成!");
			} else {
				throw new Exception(sourcePath
						+ " is not a directory or it is not exist!");
			}
		} catch (Exception e) {
			log.error("复制[" + sourcePath + "]文件异常：", e);
		}
	}

	public static void copyFile(String sourceFilePath, String desFilePath) {

		try {
			File f = new File(sourceFilePath);
			File desFile = new File(desFilePath);
			
			// 判断文件是否存在或是否为空文件
			if (!f.exists() || f.length() == 0) {
				log.warn("The source file " + sourceFilePath + " does not exist or its length is 0");
				if (desFile.exists()){
					return;
				}
			} else {
				if (desFile.exists()){
					if (f.lastModified() > desFile.lastModified()){
						desFile.delete();
					}else if (f.length()> desFile.length()){ 
						desFile.delete();
					} else if (f.length() == desFile.length()){
						return;
					}
				}
			}

			// 获得源文件长度
			long len = f.length();
			long count = (long) (len/BUFFER_SIZE) + 1;// 需要的线程数

			//用for循环处理划分文件的第一部分跟第二部分(循环次数可根据定义的线程数调整)
			for (long i = 0; i <count - 1; i++) {
				//oneNum * i 起始位置， oneNum * (i + 1)要复制数据的长度
				fixedThreadPool.execute(new WorkThread(sourceFilePath, desFilePath, BUFFER_SIZE * i, BUFFER_SIZE * (i + 1)));
			}
			//文件长度不能整除的部分放到最后一段处理
			fixedThreadPool.execute(new WorkThread(sourceFilePath, desFilePath, BUFFER_SIZE * (count-1), len));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void testCopyFile(){
		String sourceFilePath = "D:\\software\\cn_windows_server_2008_r2_standard_enterprise_datacenter_and_web_with_sp1_x64_dvd_617598.iso";
		String desFilePath = "G:\\Software\\software\\cn_windows_server_2008_r2_standard_enterprise_datacenter_and_web_with_sp1_x64_dvd_617598.iso";
		ExpressCopyFile.copyFile(sourceFilePath, desFilePath);
	}
	
	private static void testCopyFiles(){
		String sourcePath = "I:\\";
		String desPath = "G:\\";
		
		try {
			List<String> ignorePath = new ArrayList<String>();
			ignorePath.add("360Downloads");
			ignorePath.add("360WiFi");
			ignorePath.add("360极速浏览器下载");
			ignorePath.add("app");
			ignorePath.add("attachment");
			ignorePath.add("CCProxy");
			ignorePath.add("dg_exShow");
			ignorePath.add("exchange");
			ignorePath.add("jcbk");
			ignorePath.add("Oracle");
			ignorePath.add("Program Files");
			ignorePath.add("Seagate");
			ignorePath.add("$RECYCLE.BIN");
			ignorePath.add("System Volume Information");
			
			ExpressCopyFile.copyFiles(sourcePath, desPath, ignorePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		boolean isRun = true;
		//testCopyFile();
		if (isRun){
			testCopyFiles();
		}
	}
}
