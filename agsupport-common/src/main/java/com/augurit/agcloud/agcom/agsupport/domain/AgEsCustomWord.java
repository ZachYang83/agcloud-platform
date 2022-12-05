package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:41 2018/12/21
 * @Modified By:
 */
public class AgEsCustomWord {

    private String id;
    private String customWord;
    private Date changeTime;

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomWord() {
        return customWord;
    }

    public void setCustomWord(String customWord) {
        this.customWord = customWord;
    }
}
