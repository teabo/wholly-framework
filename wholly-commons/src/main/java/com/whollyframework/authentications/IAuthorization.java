package com.whollyframework.authentications;

public interface IAuthorization {

	public enum Type{
		BUTTON,FIELD,MENU
	}
	
	public enum DisplayType{
		SHOW(3),HIDDEN(4);
		
		private int type;
		DisplayType(int type){
			this.type = type;
		}
		
		public int getType(){
			return type;
		}
	}
	
	public enum FieldType{
		READONLY(1),EDIT(2),DISABLED(3),HIDDEN(4);
		
		private int type;
		FieldType(int type){
			this.type = type;
		}
		
		public int getType(){
			return type;
		}
	}
	/**
	 * 获取权限值
	 * @return
	 */
	int getValue();
	
	/**
	 * 获取权限类型（例如：按钮类型、字段类型等）
	 * @return
	 */
	Type getType();
	
	/**
	 * 获取权限名称
	 * @return
	 */
	String getName();
}
