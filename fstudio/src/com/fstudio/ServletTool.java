package com.fstudio;

import javax.servlet.http.HttpServletRequest;

public class ServletTool {
	
	/**
	 * 所有的连接必须满足name和pwd进行验证, 否则认为无效
	 * @param req
	 * @return
	 */
	public static boolean checkConnection(HttpServletRequest req)
	{
		String name = req.getParameter("name");
		String pwd = req.getParameter("pwd");
		if (name == null || pwd == null)
		{
			return false;
		}
		if (name.equals("13786191624") && pwd.equals("ostrichmyself"))
		{
			return true;
		}
		return false;
	}

}
