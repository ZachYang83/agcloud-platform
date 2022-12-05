package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:43 2018/12/21
 * @Modified By:
 */
public class AgEsSynonymWord {
    private String id;
    private String synonymWord;//同义词(词根,同义词1,同义词2,同义词N)
    private Date changeTime;
    private String word;//词
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSynonymWord() {
        return synonymWord;
    }

    public void setSynonymWord(String synonymWord) {
        this.synonymWord = synonymWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
