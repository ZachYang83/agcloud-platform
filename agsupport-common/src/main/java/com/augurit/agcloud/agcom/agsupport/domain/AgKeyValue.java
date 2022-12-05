package com.augurit.agcloud.agcom.agsupport.domain;

import java.io.Serializable;

public class AgKeyValue implements Serializable {
    private String id;

    private String domain;

    private String key;

    private String value;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
