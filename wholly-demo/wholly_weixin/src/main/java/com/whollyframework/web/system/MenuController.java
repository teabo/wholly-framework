package com.whollyframework.web.system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.support.JDBCProperty;
import com.whollyframework.util.Security;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.TreeNode;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.utils.http.ResponseUtil;
import com.whollyframework.dbservice.menu.model.Menu;
import com.whollyframework.dbservice.menu.service.MenuService;
import com.whollyframework.dbservice.permission.model.Permission;
import com.whollyframework.dbservice.permission.service.PermissionService;

import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@RequestMapping(value = "/control/system/menu")
public class MenuController extends BaseController<Menu, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2821959082135068931L;

	public MenuController() {
		super(new Menu());
	}

	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionService permissionService;

	@Override
	public IDesignService<Menu, String> getService() {
		return menuService;
	}

	/**
	 * 重置菜单的action
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/reset" })
	public String resetMenu(HttpServletRequest request, HttpServletResponse response) {
		boolean result = true;// 重置成功
		try {
			setRequest(request);
			setResponse(response);

			reset();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		getRequest().setAttribute("result", result);//
		return fullPathForward("/control/system/menu/resetMenu");
	}

	private void reset() {
		SQLExec sqlExec = new SQLExec();
		// 设置数据库参数
		sqlExec.setDriver("oracle.jdbc.driver.OracleDriver");
		sqlExec.setUrl(JDBCProperty.getProperty("jdbc.url"));
		sqlExec.setUserid(JDBCProperty.getProperty("jdbc.username"));
		sqlExec.setPassword(Security.decryptPassword(JDBCProperty.getProperty("jdbc.password")));
		// 要执行的脚本
		URL propsUrl = Thread.currentThread().getContextClassLoader().getResource("resetMenu.sql");
		sqlExec.setSrc(new File(propsUrl.getPath()));
		// 有出错的语句该如何处理
		// sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class,
		// "abort")));
		sqlExec.setPrint(false); // 设置是否输出
		// 输出到文件 sql.out 中；不设置该属性，默认输出到控制台
		// sqlExec.setOutput(new File("src/sql.out"));
		sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
		sqlExec.execute();
	}

	@RequestMapping({ "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			// String uid = getUser().getId();
			// List<Menu> ms = menuService.getTopMenu(uid);
			// menuService.getSubMenuByPid(uid, ms.get(0).getId());
			// System.out.println(ms.get(0).getName());

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list();
	}

	@RequestMapping({ "/deleteAjax" })
	public void deleteAjax(String[] _selects, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setRequest(request);
		setResponse(response);
		this._selects = _selects;
		deleteAjax();
	}

	@RequestMapping({ "/getMenuListAjax" })
	public void getMenuListAjax(HttpServletRequest request, HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			Map<String, String> menuMap = menuService.getAllMenuMap();

			ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(menuMap));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping({ "/saveAndNew" })
	public String saveAndNew(Menu content, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setRequest(request);
		setResponse(response);
		setContent(content);
		try {
			if (StringUtil.isBlank(getContent().getId())) {
				getService().doCreate(getContent());
			} else {
				getService().doUpdate(getContent());
			}
			addActionMessage("保存成功！");

			Menu m = new Menu();
			m.setIstemp(1);
			setContent(m);
			request.setAttribute("content", m);
			return create();
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("1", e.getMessage());
			return create();
		}
	}

	@RequestMapping({ "/save" })
	public String save(Menu content, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			setRequest(request);
			setResponse(response);
			String[] paramArr = getParams().getParameterAsArray("params");
			String paramStr = jsonHandle(paramArr);
			content.setParameter(paramStr);
			setContent(content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return save();
	}

	private String jsonHandle(String[] paramArr) {
		String res = "";
		if (paramArr.length > 1) {
			// Map<String, String> data = new LinkedHashMap<String, String>();
			JSONObject data = new JSONObject();
			for (int i = 0; i < paramArr.length; i += 2) {
				try {
					data.put(paramArr[i], paramArr[i + 1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			res = data.toString();
		}
		return res;
	}

	/**
	 * 转向菜单管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/toMenu")
	public String toMenuIfo() {
		return "control/system/menu/menu";
	}

	/**
	 * 异步菜单树数据
	 */
	@RequestMapping("/showMenuTree")
	public void showMenuTree(HttpServletRequest request, HttpServletResponse response) {
		List<Menu> menus = null;
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();

		String id = request.getParameter("id");

		try {
			menus = menuService.simpleQueryByParentId(id);
			String state = "open";
			if (menus != null && menus.size() > 0) {
				for (Menu menu : menus) {
					TreeNode treeNode = new TreeNode();
					treeNode.setId(menu.getId());
					treeNode.setText(menu.getName());
					treeNode.setHref(menu.getUrl());
					List<TreeNode> childs = menuService.getSubMenuTree(menu.getId(), null);
					if (childs != null && childs.size() > 0) {
						treeNode.setChildren(childs);
						treeNode.setState(state);
						if ("open".equals(state)) {
							state = "closed";
						}
					}
					treeNodes.add(treeNode);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(treeNodes));
	}

	/**
	 * 一次性加载菜单数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/showMenuTreeOnce")
	public void showMenuTreeOnce(HttpServletRequest request, HttpServletResponse response) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		TreeNode treeNode = new TreeNode();
		treeNode.setText("顶级菜单");
		treeNode.setId("");
		try {
			treeNodes = menuService.getAllMenuTree(null);
			treeNodes.add(0,treeNode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(treeNodes));
	}

	/**
	 * 获取单个菜单节点信息
	 * 
	 * @return
	 */
	@RequestMapping("/getMenuById")
	public void getMenuById(HttpServletRequest request, HttpServletResponse response) {
		Menu menu = null;
		String id = request.getParameter("id");
		
		try {
			menu = menuService.find(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(menu));
	}

	/**
	 * 添加或更新菜单节点
	 * 
	 * @return
	 */
	@RequestMapping("/saveOrUpdateMenu")
	public void saveOrUpdateMenu(Menu menu, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();

		setRequest(request);
		setResponse(response);
		String[] paramArr = getParams().getParameterAsArray("params");
		String paramStr = jsonHandle(paramArr);
		menu.setParameter(paramStr);
		setContent(menu);

		try {
			setPublicVariables();
			if (StringUtil.isBlank(getContent().getId())) {
				getService().doCreate(getContent());
			} else {
				getService().doUpdate(getContent());
			}

			map.put("msg", "保存成功！");
			map.put("mid", menu.getId());
		} catch (Exception e) {
			map.put("msg", "保存失败！");
			e.printStackTrace();
		}

		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(map));
	}

	/**
	 * 删除节点
	 * 
	 * @return
	 */
	@RequestMapping("/deleteMenuById")
	public void deleteMenuById(String id, HttpServletResponse response) {
		String msg = null;
		try {
			getService().doRemove(id);
			msg = "删除成功！";
		} catch (Exception e) {
			msg = "删除失败！";
			e.printStackTrace();
		}

		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(msg));
	}

	/**
	 * 一键上传图标
	 */
	@RequestMapping("/uploadImg")
	@ResponseBody
	public Map<String, String> uploadImg(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();

		String fileName = file.getOriginalFilename();

		System.out.println("OriginalFilename: " + fileName);
		try {
			System.out.println("InputStream: " + file.getInputStream());
			if (!file.isEmpty()) {
				String realpath = request.getSession().getServletContext().getRealPath("/res/images/icon/");
				System.out.println(realpath);
				System.out.println(request.getContextPath());
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realpath + "/" + fileName));
				String path = request.getContextPath() + "/res/images/icon/";
				map.put("path", path);
			}
			map.put("msg", "success");
		} catch (IOException e) {
			map.put("msg", "fail");
			e.printStackTrace();
		}
		map.put("fileName", fileName);
		return map;
	}

	/**
	 * 转向菜单资源节点快速绑定角色的页面
	 * 
	 * @return
	 */
	@RequestMapping("/toBindRoles")
	public String toBindRoles() {
		return "control/system/menu/menuRole";
	}

	/**
	 * 为菜单资源节点绑定角色
	 */
	@RequestMapping("/doBind")
	@ResponseBody
	public Map<String, String> doBind(String[] _selects, String mid) {
		Map<String, String> rtn = new HashMap<String, String>();

		for (String rid : _selects) {
			Permission permission = new Permission();
			permission.setRoleId(rid);
			permission.setResId(mid);

			try {
				permissionService.doCreate(permission);
				rtn.put("code", "1");
				rtn.put("msg", "操作成功！");
			} catch (Exception e) {
				rtn.put("code", "0");
				rtn.put("msg", "操作失败！");
				e.printStackTrace();
			}
		}

		return rtn;
	}

	/**
	 * 为菜单资源节点释放角色
	 */
	@RequestMapping("/unBind")
	@ResponseBody
	public Map<String, String> unBind(String[] _selects, String mid) {
		Map<String, String> rtn = new HashMap<String, String>();

		for (String rid : _selects) {
			ParamsTable params = new ParamsTable();
			params.setParameter("s_res_id_", mid);
			params.setParameter("s_role_id", rid);

			try {
				List<Permission> pList = (List<Permission>) permissionService.simpleQuery(params);
				if (pList != null && pList.size() > 0) {
					permissionService.doRemove(pList.get(0));
				}
				rtn.put("code", "1");
				rtn.put("msg", "操作成功！");
			} catch (Exception e) {
				rtn.put("code", "0");
				rtn.put("msg", "操作失败！");
				e.printStackTrace();
			}
		}

		return rtn;
	}

}
