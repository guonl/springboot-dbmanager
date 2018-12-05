package com.guonl.vo;

import java.io.Serializable;

/**
 * Created by guonl
 * Date 2018/12/5 5:55 PM
 * Description:
 */
public class SqlResultVO implements Serializable{

    /**
     * 预览的sql
     */
    private String sql;

    /**
     * sql参数
     */
    private String paraStr;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParaStr() {
        return paraStr;
    }

    public void setParaStr(String paraStr) {
        this.paraStr = paraStr;
    }
}
