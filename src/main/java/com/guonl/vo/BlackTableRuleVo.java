package com.guonl.vo;

import com.guonl.util.Page;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BlackTableRuleVo extends Page implements Serializable{

    private Integer id;

    private Integer parentId;

    private String bTableName;

    private Integer isBlack;

    private String blackField;

    private Date createdAt;

    private Date updatedAt;

    private Boolean isDel;

    private List<String> fieldList;//黑面单字段列表

    private String fieldStr;//前端传过来的字段串

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getbTableName() {
        return bTableName;
    }

    public void setbTableName(String bTableName) {
        this.bTableName = bTableName;
    }

    public Integer getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Integer isBlack) {
        this.isBlack = isBlack;
    }

    public String getBlackField() {
        return blackField;
    }

    public void setBlackField(String blackField) {
        this.blackField = blackField;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public String getFieldStr() {
        return fieldStr;
    }

    public void setFieldStr(String fieldStr) {
        this.fieldStr = fieldStr;
    }
}