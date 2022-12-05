package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-19.
 */
public class TreeUtil {
    /**
     * 获取父节点菜单
     * @param treesList 所有树菜单集合
     * @return
     */
    public final static List<Tree> getFatherNode(List<Tree> treesList){
        List<Tree> newTrees = new ArrayList<Tree>();
        for (Tree node : treesList) {
            if (StringUtils.isEmpty(node.getPid())) {//如果pid为空，则该节点为父节点
                if(HasChildren(node.getId(),treesList)){
                    //递归获取父节点下的子节点
                    if (node.getChildren() != null) {
                        List list = node.getChildren();
                        list.addAll(getChildrenNode(node.getId(), treesList));
                        node.setChildren(list);
                    } else {
                        node.setChildren(getChildrenNode(node.getId(), treesList));
                    }
                }
                //node.setState("closed");
                Map<String, Object> map = node.getAttributes();
                if (map == null) map = new HashMap<String, Object>();
                map.put("xpath", node.getXpath());
                node.setAttributes(map);
                newTrees.add(node);
            }
        }
        return newTrees;
    }
    public final static List<Tree> getFatherNode(String id,List<Tree> treesList){
        List<Tree> newTrees = new ArrayList<Tree>();
        for (Tree node : treesList) {
            if (StringUtils.isEmpty(node.getPid())&&node.getId().equals(id)) {//如果pid为空，则该节点为父节点
                if(HasChildren(node.getId(),treesList)){
                    //递归获取父节点下的子节点
                    if (node.getChildren() != null) {
                        List list = node.getChildren();
                        list.addAll(getChildrenNode(node.getId(), treesList));
                        node.setChildren(list);
                    } else {
                        node.setChildren(getChildrenNode(node.getId(), treesList));
                    }
                }
                //node.setState("closed");
                Map<String, Object> map = node.getAttributes();
                if (map == null) map = new HashMap<String, Object>();
                map.put("xpath", node.getXpath());
                node.setAttributes(map);
                newTrees.add(node);
            }
        }
        return newTrees;
    }

    /**
     * 递归获取子节点下的子节点
     * @param pid 父节点的ID
     * @param treesList 所有菜单树集合
     * @return
     */
    public final static List<Tree> getChildrenNode(String pid, List<Tree> treesList){
        List<Tree> newTrees = new ArrayList<Tree>();
        for (Tree node : treesList) {
            if (StringUtils.isEmpty(node.getPid())) continue;
            if(node.getPid().equals(pid)){
                if(HasChildren(node.getId(),treesList)){
                    //递归获取子节点下的子节点，即设置树控件中的children
                    if (node.getChildren() != null) {
                        List list = node.getChildren();
                        list.addAll(getChildrenNode(node.getId(), treesList));
                        node.setChildren(list);
                    } else {
                        node.setChildren(getChildrenNode(node.getId(), treesList));
                    }
                    //node.setState("closed");
                } else {
                    node.setIconCls("icon-folder");
                }
                //设置树控件attributes属性的数据
                Map<String, Object> map = node.getAttributes();
                if (map == null) map = new HashMap<String, Object>();
                map.put("xpath", node.getXpath());
                node.setAttributes(map);
                newTrees.add(node);
            }
        }
        return newTrees;
    }

    public final static boolean HasChildren(String id, List<Tree> treesList){
        for (Tree node : treesList){
            if (StringUtils.isEmpty(node.getPid())) continue;
            if(node.getPid().equals(id)){
                return true;
            }
        }

        return false;
    }
}
