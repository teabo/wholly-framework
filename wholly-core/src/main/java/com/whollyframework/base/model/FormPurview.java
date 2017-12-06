package com.whollyframework.base.model;

//审批文件域和按钮的权限管理

import java.util.HashMap;
import java.util.Map;

import com.whollyframework.util.StringUtil;
import com.whollyframework.util.property.DefaultProperty;

public class FormPurview {

	private Map<String, String> mapStyle;
	private Map<String, String> mapFieldReadOnly;
	private Map<String, String> mapFieldVisibility;

	private Map<String, String> mapFinishButton = new HashMap<String, String>();
	private Map<String, String> mapFinishButton2 = new HashMap<String, String>();
	private Map<String, String> mapUserButton2 = new HashMap<String, String>();
	public static Map<String, String> mapUserButton = new HashMap<String, String>();

	static {
		String userButtons = DefaultProperty.getProperty("user.button.names");
		String buttonValues = DefaultProperty.getProperty("user.button.values");
		if (!StringUtil.isBlank(userButtons)) {
			String[] buttons = userButtons.split(";");
			String[] values = new String[buttons.length];
			if (!StringUtil.isBlank(buttonValues))
				values = buttonValues.split(";");
			for (int i = 0; i < buttons.length; i++) {
				if (StringUtil.isBlank(values[i])) {
					mapUserButton.put(buttons[i], "false");
				} else {
					mapUserButton.put(buttons[i], values[i]);
				}
			}
		}
	}
	private String stopBtn = "";// 停止按钮，默认不显示
	private String startBtn = "";// 启动按钮，默认不显示
	private String publishBtn = "";// 发布按钮，默认不显示
	private String exitBtn = "true";// 默认显示
	private String saveBtn = "";// 保存按钮，默认不显示
	private String finishBtn = "";// 完成按钮，默认不显示
	private String yesBtn = "";// 同意按钮，默认不显示
	private String noBtn = "";// 不同意按钮，默认不显示
	private String zwBtn = "false";// 正文按钮
	private String attachBtn = "";// 附件按钮
	private String mindBtn = "";// 意见按钮，默认不显示
	private String writeBtn = "";// 签名按钮，默认不显示
	private String writeDlgBtn = "";// 对话框签名按钮，默认不显示
	private String hisBtnExit = "";// 历史退出按钮

	private String saveAndExit = "";// 保存退出
	private String saveAndNew = "";// 保存并新建

	private String newBtn = "";// 新建按钮
	private String deleteBtn = "";// 删除按钮

	private String rubBtn = "";// 作废退出按钮，默认不显示

	private String reportBtn = "0";// 0：默认不报表打印
	private int fstate = 0; // 流程状态
	private String sysMap = ""; // 当前活动参数
	private String modifyAttach = "false";// 设置附件是否是可以添加、修改 By 颜学辉 2006-04-06
	// （此按钮控制不再使用 颜学辉 2006-06-12）

	private int attachPurview = 0;// 文件附件的权限,以十六进制数表示,不同的数字位表示不同的权限,具体如下: By 颜学辉

	// 0x0100,0x0011, 0x0010,0x0001,0x0000
	/** 导入按钮 **/
	private String importBtn = "false";// 默认不显示

	// 插入文件头权限\\u63d2入印章权限\\u65b0建权限\\u5220除附件权限\\u4fee改权限
	// 4 \ 3 \ 2 \ 1 \ 0
	// 如果为负数则表示不能读到附件

	public String getImportBtn() {
		return importBtn;
	}

	public void setImportBtn(String importBtn) {
		this.importBtn = importBtn;
	}

	public void setFstate(int pState) {
		this.fstate = pState;
	}

	public int getFstate() {
		return this.fstate;
	}

	public String getSysMap() {
		return sysMap;
	}

	public void setSysMap(String sysMap) {
		this.sysMap = sysMap;
	}

	public void setFieldStyle(String strFieldName, String strStyle) {

		if (null == mapStyle)
			mapStyle = new HashMap<String, String>();

		mapStyle.put(strFieldName, strStyle);

	}

	public String getFieldStyle(String strFieldName) {

		if (null == mapStyle)
			return "";

		if (null == mapStyle.get(strFieldName))
			return "";

		return (String) mapStyle.get(strFieldName);
	}

	public void setFieldReadOnly(String strFieldName, boolean bReadOnly) {

		if (null == mapFieldReadOnly) {
			mapFieldReadOnly = new HashMap<String, String>();
			mapFieldVisibility = new HashMap<String, String>();
		}
		if (bReadOnly) {
			mapFieldReadOnly.put(strFieldName, "true");
			mapFieldVisibility.put(strFieldName, "display:none");
		} else {
			mapFieldReadOnly.put(strFieldName, "false");
			mapFieldVisibility.put(strFieldName, "display:");
		}
	}

