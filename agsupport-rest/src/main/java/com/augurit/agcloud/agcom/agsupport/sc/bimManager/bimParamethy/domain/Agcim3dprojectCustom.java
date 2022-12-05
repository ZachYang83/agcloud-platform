package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dproject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: libc
 * @Description: Agcim3dproject自定义扩展实体类
 * @Date: 2020/12/10 16:33
 * @Version: 1.0
 */
public class Agcim3dprojectCustom extends Agcim3dproject {

    /**
     * 作为树节点展示的时候，表示当前节点所在的层级数
     */
    private String level;

    /**
     * bim项目对应模型列表
     */
    private List<Agcim3dbuildingCustom> children = new ArrayList<>();

    public List<Agcim3dbuildingCustom> getChildren() {
        return children;
    }

    public void setChildren(List<Agcim3dbuildingCustom> children) {
        this.children = children;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
