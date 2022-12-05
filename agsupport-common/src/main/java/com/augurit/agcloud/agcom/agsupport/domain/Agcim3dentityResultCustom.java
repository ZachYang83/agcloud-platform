package com.augurit.agcloud.agcom.agsupport.domain;



/**
 * @Author: qinyg
 * @Date: 2020/10/14
 * @tips:
 */
public class Agcim3dentityResultCustom {
    private Object rows;
    private long total;
    public Agcim3dentityResultCustom() {
    }
    public Agcim3dentityResultCustom(Object rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
