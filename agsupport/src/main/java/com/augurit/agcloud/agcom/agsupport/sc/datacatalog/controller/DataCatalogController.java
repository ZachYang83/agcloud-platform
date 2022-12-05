package com.augurit.agcloud.agcom.agsupport.sc.datacatalog.controller;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.dao.DataCatalogDao;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.service.DataCatalogService;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.util.TreeNode;
import com.augurit.agcloud.agcom.agsupport.sc.func.service.IAgFunc;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/11/1.
 */

@RestController
@RequestMapping("/agsupport/datacatalog")
public class DataCatalogController {
    private static Logger logger = LoggerFactory.getLogger(DataCatalogController.class);

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private IAgFunc iAgFunc;

    @Autowired
    private DataCatalogDao dataCatalogDao;
    @Autowired
    private DataCatalogService dataCatalogService;

    @RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        String loginName = LoginHelpClient.getLoginName(request);
        AgUser agUser = this.iAgUser.findUserByName(loginName);
        boolean imp = true;
      //  List<Map> listMap = dataCatalogDao.listDataByPage(null);
        model.addAttribute("imp", Boolean.valueOf(imp));
        return new ModelAndView("agcom/datacatalog/catalogIndex");
    }

    @RequestMapping({"/tree/{id}"})
    public String tree(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        if ("all".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("onlyDB".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataOnlyDBTableTree();
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("WRP_".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataDBTableTreeByName(id);
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("dataEditAll".equals(id)) { // 数据编辑

            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);

            String roleName = "";
            String roleId = "";

            if (list != null && list.size() > 0) { // 找出超級用戶
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else { //不是超級用戶 就 根據 角色 來 控制
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // 判断用户配置
                List<Map> tables = dataCatalogService.getTableByEditRole(roleId); //對接agcloud用戶
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // 判断是否保留
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                flag = true;
                                isNull = false;
                                break;
                            }
                        }
                        // 执行去除
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // 执行去除
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }
                // 返回结果
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataEditAllExist".equals(id)) { // 数据编辑

            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else {
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // 判断用户配置的表                
                List<Map> tables = dataCatalogService.getTableByEditRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // 判断是否保留
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {

                                // 判断是否有记录
                                List li1 = new ArrayList();
                                li1.add(table);
                                List<Map> lis = dataCatalogService.getRecords(li1);
                                if (lis != null && lis.size() > 0) {
                                    isNull = false;
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        // 执行去除
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // 执行去除
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }
                // 返回结果
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataCheckAllExist".equals(id)) { // 数据审核
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else {
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // 判断用户配置的表
                List<Map> tables = dataCatalogService.getTableByCheckRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // 判断是否保留
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {

                                // 判断是否有记录
                                List li1 = new ArrayList();
                                li1.add(table);
                                List<Map> lis = dataCatalogService.getCheckRecord(li1);
                                if (lis != null && lis.size() > 0) {
                                    isNull = false;
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        // 执行去除
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // 执行去除
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // 返回结果
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataCheckAll".equals(id)) { // 数据审核
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else {
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // 判断用户配置的表
                List<Map> tables = dataCatalogService.getTableByCheckRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // 判断是否保留
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                isNull = false;
                                flag = true;
                                break;
                            }
                        }
                        // 执行去除
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // 执行去除
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // 返回结果
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataAll".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("数据库");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else {
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // 判断用户配置的表
                List<Map> tables = dataCatalogService.getTableByUser(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // 判断是否保留
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                flag = true;
                                isNull = false;
                                break;
                            }
                        }
                        // 执行去除
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // 执行去除
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // 返回结果  
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        }
        return JsonUtils.toJson(trees);
    }


    /**
     * 查询表单数据
     *
     * @param tablename
     * @param page
     * @param rows
     * @param key       查询key
     * @return
     * @throws Exception
     */
    @RequestMapping("/listfromtable/{tablename}")
    public String listFromTable(@PathVariable("tablename") String tablename, int page, int rows, String key, String approveType, HttpServletRequest request) throws Exception {
        String retStr = "";
        // 已审核
        if (approveType == null || (approveType != null && "Y".equals(approveType))) {
            // 查询表字段
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> data = dataCatalogService.getFieldByTable(lii);

            // 拼装查询参数
            String con = "";
            if (key != null && !"".equals(key)) {
                String[] ke = key.split("&");
                if (ke != null && ke.length > 0) {
                    for (String k : ke) {
                        String[] kk = k.split("=");
                        String name = kk[0];
                        if (kk != null && kk.length > 1) {
                            String value = kk[1];
                            // 判断是否日期
                            boolean isDate = false;
                            boolean isCom = false;
                            for (int i = 0; i < data.size(); i++) {
                                if ((name.equals("start_" + data.get(i).get("name").toString()) || name.equals("end_" + data.get(i).get("name").toString())) && "DATE".equals(data.get(i).get("type").toString().toUpperCase())) {
                                    isDate = true;
                                    break;
                                }
                                if (name.equals(data.get(i).get("name")) && data.get(i).get("dd") != null && !"".equals(data.get(i).get("dd"))) {
                                    isCom = true;
                                    break;
                                }
                            }
                            if (isDate) {   // 日期类型
                                if (name.startsWith("start_")) {
                                    if ("".equals(con)) {
                                        con += " " + name.replaceAll("start_", "") + ">=to_date('" + value.replaceAll("\\+", " ") + "','yyyy-MM-dd HH24:mi:ss') ";
                                    } else {
                                        con += " and " + name.replaceAll("start_", "") + ">=to_date('" + value.replaceAll("\\+", " ") + "','yyyy-MM-dd HH24:mi:ss') ";
                                    }
                                }
                                if (name.startsWith("end_")) {
                                    if ("".equals(con)) {
                                        con += " " + name.replaceAll("end_", "") + "<=to_date('" + value.replaceAll("\\+", " ") + "','yyyy-MM-dd HH24:mi:ss') ";
                                    } else {
                                        con += " and " + name.replaceAll("end_", "") + "<=to_date('" + value.replaceAll("\\+", " ") + "','yyyy-MM-dd HH24:mi:ss') ";
                                    }
                                }
                            } else if (isCom) {
                                if ("".equals(con)) {
                                    con += " " + name + "='" + value + "' ";
                                } else {
                                    con += " and  " + name + "='" + value + "' ";
                                }
                            } else {
                                if ("".equals(con)) {
                                    con += " " + name + " like '%" + value + "%' ";
                                } else {
                                    con += " and  " + name + " like '%" + value + "%' ";
                                }
                            }
                        }
                    }
                }
            }

            // 判断用户角色
            String loginName = LoginHelpClient.getLoginName(request);
            List li1 = new ArrayList();
            li1.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(li1);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("超级管理员".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else {
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            // 返回结果
            if ("超级管理员".equals(roleName)) { //對接agcloud用戶-->"超級管理員".equals(roleName)
                // 查询长条件
                if (!"".equals(con)) {
                    con = " ( " + con + " ) ";
                } else {
                    con = " ( 1=1 ) ";
                }
                retStr = dataCatalogService.listFromTable(tablename, page, rows, con);
            } else {
                // 地区Map
                Map<String, Object> distMap = new HashMap<String, Object>();
                distMap.put("440103", "荔湾区");
                distMap.put("440104", "越秀区");
                distMap.put("440105", "海珠区");
                distMap.put("440106", "天河区");
                distMap.put("440111", "白云区");
                distMap.put("440112", "黄埔区");
                distMap.put("440113", "番禺区");
                distMap.put("440114", "花都区");
                distMap.put("440115", "南沙区");
                distMap.put("440183", "增城区");
                distMap.put("440184", "从化区");

                // 查询表id
                String tableId = "";
                String distfield = "";
                List li2 = new ArrayList();
                li2.add(tablename);
                List<Map> da = dataCatalogService.getTableInfoByName(li2);
                if (da != null && da.size() > 0) {
                    tableId = da.get(0).get("id").toString();
                    if (da.get(0).get("distfield") != null && !"".equals(da.get(0).get("distfield"))) {
                        distfield = da.get(0).get("distfield").toString();
                    }
                }

                // 查询 角色 能 查询看 的地区
                List li3 = new ArrayList();
                li3.add(tableId);
                List<Map> dists = dataCatalogService.getDisById(roleId, li3);
                String dis = "";
                if (dists != null) {
                    for (int i = 0; i < dists.size(); i++) {
                        // 地区字段为空时，跳出
                        if (distfield == null || "".equals(distfield)) {
                            break;
                        }
                        if (dists.get(i).get("distid") != null && "All".equals(dists.get(i).get("distid"))) {
                            if ("".equals(dis)) {
                                dis = " 1=1 ";
                            } else {
                                dis = " or 1=1 ";
                            }
                        } else {
                            if ("".equals(dis)) {
                                dis = distfield + " like " + "'%" + dists.get(i).get("distid") + "%' or " + distfield + " like  " + "'%" + distMap.get(dists.get(i).get("distid")) + "%'";
                            } else {
                                dis = dis + " or " + distfield + " like " + "'%" + dists.get(i).get("distid") + "%' or " + distfield + " like  " + "'%" + distMap.get(dists.get(i).get("distid")) + "%'";
                            }
                        }
                    }
                }
                // 拼装地区条件

                // 地区条件
                if (!"".equals(dis)) {
                    dis = "( " + dis + " )";
                } else {
                    dis = " ( 1=1 ) ";  // 数据表 无地区时，（即无地区限制）显示全部
                }

                // 查询长条件
                if (!"".equals(con)) {
                    dis = " ( " + con + " ) and " + dis + " ";
                } else {
                    dis = " ( 1=1 )  and " + dis + " ";
                }
                retStr = dataCatalogService.listFromTableByCondition(tablename, page, rows, dis);
            }
        }
        return retStr;
    }

    @RequestMapping("/listFieldDefFromTbl/{tableID}")
    public String listFieldDefFromTbl(@PathVariable("tableID") String tableID) throws Exception {
        List<Map> list = dataCatalogService.listMetaTableField_Def(tableID);
        return JsonUtils.toJson(list);
    }


    /**
     * 所有字段
     *
     * @param tableID
     * @return
     * @throws Exception
     */
    @RequestMapping("/listFieldDefFromTblAll/{tableID}")
    public String listFieldDefFromTblAll(@PathVariable("tableID") String tableID) throws Exception {
        List<Map> list = dataCatalogService.listMetaTableField_DefAll(tableID);
        return JsonUtils.toJson(list);
    }

    /**
     * 获取表单元素(未分页)
     *
     * @param tflag
     * @param id
     * @param key
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFromDataNoPage")
    public String getFromDataNoPage(String tflag, String id, String key) throws Exception {
        List<Map> data = new ArrayList<Map>();
        data = dataCatalogService.getFromDataNoPage(tflag, id, key);
        return JSONArray.toJSONString(data);
    }

    /**
     * 获取表单元素
     *
     * @param tflag
     * @param id
     * @param key
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFromData")
    public String getFromData(String tflag, String id, String key, int page, int rows) throws Exception {
        String str = dataCatalogService.getFromData(tflag, id, key, page, rows);
        return str;
    }

    /**
     * 保存表单
     *
     * @return
     */
    @RequestMapping("/saveFromData")
    public String saveFromData(HttpServletRequest request) {
        try {
            String tflag = request.getParameter("tflag");
            if ("top".equals(tflag)) {  // 顶级
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String cname = request.getParameter("cname");
                String url = request.getParameter("url");
                String dbType = request.getParameter("dbtype");
                MetaDataDB db = new MetaDataDB();
                if (id != null && !"".equals(id)) {
                    db.setId(id);
                    db.setName(name);
                    db.setCname(cname);
                    db.setUrl(url);
                    db.setDbtype(dbType);
                    db.setEdittime(new Date());
                    dataCatalogService.updateMetaDataDB(db);
                } else {
                    db.setId(UUID.randomUUID().toString());
                    db.setName(name);
                    db.setCname(cname);
                    db.setUrl(url);
                    db.setDbtype(dbType);
                    db.setCreatetime(new Date());
                    db.setEdittime(new Date());
                    dataCatalogService.insertMetaDataDB(db);
                }
            } else if ("db".equals(tflag)) { // 数据库
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String cname = request.getParameter("cname");
                String databaseid = request.getParameter("databaseid");
                String parentId = request.getParameter("parentId");
                String priid = request.getParameter("priid");
                String dirlayerid = request.getParameter("dirlayerid");
                String layerconfig = request.getParameter("layerconfig");
                String distfield = request.getParameter("distfield");
                String searfield = request.getParameter("searfield");
                String sumfield = request.getParameter("sumfield");
                String groupfield = request.getParameter("groupfield");
                String fieldunit = request.getParameter("fieldunit");
                MetaDataTable table = new MetaDataTable();
                if (id != null && !"".equals(id)) {
                    table.setId(id);
                    table.setName(name);
                    table.setCname(cname);
                    if (databaseid != null && !"".equals(databaseid)) {
                        parentId = databaseid;
                    }
                    table.setDatabaseid(parentId);
                    table.setDirlayerid(dirlayerid);
                    table.setLayer_config(layerconfig);
                    table.setDistfield(distfield);
                    table.setSearfield(searfield);
                    table.setSumfield(sumfield);
                    table.setGroupfield(groupfield);
                    table.setFieldunit(fieldunit);
                    dataCatalogService.updateMetaDataTable(table); // 主键字段从数据库读取
                } else {
                    table.setId(UUID.randomUUID().toString());
                    table.setName(name);
                    table.setCname(cname);
                    table.setDatabaseid(parentId);
                    table.setDirlayerid(dirlayerid);
                    table.setLayer_config(layerconfig);
                    table.setDistfield(distfield);
                    table.setSearfield(searfield);
                    table.setSumfield(sumfield);
                    table.setGroupfield(groupfield);
                    table.setFieldunit(fieldunit);
                    dataCatalogService.insertMetaDataTable(table);
                }
            } else if ("table".equals(tflag)) { // 表

                //传进来的新的字段信息
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String cname = request.getParameter("cname");
                String description = request.getParameter("description");
                String type = request.getParameter("type");
                String constraint = request.getParameter("constraint");
                String unit = request.getParameter("unit");
                String dd = request.getParameter("dd");
                String editable = (request.getParameter("editable") != null && ("是".equals(request.getParameter("editable")) || "1".equals(request.getParameter("editable")))) ? "1" : "0";
                String visible = (request.getParameter("visible") != null && ("是".equals(request.getParameter("visible")) || "1".equals(request.getParameter("visible")))) ? "1" : "0";
                String dispsort = request.getParameter("dispsort");
                String prikey = (request.getParameter("prikey") != null && ("是".equals(request.getParameter("prikey")) || "1".equals(request.getParameter("prikey")))) ? "1" : "0";
                String parentId = request.getParameter("parentId"); //

                MetaDataField field = new MetaDataField();
                field.setName(name);
                field.setCname(cname);
                field.setDescription(description);
                field.setType(type);
                field.setConstraint(constraint);
                field.setUnit(unit);
                field.setDd(dd);
                field.setEditable(editable);
                field.setVisible(visible);
                field.setDispsort(dispsort);
                field.setPrikey(prikey);
                field.setTableid(parentId);


                //同步业务库的数据表字段
                List lii = new ArrayList();
                lii.add(parentId);
                List<Map> dat = dataCatalogService.getTableInfo(lii);// 根据表id查询表名
                String dbID = dat.get(0).get("databaseid").toString();
                String tableName = dat.get(0).get("name").toString();
                List dbli = new ArrayList();
                dbli.add(dbID);
                List<Map> re = dataCatalogService.getDBInfoById(dbli);
                String orlcUrl = re.get(0).get("url").toString();

                // 修改字段
                if (id != null && !"".equals(id)) {
                    field.setId(id);


                    //同步业务库的数据表字段: 暂时只对（字段名称，类型，是否为主键），进行同步
                    //--获取旧的字段信息，进行判断
                    Map fieldInfo = dataCatalogService.getFieldInfoByFieldId(id);
                    String oldName = fieldInfo.get("name").toString();
                    MetaDataField oldField = dataCatalogDao.getMetaDataField("select * from AG_WA_METADATA_FIELD where name='" + oldName + "' and tableid='" + parentId + "'");

                    ArrayList<String>  sqlArr = new ArrayList();
                    if(!oldField.getName().equals(name)) {
                        sqlArr.add("alter table "+ tableName +" rename column "+oldField.getName()+" to "+ name );
                    }

                    if (oldField.getPrikey() == null || oldField.getPrikey() == "否") {
                        oldField.setPrikey("0");
                    }
                    if(!oldField.getPrikey().equals(prikey)) {
                        if (prikey.equals("1")){
                            sqlArr.add("alter table "+tableName+" add constraint "+oldName+" primary key("+oldName+")"); //设置主键
                        }else {
                           // sqlArr.add("alter table "+tableName+" drop constraint 主键名"); //删除主键
                        }
                    }

                    if (!oldField.getType().equals(type)){
                        sqlArr.add("alter table "+ tableName +" modify ( "+ oldName + " "+ type +")");
                    }
                    if(sqlArr.size() > 0) {
                        for(String s : sqlArr) {
                            dataCatalogService.alertTableDesc(orlcUrl, s);
                        }
                    }

                    //修改元数据
                    dataCatalogService.updateMetaDataField(field);

                } else { //新增字段
                    field.setId(UUID.randomUUID().toString());
                    dataCatalogService.insertMetaDataField(field);

                    //同步业务库的数据表字段
                    dataCatalogService.alertTableDesc(orlcUrl, "alter table " + tableName + " add(" + name + " " + type + ")");
                }
            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "保存失败！"));
    }

    /**
     * 删除表单数据
     *
     * @param tflag
     * @param id
     * @return
     */
    @RequestMapping("/delFromData")
    public String delFromData(String tflag, String id, String parentId, String name) {
        try {
            if ("top".equals(tflag)) { // 顶级
                dataCatalogService.delMetaDataDB(id);
            } else if ("db".equals(tflag)) { // 数据库
                //18-9-12
                //同步业务数据库
                String tableName = name;
                String dbId = parentId;
                List dbli = new ArrayList();
                dbli.add(dbId);
                List<Map> re = dataCatalogService.getDBInfoById(dbli);
                String orlcUrl = re.get(0).get("url").toString();
                dataCatalogService.alertTableDesc(orlcUrl, "drop table " + tableName);
                dataCatalogService.delectMetaModify(tableName); //刪除與審覈相關的元數據
                dataCatalogService.delMetaDataTable(id);


                dataCatalogService.delMetaDataTable(id);
            } else if ("table".equals(tflag)) { // 表

           // 18-9-10
                //同步业务数据库
                //1.根据字段id,获取字段名
                //2.根据表id, 获取表名和数据库id
                //3.根据数据库id, 获取数据库信息：url
                Map fieldInfo = dataCatalogService.getFieldInfoByFieldId(id);
                String fieldName = fieldInfo.get("name").toString();
                List lii = new ArrayList();
                lii.add(parentId);
                List<Map> dat = dataCatalogService.getTableInfo(lii);// 根据表id查询表名
                String dbID = dat.get(0).get("databaseid").toString();
                String tableName = dat.get(0).get("name").toString();
                List dbli = new ArrayList();
                dbli.add(dbID);
                List<Map> re = dataCatalogService.getDBInfoById(dbli);
                String orlcUrl = re.get(0).get("url").toString();
                dataCatalogService.alertTableDesc(orlcUrl, "alter table " + tableName + " drop column " + fieldName);
                dataCatalogService.delMetaDataField(id);

            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "操作失败！"));
    }

    /**
     * 获取数据库与表关联
     *
     * @return
     */
    @RequestMapping("/getRelateData")
    public String getRelateData(String relateDataKey, String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // 查询已有表
        List<Map> dat = dataCatalogService.getAllTable();
        String cond = "t.table_name not in ( ";
        if (dat != null && dat.size() > 0) {
            for (int i = 0; i < dat.size(); i++) {
                if ("t.table_name not in ( ".equals(cond)) {
                    cond += "'" + dat.get(i).get("name") + "'";
                } else {
                    cond += ",'" + dat.get(i).get("name") + "'";
                }
            }
        }
        cond += ")";
        if (relateDataKey != null && !"".equals(relateDataKey)) {
            cond += " and ( t.table_name like '%" + relateDataKey + "%' or c.comments like '%" + relateDataKey + "%' )  ";
        }

        // 查询数据库url
        List lii = new ArrayList();
        lii.add(id);
        List<Map> re = dataCatalogService.getDBInfoById(lii);
        String url = re.get(0).get("url").toString();

        // 获取关联表
        data = dataCatalogService.getRelateData(url, id, cond);

        return JSONArray.toJSONString(data);
    }

    /**
     * 保存数据库与表关联
     *
     * @return
     */
    @RequestMapping("/saveRelateData")
    public String saveRelateData(String tableIds, String dbId) {
        boolean flag = true;
        try {
            // 保存现在的
            if (tableIds != null && !"".equals(tableIds)) {
                String[] tables = tableIds.split(",");
                for (int i = 0; i < tables.length; i++) {
                    String[] tab = tables[i].split("\\|");
                    String name = tab[0];
                    String cname = (tab[1] == null ? "" : tab[1]);

                    // 判断是否存在，不存在则插入                    
                    List lii = new ArrayList();
                    lii.add(name);
                    lii.add(dbId);
                    List<Map> data = dataCatalogService.getTableData(lii);
                    if (!(data != null && data.size() > 0)) {
                        MetaDataTable table = new MetaDataTable();
                        table.setId(UUID.randomUUID().toString());
                        table.setName(name);
                        table.setCname(cname);
                        table.setDatabaseid(dbId);
                        int b = dataCatalogService.insertMetaDataTable(table);
                        if (b == 0) {
                            flag = false;
                            break;
                        }
                    }
                }

                // 同步表字段
                if (flag) {
                    for (int j = 0; j < tables.length; j++) {
                        String[] tab = tables[j].split("\\|");
                        String name = tab[0];
                        // 根据表名查找表id                        
                        List lii = new ArrayList();
                        lii.add(name);
                        List<Map> data = dataCatalogService.getTableByName(lii);
                        String tableId = "";
                        if (data != null && data.size() > 0) {
                            tableId = data.get(0).get("id").toString();
                        }

                        // 获取表的字段
                        String field = getFieldData(tableId);
                        net.sf.json.JSONArray fields = net.sf.json.JSONArray.fromObject(field);
                        String fieldIds = "";
                        for (int k = 0; k < fields.size(); k++) {
                            Map obj = (Map) fields.get(k);
                            fieldIds = fieldIds + "," + obj.get("name") + "|" + obj.get("description") + "|" + obj.get("type") + "|" + obj.get("constraint") + "|" + obj.get("prikey");
                        }
                        if (!"".equals(fieldIds)) {
                            fieldIds = fieldIds.substring(1);
                        }
                        // 字段入库
                        String json = saveFieldData(fieldIds, tableId);
                        JSONObject jso = JSONObject.fromObject(json);
                        if (!(json != null && jso.get("success") != null && "true".equals(jso.get("success").toString()))) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回提示
        if (flag) {
            return JsonUtils.toJson(new ResultForm(true));
        } else {
            return JsonUtils.toJson(new ResultForm(false, "操作失败！"));
        }
    }

    /**
     * 获取表字段同步
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFieldData")
    public String getFieldData(String tableId) throws Exception {
        List<Map> data = new ArrayList<Map>();

        // 根据表id查询表名
        List lii = new ArrayList();
        lii.add(tableId);
        List<Map> dat = dataCatalogService.getTableInfo(lii);
        String tableName = "";
        if (dat != null && dat.size() > 0) {
            tableName = dat.get(0).get("name").toString();
        }

        // 查询url
        List li2 = new ArrayList();
        li2.add(tableId);
        List<Map> re = dataCatalogService.getDBById(li2);
        String url = re.get(0).get("url").toString();

        // 获取同步字段
        data = dataCatalogService.getFieldData(url, tableName, tableId);
        return JSONArray.toJSONString(data);
    }

    /**
     * 保存同步字段
     *
     * @return
     */
    @RequestMapping("/saveFieldData")
    public String saveFieldData(String fieldIds, String tableId) {
        try {
            // 保存现在的
            if (fieldIds != null && !"".equals(fieldIds)) {
                String[] tables = fieldIds.split(",");
                for (int i = 0; i < tables.length; i++) {
                    String[] tab = tables[i].split("\\|");
                    String name = tab[0];
                    String description = (tab[1] == null ? "" : tab[1]);
                    String type = (tab[2] == null ? "" : tab[2]);
                    String constraint = tab[3];
                    String prikey = ("是".equals(tab[4]) || !tab[4].equals("null") ? "1" : "");
                    // 查询主键
                    String id = "";
                    MetaDataField field = new MetaDataField();
                    // 根据表id,字段                    
                    List lii = new ArrayList();
                    lii.add(name);
                    lii.add(tableId);
                    List<Map> data = dataCatalogService.getFieldByTableId(lii);
                    if (data != null && data.size() > 0) {
                        id = data.get(0).get("id").toString();
                    }
                    if (!"".equals(id)) {
                        field.setId(id);
                        field.setName(name);
                        field.setCname(description == null ? "" : description);
                        field.setDescription(description == null ? "" : description);
                        field.setType(type);
                        field.setConstraint(constraint);
                        field.setPrikey(prikey);
                        field.setTableid(tableId);
                        field.setDd(data.get(0).get("dd") == null ? "" : data.get(0).get("dd").toString());
                        field.setDispsort(data.get(0).get("dispsort") == null ? "" : data.get(0).get("dispsort").toString());
                        field.setEditable(data.get(0).get("editable") == null ? "" : data.get(0).get("editable").toString());
                        field.setUnit(data.get(0).get("unit") == null ? "" : data.get(0).get("unit").toString());
                        field.setVisible(data.get(0).get("visible") == null ? "" : data.get(0).get("visible").toString());
                        dataCatalogService.updateMetaDataField(field);
                    } else {
                        field.setId(UUID.randomUUID().toString());
                        field.setName(name);
                        field.setCname(description == null ? "" : description);
                        field.setDescription(description == null ? "" : description);
                        field.setType(type);
                        field.setConstraint(constraint);
                        field.setPrikey(prikey);
                        // 主键非编辑可显示
                        if ("1".equals(prikey)) {
                            field.setEditable("0");
                            field.setVisible("1");
                        } else {
                            field.setEditable("1");
                            field.setVisible("1");
                        }
                        field.setTableid(tableId);
                        dataCatalogService.insertMetaDataField(field);
                    }
                }
            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "操作失败！"));
    }

    /**
     * 数据目录--首页
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/dataIndex.do")
    public ModelAndView dataIndex(HttpServletRequest request, Model model) throws Exception {
        String loginName = LoginHelpClient.getLoginName(request);
        AgUser agUser = this.iAgUser.findUserByName(loginName);
        return new ModelAndView("agcom/datacatalog/dataIndex");
    }

    /**
     * 获取表名
     *
     * @return
     */
    @RequestMapping("/getTableName")
    public String getTableName(String tflag, String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        if ("table".equals(tflag)) {  // 表
            // 数据库            
            List lii = new ArrayList();
            lii.add(id);
            data = dataCatalogService.getTableById(lii);
        }
        return JsonUtils.toJson(data);
    }

    /**
     * 修改数据
     *
     * @return
     */
    @RequestMapping("/saveData")
    public String saveData(HttpServletRequest request) {
        // 返回结果

        ResultForm res = new ResultForm(false, "");
        try {
            String prikey = request.getParameter("prikey");
            String prifield = request.getParameter("prifield");
            String tablename = request.getParameter("tablename");

            // 判断是否有主键
            if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
                // 获取字段描述
                List lii = new ArrayList();
                lii.add(tablename);
                List<Map> data = dataCatalogService.getFieldCname(lii);


                List<Map> dat = new ArrayList<Map>();
                // 查询url
                List li2 = new ArrayList();
                li2.add(tablename);
                List<Map> re = dataCatalogService.getDBByName(li2);
                String url = re.get(0).get("url").toString();

                // 查询原来值
                dat = dataCatalogService.findOrigValue(url, tablename, prifield, prikey);

                // 修改或插入
                String field = "";
                String org = "";
                String mod = "";
                if (data != null && data.size() > 0) {
                    // json
                    field += "{";
                    org += "{";
                    mod += "{";
                    for (int i = 0; i < data.size(); i++) {
                        // 字段json
                        String name = data.get(i).get("name").toString();
                        String cname = data.get(i).get("cname") == null ? "" : data.get(i).get("cname").toString();
                        // 原值json
                        String val = "";
                        if (dat != null && dat.size() > 0) {
                            val = dat.get(0).get(name.toLowerCase()) == null ? "" : dat.get(0).get(name.toLowerCase()).toString();
                        }

                        // 修改值json
                        String va = request.getParameter(name.toLowerCase()) == null ? "" : request.getParameter(name.toLowerCase());
                        if (dat == null || (dat != null && dat.size() == 0)) {
                            val = va;
                        }
                        if (i == 0) {
                            field += "'" + name + "':'" + cname + "'";
                            org += "'" + name + "':'" + val + "'";
                            mod += "'" + name + "':'" + va + "'";
                        } else {
                            field += ",'" + name + "':'" + cname + "'";
                            org += ",'" + name + "':'" + val + "'";
                            mod += ",'" + name + "':'" + va + "'";
                        }
                    }
                    field += "}";
                    org += "}";
                    mod += "}";
                }
                // 判断账号级别
                String loginName = LoginHelpClient.getLoginName(request);
                List li3 = new ArrayList();
                li3.add(loginName);
                List<Map> list = dataCatalogService.getOrgInfoByUser(li3);
                String isMod = "-1";
                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {
                        String[] deps = list.get(i).get("xpath").toString().split("/");
                        if ("局机关".equals(deps[deps.length - 1])) {
                            isMod = "0";
                        } else if ("各区水务部门".equals(deps[deps.length - 1])) {
                            isMod = "-1";
                        } else {
                            isMod = "-1";
                        }
                    }
                }
                // 查询原来的                
                List li4 = new ArrayList();
                li4.add(tablename);
                li4.add(prifield);
                li4.add(prikey);
                List<Map> li = dataCatalogService.getCheckData(li4);
                if (li != null && li.size() > 0) {  // 耒上报的修改
                    // 保存修改表
                    MetaDataModify mo = new MetaDataModify();
                    mo.setId(li.get(0).get("id").toString());
                    mo.setTableName(tablename);
                    mo.setPriField(prifield);
                    mo.setPriValue(prikey);
                    mo.setFieldDescription(field);
                    mo.setOrigValue(org);
                    mo.setModiValue(mod);
                    mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    // 判断是新增还是修改
                    if (li.get(0).get("checktype") != null && "add".equals(li.get(0).get("checktype").toString())) {
                        mo.setIsModi("");
                        mo.setIsAdd(isMod);
                        mo.setIsDel("");
                    } else if (li.get(0).get("checktype") != null && "del".equals(li.get(0).get("checktype").toString())) {
                        mo.setIsModi("");
                        mo.setIsAdd("");
                        mo.setIsDel(isMod);
                    } else if (li.get(0).get("checktype") != null && "modi".equals(li.get(0).get("checktype").toString())) {
                        mo.setIsModi(isMod);
                        mo.setIsAdd("");
                        mo.setIsDel("");
                    } else {
                        mo.setIsModi(isMod);
                        mo.setIsAdd("");
                        mo.setIsDel("");
                    }
                    mo.setOperaName(loginName);
                    int y = dataCatalogDao.upMetaDataModify(mo);
                    if (y > 0) {
                        res.setSuccess(true);
                        res.setMessage("修改成功,请等待审核...");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("修改失败！");
                    }
                } else {
                    // 保存修改表
                    MetaDataModify mo = new MetaDataModify();
                    mo.setId(UUID.randomUUID().toString());
                    mo.setTableName(tablename);
                    mo.setPriField(prifield);
                    mo.setPriValue(prikey);
                    mo.setFieldDescription(field);
                    mo.setOrigValue(org);
                    mo.setModiValue(mod);
                    mo.setRemark("");
                    mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    mo.setIsAdd("");
                    mo.setIsDel("");
                    mo.setIsModi(isMod);
                    mo.setOperaName(loginName);
                    int y = dataCatalogDao.saveMetaDataModify(mo);
                    if (y > 0) {
                        res.setSuccess(true);
                        res.setMessage("修改成功,请等待审核...");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("修改失败！");
                    }
                }
            } else {
                res.setSuccess(false);
                res.setMessage("缺失必要的参数");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.toJson(res);
    }

    /**
     * 数据目录定位
     *
     * @return
     */
    @RequestMapping("/dw")
    public ModelAndView dw(HttpServletRequest request, Model model) {
        String layerid = request.getParameter("layerid");
        String prifield = request.getParameter("prifield");
        String prikey = request.getParameter("prikey");
        String tablename = request.getParameter("tablename");
        String tablecname = request.getParameter("tablecname");
        String coox = request.getParameter("coox");
        String cooy = request.getParameter("cooy");
        String sttp = request.getParameter("sttp");
        // 返回参数
        model.addAttribute("prikey", prikey);
        model.addAttribute("prifield", prifield);
        model.addAttribute("layerid", layerid);
        model.addAttribute("tablename", tablename);
        model.addAttribute("tablecname", tablecname);
        model.addAttribute("coox", coox);
        model.addAttribute("cooy", cooy);
        model.addAttribute("sttp", sttp);
        return new ModelAndView("agcom/datacatalog/dw");
    }

    /**
     * 属性授权
     */
    @RequestMapping("/dataAuthor")
    public ModelAndView dataAuthor(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataAuthor");
    }

    /**
     * 保存授权
     *
     * @return
     */
    @RequestMapping("/saveAuthorData")
    public String saveAuthorData(HttpServletRequest request) {
        boolean flag = true;
        try {
            String tablei = request.getParameter("tableid");
            String roleid = request.getParameter("roleid");
            String distid = request.getParameter("distid");
            String ischeck = request.getParameter("ischeck");
            String isedit = request.getParameter("isedit");
            String[] tableList = tablei.split(",");
            String[] roleList = roleid.split(",");
            String[] distList = distid.split(",");
            for (int m = 0; m < tableList.length; m++) {
                String tableid = tableList[m];
                for (int i = 0; i < roleList.length; i++) {
                    String role = roleList[i];
                    // 含编辑权限
                    if ("1".equals(isedit)) {
                        for (int j = 0; j < distList.length; j++) {
                            String dist = distList[j];
                            // 判断表名、用户、地区是否已存在
                            List lii = new ArrayList();
                            lii.add(tableid);
                            lii.add(role);
                            lii.add(dist);
                            List<Map> data = dataCatalogService.getUserDataByDist(lii);
                            if (data != null && data.size() > 0) {
                                dataCatalogDao.delAuthorData(data.get(0).get("id").toString());
                            }
                            // 保存
                            AgUserData ag = new AgUserData();
                            ag.setId(UUID.randomUUID().toString());
                            ag.setTableid(tableid);
                            ag.setRoleid(role);
                            ag.setDistid(dist);
                            ag.setIscheck(ischeck);
                            ag.setIsedit(isedit);
                            int k = dataCatalogDao.insertAuthorData(ag);
                            if (k < 1) {
                                flag = false;
                                break;
                            }
                        }
                    } else {  // 不含编辑权限
                        // 判断表名、用户是否已存在
                        List lii = new ArrayList();
                        lii.add(tableid);
                        lii.add(role);
                        List<Map> data = dataCatalogService.getUserDataByUser(lii);
                        if (data != null && data.size() > 0) {
                            dataCatalogDao.delAuthorData(data.get(0).get("id").toString());
                        }
                        // 保存
                        AgUserData ag = new AgUserData();
                        ag.setId(UUID.randomUUID().toString());
                        ag.setTableid(tableid);
                        ag.setRoleid(role);
                        ag.setDistid("");
                        ag.setIscheck(ischeck);
                        ag.setIsedit(isedit);
                        int k = dataCatalogDao.insertAuthorData(ag);
                        if (k < 1) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            return JsonUtils.toJson(new ResultForm(true));
        } else {
            return JsonUtils.toJson(new ResultForm(false, "操作失败！"));
        }
    }

    /**
     * 获取属性授权
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAuthorData")
    public String getAuthorData(String tflag, String id, String key, int page, int rows, String roleId) throws Exception {
        // 获取授权数据
        String str = dataCatalogService.getAuthorData(tflag, id, key, page, rows, roleId);
        return str;
    }

    /**
     * 删除属性授权
     *
     * @return
     */
    @RequestMapping("/delAuthorData")
    public String delAuthorData(String ids) {
        boolean flag = true;
        try {
            String[] idList = ids.split(",");
            for (int i = 0; i < idList.length; i++) {
                String id = idList[i];
                int k = dataCatalogDao.delAuthorData(id);
                if (k < 1) {
                    flag = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            return JsonUtils.toJson(new ResultForm(true));
        } else {
            return JsonUtils.toJson(new ResultForm(false, "操作失败！"));
        }
    }

    /**
     * 数据审核
     *
     * @return
     */
    @RequestMapping("/dataCheck")
    public ModelAndView dataCheck(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataCheck");
    }

    /**
     * 查询审核数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCheckData")
    public String getCheckData(String tflag, String id, int page, int rows, String isAdd, String isModi, String isDel, HttpServletRequest request) throws Exception {
        // 判断用户角色
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii);
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("超级管理员".equals(list.get(k).get("name").toString())) {
                    roleName = list.get(k).get("name").toString();
                    break;
                } else {
                    roleId += ",'" + list.get(k).get("id").toString() + "'";
                }
            }
        }
        if (!"".equals(roleId)) {
            roleId = roleId.substring(1);
        }
        // 拼装条件         
        String con = "";
        if (isAdd != null && !"".equals(isAdd)) {
            con += " is_add='" + isAdd + "' ";
        } else {
            con += " 1=1 ";
        }
        if (isModi != null && !"".equals(isModi)) {
            con += " and is_modi='" + isModi + "' ";
        } else {
            con += " and 1=1 ";
        }
        if (isDel != null && !"".equals(isDel)) {
            con += " and is_del='" + isDel + "' ";
        } else {
            con += " and 1=1 ";
        }
        // 返回结果
        String str = dataCatalogService.getCheckData(tflag, roleName, con, roleId, id, page, rows);
        return str;
    }

    /**
     * 获取表的字段
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/listFieldFromTable/{tablename}")
    public String listFieldFromTable(@PathVariable("tablename") String tablename) throws Exception {
        List<Map> data = new ArrayList<Map>();
        List lii = new ArrayList();
        lii.add(tablename);
        data = dataCatalogService.getCNameById(lii);
        return JsonUtils.toJson(data);
    }

    /**
     * 驳回
     *
     * @param id
     * @param type
     * @param tablenames
     * @param prifields
     * @param privalues
     * @param view
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/passCheckDataNo")
    public String passCheckDataNo(String id, String type, String tablenames, String prifields, String privalues, String view, HttpServletRequest request) throws Exception {
        ResultForm res = new ResultForm(false, "操作失败!");
        // 审核人
        String loginName = LoginHelpClient.getLoginName(request);
        String[] ta = tablenames.split(",");
        String[] fi = prifields.split(",");
        String[] va = privalues.split(",");
        String[] idd = id.split(",");
        if (ta != null && ta.length > 0) {
            // 返回结果
            res = dataCatalogService.passCheckDataNo(type, view, loginName, ta, fi, va, idd);
        }
        return JsonUtils.toJson(res);
    }

    /**
     * 通过审核
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/passCheckData")
    public String passCheckData(String id, String type, String tablenames, String prifields, String privalues, HttpServletRequest request) throws Exception {
        ResultForm res = new ResultForm(false, "操作失败!");
        // 审核人
        String loginName = LoginHelpClient.getLoginName(request);
        String[] ta = tablenames.split(",");
        String[] fi = prifields.split(",");
        String[] va = privalues.split(",");
        String[] idd = id.split(",");
        if (ta != null && ta.length > 0) {
            // 返回结果
            res = dataCatalogService.passCheckData(type, loginName, ta, fi, va, idd);
        }
        return JsonUtils.toJson(res);
    }

    /**
     * 删除数据目录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/delData")
    public String delData(HttpServletRequest request, String tablename, String prikey, String prifield) throws Exception {
        ResultForm res = new ResultForm(false, null);
        // 判断是否有主键
        if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> data = dataCatalogService.getFieldInfo(lii);
            List<Map> dat = new ArrayList<Map>();
            // 查询url
            List li2 = new ArrayList();
            li2.add(tablename);
            List<Map> re = dataCatalogService.getDBByName(li2);
            String url = re.get(0).get("url").toString();
            // 查询原值
            dat = dataCatalogService.findOrigValue(url, tablename, prifield, prikey);
            // 字段
            String field = "";
            String org = "";
            if (data != null && data.size() > 0) {
                // json
                field += "{";
                org += "{";
                for (int i = 0; i < data.size(); i++) {
                    // 字段json
                    String name = data.get(i).get("name").toString();
                    String cname = data.get(i).get("cname") == null ? "" : data.get(i).get("cname").toString();
                    String val = "";
                    if (dat != null && dat.size() > 0) {
                        val = dat.get(0).get(name) == null ? "" : dat.get(0).get(name).toString();
                    }
                    if (i == 0) {
                        field += "'" + name + "':'" + cname + "'";
                        org += "'" + name + "':'" + val + "'";
                    } else {
                        field += ",'" + name + "':'" + cname + "'";
                        org += ",'" + name + "':'" + val + "'";
                    }
                }
                field += "}";
                org += "}";
            }

            // 判断账号级别
            String loginName = LoginHelpClient.getLoginName(request);
            List li3 = new ArrayList();
            li3.add(loginName);
            List<Map> list = dataCatalogService.getOrgInfoByUser(li3);
            String isDel = "-1";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String[] compents = list.get(i).get("xpath").toString().split("/");
                    if ("局机关".equals(compents[compents.length - 1])) {
                        isDel = "0";
                    } else if ("各区水务部门".equals(compents[compents.length - 1])) { //list.get(i).get("xpath").toString().split("/")[2]
                        isDel = "-1";
                    } else {
                        isDel = "-1";
                    }
                }
            }
            // 查询原来的
            List li4 = new ArrayList();
            li4.add(tablename);
            li4.add(prifield);
            li4.add(prikey);
            List<Map> li = dataCatalogService.getModiByKey(li4);
            if (li != null && li.size() > 0) {
                // 保存修改表
                MetaDataModify mo = new MetaDataModify();
                mo.setId(li.get(0).get("id").toString());
                mo.setTableName(tablename);
                mo.setPriField(prifield);
                mo.setPriValue(prikey);
                mo.setFieldDescription(field);
                mo.setOrigValue(org);
                mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                mo.setIsDel(isDel);
                mo.setIsAdd("");
                mo.setIsModi("");
                mo.setOperaName(loginName);
                int y = dataCatalogDao.upMetaDataModify(mo);
                if (y > 0) {
                    res.setSuccess(true);
                    res.setMessage("删除操作成功,请等待审核...");
                } else {
                    res.setSuccess(false);
                    res.setMessage("删除失败！");
                }
            } else {
                // 保存修改表
                MetaDataModify mo = new MetaDataModify();
                mo.setId(UUID.randomUUID().toString());
                mo.setTableName(tablename);
                mo.setPriField(prifield);
                mo.setPriValue(prikey);
                mo.setFieldDescription(field);
                mo.setOrigValue(org);
                mo.setRemark("");
                mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                mo.setIsAdd("");
                mo.setIsDel(isDel);
                mo.setIsModi("");
                mo.setOperaName(loginName);
                int y = dataCatalogDao.saveMetaDataModify(mo);
                if (y > 0) {
                    res.setSuccess(true);
                    res.setMessage("删除操作成功,请等待审核...");
                } else {
                    res.setSuccess(false);
                    res.setMessage("删除失败！");
                }
            }
        } else {
            res.setSuccess(false);
            res.setMessage("缺失必要的参数！");
        }
        return JsonUtils.toJson(res);
    }

    /**
     * 根据表名获取图层配置信息
     * 2017-12-19 czh
     */
    @RequestMapping("/getMetaDataByTableName/{tableName}")
    public String getMetaDataByTableName(@PathVariable String tableName) {
        try {
            if (Common.isCheckNull(tableName)) return null;
            List list = (List) dataCatalogService.getMetaDataByTableName(tableName);
            return JsonUtils.toJson(new ContentResultForm<>(true, list.size() > 0 ? list.get(0) : null, "查询成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJson(new ResultForm(false));
        }
    }

    /**
     * 数据目录的新增
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/addFieldData")
    public String addFieldData(HttpServletRequest request) throws Exception {
        ResultForm res = new ResultForm(false, null);
        String tablename = request.getParameter("tablename");
        String prifield = request.getParameter("prifield");
        String prikey = request.getParameter(prifield.toLowerCase());
        // 判断必要参数
        if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
            // 获取表字段描述
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> data = dataCatalogService.getFieldCname(lii);
            String org = "{";
            String field = "{";
            for (int i = 0; i < data.size(); i++) {
                String name = data.get(i).get("name").toString();
                String value = request.getParameter(name.toLowerCase());
                String desc = data.get(i).get("cname") == null ? "" : data.get(i).get("cname").toString();
                if (i == 0) {
                    field += "'" + name + "':'" + desc + "'";
                    org += "'" + name + "':'" + value + "'";
                } else {
                    field += ",'" + name + "':'" + desc + "'";
                    org += ",'" + name + "':'" + value + "'";
                }
            }
            org += "}";
            field += "}";

            // 判断账号级别
            String loginName = LoginHelpClient.getLoginName(request);
            List li2 = new ArrayList();
            li2.add(loginName);
            List<Map> list = dataCatalogService.getOrgInfoByUser(li2);
            String isAdd = "-1";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String[] array = list.get(i).get("xpath").toString().split("/");
                    if ("局机关".equals(array[array.length-1])) {
                        isAdd = "0";
                    } else if ("各区水务部门".equals(array[array.length-1])) {
                        isAdd = "-1";
                    } else {
                        isAdd = "-1";
                    }
                }
            }

            // 保存修改表
            MetaDataModify mo = new MetaDataModify();
            mo.setId(UUID.randomUUID().toString());
            mo.setTableName(tablename);
            mo.setPriField(prifield);
            mo.setPriValue(prikey);
            mo.setFieldDescription(field);
            mo.setOrigValue(org);
            mo.setRemark("");
            mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            mo.setIsAdd(isAdd);
            mo.setIsDel("");
            mo.setIsModi("");
            mo.setOperaName(loginName);
            int y = dataCatalogDao.saveMetaDataModify(mo);
            if (y > 0) {
                res.setSuccess(true);
                res.setMessage("新增成功,请等待审核！");
            } else {
                res.setSuccess(false);
                res.setMessage("新增失败！");
            }
        } else {
            res.setSuccess(false);
            res.setMessage("缺失必要的参数！");
        }
        return JsonUtils.toJson(res);
    }

    /**
     * 查询修改数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyData")
    public String modifyData(String tablename, String prifield, String prikey) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // 判断是否修改过
        List lii = new ArrayList();
        lii.add(tablename);
        lii.add(prifield);
        lii.add(prikey);
        data = dataCatalogService.getModiValue(lii);
        if (data != null && data.size() > 0 && data.get(0).get("value") != null && !"".equals(data.get(0).get("value"))) {
            String json = data.get(0).get("value").toString();
            JSONObject js = JSONObject.fromObject(json);
            Iterator iterator = js.keys();
            data.clear();
            Map map = new HashMap();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = js.getString(key);
                map.put(key.toLowerCase(), value);
            }
            // 拼装参数
            map.put("tablename", tablename);
            map.put("prifield", prifield);
            map.put("prikey", prikey);
            data.add(map);
        } else {
            // 查询url
            List li2 = new ArrayList();
            li2.add(tablename);
            List<Map> re = dataCatalogService.getDBByName(li2);
            String url = re.get(0).get("url").toString();

            // 查询原值
            data = dataCatalogService.findData(url, tablename, prifield, prikey);
        }

        return JsonUtils.toJson(data);
    }

    /**
     * 获取表的审核统计信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCheckTableInfo")
    public String getCheckTableInfo(String tableid, String checktype, String start_time, String end_time, String result) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String[] checktypes = checktype.split(",");

        // 返回结果
        data = dataCatalogService.getCheckTableInfo(checktypes, tableid, start_time, end_time, result);
        return JsonUtils.toJson(data);
    }

    /**
     * 审核记录
     *
     * @return
     */
    @RequestMapping("/checkRecord")
    public ModelAndView checkRecord(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/checkRecord");
    }

    /**
     * 数据上报
     *
     * @return
     */
    @RequestMapping("/dataReport")
    public ModelAndView dataReport(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataReport");
    }

    /**
     * 获取数据审核
     *
     * @return
     */
    @RequestMapping("/getCheckDataNew")
    public String getCheckDataNew(String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result, HttpServletRequest request) throws Exception {
        List<Map> data = new ArrayList<Map>();

        // 判断用户角色
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii); //對接agcloud用戶-->"超級管理員".equals(roleName)
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("超级管理员".equals(list.get(k).get("name").toString())) {
                    roleName = list.get(k).get("name").toString();
                    break;
                } else {
                    roleId += ",'" + list.get(k).get("id").toString() + "'";
                }
            }
        }
        if (!"".equals(roleId)) {
            roleId = roleId.substring(1);
        }

        // 返回结果
        String str = dataCatalogService.getCheckDataNew(roleName, roleId, checktype, start_time, end_time, tflag, tableid, page, rows, result); //對接agcloud用戶-->"超級管理員".equals(roleName)
        return str;
    }

    /**
     * 获取数据审核
     *
     * @return
     */
    @RequestMapping("/getReportDataNew")
    public String getReportDataNew(String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result, HttpServletRequest request) throws Exception {
        // 判断用户角色
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii); //對接agcloud用戶-->"超級管理員".equals(roleName)
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("超级管理员".equals(list.get(k).get("name").toString())) {
                    roleName = list.get(k).get("name").toString();
                    break;
                } else {
                    roleId += ",'" + list.get(k).get("id").toString() + "'";
                }
            }
        }
        if (!"".equals(roleId)) {
            roleId = roleId.substring(1);
        }

        // 返回结果 
        String str = dataCatalogService.getReportDataNew(roleName, roleId, start_time, end_time, checktype, tflag, tableid, page, rows, result);//對接agcloud用戶-->"超級管理員".equals(roleName)
        return str;
    }

    /**
     * 目录列表
     *
     * @return
     */
    @RequestMapping("/tempList")
    public ModelAndView tempList(HttpServletRequest request, Model model) {
        return new ModelAndView("awater/datacatalog/tempList");
    }

    /**
     * 目录注册
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/tempReg")
    public ModelAndView tempReg(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/tempReg");
    }

    /**
     * 保存模板
     *
     * @return
     */
    @RequestMapping("/saveTemp")
    public String saveTemp(String cname, String name, String field, String dbId) {
        ResultForm res = new ResultForm(false, null);

        if (name != null && !"".equals(name) && cname != null && !"".equals(cname) && field != null && !"[]".equals(field)) {
            // 操作状态
            String msg = "";
            try {
                // 查询url
                List lii = new ArrayList();
                lii.add(dbId);
                List<Map> re = dataCatalogService.getDBInfoById(lii);
                String url = re.get(0).get("url").toString();

                // 返回结果
                msg = dataCatalogService.saveTemp(url, field, name, cname);

                if ("".equals(msg)) {
                    // 生成目录记录
                    MetaDataTemp temp = new MetaDataTemp();
                    temp.setId(UUID.randomUUID().toString());
                    temp.setTable_name(name);
                    temp.setTemp_name(cname);
                    temp.setTemp_dir("WEB-INF\\ui-jsp\\awater\\datacatalog\\temp");
                    temp.setLast_update(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    int y = dataCatalogDao.insertMetaDataTemp(temp);
                    if (y > 0) {
                        res.setSuccess(true);
                        res.setMessage("操作成功！");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("生成目录失败！");
                    }
                } else {
                    res.setSuccess(false);
                    res.setMessage("创建表失败！请联系系统管理员。原因：" + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.setSuccess(false);
                res.setMessage("创建表失败！请联系系统管理员。原因：" + e.getMessage());
            }
        } else {
            res.setSuccess(false);
            res.setMessage("缺失必要的参数！");
        }

        return JsonUtils.toJson(res);
    }

    /**
     * 获取模板列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getTempData")
    public String getTempData(String temp_name, String start_time, String end_time, int page, int rows) throws Exception {
        String str = dataCatalogService.getTempData(temp_name, start_time, end_time, page, rows);
        return str;
    }

    /**
     * 删除模板
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delTempData")
    public String delTempData(String id) throws Exception {
        ResultForm res = new ResultForm(false, null);

        String[] ids = id.split(",");
        if (ids.length > 0) {
            for (String i : ids) {
                List li = new ArrayList();
                li.add(i);
                int j = dataCatalogService.delTempById(li);

                if (j > 0) {
                    res.setSuccess(true);
                    res.setMessage("删除模板操作成功！");
                } else {
                    res.setSuccess(false);
                    res.setMessage("删除模板操作失败！");
                    // 结束循环
                    break;
                }
            }
        }
        return JsonUtils.toJson(res);
    }

    /**
     * 下载模板
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/downTemp/{id}")
    public void downTemp(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws Exception {
        // 获取模板信息
        List lii = new ArrayList();
        lii.add(id);
        List<Map> list = dataCatalogService.getTempInfoById(lii);
        String fileName = "";
        if (list != null && list.size() > 0) {
            fileName += list.get(0).get("temp_name") + ".xlsx";
        }

        // 获取元数据字段
        List li = new ArrayList();
        li.add(list.get(0).get("table_name").toString().toUpperCase());
        List<Map> fields = dataCatalogService.getFieldByTable(li);

        // 拼装Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("模板示例");
        // 表头
        HSSFRow row = sheet.createRow(0);
        HSSFRow ag = sheet.createRow(1);
        for (int i = 0; i < fields.size(); i++) {
            Map map = fields.get(i);
            // 标题
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(String.valueOf(map.get("cname")));
            // 示例
            HSSFCell agCell = ag.createCell(i);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor((short) 13);// 设置背景色
            if ("DATE".equals(String.valueOf(map.get("type"))) || "date".equals(String.valueOf(map.get("type")))) {
                agCell.setCellValue("如: 年-月-日 时:分:秒");
                agCell.setCellStyle(style);
            }
            if (map.get("dd") != null && !"".equals(map.get("dd"))) {
                String key = "";
                JSONObject obj = JSONObject.fromObject(map.get("dd"));
                Iterator<String> it = obj.keys();
                while (it.hasNext()) {
                    if ("".equals(key)) {
                        key += it.next();
                    } else {
                        key += "," + it.next();
                    }
                }
                agCell.setCellValue("下拉列表类型，只需填写值，如：" + key);
                agCell.setCellStyle(style);
            }
        }

        // 下载
        String encoding = "utf-8";
        String userAgent = request.getHeader("user-agent");
        if (userAgent.toLowerCase().indexOf("msie") != -1) {
            encoding = "gbk";
        }
        fileName = fileName.substring(0, fileName.indexOf("."));
        fileName = new String(fileName.getBytes(encoding), "iso8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        OutputStream ouputStream = response.getOutputStream();

        // 输出流
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
        workbook.close();
    }

    /**
     * 上传数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadData/{id}")
    public String uploadData(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        ResultForm res = new ResultForm(false, null);
        boolean flag = true;
        boolean lostKey = true;
        // 获取存取路径
        List lii = new ArrayList();
        lii.add(id);
        List<Map> list = dataCatalogService.getTempInfoById(lii);

        // 获取表字段
        List li = new ArrayList();
        li.add(list.get(0).get("table_name").toString().toUpperCase());
        List<Map> fields = dataCatalogService.getFieldByTable(li);

        String tempDir = "";
        if (list != null && list.size() > 0) {
            tempDir += list.get(0).get("temp_dir");
        }
        // 拼接完整路径
        tempDir = request.getRealPath("") + tempDir;

        File dest = new File(tempDir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        Iterator<String> iterator = req.getFileNames();
        while (iterator.hasNext()) {
            MultipartFile f = req.getFile(iterator.next());
            String fileNames = f.getOriginalFilename();
            String fil = tempDir + File.separatorChar + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileNames;
            File file = new File(fil);
            if (file.exists()) {
                file.delete();
            }
            FileCopyUtils.copy(f.getBytes(), file);

            // 读取数据
            InputStream is = null;
            try {
                // 2007版本
                is = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(is);
                HSSFSheet sheet = workbook.getSheetAt(0);

                // 标题列
                Map map = new HashMap();

                // 获取字段行
                for (int z = 0; z < sheet.getRow(0).getLastCellNum(); z++) {
                    HSSFCell cell = sheet.getRow(0).getCell(z);
                    if (cell != null && !"".equals(cell)) {
                        for (int k = 0; k < fields.size(); k++) {
                            if (cell.getStringCellValue().trim().equals(fields.get(k).get("cname").toString())) {
                                map.put(fields.get(k).get("name").toString(), z);
                            }
                        }
                    }
                }

                // 遍历行
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }

                    String org = "{";
                    String field = "{";
                    String prifie = "";
                    String prival = "";
                    for (int e = 0; e < fields.size(); e++) {
                        String name = fields.get(e).get("name").toString();
                        String desc = fields.get(e).get("cname") == null ? "" : fields.get(e).get("cname").toString();
                        String value = "";
                        HSSFCell cell = row.getCell((int) map.get(name));
                        if (cell == null) {
                            value = "";
                        } else if (HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                            value = String.valueOf(cell.getBooleanCellValue());
                        } else if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                            value = cell.getNumericCellValue() + "";
                            if (value.endsWith(".0")) {
                                value = value.substring(0, value.length() - 2);
                            }
                        } else {
                            value = cell.getStringCellValue();
                        }

                        // 获取主键
                        if ("1".equals(fields.get(e).get("prikey"))) {
                            prifie = name;
                            prival = value;
                        }

                        // 值
                        if (e == 0) {
                            field += "'" + name + "':'" + desc + "'";
                            org += "'" + name + "':'" + value + "'";
                        } else {
                            field += ",'" + name + "':'" + desc + "'";
                            org += ",'" + name + "':'" + value + "'";
                        }
                    }
                    org += "}";
                    field += "}";

                    // 判断账号级别
                    String loginName = LoginHelpClient.getLoginName(request);
                    List li2 = new ArrayList();
                    li2.add(loginName);
                    List<Map> lis = dataCatalogService.getOrgInfoByUser(li2);
                    String isAdd = "-1";
                    if (lis != null && lis.size() > 0) {
                        for (int ii = 0; ii < list.size(); ii++) {
                            String[] deps = list.get(ii).get("xpath").toString().split("/");
                            if ("局机关".equals(deps[deps.length - 1])) {
                                isAdd = "0";
                            } else if ("各区水务部门".equals(deps[deps.length - 1])) {
                                isAdd = "-1";
                            } else {
                                isAdd = "-1";
                            }
                        }
                    }

                    // 主键字段有值时，才保存
                    if (!"".equals(prival)) {
                        // 保存修改表
                        MetaDataModify mo = new MetaDataModify();
                        mo.setId(UUID.randomUUID().toString());
                        mo.setTableName(list.get(0).get("table_name").toString().toUpperCase());
                        mo.setPriField(prifie);
                        mo.setPriValue(prival);
                        mo.setFieldDescription(field);
                        mo.setOrigValue(org);
                        mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        mo.setIsAdd(isAdd);
                        mo.setOperaName(loginName);
                        int g = dataCatalogDao.saveMetaDataModify(mo);
                        if (g < 1) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                        lostKey = false;
                    }
                }
            } catch (Exception e) {
                try {
                    // 2013版本
                    is = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(is);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    // 标题列
                    Map map = new HashMap();

                    // 获取字段行
                    for (int z = 0; z < sheet.getRow(0).getLastCellNum(); z++) {
                        XSSFCell cell = sheet.getRow(0).getCell(z);
                        if (cell != null && !"".equals(cell)) {
                            for (int k = 0; k < fields.size(); k++) {
                                if (cell.getStringCellValue().trim().equals(fields.get(k).get("cname").toString())) {
                                    map.put(fields.get(k).get("name").toString(), z);
                                }
                            }
                        }
                    }

                    // 遍历行
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        if (row == null) {
                            continue;
                        }

                        String org = "{";
                        String field = "{";
                        String prifie = "";
                        String prival = "";
                        for (int t = 0; t < fields.size(); t++) {
                            String name = fields.get(t).get("name").toString();
                            String desc = fields.get(t).get("cname") == null ? "" : fields.get(t).get("cname").toString();
                            String value = "";
                            XSSFCell cell = row.getCell((int) map.get(name));
                            if (cell == null) {
                                value = "";
                            } else if (XSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                                value = String.valueOf(cell.getBooleanCellValue());
                            } else if (XSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                                value = cell.getNumericCellValue() + "";
                                if (value.endsWith(".0")) {
                                    value = value.substring(0, value.length() - 2);
                                }
                            } else {
                                value = cell.getStringCellValue();
                            }

                            // 获取主键
                            if ("1".equals(fields.get(t).get("prikey"))) {
                                prifie = name;
                                prival = value;
                            }

                            // 值
                            if (t == 0) {
                                field += "'" + name + "':'" + desc + "'";
                                org += "'" + name + "':'" + value + "'";
                            } else {
                                field += ",'" + name + "':'" + desc + "'";
                                org += ",'" + name + "':'" + value + "'";
                            }
                        }
                        org += "}";
                        field += "}";

                        // 判断账号级别
                        String loginName = LoginHelpClient.getLoginName(request);
                        List li3 = new ArrayList();
                        li3.add(loginName);
                        List<Map> lis = dataCatalogService.getOrgInfoByUser(li3);
                        String isAdd = "-1";
                        if (lis != null && lis.size() > 0) {
                            for (int ii = 0; ii < list.size(); ii++) {
                                String[] deps = list.get(ii).get("xpath").toString().split("/");
                                if ("局机关".equals(deps[deps.length - 1])) {
                                    isAdd = "0";
                                } else if ("各区水务部门".equals(deps[deps.length - 1])) {
                                    isAdd = "-1";
                                } else {
                                    isAdd = "-1";
                                }
                            }
                        }

                        // 主键字段有值时才保存
                        if (!"".equals(prival)) {
                            // 保存修改表
                            MetaDataModify mo = new MetaDataModify();
                            mo.setId(UUID.randomUUID().toString());
                            mo.setTableName(list.get(0).get("table_name").toString().toUpperCase());
                            mo.setPriField(prifie);
                            mo.setPriValue(prival);
                            mo.setFieldDescription(field);
                            mo.setOrigValue(org);
                            mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            mo.setIsAdd(isAdd);
                            mo.setOperaName(loginName);
                            int g = dataCatalogDao.saveMetaDataModify(mo);
                            if (g < 1) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                            lostKey = false;
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    flag = false;
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (flag) {
            res.setSuccess(true);
            res.setMessage("数据上传操作成功！");
        } else {
            if (lostKey) {
                res.setSuccess(false);
                res.setMessage("数据上传操作失败！请联系系统管理员");
            } else {
                res.setSuccess(false);
                res.setMessage("上传失败！系统缺失主键，请在元数据管理中配置主键字段");
            }
        }

        return JsonUtils.toJson(res);
    }

    /**
     * 判断是否交换互动模板
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/isTempTable")
    public String isTempTable(String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // 数据库
        List lii = new ArrayList();
        lii.add(id);
        List<Map> da = dataCatalogService.getTempInfo(lii);

        // 查询表        
        List li2 = new ArrayList();
        li2.add(id);
        List<Map> dat = dataCatalogService.getTableInfo(li2);

        // 判断是否存在模板
        if (da == null || da.size() == 0) {
            // 生成目录记录
            MetaDataTemp temp = new MetaDataTemp();
            temp.setId(UUID.randomUUID().toString());
            temp.setTable_name(dat.get(0).get("name").toString());
            temp.setTemp_name(dat.get(0).get("cname") == null ? "" : dat.get(0).get("cname").toString());
            temp.setTemp_dir("WEB-INF\\ui-jsp\\awater\\datacatalog\\temp");
            temp.setLast_update(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            int y = dataCatalogDao.insertMetaDataTemp(temp);
        }

        List li3 = new ArrayList();
        li3.add(id);
        data = dataCatalogService.getTempInfo(li3);

        return JsonUtils.toJson(data);
    }

    /**
     * 上报
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/upData")
    public String upData(String ids) throws Exception {
        ResultForm res = new ResultForm(false, "操作无效！");

        if (ids != null && !"".equals(ids)) {
            res = dataCatalogService.upData(ids);
        }

        return JsonUtils.toJson(res);
    }

    /**
     * 连接测试
     *
     * @param url
     * @return
     * @throws Exception
     */
    @RequestMapping("/connectTest")
    public String connectTest(String url) throws Exception {
        ResultForm res = new ResultForm(false, "连接失败！");
        if (url != null && !"".equals(url)) {
            res = dataCatalogService.connectTest(url);
        } else {
            res.setMessage("缺失必要的参数！");
            res.setSuccess(false);
        }

        return JsonUtils.toJson(res);
    }

    /**
     * 数据上报待审核
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/findDataRecords")
    public String findDataRecords(HttpServletRequest request, String loginName) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // String loginName = LoginHelpClient.getLoginName(request);

        // 判断是否一级管理员
        List li = new ArrayList<>();
        li.add(loginName);
        List<Map> da = dataCatalogService.getRoleInfoByUser(li);
        if (da != null && da.size() > 0) {
            List<Map> dat = dataCatalogService.findDataRecords();
            for (int i = 0; i < dat.size(); i++) {
                Map map = dat.get(i);
                // 查询用户名
                String userName = " ";
                if (dat.get(i) != null && dat.get(i).get("opera_name") != null && !"".equals(dat.get(i).get("opera_name"))) {
                    List lii = new ArrayList();
                    lii.add(dat.get(i).get("opera_name"));
                    List<Map> lis = dataCatalogService.getUserName(lii);
                    if (lis != null && lis.size() > 0) {
                        userName = lis.get(0).get("user_name").toString();
                    }
                }
                map.put("userName", userName);
                data.add(map);
            }
        }
        return JsonUtils.toJson(data);
    }

    /**
     * 数据库中所有的表
     *
     * @param datebaseId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listTableByDBId/{datebaseId}")
    public String listTableByDBId(@PathVariable("datebaseId") String datebaseId) throws Exception {
        List<MetaDataTable> list = dataCatalogService.listTableByDBId(datebaseId);
        return JsonUtils.toJson(list);
    }

    /**
     * 根据数据库id，生成统计报告
     *
     * @param datebaseId
     * @return
     * @throws Exception
     */
    @RequestMapping("/createAccountingReport/{datebaseId}")
    public String createAccountingReport(@PathVariable("datebaseId") String datebaseId) {
        ResultForm res = new ResultForm(false, null);

        long startTime = System.currentTimeMillis();    //获取开始时间
        String reportStr = "报告模版未配置";
        boolean success = false;

        try {
            reportStr = dataCatalogService.createAccountingReport(datebaseId);
            success = true;
        } catch (Exception e) {
            reportStr = "统计报告生成失败";
            e.printStackTrace();

        }
        res.setSuccess(success);
        res.setMessage(reportStr);

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("1---程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return JsonUtils.toJson(res);
    }

    @RequestMapping("/getAccountingReport/{datebaseId}")
    public String getAccountingReport(@PathVariable("datebaseId") String datebaseId) {
        Map res = new HashMap();
        long startTime = System.currentTimeMillis();    //获取开始时间
        Object data;
        boolean success = false;
        try {
            data = dataCatalogService.getAccountingReport(datebaseId);
            success = true;
        } catch (Exception e) {
            data = "统计报告生成失败，请联系管理员";
            e.printStackTrace();
        }
        res.put("success", success);
        res.put("content", data);

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("1---程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return JsonUtils.toJson(res);
    }

    /**
     * 数据库中所有的表
     *
     * @param datebaseId
     * @return
     * @throws Exception
     */
    @RequestMapping("/countTableByDBId/{datebaseId}")
    public String countTableByDBId(@PathVariable("datebaseId") String datebaseId) throws Exception {
        long startTime = System.currentTimeMillis();    //获取开始时间
        Map map = dataCatalogService.countTableByDBId(datebaseId);
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("1---程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return JsonUtils.toJson(map);
    }

    /**
     * 根据表名统计 表的数量
     */
    @RequestMapping("/countRecordByTable/{tname}")
    public String countRecordByTable(@PathVariable("tname") String tname) throws Exception {
        long startTime = System.currentTimeMillis();    //获取开始时间
        Map map = dataCatalogService.countRecordByTable(tname);
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("2---程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return JsonUtils.toJson(map);
    }

    /**
     * 根据表名和列名统计 分组显示某种类型的数量
     */
    @RequestMapping("/countRecordByTableAndTypeColumn")
    public String countRecordByTableAndTypeColumn(HttpServletRequest request, String tname, String cname, String where) throws Exception {
        long startTime = System.currentTimeMillis();    //获取开始时间

        Map map = dataCatalogService.countRecordByTableColumn(tname, cname, where);

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("2---程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return JsonUtils.toJson(map);
    }
}
