package com.fstudio.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fstudio.servlet.model.MSGItem;

/**
 * WC接口的解析
 * @author tiant5
 *
 */
public class XMLParser4WC 
{
	
	
	private List<MSGItem> listMsg;
	/**
	 * 解析, 识别各类消息
	 * @param root
	 */
	public XMLParser4WC(Element root)
	{
		processRoot(root);
	}

	private void processRoot(Element root) 
	{
		NodeList nl = root.getChildNodes();
		int len = nl.getLength();
		listMsg = new ArrayList<MSGItem>();
		for ( int i = 0; i < len; i++)
		{
			Node item = nl.item(i);
			if (item instanceof Element)
			{
				//加载MSG选项
				Element el = (Element)item;
				String nodeName = el.getNodeName();
				MSGItem msgItem = new MSGItem();
				msgItem.setKey(nodeName);
				msgItem.setValue(el.getTextContent());
				listMsg.add(msgItem);
			}
		}
	}

	public List<MSGItem> getListMsg() {
		return listMsg;
	}

}
