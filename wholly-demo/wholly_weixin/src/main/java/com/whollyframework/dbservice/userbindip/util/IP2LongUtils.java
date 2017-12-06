package com.whollyframework.dbservice.userbindip.util;

/**
 * 
 * @author jojo
 *
 */
public class IP2LongUtils {

	// 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}
	/**
	 * 判断是否在ip端范围类
	 * @param startIp
	 * @param endIp
	 * @param resourceIp
	 */
    public static boolean isInnerIp(String startIp,String endIp,String resourceIp){
    	return  ipToLong(resourceIp)>=ipToLong(startIp)&&
    			(ipToLong(resourceIp)<=ipToLong(endIp));
    }
   

	/** */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(longToIP(ipToLong("255.255.252.0")
				& ipToLong("192.160.111.18")));
		String ipStr = "192.168.111.18";
		long longIp = IP2LongUtils.ipToLong(ipStr);
		System.out.println("192.168.106.18 的整数形式为：" + longIp);
		System.out.println("整数" + longIp + "转化成字符串IP地址："
				+ IP2LongUtils.longToIP(longIp));
		// ip地址转化成二进制形式输出
		System.out.println("192.168.111.18 的二进制形式为："
				+ Long.toBinaryString(longIp));
		System.out.println("------");
		System.out.println(isInnerIp("192.168.109.12", "192.168.111.14", "192.168.111.244"));
		
		
		

	}
}