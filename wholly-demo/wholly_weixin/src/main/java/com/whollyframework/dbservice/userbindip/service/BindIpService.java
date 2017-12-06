package com.whollyframework.dbservice.userbindip.service;


import java.util.List;

import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.userbindip.model.BindIp;
/**
 * 用户管理模块--IP绑定
 * @author WangWenGuang
 * @2015-1-13
 */
public interface BindIpService extends IDesignService<BindIp,String>{
  /**
   * 新增IP
   * @param json
   * @param userId
   * @throws Exception
   */
   public void saveBindIps(String json,String userId) throws Exception;
   /**
    * 保存更新IP
    * @param json
    * @throws Exception
    */
   public void updateBindIps(String json) throws Exception;
   /**
    * 删除IP
    * @param json
    * @throws Exception
    */
   public void deleteBindIps(String json)throws Exception; 
   /**
    * 查找IP
    * @param useId
    * @return
    * @throws Exception
    */
   public  List<BindIp> findUserBindIps(String useId)throws Exception;
}
