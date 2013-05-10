package com.fstudio.servlet.model;

import java.util.List;

import com.fstudio.Constants;


/**
 * 整串消息
 * @author tiant5
 *
 */
public class TotalMsg {
	
	
	private List<MSGItem> allMsg;
	
	public TotalMsg(List<MSGItem> allMSg)
	{
		this.allMsg = allMSg;
	}
	
	
	/**
	 * 消息类型
	 * @return
	 */
	public String getMsgType()
	{
		if (allMsg == null)
		{
			return null;
		}
		for (int i = 0; i < allMsg.size(); i++)
		{
			MSGItem item = allMsg.get(i);
			String key = item.getKey();
			if (key.equals(Constants.XML_WC_MsgType))
			{
				return item.getValue();
			}
		}
		return null;
	}
	
	
	/**
	 * 消息内容
	 * @return
	 */
	public String getMsgContent()
	{
		if (allMsg == null)
		{
			return null;
		}
		for (int i = 0; i < allMsg.size(); i++)
		{
			MSGItem item = allMsg.get(i);
			String key = item.getKey();
			if (key.equals(Constants.XML_WC_Content))
			{
				return item.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 全部转化为XML的字符流
	 * @return
	 */
	public String toXMLString()
	{
		return null;
	}
	
	

}
