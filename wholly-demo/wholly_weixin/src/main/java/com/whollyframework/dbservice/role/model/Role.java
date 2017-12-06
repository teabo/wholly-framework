package com.whollyframework.dbservice.role.model;

import java.util.List;

import com.whollyframework.annotation.Ignore;
import com.whollyframework.authentications.IRole;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.dbservice.roleuser.model.RoleUser;
import com.whollyframework.dbservice.roleuser.service.RoleUserService;
import com.whollyframework.utils.ServicesFactory;

public class Role extends ValueObject implements IRole{

	private static final long serialVersionUID = 1L;
	private String name; 
	private String description;

	/** setter and getter method */
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	
	@Ignore
	public String getUserCount(){
		String count = "0";
		try {
			
			RoleUserService roleUserService = (RoleUserService) ServicesFactory.getService("roleUserService");
			ParamsTable p = new ParamsTable();
			p.setParameter("s_role_id", getId());
			
			List<RoleUser> rus = (List<RoleUser>) roleUserService.simpleQuery(p);
			if(rus!=null){
				return rus.size()+"";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	

}