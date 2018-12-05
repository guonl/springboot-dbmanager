package com.guonl.vo;

import com.guonl.util.SqlTypeEnum;

import java.io.Serializable;

/**
 * Created by guonl
 * Date 2018/12/5 11:18 AM
 * Description:
 */
public class SqlExecuteVO implements Serializable{

    /**
     * 操作的表名
     */
    private String tableName;

    /**
     * sql类型
     */
    private SqlTypeEnum sqlType;

    /**
     * 设置参数
     */
    private String setParaStr;

    /**
     * 条件参数
     */
    private String whereParaStr;

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

    public String getSetParaStr() {
        return setParaStr;
    }

    public void setSetParaStr(String setParaStr) {
        this.setParaStr = setParaStr;
    }

    public String getWhereParaStr() {
        return whereParaStr;
    }

    public void setWhereParaStr(String whereParaStr) {
        this.whereParaStr = whereParaStr;
    }
}
