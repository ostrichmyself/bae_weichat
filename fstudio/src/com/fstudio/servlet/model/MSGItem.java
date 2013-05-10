package com.fstudio.servlet.model;

/**
 * 消息定义的一项
 * 发过去的数据需要配置, 因此这里最好做点配置工作
 * @author tiant5
 *
 */
public class MSGItem {
	
	private String key;//消息名
	private String value;//消息值
	private boolean isCData; //是否为CData数据类型
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isCData() {
		return isCData;
	}
	public void setCData(boolean isCData) {
		this.isCData = isCData;
	}
}
