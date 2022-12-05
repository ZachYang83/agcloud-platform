package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Augurit on 2017-05-22.
 */
@SuppressWarnings("unchecked")
public class DirTreeUtil {
    /**
     * 获取父节点菜单
     * @param treesList 所有树菜单集合
     * @return
     */
    public final static List<DirTree> getFatherNode(List<DirTree> treesList){
        List<DirTree> newTrees = new ArrayList<DirTree>();
        for (DirTree node : treesList) {
            if (StringUtils.isEmpty(node.getPid())) {//如果pid为空，则该节点为父节点
                if(HasChildren(node.getId(),treesList)){
                    //递归获取父节点下的子节点
                    if (node.getNodes() != null) {
                        List list = node.getNodes();
                        list.addAll(getChildrenNode(node.getId(), treesList));
                        node.setNodes(list);
                    } else {
                        node.setNodes(getChildrenNode(node.getId(), treesList));
                    }
                }
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
    public final static List<DirTree> getChildrenNode(String pid, List<DirTree> treesList){
        List<DirTree> newTrees = new ArrayList<DirTree>();
        for (DirTree node : treesList) {
            if (StringUtils.isEmpty(node.getPid())) continue;
            if(node.getPid().equals(pid)){
                if(HasChildren(node.getId(),treesList)){
                    //递归获取子节点下的子节点，即设置树控件中的children
                    if (node.getNodes() != null) {
                        List list = node.getNodes();
                        list.addAll(getChildrenNode(node.getId(), treesList));
                        node.setNodes(list);
                    } else {
                        node.setNodes(getChildrenNode(node.getId(), treesList));
                    }
                }
                newTrees.add(node);
            }
        }
        return newTrees;
    }

    public final static boolean HasChildren(String id, List<DirTree> treesList){
        for (DirTree node : treesList){
            if (StringUtils.isEmpty(node.getPid())) continue;
            if(node.getPid().equals(id)){
                return true;
            }
        }

        return false;
    }

    /**
     * 递归删除没有图层的目录节点
     * @param treesList
     */
    public final static void deleteTree(List<DirTree> treesList){
        Iterator<DirTree> iterator = treesList.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            if (next instanceof DirTree){
                List<DirTree> treeNodes = ((DirTree)next).getNodes();
                if (null == treeNodes || treeNodes.size() == 0){
                    treesList.remove(next);
                    iterator = treesList.iterator();
                }else {
                    deleteTree(treeNodes);
                }
            }
        }
    }
}
