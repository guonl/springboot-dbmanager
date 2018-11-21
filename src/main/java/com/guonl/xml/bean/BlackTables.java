package com.guonl.xml.bean;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by guonl
 * Date 2018/11/20 2:06 PM
 * Description:
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "tables")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackTables { // 泛化, 聚合

    @XmlElement(name = "table")
    private List<String> tableName;

    @XmlElement(name = "tc")
    private List<Columns> columnsList;

    /**
     *  JavaBean 属性名称与字段名称之间的名称冲突:
     * 使用方法：将@XmlElement和@XmlTransient分别加到javabean属性前和对应的getter方法前
     */
    @XmlTransient
    public List<String> getTableName() {
        return tableName;
    }

    public void setTableName(List<String> tableName) {
        this.tableName = tableName;
    }

    @XmlTransient
    public List<Columns> getColumnsList() {
        return columnsList;
    }

    public void setColumnsList(List<Columns> columnsList) {
        this.columnsList = columnsList;
    }
}
