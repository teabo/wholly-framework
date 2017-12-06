package com.whollyframework.web.login.log;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

/**
 * 消息处理线程池
 * 
 * @author chris
 * @since 2012-11-15
 */
public class ThreadPool {
	private final LinkedList<Thread> processes = new LinkedList<Thread>();
	private int proceCount;
	private String classz;

	public ThreadPool(int proceCount, String classz) {
		this.proceCount = proceCount;
		this.classz = classz;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void start() {
		try {

			Class clazz = Class.forName(classz);
			for (int i = 0; i < proceCount; i++) {
				Constructor con = clazz.getConstructor();
				Thread thread = (Thread) con.newInstance();
				processes.add(thread);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destory() {
		for (int i = 0; processes != null && i < processes.size(); i++) {
			try {
				Thread runThread = (Thread) processes.get(i);

				if (runThread != null) {
					runThread.interrupt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
