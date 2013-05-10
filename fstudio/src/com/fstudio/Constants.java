package com.fstudio;

public interface Constants {
	
	
	
	String CONF_XML_PATH = "config/send_msg_txt.xml";
	String CONF_EXM_PATH = "static/example/"; //单词路径
	
	public String ECHO_STR = "echostr";
	String ENCODE_SERVER = "UTF-8";
	
	//---微信的XML定义
	String XML_WC_ToUserName = "ToUserName";
	String XML_WC_FromUserName = "FromUserName";
	String XML_WC_CreateTime = "CreateTime";
	String XML_WC_MsgType = "MsgType";
	String XML_WC_Content = "Content";
	String XML_WC_MsgId = "MsgId";
	String XML_WC_FuncFlag = "FuncFlag";
	
	//消息类型, 文本消息
	String MSG_TYPE_TEXT = "text";
	//消息指令:
	String CMMD_RANDOM = "0"; //随机推荐
	String CMMD_HELP = "1"; //帮助
	String CMMD_AD = "2"; //广告
	String CMMD_MEMBER = "3"; //工作室成员
	String CACHE_USER_INFO = "cache_user_info"; //用户以及历史上访问的消息
	
	String POST_FILE_NAME = ".xml";
	
	String FILE_SEP = "/";
	
	//key属性
	String XML_CONF_ATT_KEY = "key";
	String XML_CONF_ATT_TYPE = "type";
	String XML_CONF_ATT_VALUE = "value";	
	
	String STR_HELP = "\n\n\n回复0——优美英文随机推荐 \n回复1——系统帮助. \n回复2——工作室产品 \n回复3——成员简介\n回复单词——(如have)单词的优美例句";
	//
	String REPLY_HELP = "欢迎做客凡尘工作室!" + STR_HELP;
	String REPLY_AD = "欢迎做客凡尘工作室!  \n\n更多酷炫英语, 请下载《凡尘英语》\nhttp://inform.duapp.com/static/app_dictionary/Dictionary1.1.8_31.apk "  + STR_HELP;
	String REPLY_MEMBER = "欢迎做客凡尘工作室!  \n\nDemetri Tian:\n新浪微博:@创业者凡尘 \nQQ:26762685\n\nEddy Wang:\n新浪微博:@EddyDeserve" + STR_HELP;
	String REPLY_NO_WORDS = " 不存在例句.  " + STR_HELP;
}
