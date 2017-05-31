package com.whollyframework.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.whollyframework.util.StringUtil;
import com.whollyframework.util.xml.mapper.OmitNotExistsFiledMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {
	private static Map<?, ?> omitFieldMap;
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String LT_BACKSLASH = "</";

	public static StringBuffer addStartTag(StringBuffer buf, String tagname) {
		buf.append(LT).append(tagname).append(GT);
		return buf;
	}

	public static StringBuffer addEndTag(StringBuffer buf, String tagname) {
		buf.append(LT_BACKSLASH).append(tagname).append(GT);
		return buf;
	}

	public static StringBuffer addElement(StringBuffer buf, String tagname, Map<String, String> attrs) {
		buf.append(LT).append(tagname);
		if (attrs != null && attrs.size() > 0) {
			for (Iterator<String> iterator = attrs.keySet().iterator(); iterator
					.hasNext();) {
				String ATTR_NAME = iterator.next();
				String value = attrs.get(ATTR_NAME);
				if (!StringUtil.isBlank(value)){
					buf.append(" ").append(ATTR_NAME).append("=\"")
							.append(value).append("\"");
				}
			}
		}
		buf.append(GT).append(LT_BACKSLASH).append(tagname).append(GT);
		return buf;
	}
	
     
    /**
     *  将传入xml文本转换成Java对象
     * @Title: toBean
     * @Description: TODO
     * @param xmlStr
     * @param cls  xml对应的class类
     * @return T   xml对应的class类的实例对象
     *
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     */
    public static <T> T  toBean(String xmlStr,Class<T> cls){
        //注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj=(T)xstream.fromXML(xmlStr);
        return obj;        
    }

   /**
     * 写到xml文件中去
     * @Title: writeXMLFile
     * @Description: TODO
     * @param obj 对象
     * @param absPath 绝对路径
     * @param fileName  文件名
     * @return boolean
     */
   
    public static boolean toXMLFile(Object obj, String absPath, String fileName ){
        String strXml = toXml(obj);
        String filePath = absPath + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false ;
            }
        }// end if
        OutputStream ous = null ;
        try {
            ous = new FileOutputStream(file);
            ous.write(strXml.getBytes());
            ous.flush();
        } catch (Exception e1) {
            return false;
        }finally{
            if(ous != null )
                try {
                    ous.close();
                } catch (IOException e) {
                }
        }
        return true ;
    }
     
    /**
     * 从xml文件读取报文
     * @Title: toBeanFromFile
     * @Description: TODO
     * @param absPath 绝对路径
     * @param fileName 文件名
     * @param cls
     * @throws Exception
     * @return T
     */
    public static <T> T  toBeanFromFile(String absPath, String fileName,Class<T> cls) throws Exception{
        String filePath = absPath +fileName;
        InputStream ins = null ;
        try {
            ins = new FileInputStream(new File(filePath ));
        } catch (Exception e) {
            throw new Exception("读{"+ filePath +"}文件失败！", e);
        }
         
        //String encode = useEncode(cls);
        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj =null;
        try {
            obj = (T)xstream.fromXML(ins);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new Exception("解析{"+ filePath +"}文件失败！",e);
        }
        if(ins != null)
            ins.close();
        return obj;        
    }
    /**
     * java 转换成xml
     * @Title: toXml
     * @Description: TODO
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String toXml(Object obj){
        return toXml(obj, false);
    }

	public static String toXml(Object obj, boolean ignoreFields) {
		if (ignoreFields)
			return getXstream().toXML(obj);
		else{
			XStream xstream=new XStream();
	         
	        ////如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
	        xstream.aliasSystemAttribute(null, "class");//去掉class属性
	        xstream.processAnnotations(obj.getClass()); //通过注解方式的，一定要有这句话
	        return xstream.toXML(obj);
		}
	}

	public static File toXmlFile(Object obj, String fileName)
			throws IOException {
		return toXmlFile(obj, fileName, "UTF-8");
	}

	public static File toXmlFile(Object obj, String fileName, String charSetName)
			throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			File dir = file.getParentFile();
			if (!dir.exists()) {
				if (!dir.mkdirs())
					throw new IOException("create directory '" + dir.getPath()
							+ "' failed!");
			}
			if (!file.createNewFile())
				throw new IOException("create file '" + fileName + "' failed!");
		}
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				fileName), charSetName);
		getXstream().toXML(obj, out);
		return new File(fileName);
	}

	public static Object toOjbect(String xml) {
		return getXstream().fromXML(xml);
	}

	public static Object toOjbect(File file) throws FileNotFoundException {
		return getXstream().fromXML(new FileReader(file));
	}

	/**
	 * 获取XML解析器，（注：考虑多线程中，静态方法可能存在的问题）
	 * 
	 * @return
	 */
	public static XStream getXstream() {
		XStream xstream = new XStream();
		xstream.registerConverter(new JavaBeanConverter(
				new OmitNotExistsFiledMapper(xstream.getMapper())),
				XStream.PRIORITY_LOW);
		xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
		xstream.autodetectAnnotations(true);

		initOmitField(xstream);

		return xstream;
	}

	public static Map<?, ?> parse(InputStream is) {
		Map<?, ?> map = new HashMap<Object, Object>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(is);

			if (document != null) {
				map = getNodeBean(document.getFirstChild());
			}
			return map;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	private static Map<?, ?> getNodeBean(Node parent) {
		Map<Object, Object> rtn = new HashMap<Object, Object>();

		if (parent != null) {
			Map<Object, Object> attrMap = new HashMap<Object, Object>();
			if (parent.hasAttributes()) {
				NamedNodeMap attrs = parent.getAttributes();
				for (int j = 0; j < attrs.getLength(); j++) {
					Node attr = attrs.item(j);
					attr.getNodeName();
					attr.getNodeValue();
					attrMap.put(attr.getNodeName(), attr.getNodeValue());
				}
			}

			rtn.put("tagName", parent.getNodeName());
			rtn.put("attr", attrMap);

			NodeList nodeList = parent.getChildNodes();
			if (nodeList != null) {
				List<Object> children = new ArrayList<Object>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node child = nodeList.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						children.add(getNodeBean(child));
					}
				}

				rtn.put("children", children);
			}
		}

		return rtn;
	}

	/**
	 * 初始化忽略的字段
	 * 
	 * @throws ClassNotFoundException
	 */
	private static void initOmitField(XStream xstream) {
		try {
			Map<?, ?> map = getOmitFieldMap();
			List<?> clazzes = (List<?>) map.get("children");
			for (Iterator<?> iterator = clazzes.iterator(); iterator.hasNext();) {
				Map<?, ?> clazz = (Map<?, ?>) iterator.next();
				Map<?, ?> attr = (Map<?, ?>) clazz.get("attr");
				String className = (String) attr.get("name");
				Class<?> definedIn = Class.forName(className);

				ArrayList<?> fields = (ArrayList<?>) clazz.get("children");
				for (Iterator<?> iterator2 = fields.iterator(); iterator2
						.hasNext();) {
					Map<?, ?> field = (Map<?, ?>) iterator2.next();
					Map<?, ?> fieldAttr = (Map<?, ?>) field.get("attr");
					String fieldName = (String) fieldAttr.get("name");

					xstream.omitField(definedIn, fieldName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据配置文件获取忽略字段信息
	 * 
	 * @return
	 */
	private static Map<?, ?> getOmitFieldMap() {
		if (omitFieldMap == null) {
			InputStream is = XmlUtil.class.getClassLoader()
					.getResourceAsStream("xstreamOmitField.xml");
			Map<?, ?> map = XmlUtil.parse(is);
			omitFieldMap = map;
		}
		return omitFieldMap;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getXstream());
	}
}
