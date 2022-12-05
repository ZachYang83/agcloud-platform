package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignScheme;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */

public class AgUserDesignParam extends AgUserDesignScheme {
    @JsonIgnore
    private List<AgUserDesignMaterials> materials;

    public List<AgUserDesignMaterials> getMaterials() {
        return materials;
    }

    public void setMaterials(List<AgUserDesignMaterials> materials) {
        this.materials = materials;
    }
}
