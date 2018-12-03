package com.guonl.factory.test02;

import com.guonl.util.DateUtils;

import java.util.HashMap;

/**
 * Created by guonl
 * Date 2018/11/28 2:13 PM
 * Description:
 */
public class DemoTest02 {

    public static void main(String[] args) {

        SysNetProxyCfg netProxyCfg = new SysNetProxyCfg(1, DateUtils.stringToDate("2012-05-04 14:45:35"), null, "netProxyCfgName", "netProxyCfgType", "000.000.000.000", 0);
        HashMap<String, String> fixedParams=new HashMap<String,String>();
        fixedParams.put("createDate", "NOW()");
        fixedParams.put("modifyDate", "NOW()");
        /*System.out.println(CreateSqlTools.getDeleteSql(netProxyCfg));
        System.out.println(CreateSqlTools.getDeleteSql(netProxyCfg, true));
        System.out.println(CreateSqlTools.getInsertSql(netProxyCfg));
        System.out.println(CreateSqlTools.getInsertSql(netProxyCfg, fixedParams));
        System.out.println(CreateSqlTools.getSelectAllSql(netProxyCfg));
        System.out.println(CreateSqlTools.getUpdateSql(netProxyCfg));
        System.out.println(CreateSqlTools.getUpdateSql(netProxyCfg, true));*/
        System.out.println(CreateSqlTools.getUpdateSql(netProxyCfg, true, fixedParams));
    }


}
