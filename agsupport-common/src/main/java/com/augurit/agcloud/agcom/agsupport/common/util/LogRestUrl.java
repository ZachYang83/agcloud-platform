package com.augurit.agcloud.agcom.agsupport.common.util;

import com.common.util.Common;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

@Component
public class LogRestUrl implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		String rest = request.getRequestURI();
		if (rest.endsWith("/error")) {
			return true;
		}
		String actionName = ((HandlerMethod)handler).getBean().getClass().getName().replaceAll(".*\\.([^\\.]*)$", "$1");
		String method = ((HandlerMethod)handler).getMethod().getName();

		StringBuffer sb = new StringBuffer(Common.date2String(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "  -->");
		sb.append(rest);
		sb.append("  -->" + actionName + "." + method + "()");
		String params = getValue(request);
		if(!Common.isCheckNull(params)){
			sb.append("  -->");
			sb.append(params);
		}
		System.out.println(sb);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
	}

	//执行Controller方法完成后
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	}

	/**
	 * 取得参数值
	 */
	private   String getValue(HttpServletRequest request){
		String value = "";
		Enumeration enu = request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName = (String)enu.nextElement();
			if (!"_dc".equals(paraName)
					&& !"node".equals(paraName)
					&& !"_".equals(paraName)){//_dc的参数不要
				String [] arr = request.getParameterValues(paraName);
				if (arr != null && arr.length > 1){
					value += paraName+"="+ConvertObjectArrToStr(arr) + "&";
				}else{
					value += paraName+"="+request.getParameter(paraName) + "&";
				}
			}
		}
		value = value.replaceAll("&$",  "");
		return value;
	}

	/**
	 * 把对象列表,转化成逗号分隔的字符串
	 */
	private  String ConvertObjectArrToStr(Object [] arr) {
		String result = "";
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				if (!"".equals(String.valueOf(arr[i]))) {
					result += String.valueOf(arr[i]) + ",";
				}
			}
			if (!"".equals(result)) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

}
