package com.fstudio.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 专门用来解析Usage文件的类
 * @author tiant5
 *
 */
public class XMLLocalUsageParser {
	
	private List<Usage> usageList = new ArrayList<Usage>();
	
	//单词.xml的基本属性
	private String XML_ATT_TYPE = "type";
	private String XML_ATT_ENG = "eng";
	private String XML_ATT_CN = "cn";
	private String XML_ATT_REMARK = "remark";
	
	public XMLLocalUsageParser(Element root)
	{
		processUsages(root);
	}
	
	private void processUsages(Element usElement)
	{
		NodeList childList = usElement.getChildNodes();
		int len = childList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = childList.item(i);
			if (node instanceof Element)
			{
				Element element = (Element)node;
				String type = element.getAttribute(XML_ATT_TYPE);
				String remark = element.getAttribute(XML_ATT_REMARK);
				String cn = element.getAttribute(XML_ATT_CN);
				String eng = element.getAttribute(XML_ATT_ENG);
				Usage u = new Usage();
				u.setChsSent(cn);
				u.setEngSent(eng);
				u.setRemark(remark);
				u.setType(Integer.valueOf(type));
				usageList.add(u);
			}
		}
	}

	public List<Usage> getUsageList() {
		return usageList;
	}
	
}
