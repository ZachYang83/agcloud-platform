package com.augurit.agcloud.agcom.agsupport.common.util;

import com.augurit.agcloud.agcom.agsupport.constants.WsConstants;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ElementUtil {
	
	/**
	 * @param items
	 * @param obj
	 * @return
	 * @description TODO(自动组装XML) 
	 * @version 1.0
	 * @author 吴灿
	 * @update 2012-11-05
	 */
    public static Element elementEncap(Element items,Object obj) {
        try {
            // 取得obj类的所有定义的成员变量,注意DeclaredFields不包括继承下来的成员变量
            Field[] fields = obj.getClass().getDeclaredFields();
            // 取得每个成员变量的值
            for (Field field : fields) {
                // 在member节点下添加属性节点
            	Element fieldElm = items.addElement("element");
            	fieldElm.addAttribute("name", field.getName());
            	fieldElm.addAttribute("type", field.getType().toString());
            	// 必须设置为true才可以取得成员变量的值,否则field.get(obj)
                // 一句要抛出java.lang.IllegalAccessException异常
            	field.setAccessible(true);
            	fieldElm.addAttribute("value",clazzToDbTypeString(field.getType(),field.get(obj)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return items;
    }
    
	/**
	 * @param doc
	 * @param name
	 * @param describe
	 * @return
	 * @description TODO(自动组装元数据XML头部) 
	 * @version 1.0
	 * @author 吴灿
	 * @update 2012-11-05
	 */
    public static Element getMetaDataHead(Document doc,String name,String describe) {
    	Element service = doc.addElement("Definition");
		service.addAttribute("name",name);
		service.addAttribute("describe",describe);
		service.addAttribute("targetNamespace", WsConstants.NS);
		Element methods = service.addElement("sequence");
		return methods;
    }
    
    /**
	 * @param methods
	 * @param name
	 * @param describe
	 * @param rtn
	 * @param request
	 * @param serviceName
	 * @return
	 * @description TODO(自动组装元数据XML 方法) 
	 * @version 1.0
	 * @author 吴灿
	 * @update 2012-11-05
	 */
    public static Element getMetaDataFunction(Element methods, String name, String describe, String rtn, HttpServletRequest request, String serviceName) {
    	Element function = methods.addElement("function");
    	function.addAttribute("name", name);
    	function.addAttribute("describe", describe);
    	function.addAttribute("method", "get/post");
    	function.addAttribute("return", rtn);
    	function.addAttribute("address ", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/rest/"+serviceName+"/"+name);
		return function;
    }
    
    /**
	 * @param function
	 * @param name
	 * @param type
	 * @param describe
	 * @return
	 * @description TODO(自动组装元数据XML 方法参数) 
	 * @version 1.0
	 * @author 吴灿
	 * @update 2012-11-05
	 */
    public static Element getMetaDataParam(Element function,String name,String type,String describe) {
    	Element param = function.addElement("parameter");
    	param.addAttribute("name", name);
    	param.addAttribute("type", type);
    	param.addAttribute("describe", describe);
		return param;
    }
    
	/**
	 * @param obj
	 * @param serverName
	 * @param funtion
	 * @param describe
	 * @return
	 * @description TODO(自动组装rest方法XML头部) 
	 * @version 1.0
	 * @author 吴灿
	 * @throws DocumentException 
	 * @update 2012-11-05
	 */
    public static Document getRestFunctionXML(Object obj,String serverName,String funtion,String describe) throws DocumentException {
    	Document doc = DocumentHelper.createDocument();
    	Element service = doc.addElement("Definition");
		service.addAttribute("serverName",serverName);
		service.addAttribute("funtion",funtion);
		/*try {
			describe = new String(describe.getBytes("utf-8"),"ISO8859_1");//乱码时放开注释
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		service.addAttribute("describe",describe);
		service.addAttribute("targetNamespace","www.agos.com");
		XStream xstream = new XStream();
		Element childElement = DocumentHelper.parseText(xstream.toXML(obj)).getRootElement();
		doc.getRootElement().add(childElement);
		return doc;
    }
    
    /**
     * @param clazz
     * @param o
     * @return
     * @description TODO(field类型判断 及转换) 
     * @version 1.0
     * @author 吴灿
     * @update 2012-11-05
     */
    private static String clazzToDbTypeString(Class<?> clazz,Object o) {   
    	String str = "";
    	if(null==o){
    		return str;
    	}
        if(clazz == String.class || clazz == Character.class ||
        	clazz == char.class || clazz == Integer.class || 
        	clazz == int.class || clazz == Long.class || clazz == long.class || 
        	clazz == Short.class || clazz == short.class || clazz == BigDecimal.class|| clazz == Double.class) {   
        	str =  String.valueOf(o);   
        }
        return str;
    }  
    
    /**
     * @param clazz
     * @return
     * @description TODO(field类型判断 及转换) 
     * @version 1.0
     * @author 吴灿
     * @update 2012-11-05
     */
    private static String clazzToTypeString(Class<?> clazz) {   
    	String str = "";
    	if (clazz == String.class) {
			return "String";
		}else if(clazz == Long.class) {
			return "Long";
		}else if(clazz == long.class) {
			return "long";
		}else if(clazz == Integer.class) {
			return "Integer";
		}else if(clazz == int.class) {
			return "int";
		}else if(clazz == Short.class) {
			return "Short";
		}else if(clazz == short.class) {
			return "short";
		}else if(clazz == BigDecimal.class) {
			return "BigDecimal";
		}else if(clazz == Double.class) {
			return "Double";
		}else if(clazz == Character.class) {
			return "Character";
		}
        return str;
    }
}
