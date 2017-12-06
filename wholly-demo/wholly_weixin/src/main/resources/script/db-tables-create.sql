/*
 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 23/11/2017 19:04:44
*/
-- ----------------------------
-- Table structure for WX_MSG_CHAT_HISTORY
-- ----------------------------
CREATE TABLE WX_MSG_CHAT_HISTORY (
  ID varchar(40) NOT NULL COMMENT 'ID',
  TO_USER_NAME varchar(100) DEFAULT NULL COMMENT '接收用户',
  FROM_USER_NAME varchar(200) DEFAULT NULL COMMENT '发送用户',
  CREATE_TIME INTEGER NOT NULL COMMENT '创建时间',
  MSG_TYPE varchar(40) DEFAULT NULL COMMENT '被动响应消息类型(1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息)',
  MSG_XML text DEFAULT NULL COMMENT '消息内容XML',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信聊天记录表';

-- ----------------------------
-- Table structure for WX_MSG_CATEGORY
-- ----------------------------
CREATE TABLE WX_MSG_CATEGORY (
  ID varchar(40) NOT NULL COMMENT 'ID',
  ITEM_KEY varchar(100) DEFAULT NULL COMMENT '应答标识',
  ITEM_DESC varchar(200) DEFAULT NULL COMMENT '应答描述',
  MSG_TYPE varchar(2) DEFAULT NULL COMMENT '被动响应消息类型(1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息)',
  MEDIA_ID varchar(40) DEFAULT NULL COMMENT '媒体信息ID',
  CONTENT text DEFAULT NULL COMMENT '文本消息',
  AUTHOR varchar(100) DEFAULT NULL,
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应答主题信息表';

-- ----------------------------
-- Table structure for WX_MSG_NEWS
-- ----------------------------
CREATE TABLE WX_MSG_NEWS (
  ID varchar(40) NOT NULL COMMENT 'ID',
  ITEM_ID varchar(40) NOT NULL COMMENT '主题ID',
  TITLE varchar(100) DEFAULT NULL COMMENT '新闻标题',
  DESCRIPTION text DEFAULT NULL COMMENT '简要信息',
  PICURL text DEFAULT NULL COMMENT '图片URL',
  URL text DEFAULT NULL COMMENT '新闻URL',
  AUTHOR varchar(100) DEFAULT NULL,
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻信息表';

-- ----------------------------
-- Table structure for WX_MSG_MEDIA
-- ----------------------------
CREATE TABLE WX_MSG_MEDIA (
  ID varchar(40) NOT NULL COMMENT 'ID',
  TITLE varchar(100) DEFAULT NULL COMMENT '媒体标题',
  DESCRIPTION text DEFAULT NULL COMMENT '简要信息',
  musicUrl text DEFAULT NULL COMMENT '媒体URL',
  hqMusicUrl text DEFAULT NULL COMMENT '高清媒体URL',
  funcFlag varchar(1) DEFAULT '0' COMMENT '星标',
  AUTHOR varchar(100) DEFAULT NULL,
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='媒体信息表';

-- ----------------------------
-- Table structure for SYS_CONFIG
-- ----------------------------
CREATE TABLE SYS_CONFIG (
  ID varchar(40) NOT NULL COMMENT 'ID',
  PARAM_KEY varchar(100) DEFAULT NULL COMMENT '参数名称',
  PARAM_VALUE varchar(200) DEFAULT NULL COMMENT '参数值',
  PARAM_DESC varchar(200) DEFAULT NULL COMMENT '参数描述',
  AUTHOR varchar(100) DEFAULT NULL,
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';

-- ----------------------------
-- Table structure for SYS_MENU_INFO
-- ----------------------------
CREATE TABLE SYS_MENU_INFO (
  ID varchar(40) NOT NULL,
  PARENT_ID varchar(40) DEFAULT NULL COMMENT '父菜单',
  NAME varchar(50) DEFAULT NULL COMMENT '名称',
  URL varchar(64) DEFAULT NULL COMMENT '地址',
  ICON varchar(50) DEFAULT NULL COMMENT '图标',
  TYPE varchar(10) DEFAULT NULL COMMENT '菜单类型',
  PARAMETER text COMMENT '参数',
  AUTHOR varchar(100) DEFAULT NULL COMMENT '创建人',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单信息表';

-- ----------------------------
-- Table structure for SYS_OPERATE_LOG
-- ----------------------------
CREATE TABLE SYS_OPERATE_LOG (
  ID bigint(20) NOT NULL COMMENT '主键',
  NUM_ID varchar(20) DEFAULT NULL COMMENT '流水号',
  REG_ID varchar(12) DEFAULT NULL COMMENT '应用标识',
  USER_ID varchar(40) DEFAULT NULL COMMENT '用户标识',
  ORGANIZATION varchar(100) DEFAULT NULL COMMENT '单位名称',
  ORGANIZATION_ID char(12) DEFAULT NULL COMMENT '单位机构代码',
  USER_NAME varchar(30) DEFAULT NULL COMMENT '用户姓名',
  TERMINAL_ID varchar(40) DEFAULT NULL COMMENT '终端标识',
  OPERATE_TIME text COMMENT '操作时间',
  OPERATE_TYPE int(2) DEFAULT NULL COMMENT '操作类型',
  OPERATE_RESULT char(1) DEFAULT NULL COMMENT '操作结果',
  ERROR_CODE char(4) DEFAULT NULL COMMENT '失败原因代码',
  MODEL_PATH  text DEFAULT NULL COMMENT '模块标识',
  OPERATE_NAME text COMMENT '操作所在模块或功能名称',
  OPERATE_CONDITION text COMMENT '操作条件',
  OPERATE_NUMBER bigint(20) DEFAULT NULL COMMENT '本次操作返回的条目数',
  INSERT_TIME datetime DEFAULT NULL COMMENT '日志入库时间',
  COLLECT_TYPE char(1) DEFAULT NULL COMMENT '日志采集方式',
  SENDID varchar(32) DEFAULT NULL COMMENT '日志采集批次号',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志表';

-- ----------------------------
-- Table structure for SYS_ORGANIZATION
-- ----------------------------
CREATE TABLE SYS_ORGANIZATION (
  ID varchar(36) DEFAULT NULL COMMENT '机构ID',
  NAME varchar(100) DEFAULT NULL COMMENT '机构名称',
  ORG_CODE varchar(36) DEFAULT NULL COMMENT '机构代码',
  PARENT_CODE varchar(36) DEFAULT NULL COMMENT '上级机构代码',
  ORG_TYPE decimal(1,0) DEFAULT NULL COMMENT '机构类型（0：单位，1：部门）',
  TELEPHONE varchar(20) DEFAULT NULL COMMENT '联系电话',
  DELFLAG decimal(1,0) DEFAULT NULL COMMENT '删除标记（0：未删除，1：删除）',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  AUTHOR varchar(100) DEFAULT NULL COMMENT '创建人',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '最后修改时间',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改人ID',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SYS_PERMISSION
-- ----------------------------
CREATE TABLE SYS_PERMISSION (
  ID varchar(40) NOT NULL,
  ROLE_ID varchar(40) NOT NULL COMMENT '角色ID',
  RES_TYPE varchar(32) DEFAULT NULL COMMENT '资源类型，包含菜单资源，及以后预留资源',
  RES_ID varchar(36) DEFAULT NULL COMMENT '根据资源类型，取决于具体资源对应表的主键',
  AUTHOR varchar(100) DEFAULT NULL COMMENT '创建人',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息表';

-- ----------------------------
-- Table structure for SYS_ROLE_INFO
-- ----------------------------
CREATE TABLE SYS_ROLE_INFO (
  ID varchar(40) NOT NULL,
  NAME varchar(50) DEFAULT NULL COMMENT '名称',
  DESCRIPTION varchar(50) DEFAULT NULL COMMENT '描述',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  AUTHOR varchar(100) DEFAULT NULL COMMENT '创建人',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- ----------------------------
-- Table structure for SYS_SEQUENCE
-- ----------------------------
CREATE TABLE SYS_SEQUENCE (
  seq_name varchar(50) NOT NULL COMMENT '序列名称',
  min_value int(11) NOT NULL COMMENT '最小值',
  max_value bigint(20) NOT NULL COMMENT '最大值',
  current_val int(20) NOT NULL COMMENT '当前值',
  increment_val int(11) NOT NULL DEFAULT '1' COMMENT '步长',
  PRIMARY KEY (seq_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列信息表';

-- ----------------------------
-- Table structure for SYS_USER_BIND_IP
-- ----------------------------
CREATE TABLE SYS_USER_BIND_IP (
  ID varchar(36) NOT NULL,
  USER_ID varchar(36) DEFAULT NULL COMMENT '用户ID',
  START_IP varchar(16) DEFAULT NULL COMMENT '开始IP',
  END_IP varchar(16) DEFAULT NULL COMMENT '结束IP',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '修改时间',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '修改人ID',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户IP绑定表';

-- ----------------------------
-- Table structure for SYS_USER_INFO
-- ----------------------------
CREATE TABLE SYS_USER_INFO (
  ID varchar(36) NOT NULL COMMENT 'ID',
  LOGINNAME varchar(20) DEFAULT NULL COMMENT '登录名',
  ALIAS_NAME varchar(40) DEFAULT NULL COMMENT '登录别名',
  USERPASSWORD varchar(100) DEFAULT NULL COMMENT '登录密码',
  USERNAME varchar(100) DEFAULT NULL COMMENT '用户名',
  SEX varchar(1) DEFAULT NULL COMMENT '性别',
  USERLEVEL bigint(20) DEFAULT NULL COMMENT '用户级别',
  USERTYPE int(1) DEFAULT NULL COMMENT '用户类型',
  STATUS int(1) DEFAULT NULL COMMENT '状态',
  CERT_TYPE varchar(2) DEFAULT NULL COMMENT '证件类型',
  CERT_ID varchar(40) DEFAULT NULL COMMENT '证件号码',
  LOGIN_ERROR_TIMES int(8) DEFAULT NULL COMMENT '登录失败次数',
  HAS_BIND_IP varchar(1) DEFAULT NULL COMMENT '是否启用IP绑定',
  BIND_IP varchar(16) DEFAULT NULL COMMENT '绑定IP记录ID',
  ORG_ID varchar(36) DEFAULT NULL COMMENT '部门ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  AUTHOR varchar(100) DEFAULT NULL COMMENT '创建人',
  AUTHOR_ID varchar(36) DEFAULT NULL COMMENT '创建人ID',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '修改时间',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '修改人ID',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Table structure for SYS_USER_ROLE_SET
-- ----------------------------
CREATE TABLE SYS_USER_ROLE_SET (
  ID varchar(40) NOT NULL,
  USER_ID varchar(40) DEFAULT NULL COMMENT '用户ID',
  ROLE_ID varchar(40) DEFAULT NULL COMMENT '角色ID',
  CREATED datetime DEFAULT NULL COMMENT '创建时间',
  LAST_MODIFIED datetime DEFAULT NULL COMMENT '创建者',
  LAST_MODIFY_ID varchar(36) DEFAULT NULL COMMENT '最后修改时间',
  SORT_ID bigint(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关联表';

-- ----------------------------
-- Table structure for SYS_VERSION
-- ----------------------------
CREATE TABLE SYS_VERSION (
  ID varchar(36) NOT NULL COMMENT '编号',
  APP_VERSION varchar(36) DEFAULT NULL COMMENT '应用版本',
  DB_VERSION varchar(36) DEFAULT NULL COMMENT '数据库表版本',
  VERSION_DESC text COMMENT '版本描述',
  CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='版本信息表';

