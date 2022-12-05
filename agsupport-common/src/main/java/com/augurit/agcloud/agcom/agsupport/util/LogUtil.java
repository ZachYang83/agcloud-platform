package com.augurit.agcloud.agcom.agsupport.util;

import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import eu.bitwalker.useragentutils.UserAgent;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class LogUtil {
	
    /** 
     * 计算一个对象所占字节数 
     * @param o 对象，该对象必须继承Serializable接口即可序列化
     * @return 
     * @throws IOException 
     */  
	 public static int size(final Object o) {  
		  
		int size = 0;
		if (o == null) {  
			 return size;  
		} 
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream(4096);  
			ObjectOutputStream out = new ObjectOutputStream(buf);  
			out.writeObject(o);  
			out.flush();  
			size = buf.size();  
			buf.close();  
			
		} catch (Exception e) {
			return size; 
		}
		
		return size;
	 }  
    /** 
     * 赋值对象，返回对象的引用，如果参数o为符合对象，则内部每一个对象必须可序列化 
     * @param o 对象，该对象必须继承Serializable接口即可序列化
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */  
	 public static Object copy(final Object o) throws IOException,  
		  ClassNotFoundException {  
		  if (o == null) {  
			  return null;  
		  }  
		  
		  ByteArrayOutputStream outbuf = new ByteArrayOutputStream(4096);  
		  ObjectOutput out = new ObjectOutputStream(outbuf);  
		  out.writeObject(o);  
		  out.flush();  
		  outbuf.close();  
		  
		  ByteArrayInputStream inbuf = new ByteArrayInputStream(outbuf.toByteArray());  
		  ObjectInput in = new ObjectInputStream(inbuf);  
		  return in.readObject();  
	 }

	/**
	 * 创建成功的日志基本信息 2017-12-07
	 *
	 * @param request
	 * @param funcName 操作的功能名称
	 * @return
	 */
	public static JSONObject createSuccessLogInfo(HttpServletRequest request, String funcName){
		return createLogInfo(request,funcName,true,null);
	}

	/**
	 * 创建失败日志的基本信息 2017-12-07
	 *
	 * @param request
	 * @param funcName 操作的功能名称
	 * @param errorInfo 失败信息
	 * @return
	 */
	public static JSONObject createFailLogInfo(HttpServletRequest request, String funcName,String errorInfo){
		return createLogInfo(request,funcName,false,errorInfo);
	}

	/**
	 * 创建日志基本信息 2017-12-07
	 *
	 * @param request
	 * @param funcName
	 * @param operResult 是否操作成功
	 * @param exceptionMessage 操作信息
	 * @return
	 */
	private static JSONObject createLogInfo(HttpServletRequest request, String funcName,boolean operResult,String exceptionMessage) {
		JSONObject jsonObject = new JSONObject();
		//String loginName = SecurityContext.getCurrentUser().getLoginName();
		String loginName = LoginHelpClient.getLoginName(request);
		if ("anonymousUser".equals(loginName)){
			loginName = request.getParameter("loginName");
		}
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		jsonObject.put("loginName", loginName);
		jsonObject.put("sysName", "agsupport");
		jsonObject.put("ipAddress", Common.getIpAddr(request));
		jsonObject.put("browser", userAgent.getBrowser().getName());
		jsonObject.put("funcName", funcName);
		jsonObject.put("operResult", operResult?"成功":"失败");
		if (exceptionMessage != null) {
			jsonObject.put("exceptionMessage", exceptionMessage.substring(0, exceptionMessage.length() > 500 ? 500 : exceptionMessage.length()));
		}
		return jsonObject;
	}
	
}
