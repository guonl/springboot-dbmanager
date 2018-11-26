package com.guonl.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/22 3:22 PM
 * Description:
 */
public class QueryVO implements Serializable{

    /**
     * 操作的表名
     */
    private String tableName;
    /**
     * sql类型
     */
    private String sqlType;
    /**
     * 拼装好参数的SQL语句
     */
    private String sql;

    /**
     * 被操作的字段
     */
    private Map<String,Object> paraMap;

    /**
     * 操作时间
     */
    private Date operateDate;
    /**
     * 操作人
     */
    private String operateUser;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParaMap() {
        return paraMap;
    }

    public void setParaMap(Map<String, Object> paraMap) {
        this.paraMap = paraMap;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }
}
