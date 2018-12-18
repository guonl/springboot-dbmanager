package com.guonl.vo;

import com.guonl.util.SqlTypeEnum;

import java.io.Serializable;

/**
 * Created by guonl
 * Date 2018/12/10 2:16 PM
 * Description:
 */
public class SqlExecuteVO implements Serializable {

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
    private String setJson;

    /**
     * 条件参数
     */
    private String whereJson;

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

    public String getSetJson() {
        return setJson;
    }

    public void setSetJson(String setJson) {
        this.setJson = setJson;
    }

    public String getWhereJson() {
        return whereJson;
    }

    public void setWhereJson(String whereJson) {
        this.whereJson = whereJson;
    }
}
