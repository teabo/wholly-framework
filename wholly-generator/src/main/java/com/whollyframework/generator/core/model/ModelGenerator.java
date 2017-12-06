package com.whollyframework.generator.core.model;

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
import com.whollyframework.util.StringUtil;

public class ModelGenerator extends AbstractGenerator {
	public static List<String> IGNORED_FIELDS = new ArrayList<String>();
    public static Map<String, String> IGNORED_FIELDS_MAP = new HashMap<String, String>();
    public static String[] ignoreFieldsArr = new String[]{"id" ,"parentId","authorId","author","orgId","org","created","lastModified","lastModifyId","sortId","istemp","version"};
    static{
    	IGNORED_FIELDS_MAP.put("parentid","parentId");
    	IGNORED_FIELDS_MAP.put("authorid","authorId");
    	IGNORED_FIELDS_MAP.put("orgid","orgId");
    	IGNORED_FIELDS_MAP.put("lastmodified","lastModified");
    	IGNORED_FIELDS_MAP.put("lastmodifyid","lastModifyId");
    	IGNORED_FIELDS_MAP.put("sortid","sortId");
    	
    	IGNORED_FIELDS.addAll(Arrays.asList(ignoreFieldsArr));
    	IGNORED_FIELDS.addAll(IGNORED_FIELDS_MAP.keySet());
    }
    
	public ModelGenerator() {
		super("Model");
	}

	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		model.setModelId(prop.get("model.modelId"));
		Table table = DataBaseUtil.getTable(model.getTableName());
		StringBuilder modelContents = new StringBuilder();
		if (table!=null){
			try {
				Map<String, Map<String, Object>> columnsMess = getColumnsMap(DataBaseUtil.getColumns(table));
				
				/** 生成DTO类中相关属性 */
		        Map<String, Object> columns = columnsMess.get("columns");
		        Map<String, Object> columnsComment = columnsMess.get("colComments");
		        for (Map.Entry<String, Object> map : columns.entrySet()){
		            /** 定义属性名： 将数据库表中的列名全部转换成小写字母，作为属性名 */
		            String fieldName = map.getKey().toLowerCase();
		            if (!fieldName.equalsIgnoreCase(model.getPkName())){
			            /** 处理数据类型(数据库的数据类型转化成Java相关类型) */
			            String fieldType = DataBaseUtil.toJavaType((Column) map.getValue());
			            /** 将属属中有"_"符号隔开的单词首字母大写  */
			            String[] fields = fieldName.split("_");
			            fieldName = fields[0];
			            for (int i = 1; i < fields.length; i++){
			                fieldName += capFirst(fields[i]);
			            }
			            if(!IGNORED_FIELDS.contains(fieldName)){
			            	modelContents.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append("; ");
			            	String comment = (String) columnsComment.get(map.getKey());
			            	if (!StringUtil.isBlank(comment))
			            		modelContents.append("//").append(comment);
			            	modelContents.append(" \n");
			            }
		            }
		        }
		        
		        /** 生成对应的setter与getter方法 */
		        modelContents.append("\n\t/** setter and getter method */\n");
		        for (Map.Entry<String, Object> map : columns.entrySet()){
		            /** 定义属性名： 将数据库表中的列名全部转换成小写字母，作为属性名 */
		            String fieldName = map.getKey().toLowerCase();
		            if (!fieldName.equalsIgnoreCase(model.getPkName())){
			            /** 处理数据类型(数据库的数据类型转化成Java相关类型) */
			            String fieldType = DataBaseUtil.toJavaType((Column) map.getValue());
			            /** 将属属中有"_"符号隔开的单词首字母大写  */
			            String[] fields = fieldName.split("_");
			            fieldName = fields[0];
			            for (int i = 1; i < fields.length; i++){
			                fieldName += capFirst(fields[i]);
			            }
			            if(!IGNORED_FIELDS.contains(fieldName)){
			            	 /** setter方法 */
			                modelContents.append("\tpublic void set").append(capFirst(fieldName)).append("(");
			                modelContents.append(fieldType).append(" ").append(fieldName);
			                modelContents.append("){\n");
			                modelContents.append("\t\tthis.").append(fieldName).append( " = " ).append(fieldName).append( ";\n");
			                modelContents.append("\t}\n");
			                /** getter方法 */
			                modelContents.append("\tpublic ").append( fieldType ).append( " get" ).append( capFirst(fieldName) ).append( "(){\n");
			                modelContents.append("\t\treturn this." ).append( fieldName ).append( ";\n");
			                modelContents.append("\t}\n");
			       	 	}
		            }
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.setModelContents(modelContents.toString());
	}

	 public Map<String,Map<String, Object>> getColumnsMap(List<Column> columns) throws Exception{
	        Map<String,Map<String,Object>> res = new HashMap<String,Map<String,Object>>(); 
	        Map<String, Object> columnsMap = new HashMap<String, Object>();
	        Map<String, Object> columnsCommentsMap = new HashMap<String, Object>();
	        
	        for(Column c : columns){
	            columnsMap.put(c.getFieldName(), c);
	            columnsCommentsMap.put(c.getFieldName(), c.getComment());
	        } 
	        res.put("columns", columnsMap);
	        res.put("colComments", columnsCommentsMap);
	        return res;
	    }
}
