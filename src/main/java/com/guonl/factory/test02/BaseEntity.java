package com.guonl.factory.test02;

import java.util.Date;

/**
 * Created by guonl
 * Date 2018/11/28 2:07 PM
 * Description:
 */
public class BaseEntity {

    @FieldAnnotation(fieldName="id",fieldType=FieldType.NUMBER,pk=true)
    private Integer id;

    @FieldAnnotation(fieldName="createDate",fieldType=FieldType.DATE, pk = false)
    private Date createDate;

    @FieldAnnotation(fieldName="modifyDate",fieldType=FieldType.DATE, pk = false)
    private Date modifyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BaseEntity(Integer id, Date createDate, Date modifyDate) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public BaseEntity() {
        super();
    }

}
