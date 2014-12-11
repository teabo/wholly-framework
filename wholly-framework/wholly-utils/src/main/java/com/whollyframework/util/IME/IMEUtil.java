package com.whollyframework.util.IME;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.log4j.Logger;

public class IMEUtil {
	private static Logger log = Logger.getLogger(IMEUtil.class);
	/**
	 * 匹配单个字符全拼的声母和韵母,声母可能不存在.注意y和w,虽然不在声母范围,但是居首也是有可能的
	 */
	private static final Pattern psp = Pattern.compile("^([bpmfdtnlgkhjqxrzcsyw]{0,2})([aeiouv][a-z]*)$");

	private static HanyuPinyinOutputFormat hpof;

	private static Properties wb86;

	private static Properties shuangPin;

	static {
		// 拼音格式
		hpof = new HanyuPinyinOutputFormat();
		hpof.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出全小写
		hpof.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不显示音调
		hpof.setVCharType(HanyuPinyinVCharType.WITH_V);// ü替换为v

		// 五笔数据
		wb86 = new Properties();
		URL propsUrl = Thread.currentThread().getContextClassLoader().getResource("wb86.properties");

		if (propsUrl == null) {
			throw new IllegalStateException("wb86.properties missing");
		}
		try {
			wb86.load(propsUrl.openStream());
		} catch (IOException e) {
			throw new RuntimeException("Could not load wb86.properties:" + e);
		}
		log.debug(" Load wb86.properties file successed");

		// 双拼
		shuangPin = new Properties();
		propsUrl = Thread.currentThread().getContextClassLoader().getResource("sp.properties");

		if (propsUrl == null) {
			throw new IllegalStateException("sp.properties missing");
		}
		try {
			shuangPin.load(propsUrl.openStream());
		} catch (IOException e) {
			throw new RuntimeException("Could not load sp.properties:" + e);
		}
		log.debug(" Load sp.properties file successed");
	}

	/**
	 * 将汉字转换为全拼
	 * 
	 * @param src
	 * @return String
	 */
	public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		// System.out.println(t1.length);
		String[] t2 = new String[t1.length];
		// System.out.println(t2.length);
		// 设置汉字拼音输出的格式
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				// System.out.println(t1[i]);
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], hpof);// 将汉字的几种全拼都存到t2数组中
					t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，直接取出字符并连接到字符串t4后
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}

	/**
	 * 提取每个汉字的首字母
	 * 
	 * @param str
	 * @return String
	 */
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * 将字符串转换成ASCII码
	 * 
	 * @param cnStr
	 * @return String
	 */
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		// 将字符串转换成字节序列
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			// System.out.println(Integer.toHexString(bGBK[i] & 0xff));
			// 将每个字符转换成ASCII码
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff) + " ");
		}
		return strBuf.toString();
	}

	/**
	 * 字符转化为拼音.无法转换返回null,转换成功返回所有的发音
	 * 
	 * @param c
	 * @return
	 */
	public static String[] charToPinyin(char c) {
		if (c < 0x4E00 || c > 0x9FA5) {// GBK字库在unicode中的起始和结束位置
			if (c != 0x3007) {// 圆圈0比较特殊,需要处理一下
				return null;
			}
		}
		try {
			return PinyinHelper.toHanyuPinyinStringArray(c, hpof);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			return null;
		}
	}

	/**
	 * 获取单个字符的双拼,顺序和全拼一致
	 * 
	 * @param c
	 * @return
	 */
	public static String[] charToShuangPin(char c) {
		String[] array = charToPinyin(c);
		if (array == null) {
			return array;
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String s = array[i];
			Matcher m = psp.matcher(s);
			if (m.matches()) {
				String sm = m.group(1);
				String smdz = shuangPin.getProperty(sm);
				String ym = m.group(2);
				String ymdz = shuangPin.getProperty(ym);

				String r = "";
				if (smdz != null) {
					r = smdz;
				}
				if (ymdz != null) {
					r += ymdz;
				}

				result[i] = r;
			} else {
				System.err.println("分解" + c + "拼音的拼音时发生错误!无法拆分出声母和韵母.");
			}
		}
		return result;
	}

	/**
	 * 返回字符串的双拼.多音字只取第一个发音.每个字的首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String getShuangPin(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			String[] r = charToShuangPin(c);
			if (r == null) {
				sb.append(c);
			} else {
				String py = r[0];
				if (py.length() > 0) {
					char f = Character.toUpperCase(py.charAt(0));
					py = f + py.substring(1);
				}
				sb.append(py);
			}
		}
		return sb.toString();
	}

	/**
	 * 字符转化为五笔(86),无法转化返回null
	 * 
	 * @param c
	 * @return
	 */
	public static String[] charToWubi(char c) {
		if (c < 0x4E00 || c > 0x9FA5) {// GBK字库在unicode中的起始和结束位置
			return null;
		}
		String result = wb86.getProperty(Integer.toHexString(c).toUpperCase());
		if (result == null) {
			return null;
		}
		if (result.contains(",")) {
			return result.split(",");
		} else {
			return new String[]{result};
		}
	}

	public static void main(String[] args) {
		System.out.println(IMEUtil.getPinYin("中华人民共和国--中联部"));
		System.out.println(IMEUtil.getPinYinHeadChar("中华人民共和国--中联部"));
		System.out.println(Arrays.deepToString(charToWubi('啊')));
		System.out.println(Arrays.deepToString(charToWubi('郑')));
		System.out.println(Arrays.deepToString(charToWubi('工')));
	}
}
