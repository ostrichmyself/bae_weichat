package com.fstudio.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.baidu.bae.api.memcache.BaeMemcachedClient;
import com.fstudio.Constants;
import com.fstudio.WeiXinTool;
import com.fstudio.servlet.model.MSGItem;
import com.fstudio.servlet.model.UsrAccessInfo;
import com.fstudio.xml.Usage;
import com.fstudio.xml.XMLLocalUsageParser;
import com.fstudio.xml.XMLParser4Conf;
import com.fstudio.xml.XMLParser4WC;

/**
 * 这里是微信接口
 * wc?signature=46bf566049310086acf5fb890faaa4b25c667969&echostr=5855989731078633493&timestamp=1363605524&nonce=1363453870 
 * @author tiant5
 *
 */
public class ServletWeiChat extends HttpServlet {

	private Logger logger;
	private Map<String, UsrAccessInfo> usrInfo; //全部用户列表
	private UsrAccessInfo usrWordStore; //当前用户访问单词的记录
	/**
	 * 
	 */
	private static final long serialVersionUID = 1000000000L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		super.doGet(req, resp);
	}
	
	
	
	/**
	 * 该方法用来校验ID值, 确保成为开发者
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void verify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
	    logger = Logger.getLogger(this.getServletName());
	    logger.log(Level.INFO, "req xxxxx:----:" + req);
	    resp.setContentType("text/plain");
		String echo = req.getParameter(Constants.ECHO_STR);
		if (echo == null)
		{
			resp.getWriter().println("test java bae version 1 of app ");
			resp.getWriter().println("----" + req);
			return;
		}
		else
		{
			logger.log(Level.INFO, "echo string111:----:" + echo);
			resp.getWriter().write(echo);
			return;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding(Constants.ENCODE_SERVER);
		logger = Logger.getLogger(this.getServletName());
		//进行解析
		logger.log(Level.INFO, "--Receive Request From Client----:" + req);
		
		BaeMemcachedClient cache = new BaeMemcachedClient();
		Object obj = cache.get(Constants.CACHE_USER_INFO);
		if (obj == null)
		{
			logger.log(Level.INFO,  "need New:"+ obj);
			usrInfo = new HashMap<String, UsrAccessInfo>();
			cache.add(Constants.CACHE_USER_INFO, usrInfo);
		}
		else
		{
			//可以直接取走
			logger.log(Level.INFO,  "no need New:"+ obj);
			usrInfo = (Map<String, UsrAccessInfo>)obj;
		}
		showCache("--1--");
		commonProcess(req, resp);
	}
	
	private void showCache(String ss)
	{
		Set<String> set = usrInfo.keySet();
		logger.log(Level.INFO,  ss+ "---set---:"+ set.size());
		Iterator<String> it = set.iterator();
		while (it.hasNext())
		{
			String usrKey = it.next();
			UsrAccessInfo uInfo = usrInfo.get(usrKey);
			Map<String, Integer> wordList = uInfo.getWordMap();
			
			Set<String> setItem = wordList.keySet();
			Iterator<String> itItem = setItem.iterator();
			String buf = "";
			while (itItem.hasNext())
			{
			
				String wKey = itItem.next();
			    int index = wordList.get(wKey);
			    buf += "-" + wKey + "-" + index;
			}
			logger.log(Level.INFO,  "usr---:"+ buf);
		}
	}
	
	
	/**
	 * 通常处理的方式
	 * @param req
	 * @return
	 */
	private void commonProcess(HttpServletRequest req, HttpServletResponse resp)
	{
		try {
			InputStream is = req.getInputStream();
			InputSource isSource = new InputSource(is);
			Document document = null;
			Element root = null;
			
			DocumentBuilder docBuilder = null;
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilder = docBuilderFactory.newDocumentBuilder();
				document = docBuilder.parse(isSource);
				root = document.getDocumentElement();
				if(root != null)
				{
					String commonContent = Constants.REPLY_HELP;
					XMLParser4WC parser = new XMLParser4WC(root);
					List<MSGItem> revList = parser.getListMsg();
					String fromUsrId = WeiXinTool.findKeyValueFromList(revList, Constants.XML_WC_FromUserName);
					logger.log(Level.INFO, "--fromUsrId----:" + fromUsrId);
					UsrAccessInfo usrInfoTemp = usrInfo.get(fromUsrId);
					if (usrInfoTemp == null)
					{
						logger.log(Level.INFO, "--acData == null----:" + fromUsrId);
						HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
						usrInfoTemp = new UsrAccessInfo(new Date(), wordMap);
						usrInfo.put(fromUsrId, usrInfoTemp);
						logger.log(Level.INFO, "--acData find----:" +  usrInfo.get(fromUsrId));
						store();

					}
					else
					{
						//清空过往数据
						usrInfoTemp.updateDate(new Date());
					}
					usrWordStore = usrInfoTemp;
					String toUsrId = WeiXinTool.findKeyValueFromList(revList, Constants.XML_WC_ToUserName);
					String type = WeiXinTool.findKeyValueFromList(revList, Constants.XML_WC_MsgType);
					logger.log(Level.INFO, "--types----:" + type);
					if (type.equals(Constants.MSG_TYPE_TEXT))
					{
						//通用的方式处理
						String content = WeiXinTool.findKeyValueFromList(revList, Constants.XML_WC_Content);
						content = content.trim();
						commonContent = getAnswerContent(content, req);
					}
					logger.log(Level.INFO, "--revData----:" + WeiXinTool.listMsg2String(revList));
					
					XMLParser4Conf conf = new XMLParser4Conf();
					conf.processXML(req);
					List<MSGItem> sendList = conf.getConfigXMLData();
					MSGItem msItemT = WeiXinTool.findItemFromList(sendList, Constants.XML_WC_ToUserName);
					MSGItem msItemF = WeiXinTool.findItemFromList(sendList, Constants.XML_WC_FromUserName);
					MSGItem msItemC = WeiXinTool.findItemFromList(sendList, Constants.XML_WC_Content);
					msItemT.setValue(fromUsrId);
					msItemF.setValue(toUsrId);
					msItemC.setValue(commonContent);
					String sendData = WeiXinTool.listMsg2String(sendList);
					resp.getWriter().write(sendData);
					return;
				}
				logger.log(Level.INFO, "--root is null----:");
				//
			} catch (Exception e) {
				logger.log(Level.INFO, "--XXXXXXXXXXeption----:" + e.toString());
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getAnswerContent(String content, HttpServletRequest req)
	{
		//如果是英文
		content = content.trim().toLowerCase();
		if (isWords(content))
		{
			List<Usage> uList = getUsages4word(content, req);
			return showReply4Word(content, uList);
		}
		
		if (content.equals(Constants.CMMD_RANDOM))
		{
			String word = getRandomWord(req);
			List<Usage> uList = getUsages4word(word, req);
			return showReply4Word(word, uList);
		}
		
		if (content.equals(Constants.CMMD_HELP))
		{
			return Constants.REPLY_HELP;
		}
		
		if (content.equals(Constants.CMMD_AD))
		{
			return Constants.REPLY_AD;
		}
		if (content.equals(Constants.CMMD_MEMBER))
		{
			return Constants.REPLY_MEMBER;
		}
		return Constants.REPLY_HELP;
	}
	
	private String showReply4Word(String word, List<Usage> uList)
	{
		if (uList == null || uList.size() == 0)
		{
			return Constants.REPLY_NO_WORDS;
		}
		int len = uList.size();
		Integer indexInteger = usrWordStore.getIndex4word(word); //如果为空, 则需要将其置为0
		logger.log(Level.INFO, "--indexInteger----:" + indexInteger);
		int index = -1;
		if (indexInteger == null)
		{
			index = 0;
		}
		else
		{
			index = indexInteger;
		}
		if (index >= len)
		{
			index = 0;
		}
		String res = "【坐等光阴逝去, 不如背诵美文一句】\n\n\n";
		Usage use = uList.get(index);
		logger.log(Level.INFO, "--usages----:" + use.getEngSent());
		usrWordStore.putWordWithIndex(word, index + 1); //再次调用的时候, 会出现下一个例句
		store();
		return res + use.getEngSent() + "\n\n" + use.getChsSent() + "\n\n\n--【凡尘工作室】整理出品";
	}
	
	//caceh每做一次更新, 需要进行一次保存
	private void store()
	{
		BaeMemcachedClient cache = new BaeMemcachedClient();
		cache.set(Constants.CACHE_USER_INFO, usrInfo);
	}
	
	
	private boolean isWords(String email)
	{
		Pattern p = Pattern.compile("^[a-zA-Z]{2,}"); 
		Matcher m = p.matcher(email); 
		return m.matches(); 
	}
	
	private String getRandomWord(HttpServletRequest req)
	{
		String realPath = req.getServletContext().getRealPath(Constants.CONF_EXM_PATH);
		File dir = new File(realPath);
		logger.log(Level.INFO, "--dir----:" + dir.isDirectory());
		File[] a_zDirs = dir.listFiles();
		if (a_zDirs == null || a_zDirs.length == 0)
		{
			logger.log(Level.INFO, "--a_zDirs----:" + a_zDirs.length);
			return null;
		}
		int charIndex = getRandomIdx(a_zDirs.length);
		File selDir = a_zDirs[charIndex];
		File[] wWords = selDir.listFiles();
		if (wWords == null || wWords.length == 0)
		{
			logger.log(Level.INFO, "--wWords----:" + wWords.length);
			return null;
		}
		int wordIndex = getRandomIdx(wWords.length);
		File selFile = wWords[wordIndex];
		String fileName = selFile.getName();
		String word = fileName.replace(Constants.POST_FILE_NAME, "");
		return word;
	}
	
	/**
	 * 得到某个文件的全部例句
	 * @param selFile
	 * @return
	 */
	private List<Usage> getUsages4File(File selFile)
	{
		if (!selFile.exists())
		{
			return null;
		}
		logger.log(Level.INFO, "--YYYYeption----:" + selFile.getName());
		//终于可以进行转换
		try {
			FileInputStream fis = new FileInputStream(selFile);
			InputSource isSource = new InputSource(fis);
			Document document = null;
			DocumentBuilder docBuilder = null;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(isSource);
			Element root = document.getDocumentElement();
			if(root != null)
			{
				XMLLocalUsageParser parser = new XMLLocalUsageParser(document.getDocumentElement());
				List<Usage> uList = parser.getUsageList();
				return uList;
			}
			return null;
		} catch (Exception e) 
		{
			logger.log(Level.INFO, "--zzzzeption----:" + e.toString());
		}
		return null;
	}
	
	/**
	 * 得到某个单词的全部例句
	 * @param word
	 * @param req
	 * @return
	 */
	private List<Usage> getUsages4word(String word, HttpServletRequest req)
	{
		String innerPath = word.charAt(0) + Constants.FILE_SEP + word + Constants.POST_FILE_NAME;
		String realPath = req.getServletContext().getRealPath(Constants.CONF_EXM_PATH + innerPath);
		File wordXMLFile = new File(realPath);
		logger.log(Level.INFO, "--getUsage4word -- dir----:" + wordXMLFile.getAbsolutePath());
		//得到某个文件的全部例句
		List<Usage> listU = getUsages4File(wordXMLFile);
		return listU;
	}
	
	private int getRandomIdx(int size)
	{
		if (size == 1)
		{
			return 0;
		}
		else
		{
			double rate = Math.random();
			return (int)((size - 1) * rate);
		}
	}
	
	

}
