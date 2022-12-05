package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.util;

import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: libc
 * @Description: 构建树结构集合工具类
 * @Date: 2020/11/5 10:18
 * @Version: 1.0
 */
public class TreeUtils {

    /**
     * List集合转为a-tree树状数据(调用方式一)
     *
     * @param list       需要转为树状的集合
     * @param toTreeNode 对象转为TreeNode节点Function
     * @return
     */
    public static <T> List<TreeNode<T>> getTreeList(List<T> list, Function<T, TreeNode<T>> toTreeNode) {
        //对象--封装-->树节点
        List<TreeNode<T>> treeNodeList = list.stream().map(toTreeNode).collect(Collectors.toList());
        return nodeToTree(treeNodeList);
    }


    /**
     * List集合转为a-tree树状数据(调用方式二)
     *
     * @param list      需要转为树状的集合
     * @param key       TreeNode的key对应对象的属性
     * @param title     TreeNode的title对应对象的属性
     * @param parentKey TreeNode的parentKey对应对象的属性
     * @return
     */
    public static <T> List<TreeNode<T>> getTreeList(List<T> list, Function<T, String> key, Function<T, String> title, Function<T, String> parentKey) {
        List<String> keys = list.stream().map(key).collect(Collectors.toList());
        List<String> titles = list.stream().map(title).collect(Collectors.toList());
        List<String> parentKeys = list.stream().map(parentKey).collect(Collectors.toList());
        //对象--封装-->树节点
        List<TreeNode<T>> treeNodeList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TreeNode<T> treeNode = new TreeNode<>(list.get(i), keys.get(i), titles.get(i), parentKeys.get(i));
            treeNodeList.add(treeNode);
        }
        return nodeToTree(treeNodeList);
    }


    /**
     * 把list节点封装为树状数据
     *
     * @param treeNodeList 需要转为tree树状的list集合
     * @param <T>
     * @return
     */
    public static <T> List<TreeNode<T>> nodeToTree(List<TreeNode<T>> treeNodeList) {
        //把list对象存储到map中，方便后面用于树状拼接
        Map<String, TreeNode<T>> treeNodeMap = treeNodeList.stream().collect(Collectors.toMap(TreeNode::getKey, i -> i, (v1, v2) -> v2));
        //连接树节点，封装为树状
        List<TreeNode<T>> data = new ArrayList<>();
        treeNodeList.forEach(val -> {
            String pk = val.getParentKey();
            TreeNode<T> parent = treeNodeMap.get(pk);
            if (parent == null) {
                //该节点为顶级节点，直接添加到返回数据中
                data.add(val);
            } else {
                //该节点有父节点，则需添加到其父节点的子节点中
                parent.getChildren().add(val);
            }
        });
        return data;
    }
}
