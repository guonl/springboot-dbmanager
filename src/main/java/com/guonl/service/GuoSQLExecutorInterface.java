package com.guonl.service;

import com.tntxia.jdbc.PageBean;

import java.util.List;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/15 7:29 PM
 * Description:
 */
public interface GuoSQLExecutorInterface {

    Map<String, Object> queryForMap(String var1, Object[] var2) throws Exception;

    Map<String, Object> queryForMap(String var1, Object[] var2, boolean var3) throws Exception;

    List<Object> queryForList(String var1, boolean var2) throws Exception;

    List<Object> queryForList(String var1, Object[] var2, boolean var3) throws Exception;

    int queryForInt(String var1) throws Exception;

    int queryForInt(String var1, Object[] var2) throws Exception;

    String getString(String var1);

    Map<String, Object> queryForPagingResult(Class var1, PageBean var2) throws Exception;
}
