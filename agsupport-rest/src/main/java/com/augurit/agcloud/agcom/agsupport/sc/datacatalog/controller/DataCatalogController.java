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
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("onlyDB".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataOnlyDBTableTree();
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("WRP_".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            List<TreeNode> son = dataCatalogService.getMetaDataDBTableTreeByName(id);
            top.setChildren(son);
            top.setState("closed");
            trees.add(top);
        } else if ("dataEditAll".equals(id)) { // ????????????

            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);

            String roleName = "";
            String roleId = "";

            if (list != null && list.size() > 0) { // ??????????????????
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
                        roleName = list.get(k).get("name").toString();
                        break;
                    } else { //?????????????????? ??? ?????? ?????? ??? ??????
                        roleId += ",'" + list.get(k).get("id").toString() + "'";
                    }
                }
            }
            if (!"".equals(roleId)) {
                roleId = roleId.substring(1);
            }

            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // ??????????????????
                List<Map> tables = dataCatalogService.getTableByEditRole(roleId); //??????agcloud??????
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // ??????????????????
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                flag = true;
                                isNull = false;
                                break;
                            }
                        }
                        // ????????????
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // ????????????
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }
                // ????????????
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataEditAllExist".equals(id)) { // ????????????

            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
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

            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // ????????????????????????                
                List<Map> tables = dataCatalogService.getTableByEditRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // ??????????????????
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {

                                // ?????????????????????
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
                        // ????????????
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // ????????????
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }
                // ????????????
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataCheckAllExist".equals(id)) { // ????????????
            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
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

            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // ????????????????????????
                List<Map> tables = dataCatalogService.getTableByCheckRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // ??????????????????
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {

                                // ?????????????????????
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
                        // ????????????
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // ????????????
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // ????????????
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataCheckAll".equals(id)) { // ????????????
            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
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

            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // ????????????????????????
                List<Map> tables = dataCatalogService.getTableByCheckRole(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // ??????????????????
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                isNull = false;
                                flag = true;
                                break;
                            }
                        }
                        // ????????????
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // ????????????
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // ????????????
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        } else if ("dataAll".equals(id)) {
            TreeNode top = new TreeNode();
            top.setText("?????????");
            top.setId("1");
            top.setIconCls("icon-folder");
            top.setTflag("top");
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List lii = new ArrayList();
            lii.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(lii);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
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

            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            } else {
                List<TreeNode> son = dataCatalogService.getMetaDataDBTableTree();

                // ????????????????????????
                List<Map> tables = dataCatalogService.getTableByUser(roleId);
                for (int i = 0; i < son.size(); i++) {
                    boolean isNull = true;
                    TreeNode node = son.get(i);
                    List<TreeNode> child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        boolean flag = false;
                        // ??????????????????
                        for (int k = 0; k < tables.size(); k++) {
                            String table = tables.get(k).get("tableid").toString();
                            if (child.get(j).getId().equals(table)) {
                                flag = true;
                                isNull = false;
                                break;
                            }
                        }
                        // ????????????
                        if (!flag) {
                            child.remove(j);
                            j = j - 1;
                        }
                    }
                    // ????????????
                    if (isNull) {
                        son.remove(i);
                        i = i - 1;
                    }
                }

                // ????????????  
                top.setChildren(son);
                top.setState("closed");
                trees.add(top);
            }
        }
        return JsonUtils.toJson(trees);
    }


    /**
     * ??????????????????
     *
     * @param tablename
     * @param page
     * @param rows
     * @param key       ??????key
     * @return
     * @throws Exception
     */
    @RequestMapping("/listfromtable/{tablename}")
    public String listFromTable(@PathVariable("tablename") String tablename, int page, int rows, String key, String approveType, HttpServletRequest request) throws Exception {
        String retStr = "";
        // ?????????
        if (approveType == null || (approveType != null && "Y".equals(approveType))) {
            // ???????????????
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> data = dataCatalogService.getFieldByTable(lii);

            // ??????????????????
            String con = "";
            if (key != null && !"".equals(key)) {
                String[] ke = key.split("&");
                if (ke != null && ke.length > 0) {
                    for (String k : ke) {
                        String[] kk = k.split("=");
                        String name = kk[0];
                        if (kk != null && kk.length > 1) {
                            String value = kk[1];
                            // ??????????????????
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
                            if (isDate) {   // ????????????
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

            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List li1 = new ArrayList();
            li1.add(loginName);
            List<Map> list = dataCatalogService.getRoleInfo(li1);
            String roleName = "";
            String roleId = "";
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    if ("???????????????".equals(list.get(k).get("name").toString())) {
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

            // ????????????
            if ("???????????????".equals(roleName)) { //??????agcloud??????-->"???????????????".equals(roleName)
                // ???????????????
                if (!"".equals(con)) {
                    con = " ( " + con + " ) ";
                } else {
                    con = " ( 1=1 ) ";
                }
                retStr = dataCatalogService.listFromTable(tablename, page, rows, con);
            } else {
                // ??????Map
                Map<String, Object> distMap = new HashMap<String, Object>();
                distMap.put("440103", "?????????");
                distMap.put("440104", "?????????");
                distMap.put("440105", "?????????");
                distMap.put("440106", "?????????");
                distMap.put("440111", "?????????");
                distMap.put("440112", "?????????");
                distMap.put("440113", "?????????");
                distMap.put("440114", "?????????");
                distMap.put("440115", "?????????");
                distMap.put("440183", "?????????");
                distMap.put("440184", "?????????");

                // ?????????id
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

                // ?????? ?????? ??? ????????? ?????????
                List li3 = new ArrayList();
                li3.add(tableId);
                List<Map> dists = dataCatalogService.getDisById(roleId, li3);
                String dis = "";
                if (dists != null) {
                    for (int i = 0; i < dists.size(); i++) {
                        // ??????????????????????????????
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
                // ??????????????????

                // ????????????
                if (!"".equals(dis)) {
                    dis = "( " + dis + " )";
                } else {
                    dis = " ( 1=1 ) ";  // ????????? ???????????????????????????????????????????????????
                }

                // ???????????????
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
     * ????????????
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
     * ??????????????????(?????????)
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
     * ??????????????????
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
     * ????????????
     *
     * @return
     */
    @RequestMapping("/saveFromData")
    public String saveFromData(HttpServletRequest request) {
        try {
            String tflag = request.getParameter("tflag");
            if ("top".equals(tflag)) {  // ??????
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
            } else if ("db".equals(tflag)) { // ?????????
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
                    dataCatalogService.updateMetaDataTable(table); // ??????????????????????????????
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
            } else if ("table".equals(tflag)) { // ???

                //??????????????????????????????
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String cname = request.getParameter("cname");
                String description = request.getParameter("description");
                String type = request.getParameter("type");
                String constraint = request.getParameter("constraint");
                String unit = request.getParameter("unit");
                String dd = request.getParameter("dd");
                String editable = (request.getParameter("editable") != null && ("???".equals(request.getParameter("editable")) || "1".equals(request.getParameter("editable")))) ? "1" : "0";
                String visible = (request.getParameter("visible") != null && ("???".equals(request.getParameter("visible")) || "1".equals(request.getParameter("visible")))) ? "1" : "0";
                String dispsort = request.getParameter("dispsort");
                String prikey = (request.getParameter("prikey") != null && ("???".equals(request.getParameter("prikey")) || "1".equals(request.getParameter("prikey")))) ? "1" : "0";
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


                //?????????????????????????????????
                List lii = new ArrayList();
                lii.add(parentId);
                List<Map> dat = dataCatalogService.getTableInfo(lii);// ?????????id????????????
                String dbID = dat.get(0).get("databaseid").toString();
                String tableName = dat.get(0).get("name").toString();
                List dbli = new ArrayList();
                dbli.add(dbID);
                List<Map> re = dataCatalogService.getDBInfoById(dbli);
                String orlcUrl = re.get(0).get("url").toString();

                // ????????????
                if (id != null && !"".equals(id)) {
                    field.setId(id);


                    //?????????????????????????????????: ????????????????????????????????????????????????????????????????????????
                    //--???????????????????????????????????????
                    Map fieldInfo = dataCatalogService.getFieldInfoByFieldId(id);
                    String oldName = fieldInfo.get("name").toString();
                    MetaDataField oldField = dataCatalogDao.getMetaDataField("select * from AG_WA_METADATA_FIELD where name='" + oldName + "' and tableid='" + parentId + "'");

                    ArrayList<String>  sqlArr = new ArrayList();
                    if(!oldField.getName().equals(name)) {
                        sqlArr.add("alter table "+ tableName +" rename column "+oldField.getName()+" to "+ name );
                    }

                    if (oldField.getPrikey() == null || oldField.getPrikey() == "???") {
                        oldField.setPrikey("0");
                    }
                    if(!oldField.getPrikey().equals(prikey)) {
                        if (prikey.equals("1")){
                            sqlArr.add("alter table "+tableName+" add constraint "+oldName+" primary key("+oldName+")"); //????????????
                        }else {
                           // sqlArr.add("alter table "+tableName+" drop constraint ?????????"); //????????????
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

                    //???????????????
                    dataCatalogService.updateMetaDataField(field);

                } else { //????????????
                    field.setId(UUID.randomUUID().toString());
                    dataCatalogService.insertMetaDataField(field);

                    //?????????????????????????????????
                    dataCatalogService.alertTableDesc(orlcUrl, "alter table " + tableName + " add(" + name + " " + type + ")");
                }
            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "???????????????"));
    }

    /**
     * ??????????????????
     *
     * @param tflag
     * @param id
     * @return
     */
    @RequestMapping("/delFromData")
    public String delFromData(String tflag, String id, String parentId, String name) {
        try {
            if ("top".equals(tflag)) { // ??????
                dataCatalogService.delMetaDataDB(id);
            } else if ("db".equals(tflag)) { // ?????????
                //18-9-12
                //?????????????????????
                String tableName = name;
                String dbId = parentId;
                List dbli = new ArrayList();
                dbli.add(dbId);
                List<Map> re = dataCatalogService.getDBInfoById(dbli);
                String orlcUrl = re.get(0).get("url").toString();
                dataCatalogService.alertTableDesc(orlcUrl, "drop table " + tableName);
                dataCatalogService.delectMetaModify(tableName); //?????????????????????????????????
                dataCatalogService.delMetaDataTable(id);


                dataCatalogService.delMetaDataTable(id);
            } else if ("table".equals(tflag)) { // ???

           // 18-9-10
                //?????????????????????
                //1.????????????id,???????????????
                //2.?????????id, ????????????????????????id
                //3.???????????????id, ????????????????????????url
                Map fieldInfo = dataCatalogService.getFieldInfoByFieldId(id);
                String fieldName = fieldInfo.get("name").toString();
                List lii = new ArrayList();
                lii.add(parentId);
                List<Map> dat = dataCatalogService.getTableInfo(lii);// ?????????id????????????
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
        return JsonUtils.toJson(new ResultForm(false, "???????????????"));
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @RequestMapping("/getRelateData")
    public String getRelateData(String relateDataKey, String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // ???????????????
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

        // ???????????????url
        List lii = new ArrayList();
        lii.add(id);
        List<Map> re = dataCatalogService.getDBInfoById(lii);
        String url = re.get(0).get("url").toString();

        // ???????????????
        data = dataCatalogService.getRelateData(url, id, cond);

        return JSONArray.toJSONString(data);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @RequestMapping("/saveRelateData")
    public String saveRelateData(String tableIds, String dbId) {
        boolean flag = true;
        try {
            // ???????????????
            if (tableIds != null && !"".equals(tableIds)) {
                String[] tables = tableIds.split(",");
                for (int i = 0; i < tables.length; i++) {
                    String[] tab = tables[i].split("\\|");
                    String name = tab[0];
                    String cname = (tab[1] == null ? "" : tab[1]);

                    // ???????????????????????????????????????                    
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

                // ???????????????
                if (flag) {
                    for (int j = 0; j < tables.length; j++) {
                        String[] tab = tables[j].split("\\|");
                        String name = tab[0];
                        // ?????????????????????id                        
                        List lii = new ArrayList();
                        lii.add(name);
                        List<Map> data = dataCatalogService.getTableByName(lii);
                        String tableId = "";
                        if (data != null && data.size() > 0) {
                            tableId = data.get(0).get("id").toString();
                        }

                        // ??????????????????
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
                        // ????????????
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
        // ????????????
        if (flag) {
            return JsonUtils.toJson(new ResultForm(true));
        } else {
            return JsonUtils.toJson(new ResultForm(false, "???????????????"));
        }
    }

    /**
     * ?????????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFieldData")
    public String getFieldData(String tableId) throws Exception {
        List<Map> data = new ArrayList<Map>();

        // ?????????id????????????
        List lii = new ArrayList();
        lii.add(tableId);
        List<Map> dat = dataCatalogService.getTableInfo(lii);
        String tableName = "";
        if (dat != null && dat.size() > 0) {
            tableName = dat.get(0).get("name").toString();
        }

        // ??????url
        List li2 = new ArrayList();
        li2.add(tableId);
        List<Map> re = dataCatalogService.getDBById(li2);
        String url = re.get(0).get("url").toString();

        // ??????????????????
        data = dataCatalogService.getFieldData(url, tableName, tableId);
        return JSONArray.toJSONString(data);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping("/saveFieldData")
    public String saveFieldData(String fieldIds, String tableId) {
        try {
            // ???????????????
            if (fieldIds != null && !"".equals(fieldIds)) {
                String[] tables = fieldIds.split(",");
                for (int i = 0; i < tables.length; i++) {
                    String[] tab = tables[i].split("\\|");
                    String name = tab[0];
                    String description = (tab[1] == null ? "" : tab[1]);
                    String type = (tab[2] == null ? "" : tab[2]);
                    String constraint = tab[3];
                    String prikey = ("???".equals(tab[4]) || !tab[4].equals("null") ? "1" : "");
                    // ????????????
                    String id = "";
                    MetaDataField field = new MetaDataField();
                    // ?????????id,??????                    
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
                        // ????????????????????????
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
        return JsonUtils.toJson(new ResultForm(false, "???????????????"));
    }

    /**
     * ????????????--??????
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
     * ????????????
     *
     * @return
     */
    @RequestMapping("/getTableName")
    public String getTableName(String tflag, String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        if ("table".equals(tflag)) {  // ???
            // ?????????            
            List lii = new ArrayList();
            lii.add(id);
            data = dataCatalogService.getTableById(lii);
        }
        return JsonUtils.toJson(data);
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/saveData")
    public String saveData(HttpServletRequest request) {
        // ????????????

        ResultForm res = new ResultForm(false, "");
        try {
            String prikey = request.getParameter("prikey");
            String prifield = request.getParameter("prifield");
            String tablename = request.getParameter("tablename");

            // ?????????????????????
            if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
                // ??????????????????
                List lii = new ArrayList();
                lii.add(tablename);
                List<Map> data = dataCatalogService.getFieldCname(lii);


                List<Map> dat = new ArrayList<Map>();
                // ??????url
                List li2 = new ArrayList();
                li2.add(tablename);
                List<Map> re = dataCatalogService.getDBByName(li2);
                String url = re.get(0).get("url").toString();

                // ???????????????
                dat = dataCatalogService.findOrigValue(url, tablename, prifield, prikey);

                // ???????????????
                String field = "";
                String org = "";
                String mod = "";
                if (data != null && data.size() > 0) {
                    // json
                    field += "{";
                    org += "{";
                    mod += "{";
                    for (int i = 0; i < data.size(); i++) {
                        // ??????json
                        String name = data.get(i).get("name").toString();
                        String cname = data.get(i).get("cname") == null ? "" : data.get(i).get("cname").toString();
                        // ??????json
                        String val = "";
                        if (dat != null && dat.size() > 0) {
                            val = dat.get(0).get(name.toLowerCase()) == null ? "" : dat.get(0).get(name.toLowerCase()).toString();
                        }

                        // ?????????json
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
                // ??????????????????
                String loginName = LoginHelpClient.getLoginName(request);
                List li3 = new ArrayList();
                li3.add(loginName);
                List<Map> list = dataCatalogService.getOrgInfoByUser(li3);
                String isMod = "-1";
                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {
                        String[] deps = list.get(i).get("xpath").toString().split("/");
                        if ("?????????".equals(deps[deps.length - 1])) {
                            isMod = "0";
                        } else if ("??????????????????".equals(deps[deps.length - 1])) {
                            isMod = "-1";
                        } else {
                            isMod = "-1";
                        }
                    }
                }
                // ???????????????                
                List li4 = new ArrayList();
                li4.add(tablename);
                li4.add(prifield);
                li4.add(prikey);
                List<Map> li = dataCatalogService.getCheckData(li4);
                if (li != null && li.size() > 0) {  // ??????????????????
                    // ???????????????
                    MetaDataModify mo = new MetaDataModify();
                    mo.setId(li.get(0).get("id").toString());
                    mo.setTableName(tablename);
                    mo.setPriField(prifield);
                    mo.setPriValue(prikey);
                    mo.setFieldDescription(field);
                    mo.setOrigValue(org);
                    mo.setModiValue(mod);
                    mo.setLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    // ???????????????????????????
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
                        res.setMessage("????????????,???????????????...");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("???????????????");
                    }
                } else {
                    // ???????????????
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
                        res.setMessage("????????????,???????????????...");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("???????????????");
                    }
                }
            } else {
                res.setSuccess(false);
                res.setMessage("?????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.toJson(res);
    }

    /**
     * ??????????????????
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
        // ????????????
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
     * ????????????
     */
    @RequestMapping("/dataAuthor")
    public ModelAndView dataAuthor(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataAuthor");
    }

    /**
     * ????????????
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
                    // ???????????????
                    if ("1".equals(isedit)) {
                        for (int j = 0; j < distList.length; j++) {
                            String dist = distList[j];
                            // ?????????????????????????????????????????????
                            List lii = new ArrayList();
                            lii.add(tableid);
                            lii.add(role);
                            lii.add(dist);
                            List<Map> data = dataCatalogService.getUserDataByDist(lii);
                            if (data != null && data.size() > 0) {
                                dataCatalogDao.delAuthorData(data.get(0).get("id").toString());
                            }
                            // ??????
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
                    } else {  // ??????????????????
                        // ????????????????????????????????????
                        List lii = new ArrayList();
                        lii.add(tableid);
                        lii.add(role);
                        List<Map> data = dataCatalogService.getUserDataByUser(lii);
                        if (data != null && data.size() > 0) {
                            dataCatalogDao.delAuthorData(data.get(0).get("id").toString());
                        }
                        // ??????
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
            return JsonUtils.toJson(new ResultForm(false, "???????????????"));
        }
    }

    /**
     * ??????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAuthorData")
    public String getAuthorData(String tflag, String id, String key, int page, int rows, String roleId) throws Exception {
        // ??????????????????
        String str = dataCatalogService.getAuthorData(tflag, id, key, page, rows, roleId);
        return str;
    }

    /**
     * ??????????????????
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
            return JsonUtils.toJson(new ResultForm(false, "???????????????"));
        }
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/dataCheck")
    public ModelAndView dataCheck(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataCheck");
    }

    /**
     * ??????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCheckData")
    public String getCheckData(String tflag, String id, int page, int rows, String isAdd, String isModi, String isDel, HttpServletRequest request) throws Exception {
        // ??????????????????
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii);
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("???????????????".equals(list.get(k).get("name").toString())) {
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
        // ????????????         
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
        // ????????????
        String str = dataCatalogService.getCheckData(tflag, roleName, con, roleId, id, page, rows);
        return str;
    }

    /**
     * ??????????????????
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
     * ??????
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
        ResultForm res = new ResultForm(false, "????????????!");
        // ?????????
        String loginName = LoginHelpClient.getLoginName(request);
        String[] ta = tablenames.split(",");
        String[] fi = prifields.split(",");
        String[] va = privalues.split(",");
        String[] idd = id.split(",");
        if (ta != null && ta.length > 0) {
            // ????????????
            res = dataCatalogService.passCheckDataNo(type, view, loginName, ta, fi, va, idd);
        }
        return JsonUtils.toJson(res);
    }

    /**
     * ????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/passCheckData")
    public String passCheckData(String id, String type, String tablenames, String prifields, String privalues, HttpServletRequest request) throws Exception {
        ResultForm res = new ResultForm(false, "????????????!");
        // ?????????
        String loginName = LoginHelpClient.getLoginName(request);
        String[] ta = tablenames.split(",");
        String[] fi = prifields.split(",");
        String[] va = privalues.split(",");
        String[] idd = id.split(",");
        if (ta != null && ta.length > 0) {
            // ????????????
            res = dataCatalogService.passCheckData(type, loginName, ta, fi, va, idd);
        }
        return JsonUtils.toJson(res);
    }

    /**
     * ??????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/delData")
    public String delData(HttpServletRequest request, String tablename, String prikey, String prifield) throws Exception {
        ResultForm res = new ResultForm(false, null);
        // ?????????????????????
        if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> data = dataCatalogService.getFieldInfo(lii);
            List<Map> dat = new ArrayList<Map>();
            // ??????url
            List li2 = new ArrayList();
            li2.add(tablename);
            List<Map> re = dataCatalogService.getDBByName(li2);
            String url = re.get(0).get("url").toString();
            // ????????????
            dat = dataCatalogService.findOrigValue(url, tablename, prifield, prikey);
            // ??????
            String field = "";
            String org = "";
            if (data != null && data.size() > 0) {
                // json
                field += "{";
                org += "{";
                for (int i = 0; i < data.size(); i++) {
                    // ??????json
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

            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List li3 = new ArrayList();
            li3.add(loginName);
            List<Map> list = dataCatalogService.getOrgInfoByUser(li3);
            String isDel = "-1";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String[] compents = list.get(i).get("xpath").toString().split("/");
                    if ("?????????".equals(compents[compents.length - 1])) {
                        isDel = "0";
                    } else if ("??????????????????".equals(compents[compents.length - 1])) { //list.get(i).get("xpath").toString().split("/")[2]
                        isDel = "-1";
                    } else {
                        isDel = "-1";
                    }
                }
            }
            // ???????????????
            List li4 = new ArrayList();
            li4.add(tablename);
            li4.add(prifield);
            li4.add(prikey);
            List<Map> li = dataCatalogService.getModiByKey(li4);
            if (li != null && li.size() > 0) {
                // ???????????????
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
                    res.setMessage("??????????????????,???????????????...");
                } else {
                    res.setSuccess(false);
                    res.setMessage("???????????????");
                }
            } else {
                // ???????????????
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
                    res.setMessage("??????????????????,???????????????...");
                } else {
                    res.setSuccess(false);
                    res.setMessage("???????????????");
                }
            }
        } else {
            res.setSuccess(false);
            res.setMessage("????????????????????????");
        }
        return JsonUtils.toJson(res);
    }

    /**
     * ????????????????????????????????????
     * 2017-12-19 czh
     */
    @RequestMapping("/getMetaDataByTableName/{tableName}")
    public String getMetaDataByTableName(@PathVariable String tableName) {
        try {
            if (Common.isCheckNull(tableName)) return null;
            List list = (List) dataCatalogService.getMetaDataByTableName(tableName);
            return JsonUtils.toJson(new ContentResultForm<>(true, list.size() > 0 ? list.get(0) : null, "????????????"));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJson(new ResultForm(false));
        }
    }

    /**
     * ?????????????????????
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
        // ??????????????????
        if (tablename != null && !"".equals(tablename) && prifield != null && !"".equals(prifield) && prikey != null && !"".equals(prikey)) {
            // ?????????????????????
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

            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            List li2 = new ArrayList();
            li2.add(loginName);
            List<Map> list = dataCatalogService.getOrgInfoByUser(li2);
            String isAdd = "-1";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String[] array = list.get(i).get("xpath").toString().split("/");
                    if ("?????????".equals(array[array.length-1])) {
                        isAdd = "0";
                    } else if ("??????????????????".equals(array[array.length-1])) {
                        isAdd = "-1";
                    } else {
                        isAdd = "-1";
                    }
                }
            }

            // ???????????????
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
                res.setMessage("????????????,??????????????????");
            } else {
                res.setSuccess(false);
                res.setMessage("???????????????");
            }
        } else {
            res.setSuccess(false);
            res.setMessage("????????????????????????");
        }
        return JsonUtils.toJson(res);
    }

    /**
     * ??????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyData")
    public String modifyData(String tablename, String prifield, String prikey) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // ?????????????????????
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
            // ????????????
            map.put("tablename", tablename);
            map.put("prifield", prifield);
            map.put("prikey", prikey);
            data.add(map);
        } else {
            // ??????url
            List li2 = new ArrayList();
            li2.add(tablename);
            List<Map> re = dataCatalogService.getDBByName(li2);
            String url = re.get(0).get("url").toString();

            // ????????????
            data = dataCatalogService.findData(url, tablename, prifield, prikey);
        }

        return JsonUtils.toJson(data);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCheckTableInfo")
    public String getCheckTableInfo(String tableid, String checktype, String start_time, String end_time, String result) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String[] checktypes = checktype.split(",");

        // ????????????
        data = dataCatalogService.getCheckTableInfo(checktypes, tableid, start_time, end_time, result);
        return JsonUtils.toJson(data);
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/checkRecord")
    public ModelAndView checkRecord(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/checkRecord");
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/dataReport")
    public ModelAndView dataReport(HttpServletRequest request, Model model) {
        return new ModelAndView("agcom/datacatalog/dataReport");
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping("/getCheckDataNew")
    public String getCheckDataNew(String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result, HttpServletRequest request) throws Exception {
        List<Map> data = new ArrayList<Map>();

        // ??????????????????
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii); //??????agcloud??????-->"???????????????".equals(roleName)
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("???????????????".equals(list.get(k).get("name").toString())) {
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

        // ????????????
        String str = dataCatalogService.getCheckDataNew(roleName, roleId, checktype, start_time, end_time, tflag, tableid, page, rows, result); //??????agcloud??????-->"???????????????".equals(roleName)
        return str;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping("/getReportDataNew")
    public String getReportDataNew(String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result, HttpServletRequest request) throws Exception {
        // ??????????????????
        String loginName = LoginHelpClient.getLoginName(request);
        List lii = new ArrayList();
        lii.add(loginName);
        List<Map> list = dataCatalogService.getRoleInfo(lii); //??????agcloud??????-->"???????????????".equals(roleName)
        String roleName = "";
        String roleId = "";
        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                if ("???????????????".equals(list.get(k).get("name").toString())) {
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

        // ???????????? 
        String str = dataCatalogService.getReportDataNew(roleName, roleId, start_time, end_time, checktype, tflag, tableid, page, rows, result);//??????agcloud??????-->"???????????????".equals(roleName)
        return str;
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping("/tempList")
    public ModelAndView tempList(HttpServletRequest request, Model model) {
        return new ModelAndView("awater/datacatalog/tempList");
    }

    /**
     * ????????????
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
     * ????????????
     *
     * @return
     */
    @RequestMapping("/saveTemp")
    public String saveTemp(String cname, String name, String field, String dbId) {
        ResultForm res = new ResultForm(false, null);

        if (name != null && !"".equals(name) && cname != null && !"".equals(cname) && field != null && !"[]".equals(field)) {
            // ????????????
            String msg = "";
            try {
                // ??????url
                List lii = new ArrayList();
                lii.add(dbId);
                List<Map> re = dataCatalogService.getDBInfoById(lii);
                String url = re.get(0).get("url").toString();

                // ????????????
                msg = dataCatalogService.saveTemp(url, field, name, cname);

                if ("".equals(msg)) {
                    // ??????????????????
                    MetaDataTemp temp = new MetaDataTemp();
                    temp.setId(UUID.randomUUID().toString());
                    temp.setTable_name(name);
                    temp.setTemp_name(cname);
                    temp.setTemp_dir("WEB-INF\\ui-jsp\\awater\\datacatalog\\temp");
                    temp.setLast_update(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    int y = dataCatalogDao.insertMetaDataTemp(temp);
                    if (y > 0) {
                        res.setSuccess(true);
                        res.setMessage("???????????????");
                    } else {
                        res.setSuccess(false);
                        res.setMessage("?????????????????????");
                    }
                } else {
                    res.setSuccess(false);
                    res.setMessage("??????????????????????????????????????????????????????" + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.setSuccess(false);
                res.setMessage("??????????????????????????????????????????????????????" + e.getMessage());
            }
        } else {
            res.setSuccess(false);
            res.setMessage("????????????????????????");
        }

        return JsonUtils.toJson(res);
    }

    /**
     * ??????????????????
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
     * ????????????
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
                    res.setMessage("???????????????????????????");
                } else {
                    res.setSuccess(false);
                    res.setMessage("???????????????????????????");
                    // ????????????
                    break;
                }
            }
        }
        return JsonUtils.toJson(res);
    }

    /**
     * ????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/downTemp/{id}")
    public void downTemp(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws Exception {
        // ??????????????????
        List lii = new ArrayList();
        lii.add(id);
        List<Map> list = dataCatalogService.getTempInfoById(lii);
        String fileName = "";
        if (list != null && list.size() > 0) {
            fileName += list.get(0).get("temp_name") + ".xlsx";
        }

        // ?????????????????????
        List li = new ArrayList();
        li.add(list.get(0).get("table_name").toString().toUpperCase());
        List<Map> fields = dataCatalogService.getFieldByTable(li);

        // ??????Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("????????????");
        // ??????
        HSSFRow row = sheet.createRow(0);
        HSSFRow ag = sheet.createRow(1);
        for (int i = 0; i < fields.size(); i++) {
            Map map = fields.get(i);
            // ??????
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(String.valueOf(map.get("cname")));
            // ??????
            HSSFCell agCell = ag.createCell(i);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor((short) 13);// ???????????????
            if ("DATE".equals(String.valueOf(map.get("type"))) || "date".equals(String.valueOf(map.get("type")))) {
                agCell.setCellValue("???: ???-???-??? ???:???:???");
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
                agCell.setCellValue("?????????????????????????????????????????????" + key);
                agCell.setCellStyle(style);
            }
        }

        // ??????
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

        // ?????????
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
        workbook.close();
    }

    /**
     * ????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadData/{id}")
    public String uploadData(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        ResultForm res = new ResultForm(false, null);
        boolean flag = true;
        boolean lostKey = true;
        // ??????????????????
        List lii = new ArrayList();
        lii.add(id);
        List<Map> list = dataCatalogService.getTempInfoById(lii);

        // ???????????????
        List li = new ArrayList();
        li.add(list.get(0).get("table_name").toString().toUpperCase());
        List<Map> fields = dataCatalogService.getFieldByTable(li);

        String tempDir = "";
        if (list != null && list.size() > 0) {
            tempDir += list.get(0).get("temp_dir");
        }
        // ??????????????????
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

            // ????????????
            InputStream is = null;
            try {
                // 2007??????
                is = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(is);
                HSSFSheet sheet = workbook.getSheetAt(0);

                // ?????????
                Map map = new HashMap();

                // ???????????????
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

                // ?????????
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

                        // ????????????
                        if ("1".equals(fields.get(e).get("prikey"))) {
                            prifie = name;
                            prival = value;
                        }

                        // ???
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

                    // ??????????????????
                    String loginName = LoginHelpClient.getLoginName(request);
                    List li2 = new ArrayList();
                    li2.add(loginName);
                    List<Map> lis = dataCatalogService.getOrgInfoByUser(li2);
                    String isAdd = "-1";
                    if (lis != null && lis.size() > 0) {
                        for (int ii = 0; ii < list.size(); ii++) {
                            String[] deps = list.get(ii).get("xpath").toString().split("/");
                            if ("?????????".equals(deps[deps.length - 1])) {
                                isAdd = "0";
                            } else if ("??????????????????".equals(deps[deps.length - 1])) {
                                isAdd = "-1";
                            } else {
                                isAdd = "-1";
                            }
                        }
                    }

                    // ?????????????????????????????????
                    if (!"".equals(prival)) {
                        // ???????????????
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
                    // 2013??????
                    is = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(is);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    // ?????????
                    Map map = new HashMap();

                    // ???????????????
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

                    // ?????????
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

                            // ????????????
                            if ("1".equals(fields.get(t).get("prikey"))) {
                                prifie = name;
                                prival = value;
                            }

                            // ???
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

                        // ??????????????????
                        String loginName = LoginHelpClient.getLoginName(request);
                        List li3 = new ArrayList();
                        li3.add(loginName);
                        List<Map> lis = dataCatalogService.getOrgInfoByUser(li3);
                        String isAdd = "-1";
                        if (lis != null && lis.size() > 0) {
                            for (int ii = 0; ii < list.size(); ii++) {
                                String[] deps = list.get(ii).get("xpath").toString().split("/");
                                if ("?????????".equals(deps[deps.length - 1])) {
                                    isAdd = "0";
                                } else if ("??????????????????".equals(deps[deps.length - 1])) {
                                    isAdd = "-1";
                                } else {
                                    isAdd = "-1";
                                }
                            }
                        }

                        // ??????????????????????????????
                        if (!"".equals(prival)) {
                            // ???????????????
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
            res.setMessage("???????????????????????????");
        } else {
            if (lostKey) {
                res.setSuccess(false);
                res.setMessage("???????????????????????????????????????????????????");
            } else {
                res.setSuccess(false);
                res.setMessage("??????????????????????????????????????????????????????????????????????????????");
            }
        }

        return JsonUtils.toJson(res);
    }

    /**
     * ??????????????????????????????
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/isTempTable")
    public String isTempTable(String id) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // ?????????
        List lii = new ArrayList();
        lii.add(id);
        List<Map> da = dataCatalogService.getTempInfo(lii);

        // ?????????        
        List li2 = new ArrayList();
        li2.add(id);
        List<Map> dat = dataCatalogService.getTableInfo(li2);

        // ????????????????????????
        if (da == null || da.size() == 0) {
            // ??????????????????
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
     * ??????
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/upData")
    public String upData(String ids) throws Exception {
        ResultForm res = new ResultForm(false, "???????????????");

        if (ids != null && !"".equals(ids)) {
            res = dataCatalogService.upData(ids);
        }

        return JsonUtils.toJson(res);
    }

    /**
     * ????????????
     *
     * @param url
     * @return
     * @throws Exception
     */
    @RequestMapping("/connectTest")
    public String connectTest(String url) throws Exception {
        ResultForm res = new ResultForm(false, "???????????????");
        if (url != null && !"".equals(url)) {
            res = dataCatalogService.connectTest(url);
        } else {
            res.setMessage("????????????????????????");
            res.setSuccess(false);
        }

        return JsonUtils.toJson(res);
    }

    /**
     * ?????????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/findDataRecords")
    public String findDataRecords(HttpServletRequest request, String loginName) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // String loginName = LoginHelpClient.getLoginName(request);

        // ???????????????????????????
        List li = new ArrayList<>();
        li.add(loginName);
        List<Map> da = dataCatalogService.getRoleInfoByUser(li);
        if (da != null && da.size() > 0) {
            List<Map> dat = dataCatalogService.findDataRecords();
            for (int i = 0; i < dat.size(); i++) {
                Map map = dat.get(i);
                // ???????????????
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
     * ????????????????????????
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
     * ???????????????id?????????????????????
     *
     * @param datebaseId
     * @return
     * @throws Exception
     */
    @RequestMapping("/createAccountingReport/{datebaseId}")
    public String createAccountingReport(@PathVariable("datebaseId") String datebaseId) {
        ResultForm res = new ResultForm(false, null);

        long startTime = System.currentTimeMillis();    //??????????????????
        String reportStr = "?????????????????????";
        boolean success = false;

        try {
            reportStr = dataCatalogService.createAccountingReport(datebaseId);
            success = true;
        } catch (Exception e) {
            reportStr = "????????????????????????";
            e.printStackTrace();

        }
        res.setSuccess(success);
        res.setMessage(reportStr);

        long endTime = System.currentTimeMillis();    //??????????????????
        System.out.println("1---?????????????????????" + (endTime - startTime) + "ms");    //????????????????????????
        return JsonUtils.toJson(res);
    }

    @RequestMapping("/getAccountingReport/{datebaseId}")
    public String getAccountingReport(@PathVariable("datebaseId") String datebaseId) {
        Map res = new HashMap();
        long startTime = System.currentTimeMillis();    //??????????????????
        Object data;
        boolean success = false;
        try {
            data = dataCatalogService.getAccountingReport(datebaseId);
            success = true;
        } catch (Exception e) {
            data = "?????????????????????????????????????????????";
            e.printStackTrace();
        }
        res.put("success", success);
        res.put("content", data);

        long endTime = System.currentTimeMillis();    //??????????????????
        System.out.println("1---?????????????????????" + (endTime - startTime) + "ms");    //????????????????????????
        return JsonUtils.toJson(res);
    }

    /**
     * ????????????????????????
     *
     * @param datebaseId
     * @return
     * @throws Exception
     */
    @RequestMapping("/countTableByDBId/{datebaseId}")
    public String countTableByDBId(@PathVariable("datebaseId") String datebaseId) throws Exception {
        long startTime = System.currentTimeMillis();    //??????????????????
        Map map = dataCatalogService.countTableByDBId(datebaseId);
        long endTime = System.currentTimeMillis();    //??????????????????
        System.out.println("1---?????????????????????" + (endTime - startTime) + "ms");    //????????????????????????
        return JsonUtils.toJson(map);
    }

    /**
     * ?????????????????? ????????????
     */
    @RequestMapping("/countRecordByTable/{tname}")
    public String countRecordByTable(@PathVariable("tname") String tname) throws Exception {
        long startTime = System.currentTimeMillis();    //??????????????????
        Map map = dataCatalogService.countRecordByTable(tname);
        long endTime = System.currentTimeMillis();    //??????????????????
        System.out.println("2---?????????????????????" + (endTime - startTime) + "ms");    //????????????????????????
        return JsonUtils.toJson(map);
    }

    /**
     * ??????????????????????????? ?????????????????????????????????
     */
    @RequestMapping("/countRecordByTableAndTypeColumn")
    public String countRecordByTableAndTypeColumn(HttpServletRequest request, String tname, String cname, String where) throws Exception {
        long startTime = System.currentTimeMillis();    //??????????????????

        Map map = dataCatalogService.countRecordByTableColumn(tname, cname, where);

        long endTime = System.currentTimeMillis();    //??????????????????
        System.out.println("2---?????????????????????" + (endTime - startTime) + "ms");    //????????????????????????
        return JsonUtils.toJson(map);
    }
}
