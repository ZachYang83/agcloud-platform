package com.augurit.agcloud.agcom.agsupport.common.util;

import com.common.util.Common;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by chendingxing on 2017-12-26.
 */
public class JsonTreeUtil {

    //存放每个专题的一整棵json树，用于返回给前端
    private static Map<String,JSONArray> treeJsons = new ConcurrentHashMap<>();
    private static Object lock = new Object();//用于同步的锁
    /**
     * 根据专题名称获取专题文件的内容
     * @param projectPath
     * @param projectName
     * @return json字符串，不能操作，只能直接返回给前台
     */
    public static String getProject(String projectPath,String projectName){
        return getTreeJsonarray(projectPath,projectName).toString();
    }

    /**
     * 根据专题名称获取专题文件的内容
     * @param projectPath
     * @param projectName
     * @return jsonarray对象，可以进行相关操作
     */
    private static JSONArray getTreeJsonarray(String projectPath,String projectName){
        JSONArray treeJson = null;
        if(treeJsons.containsKey(projectName)){
            treeJson = treeJsons.get(projectName);
        }else {
            //组装文件路径
            projectPath = getProjectPath(projectPath,projectName);
            //获取json文件的内容（字符串形式）
            String json = getTreeJson(projectPath);
            //转为json对象
            treeJson = JSONArray.fromObject(json);
            treeJsons.put(projectName,treeJson);
        }
        return treeJson;
    }
    /**
     * 创建专题
     * @param projectPath
     * @param projectName
     * @param mapParamId
     * @return
     */
    public static String addProject(String projectPath,String projectName,String mapParamId,String owner,int projectOrder){
        //添加节点到指定目录下，返回新的json字符串
        String treeJson = addTreeJson(projectPath,projectName,mapParamId,owner,projectOrder);
        return treeJson;
    }

    /**
     * 更新专题
     * @param projectPath
     * @param projectName
     * @param oldProjectName
     * @param mapParamId
     * @return
     */
    public static String updateProject(String projectPath,String projectName,String oldProjectName,String mapParamId,int projectOrder){
        String oldPath = projectPath + oldProjectName + ".json";
        String pName = oldProjectName;
        checkProject(projectPath,oldProjectName);
        JSONArray tree = treeJsons.get(oldProjectName);//是一棵包含一个节点的树
        JSONObject root = tree.getJSONObject(0);
        boolean hasChange = false;
        if(!Common.isCheckNull(mapParamId) && !root.get("mapParamId").equals(mapParamId)){
            root.put("mapParamId",mapParamId);
            hasChange = true;
        }
        if(!Common.isCheckNull(projectOrder) && root.getInt("projectOrder") != projectOrder){
            //同步更新其他专题序号,先更新其他专题的序号，再设置当前专题的序号
            updateProjectOrder(projectPath,oldProjectName,projectOrder,false);
            root.put("projectOrder",projectOrder);
            hasChange = true;
        }
        if(!Common.isCheckNull(projectName) && !oldProjectName.equals(projectName)){
            root.put("text",projectName);
            JSONArray jsonTree = treeJsons.remove(oldProjectName);
            treeJsons.put(projectName,jsonTree);
            String newPath = getProjectPath(projectPath,projectName);
            //保存到新的专题json文件，并删除旧文件
            saveJsonToFile(jsonTree,newPath);
            deleteFile(oldPath);
            pName = projectName;
        }else{
            if(hasChange){
                JSONArray jsonTree = treeJsons.get(oldProjectName);
                saveJsonToFile(jsonTree,oldPath);
            }
        }
        return pName;
    }

    /**
     * 删除专题
     * @param path
     * @param projectName
     */
    public static void deleteProject(String path,String projectName){
        treeJsons.remove(projectName);
        String fileName = getProjectPath(path,projectName);
        deleteFile(fileName);
    }

