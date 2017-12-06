package com.whollyframework.web.login.log;

import java.util.LinkedList;

/**
 * 消息队列
 * 
 * @author Chris
 * @since 2012-11-15
 */
public class LoginLogQueue
{
	private static LinkedList<LoginLog> queue = new LinkedList<LoginLog>();
	private static byte[] lock = new byte[0];

	/**
	 * 把提醒消息添加到队列中
	 * 
	 * @author Andy
	 * @since 2012-05-09
	 */
	public static void addQueue(LoginLog runn)
	{
		synchronized (queue)
		{
			queue.addLast(runn);
			queue.notifyAll();
		}
	}

	/**
	 * 从队列中取出提醒消息
	 * 
	 * @author Chris
	 * @since 2012-11-15
	 */
	public static LoginLog getQueue() throws Exception
	{
		LoginLog remind = null;
		synchronized (queue)
		{
			if (queue.isEmpty())
			{
				queue.wait();
			}
			remind = queue.removeFirst();
		}
		return remind;
	}

	public static boolean isEmpty()
	{
		boolean isEmpty = false;
		synchronized (lock)
		{
			if(queue==null){
				isEmpty = true;
			}else{
				if (queue.size() <= 0)
				{
					isEmpty = true;
				}
			}
		}
		return isEmpty;
	}

}
