package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain;

import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuilding;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: libc
 * @Description: Agcim3dbuilding自定义扩展实体类
 * @Date: 2020/12/11 10:45
 * @Version: 1.0
 */
public class Agcim3dbuildingCustom extends Agcim3dbuilding  {

    /**
     * 将buildingname 转name 对应tree节点title信息
     */
    private String name;

    /**
     * 作为树节点展示的时候，表示当前节点所在的层级数
     */
    private String level;

    /**
     * 建筑对应的楼层列表
     */
    private List<Agcim3dentityXCustom> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Agcim3dentityXCustom> getChildren() {
        return children;
    }

    public void setChildren(List<Agcim3dentityXCustom> children) {
        this.children = children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