	public boolean getFieldReadOnly(String strFieldName) {
		// 返回的域的可读属性，默认为只读
		if (null == mapFieldReadOnly) {
			return true;
		} else if (null == mapFieldReadOnly.get(strFieldName))
			return true;
		else
			return Boolean.parseBoolean((String) mapFieldReadOnly
					.get(strFieldName));
	}

	public String getFieldVisibility(String strFieldName) {

		// 返回的域的可视属性，默认为显示
		String strVisibility;
		if (null == mapFieldVisibility) {
			strVisibility = "";
		} else if (null == mapFieldVisibility.get(strFieldName))
			strVisibility = "display:none";
		else
			strVisibility = (String) mapFieldVisibility.get(strFieldName);

		return strVisibility;
	}

	// 添加一个完成类型按钮（在同意、不同意按钮后面显示，当文件有多个分支时使用此方法添加）
	// strBtName参数给出按钮名称（中文名）
	// strFinishName参数定义提交时分支判断的关键字。
	public void addFinishButton(String strBtName, String strFinishName) {
		if (null == this.mapFinishButton)
			mapFinishButton = new HashMap<String, String>();

		mapFinishButton.put(strBtName, strFinishName);
	}

	public void addFinishButton(String strBtName, String strFinishName,
			boolean widthways) {
		if (widthways) {
			if (null == this.mapFinishButton2)
				mapFinishButton2 = new HashMap<String, String>();

			mapFinishButton2.put(strBtName, strFinishName);
		} else {
			addFinishButton(strBtName, strFinishName);
		}
	}

	public void removeFinishButton(String btnName) {
		removeFinishButton(btnName, false);
	}

	public void removeFinishButton(String btnName, boolean widthways) {
		if (widthways) {
			mapFinishButton2.remove(btnName);
		} else {
			mapFinishButton.remove(btnName);
		}
	}

	// 返回所有完成类型的按钮
	public Map<String, String> getMapFinishButton2() {
		return this.mapFinishButton2;
	}

	// 返回所有完成类型的按钮
	public Map<String, String> getMapFinishButton() {
		return this.mapFinishButton;
	}

	public boolean getAttachBtn() {
		return Boolean.parseBoolean(attachBtn);
	}

	public void setAttachBtn(String attachBtn) {
		this.attachBtn = attachBtn;
	}

	public boolean getExitBtn() {
		return Boolean.parseBoolean(exitBtn);
	}

	public void setExitBtn(String exitBtn) {
		this.exitBtn = exitBtn;
	}

	public boolean getFinishBtn() {
		return Boolean.parseBoolean(finishBtn);
	}

	public void setFinishBtn(String finishBtn) {
		this.finishBtn = finishBtn;
	}

	public boolean getMindBtn() {
		return Boolean.parseBoolean(mindBtn);
	}

	public void setMindBtn(String mindBtn) {
		this.mindBtn = mindBtn;
	}

	public boolean getNoBtn() {
		return Boolean.parseBoolean(noBtn);
	}

	public void setNoBtn(String noBtn) {
		this.noBtn = noBtn;
	}

	public boolean getSaveBtn() {
		return Boolean.parseBoolean(saveBtn);
	}

	public void setSaveBtn(String saveBtn) {
		this.saveBtn = saveBtn;
	}

	public boolean getSaveAndNew() {
		return Boolean.parseBoolean(saveAndNew);
	}

	public void setSaveAndNew(String saveAndNew) {
		this.saveAndNew = saveAndNew;
	}

	public boolean getWriteBtn() {
		return Boolean.parseBoolean(writeBtn);
	}

	public void setWriteBtn(String writeBtn) {
		this.writeBtn = writeBtn;
	}

	public boolean getYesBtn() {
		return Boolean.parseBoolean(yesBtn);
	}

	public void setYesBtn(String yesBtn) {
		this.yesBtn = yesBtn;
	}

	public boolean getZwBtn() {
		return Boolean.parseBoolean(zwBtn);
	}

	public void setZwBtn(String zwBtn) {
		this.zwBtn = zwBtn;
	}

	public void setBtnStyle_ReadOnly() {
		this.setSaveBtn("false");
		this.setSaveAndExit("false");
		this.setSaveAndNew("false");
		this.setFinishBtn("false");
		this.setWriteBtn("false");
		this.setMindBtn("false");
		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setZwBtn("false");
		this.setAttachBtn("false");
		this.setNewBtn("false");
		this.setDeleteBtn("false");
		this.setExitBtn("true");
		this.addUserButton("retakeBtn", "false");
	}

