package com.whollyframework.dbservice.menu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.whollyframework.annotation.Ignore;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Environment;
import com.whollyframework.dbservice.menu.service.MenuService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.utils.ServicesFactory;
import com.whollyframework.utils.http.UrlUtil;

public class Menu extends ValueObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String icon; //图标 
	private String name; //名称 
	private String url; //地址 
	private String parameter; //null 
	private String type; //null 
	
	private List<Menu> subMenu = new ArrayList<Menu>();
	
	@Ignore
	public String getParentmenu_name(){
		if(!StringUtil.isBlank(getParentId())){
			MenuService menuService = (MenuService) ServicesFactory.getService("menuService");
			try {
				Menu m = menuService.find(getParentId());
				if(m!=null){
					if(StringUtil.isBlank(m.getName())){
						return "顶级菜单";
					}else{
						return m.getName();						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	
	@Ignore
	public String getType_name(){
		if(!StringUtil.isBlank(getType())){
			try {
				if("@@".equals(getType())) return "主菜单";
				if("05".equals(getType())) return "内部链接";
				if("06".equals(getType())) return "外部链接";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	@Ignore
	public String getMenuUrlWithParams(){
		String url = getUrl();
		if(!StringUtil.isBlank(getParameter())){
			try {
				Map<String, Object> parameters = JsonUtil.toMap(getParameter());
				url = UrlUtil.parameterize(url, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if("05".equals(getType())){
			String contextPath = Environment.getInstance().getContextPath();
			url = contextPath +url; 
		}
		if("@@".equals(getType())){
			url = "";
		}
		return url;
	}
	
	@Ignore
	public String getIconUrl(){
		if(!StringUtil.isBlank(getIcon())){
			String contextPath = Environment.getInstance().getContextPath();
			this.icon = contextPath +"/res/images/icon/"+ icon; 
		}
		return this.icon;
	}
	
	/** setter and getter method */
	public void setIcon(String icon){
		this.icon = icon;
	}
	public String getIcon(){
		return this.icon; 
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}
	public void setParameter(String parameter){
		this.parameter = parameter;
	}
	public String getParameter(){
		return this.parameter;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}

	@Ignore
	public List<Menu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}
	
	@Ignore
	public void addSubMenu(Menu subMenu){
		this.getSubMenu().add(subMenu);
	}
	
}