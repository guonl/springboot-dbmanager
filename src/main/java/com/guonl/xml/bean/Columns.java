package com.guonl.xml.bean;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by guonl
 * Date 2018/11/20 1:44 PM
 * Description: 黑名单表字段
 */

@SuppressWarnings("serial")
@XmlRootElement(name = "tc")
@XmlAccessorType(XmlAccessType.FIELD)
public class Columns {

    @XmlAttribute(name = "name")
    private String tableName;//字段所对应表

    @XmlElement(name = "c")
    private List<String> fieldList;//黑名单字段

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @XmlTransient
    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
}
