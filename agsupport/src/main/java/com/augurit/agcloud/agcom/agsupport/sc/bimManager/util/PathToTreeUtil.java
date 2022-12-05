package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;


import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 *
 * 根据字符串文件相对路径生成文件树结构
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public class PathToTreeUtil {
    public static String getType(String filename, String path) {
        if (path.endsWith(filename)) {
            return "file";
        }
        return "folder";
    }

    public static void addPath(HashMap<String, Object> root, String path) {
        String url = "";
        String[] pathArr = path.split("/");
        for (String name : pathArr) {
            url += "/" + name;
            boolean flag = true;
            for (HashMap<String, Object> node : (ArrayList<HashMap<String, Object>>) root.get("content")) {
                if (node.get("name").equals(name)) {
                    root = node;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                HashMap<String, Object> new_node = new HashMap<>();
                new_node.put("name", name);
                new_node.put("type", getType(name, path));
                new_node.put("url", url);
                new_node.put("content", new ArrayList<HashMap<String, Object>>());
                ((ArrayList<HashMap<String, Object>>) root.get("content")).add(new_node);
                root = new_node;
            }
        }
    }

    public static HashMap<String, Object> generate_data(String s) {
        String[] paths = s.split(",");
        HashMap<String, Object> root = new HashMap<>();
        root.put("name", "");
        root.put("url", "");
        root.put("type", "");
        ArrayList<String> arrayList = new ArrayList<>();
        root.put("content", arrayList);
        for (String path : paths) {
            addPath(root, path);
        }
        return root;
    }

    public static String readFile(String pathname) throws Exception {
        String str = "";
        File file = new File(pathname);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            // size  为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);


            str = new String(buffer, "GB2312");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }finally {
            try{
                if(in != null){
                    in.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return str;


    }


    /****
     * 1. 主分析程序，拿到路径总的字符串，进行分析
     * @param path
     * @return
     */
    public static JSONObject pathToTree(String path) {
        path = transFormChar(path);
        HashMap<String, Object> root = new HashMap<>();
        root = generate_data(path);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", root);
        return jsonObject;
    }

    /**
     * 字符串转译
     * @param path
     * @return
     */
    public static String transFormChar(String path) {
//        System.out.println(path);
        //将双引号去掉，将多余的空字符去掉
        path = path.replaceAll("\"", "").replaceAll(" ", "");

        /**
         * 把\\转成/
         */
        if (path.contains("\\\\")) {
            path = path.replaceAll("\\\\", "/");
            // path =c/a/bb//cc/dd//ee
            System.out.println("把\\\\转成/");
            System.out.println(path);
        }

        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
            System.out.println("把//变成/");
            System.out.println(path);
        }
        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
            System.out.println("再次把//变成/");
            System.out.println(path);
        }
        return path;
    }

    public static void main(String[] args) throws Exception {
        String str = "\"a//1.txt\",\"a/b\\\\2.txt\",\"a/b\\\\\\\\3.txt\",\"a/b/c/4.txt\",\"a/b/c/5.txt\"";
        System.out.println(pathToTree(str).toString());





    }

}

