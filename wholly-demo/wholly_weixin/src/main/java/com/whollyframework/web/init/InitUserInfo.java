package com.whollyframework.web.init;

import java.util.Date;

import org.apache.log4j.Logger;

import com.whollyframework.util.Security;
import com.whollyframework.util.sequence.Sequence;
import com.whollyframework.utils.ServicesFactory;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.dbservice.user.service.UserService;

/**
 * The user initialization object.
 */
public class InitUserInfo implements IInitialization {

	private final static Logger LOG = Logger.getLogger(InitUserInfo.class);
	
	public void run() {
		
		try {
			UserService service = (UserService)ServicesFactory.getService("userService");
			LOG.info("检查用户信息......");
			if (service.isEmpty()) {
				LOG.info("用户信息初始化......");
				UserVO user = new UserVO();
				user.setId(Sequence.getSequence());
				user.setName("系统管理员");
				user.setLoginName("administrator");
				user.setAliasName("admin");
				user.setUserPassword(Security.encryptPassword("123"));
				user.setSex("1");
				user.setSuperAdmin(true);
				user.setStatus(1);
				user.setUserType(0);
				user.setCertType("00");
				user.setCertId("123456789987654321");
				user.setSortId(100);
				user.setAuthor("系统管理员");
				user.setAuthorId(user.getId());
				user.setCreated(new Date());
				user.setLastModifyId(user.getId());
				user.setLastModified(new Date());
				service.doCreate(user);
				LOG.info("用户信息初始化成功！");
			}
		} catch (Exception e) {
			LOG.error("用户信息初始化失败！");
			throw new RuntimeException(e.getMessage());
		}
	}
}
