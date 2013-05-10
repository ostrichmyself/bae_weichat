package com.fstudio;

import java.util.List;

import com.fstudio.servlet.model.MSGItem;
import com.fstudio.xml.Usage;

/**
 * 微信的工具
 * @author tiant5
 *
 */
public class WeiXinTool 
{
	
	/**
	 * 从一个List中找到key对应的value
	 * @param list
	 * @param key
	 * @return
	 */
	public static String findKeyValueFromList(List<MSGItem> list, String key)
	{
		if (list == null)
		{
			return null;
		}
		
		int size = list.size();
		for ( int i = 0; i < size; i++)
		{
			MSGItem item = list.get(i);
			String itemKey = item.getKey();
			if ( itemKey.equals(key))
			{
				return item.getValue();
			}
		}
		return null;
	}
	
	
	/**
	 * 从一个List中找到key对应的value
	 * @param list
	 * @param key
	 * @return
	 */
	public static MSGItem findItemFromList(List<MSGItem> list, String key)
	{
		if (list == null)
		{
			return null;
		}
		
		int size = list.size();
		for ( int i = 0; i < size; i++)
		{
			MSGItem item = list.get(i);
			String itemKey = item.getKey();
			if ( itemKey.equals(key))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 将一个MSGList转化为String
	 * @param list
	 * @return
	 */
	public static String listMsg2String(List<MSGItem> list)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("<xml>");
		for (int i = 0; i < list.size(); i++)
		{
			MSGItem item = list.get(i);
			
			String key = item.getKey();
			buf.append("<");
			buf.append(key);
			buf.append(">");
			boolean type = item.isCData();
			String value = item.getValue();
			loadContent(value, type, buf);
			
			buf.append("</");
			buf.append(key);
			buf.append(">");
		}
		buf.append("</xml>");
		return buf.toString();
	}
	
	private static void loadContent(String value, boolean isCdata, StringBuffer buf)
	{
		if (isCdata)
		{
			buf.append("<![CDATA[");
			buf.append(value);
			buf.append("]]>");
		}
		else
		{
			buf.append(value);
		}
	}
	

}
