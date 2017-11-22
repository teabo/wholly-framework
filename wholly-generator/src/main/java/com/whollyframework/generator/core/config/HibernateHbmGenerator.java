package com.whollyframework.generator.core.config;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;
import com.whollyframework.generator.core.AbstractGenerator;
import com.whollyframework.generator.dbutil.DataBaseUtil;
import com.whollyframework.generator.model.JavaModel;

public class HibernateHbmGenerator extends AbstractGenerator {
	public static List<String> IGNORED_FIELDS = new ArrayList<String>();
	public static Map<String, String> IGNORED_FIELDS_MAP = new HashMap<String, String>();
	public static String[] ignoreFieldsArr = new String[] { "id", "parentId", "authorId", "author", "orgId", "org",
			"created", "lastModified", "lastModifyId", "sortId", "istemp", "version" };
	static {
		IGNORED_FIELDS_MAP.put("parentid", "parentId");
		IGNORED_FIELDS_MAP.put("authorid", "authorId");
		IGNORED_FIELDS_MAP.put("orgid", "orgId");
		IGNORED_FIELDS_MAP.put("lastmodified", "lastModified");
		IGNORED_FIELDS_MAP.put("lastmodifyid", "lastModifyId");
		IGNORED_FIELDS_MAP.put("sortid", "sortId");

		IGNORED_FIELDS.addAll(Arrays.asList(ignoreFieldsArr));
		IGNORED_FIELDS.addAll(IGNORED_FIELDS_MAP.keySet());
	}

	public HibernateHbmGenerator() {
		super("Hibernate.hbm");
	}

	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		model.setModelId(prop.get("model.modelId"));
		StringBuilder rtn = new StringBuilder();
		Table table = DataBaseUtil.getTable(model.getTableName());
		if (table != null) {
			try {
				rtn.append(createPkElement(model));
				List<Column> columns = DataBaseUtil.getColumns(table);
				rtn.append(createColumnElements(columns, model));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.setModelFields(rtn.toString());
	}

	private String createPkElement(JavaModel model) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("\t\t<id name=\"").append(model.getModelId()).append("\" column=\"");
		rtn.append(model.getPkName()).append("\">\n");
		rtn.append("\t\t\t<generator class=\"assigned\" />\n");
		rtn.append("\t\t</id>\n\n");
		return rtn.toString();
	}

	/**
	 * 生成字段名拼接的字符串
	 * 
	 * @return
	 * @throws Exception
	 */
	private String createColumnElements(List<Column> columns, JavaModel model) {
		StringBuilder rtn = new StringBuilder();
		if (columns == null || columns.size() == 0) {
			return rtn.toString();
		}

		for (Column c : columns) {
			String fieldName = c.getFieldName();
			if (!fieldName.equalsIgnoreCase(model.getPkName()))
				rtn.append(createElement(getBeanFieldName(fieldName), fieldName, c.getTypeCode()));
		}
		return rtn.toString();
	}

	private String getBeanFieldName(String col) {

		/** 定义属性名： 将数据库表中的列名全部转换成小写字母，作为属性名 */
		String fieldName = col.toLowerCase();
		/** 将属属中有"_"符号隔开的单词首字母大写 */
		String[] fields = fieldName.split("_");
		fieldName = fields[0];
		for (int i = 1; i < fields.length; i++) {
			fieldName += capFirst(fields[i]);
		}
		return (IGNORED_FIELDS_MAP.get(fieldName) != null ? IGNORED_FIELDS_MAP.get(fieldName) : fieldName);
	}

	private String createElement(String beanFieldName, String fieldName, int typeCode) {
		StringBuilder rtn = new StringBuilder();
		try {
			String type = "java.lang.String";
			switch (typeCode) {
			case Types.BIT:
				type = "java.lang.Boolean";
				break;
			case Types.BIGINT:
				type = "long";
				break;
			case Types.DECIMAL:
				type = "double";
				break;
			case Types.INTEGER:
				type = "int";
				break;
			case Types.SMALLINT:
				type = "short";
				break;
			case Types.FLOAT:
			case Types.DOUBLE:
				type = "double";
				break;
			case Types.VARCHAR:
				type = "java.lang.String";
				break;
			case Types.DATE:
			case Types.TIMESTAMP:
				type = "java.util.Date";
				break;
			case Types.BLOB:
				type = "byte[]";
			}
			rtn.append("\t\t<property name=\"").append(beanFieldName).append("\" type=\"").append(type).append("\"\n");
			rtn.append("\t\t\tupdate=\"true\" insert=\"true\" column=\"").append(fieldName).append("\" />\n\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rtn.toString();
	}
}
