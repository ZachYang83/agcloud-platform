package com.augurit.agcloud.agcom.agsupport.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: libc
 * @Description: 建筑构件-实体类
 * @Date: 2020/9/3 11:24
 * @Version: 1.0
 */
public class AgBuildingComponent implements Serializable {
    // id主键
    private String id;
    // 表代码(两位阿拉伯数字：01)
    private String tableCode;
    // 大类代码(两位阿拉伯数字：01)
    private String largeCode;
    // 中类代码(两位阿拉伯数字：01)
    private String mediumCode;
    // 小类代码(两位阿拉伯数字：01)
    private String smallCode;
    // 细类代码(两位阿拉伯数字：01)
    private String detailCode;
    // 类目中文
    private String chineseName;
    // 类目英文
    private String englishName;
    // 建筑构件分类编码（对应数字字典）
    private String type;
    // 备注
    private String remark;
    // 创建时间
    private Timestamp createTime;
    // 修改时间
    private Timestamp modifyTime;

    // 类目编码 （格式：表代码-大类代码.中类代码.小类代码.细类代码,如：30-01.10.40.20）
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getLargeCode() {
        return largeCode;
    }

    public void setLargeCode(String largeCode) {
        this.largeCode = largeCode;
    }

    public String getMediumCode() {
        return mediumCode;
    }

    public void setMediumCode(String mediumCode) {
        this.mediumCode = mediumCode;
    }

    public String getSmallCode() {
        return smallCode;
    }

    public void setSmallCode(String smallCode) {
        this.smallCode = smallCode;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     *
     * @Author: libc
     * @Date: 2020/11/6 9:52
     * @tips: 拼接构件的完整编码
     * @return
     */
    public String getCode() {
        if (StringUtils.isNotBlank(code)) {
            return code;
        }
        // 返回组装后的完整编码  类目编码 （格式：表代码-大类代码.中类代码.小类代码.细类代码,如：30-01.10.40.20）
        StringBuffer tempCode = new StringBuffer();

        if (StringUtils.isNotBlank(tableCode)) {
            tempCode.append(tableCode);
        }
        if (StringUtils.isNotBlank(largeCode)) {
            tempCode.append("-" + largeCode);
        }
        if (StringUtils.isNotBlank(mediumCode)) {
            tempCode.append("." + mediumCode);
        }
        if (StringUtils.isNotBlank(smallCode)) {
            tempCode.append("." + smallCode);
        }
        if (StringUtils.isNotBlank(detailCode)) {
            tempCode.append("." + detailCode);
        }
        return tempCode.toString();
    }

    /**
     * 构建 构件分类的父节点code
     *
     * @return
     */
    public String getParentCode() {
        String result = null;
        String code = this.getCode();
        // 按照 "." 切割，如果是细类，可以得到四个字符串；其他类代码可以得到3个字符串
        String[] codeArr = code.split("\\.");
        if (codeArr.length == 4) {
            // 细类获取父节点code （直接截去最后一层代码）
            result = code.substring(0, code.lastIndexOf("."));
        } else if (codeArr.length == 3) {
            // 其他类获取父节点code
            if ("00".equals(codeArr[1])) {
                // 获取大类父节点， =》 表代码
                result = this.tableCode;
            } else if ("00".equals(codeArr[2])) {
                // 获取中类父节点， =》 表代码 + 大类代码 + 00 + 00
                result = codeArr[0] + ".00" + ".00";
            } else if (!"00".equals(codeArr[1]) && !"00".equals(codeArr[2])) {
                // 获取小类父节点 =》表代码 + 大类代码 + 中类代码 + 00
                String subStr = code.substring(0, code.lastIndexOf("."));
                result = subStr + ".00";
            }
        }
        return result;
    }


    public void setCode(String code) {
        this.code = code;
    }
}
