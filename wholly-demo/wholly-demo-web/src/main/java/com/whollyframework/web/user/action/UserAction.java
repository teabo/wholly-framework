package com.whollyframework.web.user.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.web.user.model.UserVO;
import com.whollyframework.web.user.service.IUserService;

@Controller
@RequestMapping(value = "/test")
public class UserAction extends BaseController<UserVO, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2821959082135068931L;

	public UserAction() {
		super(new UserVO());
	}

	@Autowired
	IUserService iUserService;

    @Override
    public IDesignService<UserVO, String> getService() {
        return iUserService;
    }


	
}