    /**
     * 添加目录
     * @param projectPath
     * @param projectName
     * @param id
     * @param dirName
     * @return
     */
    public static String addDir(String projectPath,String projectName,String id, String dirName){
        checkProject(projectPath,projectName);
        //新增节点，并将节点添加到内存中
        String nodeId = addTreeNode(projectName,id,dirName);
        //保存整棵树到文件中
        projectPath = getProjectPath(projectPath,projectName);
        saveJsonToFile(treeJsons.get(projectName),projectPath);
        return nodeId;
    }

    /**
     * 更新目录
     * @param projectPath
     * @param projectName
     * @param id
     * @param dirName
     * @return
     */
    public static String updateDir(String projectPath,String projectName,String id, String dirName){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree,id);
        aa.put("text",dirName);
        //保存整棵树到文件中
        projectPath = getProjectPath(projectPath,projectName);
        saveJsonToFile(treeJsons.get(projectName),projectPath);
        return null;
    }

    /**
     * 删除目录
     * @param projectPath
     * @param projectName
     * @param id
     */
    public static void deleteDir(String projectPath,String projectName,String id){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree, id);
        JSONObject node = findJsonTreeNode(tree, aa.getString("pid"));
        JSONArray children = node.getJSONArray("children");
        for(int i=0; i<children.size(); i++){
            if(id.equals(children.getJSONObject(i).getString("id"))){
                children.remove(i);
                break;
            }
        }
        if(children.size() == 0){
            node.remove("state");
        }
        //保存整棵树到文件中
        projectPath = getProjectPath(projectPath,projectName);
        saveJsonToFile(treeJsons.get(projectName),projectPath);
    }

    /**
     * 获取专题列表
     * @param projectPath
     * @return
     */
    public static List<Map> getTreeJsonList(String projectPath){
        List<Map> list = new ArrayList<>();
        try {
            projectPath = URLDecoder.decode(projectPath, "utf-8");
            File file = new File(projectPath);
            if(file.exists() && file.isDirectory()){
                String[] fileNames = file.list();
                for (String fn : fileNames) {
                    Map map = new HashMap();
                    String projectName = fn.replace(".json","");
                    //后期json树，没有则顺带初始化
                    JSONArray treeJsonarray = getTreeJsonarray(projectPath, projectName);
                    JSONObject tree = treeJsonarray.getJSONObject(0);
                    map.put("id",projectName);
                    map.put("name",projectName);
                    map.put("value",projectName);
                    map.put("owner",tree.getString("owner"));
                    map.put("roleIds",tree.get("roleIds"));
                    map.put("mapParamId",tree.get("mapParamId"));
                    map.put("projectOrder",tree.get("projectOrder"));
                    list.add(map);
                }
                //根据projectOrder排序,升序，即1在第一个
                Collections.sort(list, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        return (int)o1.get("projectOrder") - (int)o2.get("projectOrder");
                    }
                });
            }else{
                file.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 格式化json字符串
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\'){
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    /**
     * 递归获取JSONArray中的对象到list中
     * @param jsonArray
     * @param map
     */
    public static void getMapByJSONArray(JSONArray jsonArray, Map<String,JSONObject> map){
        for(int i=0; i<jsonArray.size(); i++){
            Object o = jsonArray.get(i);
            if(Common.isCheckNull(o)){
                continue;
            }
            if(o instanceof JSONArray) {
                JSONArray ja = (JSONArray)o;
                getMapByJSONArray(ja,map);
            }else{
                JSONObject jo = (JSONObject)o;
                map.put(jo.getString("id"),jo);//加入到列表中
                if(jo.containsKey("children")){
                    Object joo = jo.get("children");
                    if(joo instanceof JSONArray){
                        JSONArray children = (JSONArray)joo;
                        getMapByJSONArray(children,map);
                    }
                }
            }
        }
    }
    /**
     * 遍历树，根据id查找到节点，并根据修改类型进行相应操作
     * @param jsonArray
     * @param id
     */
    public static JSONObject findJsonTreeNode(JSONArray jsonArray,String id){
        for(int i=0; i<jsonArray.size(); i++){
            Object o = jsonArray.get(i);
            if(Common.isCheckNull(o)){
                continue;
            }
            JSONObject jo = (JSONObject)o;
            if(id.equals(jo.getString("id"))){
                return jo;
            }else{
                if(jo.containsKey("children")){
                    Object joo = jo.get("children");
                    if(joo instanceof JSONArray){
                        JSONArray children = (JSONArray)joo;
                        JSONObject temp = findJsonTreeNode(children,id);
                        if (temp != null) return temp;
                    }
                }
            }
        }
        return null;
    }
    /**
     * 获取树的所有节点map
     * @param projectPath
     * @param projectName
     * @return
     */
    public static Map<String,JSONObject> getJsonTreeNodes(String projectPath,String projectName){
        checkProject(projectPath,projectName);
        //获取json文件的内容并转为json对象
        JSONArray jsonArray = treeJsons.get(projectName);
        Map<String,JSONObject> map = new HashMap<>();
        getMapByJSONArray(jsonArray,map);
        return map;
    }
    /**
     * 读取json文件，获取专题树的json字符串格式数据
     * @param path
     * @return
     */
    public static String getTreeJson(String path){
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        try {
            InputStream in = new FileInputStream(file);
            Scanner scanner = new Scanner(in, "utf-8");
            while(scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
//    public static String getTreeJson(String projectName){
//        String filePath = "/project/" + projectName + ".json";
//        InputStream in = JsonTreeUtil.class.getResourceAsStream(filePath);
//        if(in == null){
//            return null;
//        }
//        StringBuilder buffer = new StringBuilder();
//        Scanner scanner = new Scanner(in, "utf-8");
//        while(scanner.hasNextLine()) {
//            buffer.append(scanner.nextLine());
//        }
//        scanner.close();
//        return buffer.toString();
//    }
    /**
     * 增加json树文件，如果文件已经存在则返回空，不能创建
     * @param projectName
     * @param mapParamId
     * @return
     */
    public static String addTreeJson(String path,String projectName,String mapParamId,String owner,int projectOrder){
        String treeJson = null;
        String fileName = projectName + ".json";
        try {
            path = URLDecoder.decode(path,"utf-8");
            boolean hasProject = isExistProject(path,fileName);//是否已经存在相同名称的专题，存在则不创建
            if(!hasProject){
                String tempFile = path + fileName;
                JSONObject root = new JSONObject();
                root.put("id","root");
                root.put("text",projectName);
                root.put("iconCls","icon-folder");
                root.put("mapParamId",mapParamId);
                root.put("owner",owner);
                root.put("projectOrder",projectOrder);
                root.put("roleIds",new ArrayList<>());
                root.put("layerIds",new ArrayList<>());
                root.put("children",new JSONArray());
                JSONArray jsonArr = new JSONArray();
                jsonArr.add(root);
                treeJson = jsonArr.toString();
                saveJsonToFile(jsonArr,tempFile);//持久化当前专题
                updateProjectOrder(path,projectName,projectOrder,true);//更新所有专题序号
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeJson;
    }

    /**
     * 根据专题序号遍历专题列表看是否存在相同序号，存在则序号大于等于这个序号的专题顺序顺延1，并持久化
     * @param path 专题存放目录
     * @param projectName 当前专题名称
     * @param projectOrder 当前专题要修改的目标序号
     * @param isAddProject true是添加专题 false是更新专题
     */
    private static void updateProjectOrder(String path,String projectName,int projectOrder,boolean isAddProject){
        File file = new File(path);
        if(file.exists() && file.isDirectory()){
            String[] fileNames = file.list();
            boolean harPorjectOrder = false;
            int currentProjectOrder = 1;
            for(String fn : fileNames){
                String pjn = fn.replace(".json", "");
                if(!pjn.equals(projectName)){//只遍历其他专题，判断是否序号重复
                    JSONArray tree = getTreeJsonarray(path, pjn);
                    int projectOrder1 = tree.getJSONObject(0).getInt("projectOrder");
                    if(projectOrder1 == projectOrder){
                        harPorjectOrder = true;
                    }
                }else{
                    //同时会将当前专题缓存起来
                    JSONArray tree = getTreeJsonarray(path, pjn);
                    currentProjectOrder = tree.getJSONObject(0).getInt("projectOrder");
                }
            }
            //如果存在则顺延
            if(harPorjectOrder){
                for(String fn : fileNames){
                    String pjn = fn.replace(".json", "");
                    if(!pjn.equals(projectName)) {//只遍历其他专题，更新序号
                        JSONArray tree = getTreeJsonarray(path, pjn);
                        int otherProjectOrder = tree.getJSONObject(0).getInt("projectOrder");
                        if(isAddProject){//新增专题时的更新
                            if (otherProjectOrder >= projectOrder) {
                                tree.getJSONObject(0).put("projectOrder", otherProjectOrder + 1);
                                saveJsonToFile(tree,path + fn);//持久化
                            }
                        }else{//更新专题时的更新
                            if(otherProjectOrder < currentProjectOrder && otherProjectOrder >= projectOrder){
                                tree.getJSONObject(0).put("projectOrder", otherProjectOrder + 1);
                                saveJsonToFile(tree,path + fn);//持久化
                            }else if(currentProjectOrder < projectOrder && otherProjectOrder <= projectOrder){
                                tree.getJSONObject(0).put("projectOrder", otherProjectOrder - 1);
                                saveJsonToFile(tree,path + fn);//持久化
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 检查是否已存在专题
     * @param path
     * @param fileName
     * @return
     */
    private static boolean isExistProject(String path,String fileName){
        File file = new File(path);
        if(file.exists() && file.isDirectory()){
            String[] fileNames = file.list();
            for(String fn : fileNames){
                if(fn.equals(fileName)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 将json字符串持久化到文件中
     * @param jsonArr
     * @param projectPath
     */
    public static void saveJsonToFile(JSONArray jsonArr,String projectPath){
        synchronized(lock){
            try {
                projectPath = URLDecoder.decode(projectPath, "utf-8");
                String treeJson = formatJson(jsonArr.toString());
                Common.writeUTFFile(projectPath,new StringBuffer(treeJson));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param path  最后带斜杠的
     * @param projectName 专题名称
     * @return
     */
    public static String getProjectPath(String path,String projectName){
        return path + projectName + ".json";
    }

    public static void deleteFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 创建目录节点，并返回节点
     * @param dirName
     * @return
     */
    public static String addTreeNode(String projectName,String id,String dirName){
        JSONArray tree = treeJsons.get(projectName);
        //拿到当前节点
        JSONObject node = findJsonTreeNode(tree,id);
        node.put("state","closed");
        //创建节点
        JSONObject subNode = new JSONObject();
        String nodeId = UUIDUtil.getUUID();
        subNode.put("id",nodeId);//本身id
        subNode.put("text",dirName);
        subNode.put("iconCls","icon-folder");
        subNode.put("pid",id);//父节点id
        subNode.put("layerIds",new ArrayList<>());
        subNode.put("children",new JSONArray());
        JSONArray children;
        if(node.containsKey("children")){
            Object obj = node.get("children");
            if(obj instanceof JSONArray){
                children = (JSONArray)obj;
                children.add(subNode);
            }else{
                children = new JSONArray();
                children.add(subNode);
                node.put("children",children);
            }
        }else{
            children = new JSONArray();
            children.add(subNode);
            node.put("children",children);
        }
        return nodeId;
    }

    /**
     * 添加指定list类型字段的数据
     * @param projectPath
     * @param projectName
     * @param id
     * @param ids
     */
    public static void addProjectIds(String projectPath,String projectName,String id,String ids,String field){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree,id);
        Object obj = aa.get(field);
        List tempIds = null;
        if(obj instanceof List){
            tempIds = (List)obj;
        }else{
            tempIds = new ArrayList();
        }
        String[] addIds = ids.split(",");
        for(String aid : addIds){
            if(!tempIds.contains(aid)){
                tempIds.add(aid);
            }
        }
        aa.put(field,tempIds);
        String path = getProjectPath(projectPath,projectName);
        saveJsonToFile(tree,path);
    }

    /**
     * 移除指定list类型字段的数据
     * @param projectPath
     * @param projectName
     * @param id
     * @param ids
     */
    public static void removeProjectIds(String projectPath,String projectName,String id,String ids,String field){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree,id);
        Object obj = aa.get(field);
        List layerIds = null;
        if(obj instanceof List){
            layerIds = (List)obj;
            String[] addIds = ids.split(",");
            for(String aid : addIds){
                if(layerIds.contains(aid)){
                    layerIds.remove(aid);
                }
            }
            aa.put("layerIds",layerIds);
            String path = getProjectPath(projectPath,projectName);
            saveJsonToFile(tree,path);
        }
    }

    /**
     * 获取指定list类型字段的值
     * @param projectPath
     * @param projectName
     * @param id
     * @param field
     * @return
     */
    public static List getProjectIds(String projectPath,String projectName, String id,String field){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree,id);
        Object obj = aa.get(field);
        List ids = null;
        if(obj instanceof List) {
            ids = (List) obj;
        }
        return ids;
    }

    /**
     * 移除内存中的json树
     * @param projectName
     */
    public static void removeTreeJson(String projectName){
        treeJsons.remove(projectName);
    }
    /**
     * 检查专题数据是否已经加载进内存，没有则加载
     * @param projectPath
     * @param projectName
     */
    private static void checkProject(String projectPath,String projectName){
        if(!treeJsons.containsKey(projectName)){
            getProject(projectPath,projectName);
        }
    }
    public static String updateTreeJson(Map<String,JSONObject> map,String id){
        if(map.containsKey(id)){
            JSONObject joo = map.get(id);
            JSONObject jooo = new JSONObject();
            JSONObject attribute = new JSONObject();
            attribute.put("mapParamId","5d8251fe-5e76-434c-9e67-dea3420e5490");
            jooo.put("id","ugcgcugkjckcyucu");
            jooo.put("text","hahaha");
            jooo.put("pid",id);
            jooo.put("attribute",attribute);
            jooo.put("iconCls","icon-folder");
            if(joo.containsKey("children")){
                JSONArray subc = joo.getJSONArray("children");
                if(subc == null){
                    subc = new JSONArray();
                }
                subc.add(jooo);
            }else{
                JSONArray children = new JSONArray();
                children.add(jooo);
                joo.put("children",children);
            }
        }
        return null;
    }
    public static void deleteTreeJson(String projectName){
        String filePath = "/project/";
        String fileName = projectName + ".json";
        String path = JsonTreeUtil.class.getResource(filePath).toString().replaceAll("^file:/", "");
        try {
            path = URLDecoder.decode(path,"utf-8") + fileName;
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void getLayerIds(String projectPath,String projectName,String id,String ids,String field){
        checkProject(projectPath,projectName);
        JSONArray tree = treeJsons.get(projectName);
        JSONObject aa = findJsonTreeNode(tree,id);
        Object obj = aa.get(field);
        List tempIds = null;
        if(obj instanceof List){
            tempIds = (List)obj;
        }else{
            tempIds = new ArrayList();
        }
        String[] addIds = ids.split(",");
        for(String aid : addIds){
            if(!tempIds.contains(aid)){
                tempIds.add(aid);
            }
        }
        aa.put(field,tempIds);
        String path = getProjectPath(projectPath,projectName);
        saveJsonToFile(tree,path);
    }

    public static void main(String[] args) {
        //1、读取成json对象
//        String projectName = "南沙区专题";
//        String json = getTreeJson(projectName);
//        JSONArray jsonArr = JSONArray.fromObject(json);
//        System.out.println(jsonArr.toString());
        //2、遍历
//        Map<String,JSONObject> map = new HashMap<>();
//        getMapByJSONArray(jsonArr,map);

        //3、修改
//        String id = "60127a65-fc1d-4ed5-93a7-5f84109a4585";
//        updateTreeJson(map,id);
        //4、保存
//        String haha = addTreeJson("南沙区专题", "123");
//        System.out.println(haha);
    }
}
