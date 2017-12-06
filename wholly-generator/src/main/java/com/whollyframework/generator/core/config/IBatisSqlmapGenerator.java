package com.whollyframework.generator.core.config;

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

public class IBatisSqlmapGenerator extends AbstractGenerator {
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

	public IBatisSqlmapGenerator() {
		super("IBatis.Sqlmap");
	}

	public IBatisSqlmapGenerator(String generator) {
		super(generator);
	}

	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		model.setModelId(prop.get("model.modelId"));
		Table table = DataBaseUtil.getTable(model.getTableName());
		if (table != null) {
			try {
				List<Column> columns = DataBaseUtil.getColumns(table);
				Map<String, String> sqlfields = getColumnStrMap(model, columns);

				model.setSqlFields(sqlfields.get("columns_res"));
				model.setModelFields(sqlfields.get("fields_res"));
				model.setSelectfileds(sqlfields.get("select_res"));
				model.setUpdateFields(getColumnEqStr(model, columns));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getColumnEqStr(JavaModel model, List<Column> columns) {
		if (columns == null || columns.size() == 0) {
			return "";
		}
		StringBuilder res = new StringBuilder();

		for (Column c : columns) {
			String fieldName = c.getFieldName();
			if (model.getPkName().equals(fieldName)) {
				continue;
			}
			res.append("\t\t").append(fieldName).append(" = ").append(getFiledName(model, fieldName)).append(" ,\n ");
		}
		return res.substring(0, res.lastIndexOf(","));
	}

	/**
	 * 生成字段名拼接的字符串
	 * 
	 * @return
	 * @throws Exception
	 * @author zhenghaix 2014年10月9日
	 */
	public Map<String, String> getColumnStrMap(JavaModel model, List<Column> columns) {
		Map<String, String> res = new HashMap<String, String>();
		if (columns == null || columns.size() == 0) {
			return new HashMap<String, String>();
		}
		StringBuilder columns_res = new StringBuilder();
		StringBuilder fields_res = new StringBuilder();
		StringBuilder select_res = new StringBuilder();

		for (Column c : columns) {
			String fieldName = c.getFieldName();
			select_res.append(fieldName).append(" ").append(getBeanFiledName(model, fieldName)).append(" , ");
			if (model.getPkName().equals(fieldName)) {
				continue;
			}
			columns_res.append(fieldName).append(" , ");
			fields_res.append(getFiledName(model, fieldName)).append(" , ");
		}
		res.put("columns_res", columns_res.substring(0, columns_res.lastIndexOf(",")));
		res.put("fields_res", fields_res.substring(0, fields_res.lastIndexOf(",")));
		res.put("select_res", select_res.substring(0, select_res.lastIndexOf(",")));
		return res;
	}

	/**
	 * @return
	 * @throws Exception
	 *
	 */
	protected String getFiledName(JavaModel model, String col) {
		return "#" + getBeanFiledName(model, col) + "#";
	}

	protected String getBeanFiledName(JavaModel model,String col) {
		if (col.equalsIgnoreCase(model.getPkName())){
			return model.getModelId();
		}
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
}
