package com.augurit.agcloud.agcom.agsupport.sc.address.controller;

import com.augurit.agcloud.agcom.agsupport.sc.address.service.IAgAddress;
import com.coordtrans.service.impl.CoordTransAppImpl;
import com.coordtrans.service.impl.CoordTransBaseImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-07-06.
 */
@RestController
@RequestMapping("/agsupport/address")
public class AgAddressController {

    private static Logger logger = LoggerFactory.getLogger(AgAddressController.class);

    @Autowired
    private IAgAddress iAgAddress;

    private int ProjNo = 38;
    private com.coordtrans.service.ICoordTransApp ICoordTransApp = new CoordTransAppImpl();
    private com.coordtrans.service.ICoordTransBase ICoordTransBase = new CoordTransBaseImpl();

    @RequestMapping("/index.do")
    public ModelAndView index() {
        return new ModelAndView("/agcom/address/addressIndex");
    }

    /**
     * 匹配地址
     * @param address
     * @return
     */
    @RequestMapping("/matchAddress")
    public List<Map> matchAddress(String address)  throws Exception{
        List<Map> dmdzList = iAgAddress.matchAddress(address);
        return dmdzList;
    }
    /**
     * 批量匹配地址
     * @param params 需要匹配地址集合
     * @return
     */
    @RequestMapping("/matchAddressList")
    public Map matchAddressList(String params) throws Exception{
        JSONArray jsonArray = JSONArray.fromObject(params);
        List<String> addressList = JSONArray.toList(jsonArray,String.class);
        Map rsMap = iAgAddress.matchAddressList(addressList);
        return rsMap;
    }
    /**
     * 匹配地址
     * @return
     */
    @RequestMapping("/matchAddressMap")
    public List<Map> matchAddressMap(String params) throws Exception{
        JSONObject jsonO = JSONObject.fromObject(params);
        Map addressMap = jsonO.getJSONObject("addressMap");//地址对象
        String addressCol = jsonO.getString("addressCol");//地址列名
        List<Map> dmdzList = iAgAddress.matchAddressMap(addressMap,addressCol);
        return dmdzList;
    }
    /**
     * 批量匹配地址
     * @return
     */
    @RequestMapping("/matchAddressMapList")
        public Map<String,List<Map>> matchAddressMapList(String params) throws Exception{
            JSONObject jsonO = JSONObject.fromObject(params);
            List<Map> addressMapArr=jsonO.getJSONArray("addressMapArr");//地址对象集合
            String addressCol = jsonO.getString("addressCol");//地址列名
            Map dmdzList = iAgAddress.matchAddressMapList(addressMapArr,addressCol);
            return dmdzList;
    }
    /**
     * 通过文件进行匹配地址
     * @return
     */
    @RequestMapping("/matchAddressByFile")
    public Map<String,List<Map>> matchAddressByFile(String params) throws Exception{
        JSONObject jsonO = JSONObject.fromObject(params);
        List<Map> addressMapArr=jsonO.getJSONArray("addressMapArr");//地址对象集合
        String addressCol = jsonO.getString("addressCol");//地址列名
        Map dmdzList = iAgAddress.matchAddressMapList(addressMapArr,addressCol);
        return dmdzList;
    }

    /**
     * 检测百度网址是否可连接
     * @return
     */
    @RequestMapping("/testBdConn")
    public boolean testBdConn()  {
        System.setProperty("sun.net.client.defaultConnectTimeout", "1000");
        System.setProperty("sun.net.client.defaultReadTimeout", "2000");
        java.io.BufferedReader in;
        String s = null;
        try {
            String getUrl = "http://api.map.baidu.com/?qt=s&c=131&wd=1&rn=10&ie=utf-8&oue=1&fromproduct=jsapi&res=api&callback=BMap._rd._cbk14427";
            URL url = new URL(getUrl);
            URLConnection con = url.openConnection();
            in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
            while ((s = in.readLine()) != null) {
                if (s.length() > 0) {
			        return true;
                }
            }
            in.close();
        } catch (Exception ex) {
            System.out.println("百度拾取系统没被开启！");
            return false;
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
