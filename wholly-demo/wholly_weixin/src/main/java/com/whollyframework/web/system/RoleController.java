package com.whollyframework.web.system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.TreeNode;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.utils.http.ResponseUtil;
import com.whollyframework.dbservice.menu.service.MenuService;
import com.whollyframework.dbservice.permission.model.Permission;
import com.whollyframework.dbservice.permission.service.PermissionService;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.role.service.RoleService;
import com.whollyframework.dbservice.roleuser.service.RoleUserService;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.dbservice.user.service.UserServiceImpl;


@Controller
@Scope("prototype")
@RequestMapping(value = "/control/system/role")
public class RoleController extends BaseController<Role, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2821959082135068931L;

	public RoleController() {
		super(new Role());
	}

	@Autowired
	RoleService roleService;
	
	@Autowired
	RoleUserService roleUserService;
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	PermissionService permissionService;

    @Override
    public IDesignService<Role, String> getService() {
        return roleService;
    }
    
    @RequestMapping({ "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		return forward("list");
	}
    
    @RequestMapping({"/deleteAjax"})
    public void deleteAjax(String[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
         setRequest(request);
         setResponse(response);
         this._selects = _selects;
         deleteAjax();
       }
   
    
    @RequestMapping({"/saveAndNew"})
    public String saveAndNew(Role content, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	setRequest(request);
        setResponse(response);
        setContent(content);
	    try
	    {
	      setPublicVariables();
	      if (StringUtil.isBlank(getContent().getId())) {
	    	  getService().doCreate(getContent());   
	      }else {
	    	  getService().doUpdate(getContent());
	      }
	      addActionMessage("保存成功！");
	      
	      Role m = new Role();
	      m.setIstemp(1);
	      setContent(m);
	      request.setAttribute("content", m);
	      
	      return create();
	    } catch (Exception e) {
	      e.printStackTrace();
	      addFieldError("1", e.getMessage());
	      return forward("content");
	    }
	  }
	
    @RequestMapping({"/roleUserList"})
	protected String roleUserList(HttpServletRequest request, HttpServletResponse response) {
    	setRequest(request);
        setResponse(response);
		return forward("/role_user_list");
	 
	  }
    
    protected String save() {
        boolean isNew = ((ValueObject)getContent()).getIstemp() == 1;
        try
        {
          setPublicVariables();
          if (isNew) getService().doCreate(getContent()); else {
            getService().doUpdate(getContent());
          }
          addActionMessage("保存成功！");
        } catch (Exception e) {
          e.printStackTrace();
          addFieldError("1", e.getMessage());
          if (isNew) ((ValueObject)getContent()).setIstemp(1);
          return forward("content");
        }
        getRequest().setAttribute("content", getContent());
        return redirect("list.action");
      }
    
    @RequestMapping({"/jsonUserRoleList"})
    public void jsonUserRoleList(HttpServletRequest request, HttpServletResponse response){
    	 try {
    		 //this.datas = getService().query(getParams());
    		 setRequest(request);
    	     setResponse(response);
    	     
    	     ParamsTable params = getParams();
    	     String type = params.getParameterAsString("type");//1 已加入用户  2 未加入用户
    	     String roleid = params.getParameterAsString("roleid");//角色id
    	     
    	     DataPackage<UserVO> userData = new DataPackage<UserVO>();
    	     
    	     if(!StringUtil.isBlank(roleid)){//编辑进入
    	    	 if("1".equals(type)){
    	    		 params.setParameter("$SYS_USER_ROLE_SET$[ID==USER_ID]S_ROLE_ID",roleid);
    	    	     userData = userService.query(params);
    	    	 }else if("2".equals(type)){//为加入的用户
    	    		 //
    	    		 //ParamsTable params1 = new ParamsTable();
    	    	     params.setParameter("$SYS_USER_ROLE_SET$[ID!=USER_ID]S_ROLE_ID",roleid);
    	    	     userData = userService.query(params);
    	    	 }
    	     }else{//新增进入
    	    	if("2".equals(type)){
    	    		userData = userService.query(params);
    	    	}
    	     }
    		 ResponseUtil.setJsonToResponse(getResponse(),userData.toJSON());
    		 
	     } catch (Exception e) {
	         Map<String, String> rtn = new HashMap<String, String>();
	         rtn.put("code", "0");
	         rtn.put("error", "查询参数异常：" + e.getMessage());
	         ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(rtn));
	     }
    }
    
    @RequestMapping({"/unBindAjax"})
    public void unBindAjax(HttpServletRequest request, HttpServletResponse response){
    	Map<String, String> res = new HashMap<String, String>();
    	try {
   		 //this.datas = getService().query(getParams());
   		 setRequest(request);
   	     setResponse(response);
   	     ParamsTable params = getParams();
   	     
   	     //IWebUser curr_user =  getUser();
   	     
   	     String roleid = params.getParameterAsString("roleid");
   	     String uids = params.getParameterAsString("uids");
   	     
   	     if(!StringUtil.isBlank(uids)){
   	    	roleService.doRoleUserRemove(roleid,Arrays.asList(uids.split(",")));   	    	 
   	     }
   	     
   	     res.put("code", "1");
   	     res.put("msg", "解绑成功");
   	     
    	}catch(Exception e){
    		res.put("code", "0");
    		res.put("msg", "解绑失败");
    		e.printStackTrace();
    	}
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(res));
    }
    
    @RequestMapping({"/bindAjax"})
    public void bindAjax(HttpServletRequest request, HttpServletResponse response){
    	Map<String, String> res = new HashMap<String, String>();
    	try {
  		 //this.datas = getService().query(getParams());
  		 setRequest(request);
  	     setResponse(response);
  	     ParamsTable params = getParams();
  	     String roleid = params.getParameterAsString("roleid");
 	     String uids = params.getParameterAsString("uids");
 	     IWebUser curr_user =  getUser();
 	     if(!StringUtil.isBlank(uids)){
 	    	roleService.doRoleUserBind(roleid,Arrays.asList(uids.split(",")), curr_user); 	    	
 	     }
 	     res.put("code", "1");
  	     res.put("msg", "绑定成功");
       	}catch(Exception e){
       		res.put("code", "0");
    		res.put("msg", "绑定失败");
       		e.printStackTrace();
       	}
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(res));
    }
    
    @RequestMapping({"/rolPermissionBindIndex"})
    public String rolPermissionBindIndex(HttpServletRequest request, HttpServletResponse response){
    	try {
     		 //this.datas = getService().query(getParams());
     		 setRequest(request);
     	     setResponse(response);
     	     List<Role> roles = roleService.getAll();
     	     
     	     List<TreeNode> menuTrees = menuService.getAllMenuTree(null);
     	     getRequest().setAttribute("roles", JsonUtil.toJson(roles));
     	     getRequest().setAttribute("menuTrees", JsonUtil.toJson(menuTrees));
     	     
    	}catch(Exception e){
       		e.printStackTrace();
       	}
    	return forward("role_permission");
    }
    
    @RequestMapping({"/rolePermissionBind"})
    public void rolePermissionBind(HttpServletRequest request, HttpServletResponse response){
    	Map<String, String> res = new HashMap<String, String>();
    	try {
     		 //this.datas = getService().query(getParams());
     		 setRequest(request);
     	     setResponse(response);
     	     IWebUser currUser =  getUser();
     	     ParamsTable params = getParams();
     	     String roleid = params.getParameterAsString("roleid");
    	     String resids = params.getParameterAsString("resids");
    	     
    	     if(!StringUtil.isBlank(resids)){
    	    	permissionService.doRoleResBind(roleid,Arrays.asList(resids.split(",")), currUser); 	    	
    	     }else{
    	    	 permissionService.doRoleResBind(roleid,new ArrayList<String>(), currUser);
    	     }
    	     
     	     res.put("code", "1");
     	    res.put("msg", "绑定成功");
    	}catch(Exception e){
       		res.put("code", "2");
    		res.put("msg", "绑定资源权限出错");
       		e.printStackTrace();
       	}
    	ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(res));
    }
    
    @RequestMapping({"/rolePermissionLoadAjax"})
    public void rolePermissionLoadAjax(HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> res = new HashMap<String, Object>();
    	try {
     		 //this.datas = getService().query(getParams());
     		 setRequest(request);
     	     setResponse(response);
     	     
     	     //List<Role> roles = roleService.getAll();
     	     String roleid = getParams().getParameterAsString("roleid");
     	     ParamsTable p = new ParamsTable();
     	     p.setParameter("s_role_id",roleid);
     	     
     	     ArrayList<Permission> pLst = (ArrayList<Permission>) permissionService.simpleQuery(p);
     	     ArrayList<String> sLst = new ArrayList<String>();
     	     if(pLst!=null && pLst.size()>0){
     	    	 for(Permission ps : pLst){
     	    		sLst.add(ps.getResId());
     	    	 }
     	     }
     	     List<TreeNode> menuTrees = menuService.getAllMenuTree(sLst);
     	     res.put("code", "1");
     	     res.put("data", menuTrees);
     	     
    	}catch(Exception e){
    		res.put("code", "2");
    		res.put("msg", "获取角色所属资源出错");
       		e.printStackTrace();
       	}
    	ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(res));
    }
    
    /**
     * 获取所有角色
     */
    @RequestMapping("/showRoleList")
    public void showRoleList(HttpServletResponse response) {
    	List<Role> roleList = null;
    	try {
			roleList = getService().getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(roleList));
    }
    
    /**
     * 根据菜单资源id获取相关联的角色id
     */
    @RequestMapping("/showRidsByMid")
    public void showRidsByMid(String mid, HttpServletResponse response) {
    	List<String> rids = null;
    	
    	try {
			rids = permissionService.getRidsByMid(mid);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(rids));
    }
    
    /**
     * 根据资源节点展示位未关联的角色
     */
    @RequestMapping("/showNotAssociatedRoles")
    public void showNotAssociatedRoles(String mid, HttpServletResponse response) {
    	List<Role> roles = null;
    	
    	try {
			roles = roleService.showRolesNotEqualMid(mid, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(roles));
    }
    
    /**
     * 根据资源节点展示位关联的角色
     */
    @RequestMapping("/showAssociatedRoles")
    public void showAssociatedRoles(String mid, HttpServletResponse response) {
    	List<Role> roles = null;
    	
    	try {
			roles = roleService.showRolesByMid(mid, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(roles));
    }
    
    /**
     * 根据条件查询角色信息
     * @param response
     */
    @RequestMapping("/searchRoles")
    public void searchRoles(HttpServletRequest request, HttpServletResponse response) {
    	 setRequest(request);
    	
    	List<Role> roles = null;
    	
		String mid = getParams().getParameterAsString("mid");
		String name = getParams().getParameterAsString("name");
		String type = getParams().getParameterAsString("type");
    	
		if("1".equals(type)) {
			try {
				roles = roleService.showRolesByMid(mid, name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				roles = roleService.showRolesNotEqualMid(mid, name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	 
		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(roles));
    }
    
}
