package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Map;

public class AgWktFeature {


        private String wkt;
        private Map<String, String> properties;

        public String getWkt() {
            return wkt;
        }

        public void setWkt(String wkt) {
            this.wkt = wkt;
        }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
