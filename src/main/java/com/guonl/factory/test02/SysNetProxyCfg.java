package com.guonl.factory.test02;

import java.util.Date;

/**
 * Created by guonl
 * Date 2018/11/28 2:09 PM
 * Description:
 */
@TableAnnotation(tableName="sysNetProxyCfg")
public class SysNetProxyCfg extends BaseEntity{

    @FieldAnnotation(fieldName = "name", fieldType = FieldType.STRING, pk = false)
    private String name;

    @FieldAnnotation(fieldName = "type", fieldType = FieldType.STRING, pk = false)
    private String type;

    @FieldAnnotation(fieldName = "proxyHostIp", fieldType = FieldType.STRING, pk = false)
    private String proxyHostIp;

    @FieldAnnotation(fieldName = "proxyPort", fieldType = FieldType.NUMBER, pk = false)
    private Integer proxyPort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProxyHostIp() {
        return proxyHostIp;
    }

    public void setProxyHostIp(String proxyHostIp) {
        this.proxyHostIp = proxyHostIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public SysNetProxyCfg(Integer id, Date createDate, Date modifyDate, String name, String type, String proxyHostIp, Integer proxyPort) {
        super(id, createDate, modifyDate);
        this.name = name;
        this.type = type;
        this.proxyHostIp = proxyHostIp;
        this.proxyPort = proxyPort;
    }

    public SysNetProxyCfg() {
        super();
    }

}
