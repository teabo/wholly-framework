package com.whollyframework.authentications;

import java.util.HashMap;
import java.util.Map;

import com.whollyframework.authentications.IAuthorization.DisplayType;
import com.whollyframework.authentications.IAuthorization.FieldType;
import com.whollyframework.authentications.IAuthorization.Type;
import com.whollyframework.base.model.Authorization;

public class Authorizations {

	private Map<String, IAuthorization> buttons = new HashMap<String, IAuthorization>();
	private Map<String, IAuthorization> fields = new HashMap<String, IAuthorization>();
	private Map<String, IAuthorization> menus = new HashMap<String, IAuthorization>();
	
	public enum Buttons{
		Save("save"),SavaAndNew("savaAndNew"),New("new"),Delete("delete"),Back("back"),Print("print");
		private String value;
		Buttons(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public String toString(){
			return this.value;
		}
	}

	public void addAuthorization(IAuthorization authorization) {
		switch (authorization.getType()) {
		case BUTTON:
			buttons.put(authorization.getName(), authorization);
			break;
		case FIELD:
			fields.put(authorization.getName(), authorization);
			break;
		case MENU:
			menus.put(authorization.getName(), authorization);
			break;
		default:
			break;
		}
	}

	public void removeAuthorization(IAuthorization authorization) {
		switch (authorization.getType()) {
		case BUTTON:
			buttons.remove(authorization.getName());
			break;
		case FIELD:
			fields.remove(authorization.getName());
			break;
		case MENU:
			menus.remove(authorization.getName());
			break;
		default:
			break;
		}
	}

	public void addAuthorization(Type type, String name, int value) {
		addAuthorization(new Authorization(type, name, value));
	}

	public void removeAuthorization(Type type, String name, int value) {
		removeAuthorization(new Authorization(type, name, value));
	}

	public boolean checkButton(String button) {
		IAuthorization authorization = buttons.get(button);
		if (authorization != null)
			return authorization.getValue() == DisplayType.SHOW.getType();
		return false;
	}

	public boolean checkMenu(String button) {
		IAuthorization authorization = menus.get(button);
		if (authorization != null)
			return authorization.getValue() == DisplayType.SHOW.getType();
		return false;
	}

	public boolean checkFieldReadyOnly(String fieldName) {
		IAuthorization authorization = fields.get(fieldName);
		if (authorization != null)
			return authorization.getValue() == FieldType.READONLY.getType();
		return false;
	}

	public boolean checkFieldEdit(String fieldName) {
		IAuthorization authorization = fields.get(fieldName);
		if (authorization != null)
			return authorization.getValue() == FieldType.EDIT.getType();
		return false;
	}

	public boolean checkFieldDisabled(String fieldName) {
		IAuthorization authorization = fields.get(fieldName);
		if (authorization != null)
			return authorization.getValue() == FieldType.DISABLED.getType();
		return false;
	}

	public boolean checkFieldHidden(String fieldName) {
		IAuthorization authorization = fields.get(fieldName);
		if (authorization != null)
			return authorization.getValue() == FieldType.HIDDEN.getType();
		return false;
	}

	public void buttonShow(Buttons button) {
		addAuthorization(Type.BUTTON, button.getValue(), DisplayType.SHOW.getType());
	}

	public void buttonHidden(Buttons button) {
		removeAuthorization(Type.BUTTON, button.getValue(), DisplayType.SHOW.getType());
	}
	
	public void fieldEditable(String fieldName){
		addAuthorization(Type.FIELD, fieldName, FieldType.EDIT.getType());
	}
	
	public void fieldReadyOnly(String fieldName){
		addAuthorization(Type.FIELD, fieldName, FieldType.READONLY.getType());
	}
	
	public void fieldDisabled(String fieldName){
		addAuthorization(Type.FIELD, fieldName, FieldType.DISABLED.getType());
	}
	
	public void fieldHidden(String fieldName){
		addAuthorization(Type.FIELD, fieldName, FieldType.HIDDEN.getType());
	}

	public void resourceShow(String resource){
		addAuthorization(Type.MENU, resource, DisplayType.SHOW.getType());
	}
	
	public void resourceHidden(String resource){
		addAuthorization(Type.MENU, resource, DisplayType.HIDDEN.getType());
	}
}
