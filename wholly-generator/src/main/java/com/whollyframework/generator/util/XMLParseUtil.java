package com.whollyframework.generator.util;

import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 解析 xml 的入口类
 * @author Andy
 * @date 2011-12-16
 *
 */
public class XMLParseUtil {
	
	/**
	 * 错误代码： 10001 表示 数据库错误, 10002 表示 数据错误 , 10003表示xml解析错误
	 * 	10004 数据模板工厂创建错误
	 */
	public final static String[] ERROR_CODE =  {"10001", "10002", "10003", "10004"};

	/**
	 * 得到 xml 解析后的对象, 
	 * @param is xml 文件流
	 * @param parser 解析该xml文件所用的xml解析器
	 * @return 返回解析后的对象, 通过该对象可拿到xml对应的标签对象数据
	 * @throws Exception
	 */
	public static IParseXML getParser(InputStream is, IParseXML parser) throws WebException{
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sAXParser = spf.newSAXParser();
			sAXParser.parse(is, (AbstractParseXML)parser);
			return parser;
		} catch (Exception e) {
			throw new WebException("xml 解析错误",e,ERROR_CODE[3]);
		}
		
	}
}
