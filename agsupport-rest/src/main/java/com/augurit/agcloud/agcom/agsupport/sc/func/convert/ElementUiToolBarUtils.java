package com.augurit.agcloud.agcom.agsupport.sc.func.convert;

import com.augurit.agcloud.agcom.agsupport.domain.OpuRsToolBar;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElementUiToolBarUtils {
    public static List<ElementUiRsTreeNode> buildTree(List<OpuRsToolBar> funcs, boolean isNeedFullPath) {
        List<ElementUiRsTreeNode> treeNodes = new ArrayList();
        List<ElementUiRsTreeNode> rootNodes = getRootNodes(funcs, isNeedFullPath);
        if (rootNodes != null && rootNodes.size() > 0) {
            Iterator var4 = rootNodes.iterator();

            while(var4.hasNext()) {
                ElementUiRsTreeNode rootNode = (ElementUiRsTreeNode)var4.next();
                buildChildNodes(rootNode, funcs, isNeedFullPath);
                treeNodes.add(rootNode);
            }
        }

        return treeNodes;
    }

    public static void buildChildNodes(ElementUiRsTreeNode node, List<OpuRsToolBar> funcs, boolean isNeedFullPath) {
        List<ElementUiRsTreeNode> children = getChildNodes(node, funcs, isNeedFullPath);
        if (!children.isEmpty()) {
            Iterator var4 = children.iterator();

            while(var4.hasNext()) {
                ElementUiRsTreeNode child = (ElementUiRsTreeNode)var4.next();
                buildChildNodes(child, funcs, isNeedFullPath);
            }

            node.setChildren(children);
        }

    }

    public static List<ElementUiRsTreeNode> getChildNodes(ElementUiRsTreeNode pnode, List<OpuRsToolBar> funcs, boolean isNeedFullPath) {
        List<ElementUiRsTreeNode> childNodes = new ArrayList();
        Iterator var4 = funcs.iterator();

        while(var4.hasNext()) {
            OpuRsToolBar n = (OpuRsToolBar)var4.next();
            if (StringUtils.isNotBlank(pnode.getId()) && pnode.getId().equals(n.getParentFuncId())) {
                ElementUiRsTreeNode nNode = ElementUiRsTreeNodeConvert.convertFunc(n, isNeedFullPath);
                childNodes.add(nNode);
            }
        }

        return childNodes;
    }

    public static List<ElementUiRsTreeNode> getRootNodes(List<OpuRsToolBar> allFuncs, boolean isNeedFullPath) {
        List<ElementUiRsTreeNode> rootNodes = new ArrayList();
        List<OpuRsToolBar> firstFuncList = new ArrayList();
        Iterator var4 = allFuncs.iterator();

        while(var4.hasNext()) {
            OpuRsToolBar func = (OpuRsToolBar)var4.next();
            if (rootNode(func)) {
                firstFuncList.add(func);
                rootNodes.add(ElementUiRsTreeNodeConvert.convertFunc(func, isNeedFullPath));
            }
        }

        if (firstFuncList != null && firstFuncList.size() > 0) {
            System.out.println("根节点个数：" + firstFuncList.size());
            allFuncs.removeAll(firstFuncList);
        }

        return rootNodes;
    }

    public static boolean rootNode(OpuRsToolBar node) {
        return StringUtils.isBlank(node.getParentFuncId());
    }
}
