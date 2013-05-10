package com.fstudio.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.bae.api.memcache.BaeMemcachedClient;
import com.fstudio.Constants;
import com.fstudio.ServletTool;

/**
 * http://fstudio.duapp.com/clean?name=13786191624&pwd=ostrichmyself
 * 清除系统Cache内存, 需要验证身份
 * 一般系统进行重新发布之后, 需要做这一步
 * @author tiant5
 *
 */
public class CleanServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		logger = Logger.getLogger(this.getServletName());
		BaeMemcachedClient cache = new BaeMemcachedClient();
		boolean isUsrOK = ServletTool.checkConnection(req);
		if (!isUsrOK)
		{
			logger.log(Level.INFO, "please input correct pwd and name!");
			logger.log(Level.INFO, "no chance to delete!");
			resp.getWriter().println("no chance to delete!");
			return;
		}
		else
		{
			boolean delResult = cache.delete(Constants.CACHE_USER_INFO);
			logger.log(Level.INFO, "delete cache key" + Constants.CACHE_USER_INFO + " result:" + delResult);
			resp.getWriter().println("delete cache key" + Constants.CACHE_USER_INFO + " result:" + delResult);
			return;
		}
	
	}
	

}
