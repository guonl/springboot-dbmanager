package com.guonl.vo;

import java.util.Date;

/**
 * Created by guonl
 * Date 2018/11/22 3:39 PM
 * Description:
 */
public class OperateLog {

    private Long id;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 拼装好参数的SQL语句
     */
    private String statement;
    /**
     * 拼装好参数的SQL语句
     */
    private Date operateDate;
    /**
     * 操作人
     */
    private String operateUser;
    /**
     * 操作的表名
     */
    private String tableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
