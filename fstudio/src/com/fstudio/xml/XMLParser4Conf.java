package com.fstudio.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fstudio.Constants;
import com.fstudio.servlet.model.MSGItem;

/**
 * 消息配置文件的解析, 可以帮助识别不同的消息
 * @author tiant5
 *
 */
public class XMLParser4Conf 
{
	
	private List<MSGItem> configXMLData;
	/**
	 * 解析, 识别各类消息
	 * @param root
	 */
	public XMLParser4Conf()
	{

	}
	/**
	 * 加载xml
	 * @param req
	 */
	public void processXML(HttpServletRequest req)
	{
		try {
			String realPath = req.getServletContext().getRealPath(Constants.CONF_XML_PATH);
			FileInputStream fis = new FileInputStream(new File(realPath));
			InputSource isSource = new InputSource(fis);
			Document document = null;
			Element root = null;
			DocumentBuilder docBuilder = null;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(isSource);
			root = document.getDocumentElement();
			if(root != null)
			{
				configXMLData = new ArrayList<MSGItem>();
				processRoot(root);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MSGItem> getConfigXMLData() {
		return configXMLData;
	}



	private void processRoot(Element root) 
	{
		NodeList nl = root.getChildNodes();
		int len = nl.getLength();
		for ( int i = 0; i < len; i++)
		{
			Node item = nl.item(i);
			if (item instanceof Element)
			{
				Element theItemElement = (Element)item;
				MSGItem mItem = processPara(theItemElement);
				configXMLData.add(mItem);
			}
		}
		
	}
	
	private MSGItem processPara(Element paraElement)
	{
		MSGItem mItem  = new MSGItem();
		String keyValue = paraElement.getAttribute(Constants.XML_CONF_ATT_KEY);
		String typeValue = paraElement.getAttribute(Constants.XML_CONF_ATT_TYPE);
		//value可能用于默认值
		String valueValue = paraElement.getAttribute(Constants.XML_CONF_ATT_VALUE);
		mItem.setKey(keyValue);
		mItem.setValue(valueValue);
		if (typeValue != null)
		{
			mItem.setCData(true);
		}
		return mItem;
	}

}
