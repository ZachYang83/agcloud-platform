package com.augurit.agcloud.agcom.agsupport.common.util;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.common.util.Common;
import com.common.util.ConfigProperties;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyUtil {

	public static String getProxyPreUrl(HttpServletRequest request){
		String preUrl = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath() + "/";
		return preUrl;
	}
	public static String getProxyPreUrl(){
		String preUrl = ConfigProperties.getByKey("agproxy.url") +  "/";
		return preUrl;
	}
	public static String getProxyPreUrl(String key){
		String preUrl = ConfigProperties.getByKey(key) +  "/";
		return preUrl;
	}
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			//序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
		 
		}
		return null;
	}
		 
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			//反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		 
		}
		return null;
	}
	public static <T> T getObjectFromJson(String json , Class<T> c){
		JSONObject obj = new JSONObject().fromObject(json);//将json字符串转换为json对象
		T jb = (T) JSONObject.toBean(obj,c);//将json对象转换为Person对象
		return jb;
	}

	public static String getJsonFromObject(Object obj){
		JSONObject json = JSONObject.fromObject(obj);//将java对象转换为json对象
		String str = json.toString();//将json对象转换为字符串
		return str;
	}

	public static String getMongodbTime(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = df.format(date);
		return format;
	}

	public static void main(String[] args) {
//		AgServiceLog agServiceLog = new AgServiceLog();
//		agServiceLog.setId("000000");
//		String jsonFromObject = getJsonFromObject(agServiceLog);
//		System.out.println(jsonFromObject);
//		AgServiceLog objectFromJson = getObjectFromJson(jsonFromObject, AgServiceLog.class);
//		System.out.println(objectFromJson.getId());
//		String uri = "agproxy/ogc/mapservice/tdlyxz/wcs";
//		String split = uri.replaceAll("^.*/([^/]+)/(ogc)/([^/]+)/([^/]+)/([^/]+)(/.*)?$", "$1/$2/$3/$4/$5");
//		System.out.println(split);

//		String s = "dfiafao";
//		System.out.println(s.contains(""));
//		System.out.println(s.contains(null));//报错

	}

	public static boolean checkLayerProxyUrlAndUrl(AgLayer agLayer){
		boolean result = true;
		try {
			if (!Common.isCheckNull(agLayer) && !Common.isCheckNull(agLayer.getProxyUrl())) {
				String url = agLayer.getUrl();
				Pattern pattern = Pattern.compile("/");
				Matcher findMatcher = pattern.matcher(url);
				int number = 0;
				while (findMatcher.find()) {
					number++;
					if (number == 3) {//当“/”第3次出现时停止
						break;
					}
				}
				int i = findMatcher.start();// “/” 第3次出现的位置
				result = true;
			}
		}
		catch (Exception e){
			result =false;
		}
		return  result;
	}
}
