package com.whollyframework.util.file;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkThread extends Thread {
	private static final Logger log = LoggerFactory.getLogger(WorkThread.class);

	private String sourceFile;// 原文件地址
	private String desFile;// 目标文件地址

	private long start, end;// start指定起始位置，end指定结束位置

	public WorkThread(String sourceFile, String desFile, long begin, long end) {
		this.start = begin;
		this.end = end;
		this.sourceFile = sourceFile;
		this.desFile = desFile;
	}

	// 重写run()方法

	public void run() {
		RandomAccessFile in = null;
		RandomAccessFile out = null;
		try {
			// 创建一个只读的随机访问文件
			in = new RandomAccessFile(sourceFile, "r");

			// 创建一个可读可写的随机访问文件
			out = new RandomAccessFile(desFile, "rw");

			in.seek(start);// 将输入跳转到指定位置
			out.seek(start);// 从指定位置开始写

			FileChannel inChannel = in.getChannel(); // 文件输入通道
			FileChannel outChannel = out.getChannel();// 文件输出通道

			// 锁住需要操作的区域,false代表锁住
			FileLock lock = outChannel.lock(start, (end - start), false);

			// 将字节从此通道的文件传输到给定的可写入字节的outChannel通道。
			inChannel.transferTo(start, (end - start), outChannel);

			lock.release();// 释放锁
			log.debug("复制[" + sourceFile + "]文件内容{"+start+"~"+end+"}完成");
		} catch (Exception e) {
			log.error("复制[" + sourceFile + "]文件内容异常：", e);
			e.printStackTrace();
		} finally{
			close(out);// 从里到外关闭文件
			close(in);// 关闭文件
		}

	}
	
	private void close(Closeable call){
		try {
			if (call!=null)
				call.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
