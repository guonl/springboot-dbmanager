package com.guonl.vo;

import com.guonl.util.SqlTypeEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/12/4 10:01 AM
 * Description:
 */
public class SqlQueryVO implements Serializable{

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * sql类型
     */
    private SqlTypeEnum sqlType;

    /**
     * 拼装好参数的SQL语句
     */
    private String sql;

    /**
     * 更新参数字段
     */
    private Map<String,Object> setMap;

    /**
     * 条件参数字段
     */
    private Map<String,Object> whereMap;

    /**
     * 操作时间
     */
    private String operateDate;

    /**
     * 操作人
     */
    private String operator;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SqlTypeEnum getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlTypeEnum sqlType) {
        this.sqlType = sqlType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getSetMap() {
        return setMap;
    }

    public void setSetMap(Map<String, Object> setMap) {
        this.setMap = setMap;
    }

    public Map<String, Object> getWhereMap() {
        return whereMap;
    }

    public void setWhereMap(Map<String, Object> whereMap) {
        this.whereMap = whereMap;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