	/**
	 * 设置按钮样式 只显示退出、正文、附件
	 */
	public void setBtnStyle_OnlyAttach() {
		setBtnStyle_ReadOnly();
		this.setAttachBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、完成、正文、附件、签名
	 */
	public void setBtnStyle_Write() {
		this.setBtnStyle_OnlyAttach();
		this.setFinishBtn("true");
		this.setWriteBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、完成、正文、附件、签名
	 */
	public void setBtnStyle_WriteBc() {
		this.setBtnStyle_OnlyAttach();
		this.setSaveBtn("true");
		this.setFinishBtn("true");
		this.setWriteBtn("true");
	}

	/**
	 * 设置按钮样式 显示退出、保存退出、归档、正文、附件、签名
	 */
	public void setBtnStyle_Pige() {
		this.setBtnStyle_OnlyAttach();
		this.setFinishBtn("true");
		this.setWriteBtn("true");
		addFinishButton("保存退出", "No");
		addFinishButton("归档", "Yes");
	}

	/**
	 * 设置按钮样式 显示退出、保存退出、归档、正文、附件、签名
	 */
	public void setBtnStyle_Pige1() {
		this.setBtnStyle_OnlyAttach();
		this.setFinishBtn("true");
		addFinishButton("保存退出", "No");
	}

	/**
	 * 设置按钮样式 只显示退出、完成、正文、附件
	 */
	public void setBtnStyle_Finish() {
		this.setBtnStyle_OnlyAttach();
		this.setFinishBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、完成、正文、附件、意见
	 */
	public void setBtnStyle_Mind() {
		this.setBtnStyle_OnlyAttach();
		this.setFinishBtn("true");
		this.setMindBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、同意、不同意、意见
	 */
	public void setBtnStyle_MindOnly() {
		this.setExitBtn("true");
		this.setYesBtn("true");
		this.setNoBtn("true");
		this.setMindBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、同意、不同意、正文、附件、意见
	 */
	public void setBtnStyle_YesOrNo() {
		this.setBtnStyle_OnlyAttach();
		this.setYesBtn("true");
		this.setNoBtn("true");
		this.setMindBtn("true");
	}

	/**
	 * 设置按钮样式 只显示退出、同意、不同意、正文、附件、意见
	 */
	public void setBtnStyle_YesOrNoQm() {
		this.setBtnStyle_OnlyAttach();
		this.setYesBtn("true");
		this.setNoBtn("true");
		this.setWriteBtn("true");
	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_CreateFile() {
		addFinishButton("发送", "Yes");
		this.setExitBtn("true");
		this.setAttachBtn("false");
		this.setSaveBtn("true");
		this.addUserButton("retakeBtn", "false");
	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_CreateFileZP() {
		addFinishButton("发送", "Yes");
		this.setExitBtn("true");
		this.setSaveBtn("false");
		this.setAttachBtn("false");
		this.setSaveBtn("true");
	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_OnlyFinish() {
		this.setExitBtn("true");
		this.setFinishBtn("true");

		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setSaveBtn("false");
		this.setZwBtn("false");
		this.setWriteBtn("false");
		this.setMindBtn("false");
		this.setAttachBtn("false");

	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_OnlyMind() {
		this.setExitBtn("true");
		this.setFinishBtn("true");
		this.setAttachBtn("true");
		this.setMindBtn("true");

		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setSaveBtn("false");
		this.setWriteBtn("false");
	}

	/**
	 * 设置按钮样式 只显示退出、同意、不同意、正文、附件、意见
	 */
	public void setBtnStyle_Write1() {
		this.setBtnStyle_OnlyAttach();
		this.setYesBtn("true");
		this.setNoBtn("true");
	}

	/**
	 * 设置按钮样式 显示退出、保存、正文、附件、意见
	 */
	public void setBtnStyle_NoAttach() {
		this.setExitBtn("true");
		this.setFinishBtn("false");
		this.setYesBtn("true");
		this.setNoBtn("true");
		this.setSaveBtn("false");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("true");
		this.setWriteBtn("false");
	}

	/**
	 * 设置按钮样式，起草状态 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_NoMindAndAttach() {
		this.setExitBtn("true");
		this.setFinishBtn("true");
		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setYesBtn("false");
		this.setNoBtn("fslse");
		this.setSaveBtn("true");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("false");
		this.setWriteBtn("false");

	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_NoSave() {
		this.setExitBtn("true");
		this.setFinishBtn("true");
		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setNoBtn("fslse");
		this.setSaveBtn("fslse");
		this.setZwBtn("false");
		this.setAttachBtn("fslse");
		this.setMindBtn("false");
		this.setWriteBtn("false");
	}

	/**
	 * 设置按钮样式 添加保存退出、完成按钮，再显示保存、正文、附件、意见
	 */
	public void setBtnStyle_NoMindAndAttach1() {
		this.setExitBtn("true");
		addFinishButton("发送", "Yes");
		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setYesBtn("false");
		this.setNoBtn("fslse");
		this.setSaveBtn("true");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("false");
		this.setWriteBtn("false");
	}

	public void setBtnStyle_Zdy() {
		this.setExitBtn("true");
		this.setFinishBtn("false");
		this.setYesBtn("true");
		this.setNoBtn("false");
		this.setSaveBtn("false");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("true");
		this.setWriteBtn("false");
	}

	/**
	 * 设置按钮样式 没有意见和附件按钮
	 */
	public void setBtnStyle_NoAddmindAndAttach() {
		this.setExitBtn("true");
		this.setFinishBtn("false");
		this.setYesBtn("true");
		this.setNoBtn("true");
		this.setSaveBtn("false");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("false");
		this.setWriteBtn("false");
	}

	/**
	 * 设置按钮样式 没有意见和附件按钮
	 */
	public void setBtnStyle_NoFinishBtn() {
		this.setExitBtn("true");
		this.setFinishBtn("false");
		this.setYesBtn("false");
		this.setNoBtn("false");
		this.setSaveBtn("true");
		this.setZwBtn("false");
		this.setAttachBtn("ture");
		this.setMindBtn("false");
		this.setWriteBtn("false");
		this.setSaveAndExit("true");
	}

	public void setBtnStyle_Normal() {
		this.setSaveBtn("true");
		this.setSaveAndExit("true");
		this.setSaveAndNew("true");
		this.setExitBtn("true");
		this.setNewBtn("true");
		this.setDeleteBtn("true");
	}

	public Map<String, String> getMapFieldReadOnly() {
		return mapFieldReadOnly;
	}

	public Map<String, String> getMapFieldVisibility() {
		return mapFieldVisibility;
	}

	public String getHisBtnExit() {
		return hisBtnExit;
	}

	public void setHisBtnExit(String hisBtnExit) {
		this.hisBtnExit = hisBtnExit;
	}

	public boolean getRubBtn() {
		return Boolean.parseBoolean(rubBtn);
	}

	public void setRubBtn(String rubBtn) {
		this.rubBtn = rubBtn;
	}

	public boolean getSaveAndExit() {
		return Boolean.parseBoolean(saveAndExit);
	}

	public void setSaveAndExit(String saveAndExit) {
		this.saveAndExit = saveAndExit;
	}

	public String getReportBtn() {
		return reportBtn;
	}

	public void setReportBtn(String reportBtn) {
		this.reportBtn = reportBtn;
	}

	public boolean getWriteDlgBtn() {
		return Boolean.parseBoolean(writeDlgBtn);
	}

	public void setWriteDlgBtn(String writeDlgBtn) {
		this.writeDlgBtn = writeDlgBtn;
	}

	public boolean getNewBtn() {
		return Boolean.parseBoolean(newBtn);
	}

	public void setNewBtn(String newBtn) {
		this.newBtn = newBtn;
	}

	public boolean getDeleteBtn() {
		return Boolean.parseBoolean(deleteBtn);
	}

	public void setDeleteBtn(String deleteBtn) {
		this.deleteBtn = deleteBtn;
	}

	public boolean getModifyAttach() {
		return Boolean.parseBoolean(modifyAttach);
	}

	public void setModifyAttach(String modifyAttach) {
		this.modifyAttach = modifyAttach;
	}

	public int getAttachPurview() {
		return attachPurview;
	}

	public void setAttachPurview(int attachPurview) {
		this.attachPurview = attachPurview;
	}

	public void addUserButton(String button, String value) {
		mapUserButton2.put(button, value);
	}

	public boolean getUserButton(String button) {
		String value = mapUserButton2.get(button);
		if (StringUtil.isBlank(value))
			return Boolean.parseBoolean(mapUserButton.get(button));
		return Boolean.parseBoolean(value);
	}

	public String getPublishBtn() {
		return publishBtn;
	}

	public void setPublishBtn(String publishBtn) {
		this.publishBtn = publishBtn;
	}

	public String getStopBtn() {
		return stopBtn;
	}

	public void setStopBtn(String stopBtn) {
		this.stopBtn = stopBtn;
	}

	public String getStartBtn() {
		return startBtn;
	}

	public void setStartBtn(String startBtn) {
		this.startBtn = startBtn;
	}
	
}
