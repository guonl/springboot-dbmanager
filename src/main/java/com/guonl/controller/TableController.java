package com.guonl.controller;

import com.google.common.collect.Lists;
import com.guonl.service.TableService;
import com.guonl.vo.FrontResult;
import com.guonl.vo.QueryVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/21 4:52 PM
 * Description: 主要是获取表信息接口
 */
@Controller
@RequestMapping("/table")
public class TableController {

    @Autowired
    private TableService tableService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public FrontResult getTableList(){
        List<String> allTables = tableService.getAllTable();
        return FrontResult.success(allTables);

    }

    @ResponseBody
    @RequestMapping(value = "/column/{tableName}",method = RequestMethod.GET)
    public FrontResult getTableColumn(@PathVariable String tableName){
        List<String> column = tableService.getTableColumn(tableName);
        return FrontResult.success(column);
    }

    @ResponseBody
    @RequestMapping(value = "sql-execute",method = RequestMethod.GET)
    public FrontResult sqlExecute(QueryVO queryVO){
        String sql = jointSql(queryVO);
        queryVO.setSql(sql);
        FrontResult result = tableService.sqlExecute(queryVO);
        return result;

    }

    private String jointSql(QueryVO queryVO){
        String sql = "";
        String sqlType = queryVO.getSqlType();
        if("select".equals(sqlType)){
            sql = getSelectSql(queryVO);
        }else if("insert".equals(sqlType)){
            sql = getInsertSql(queryVO);
        }else if("update".equals(sqlType)){
            sql = getUpdateSql(queryVO);
        }else if("delete".equals(sqlType)){
            sql = getDeleteSql(queryVO);
        }
        return sql;
    }

    private String getSelectSql(QueryVO queryVO){
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(queryVO.getTableName() + " where 1=1 ");
        Map<String, Object> paraMap = queryVO.getParaMap();
        paraMap.forEach((k,v)->{
            buffer.append("and " + k + "=" + v);
        });
        return buffer.toString();
    }

    private String getInsertSql(QueryVO queryVO){
        StringBuffer buffer = new StringBuffer();
        buffer.append("insert into " + queryVO.getTableName() + "(");
        Map<String, Object> paraMap = queryVO.getParaMap();
        List<String> kList = Lists.newArrayList();
        List<Object> cList = Lists.newArrayList();
        paraMap.forEach((k,v)->{
            kList.add(k);
            cList.add(v);
        });
        kList.forEach(x->{
            buffer.append(x + ",");
        });
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(") values (");
        cList.forEach(x->{
            buffer.append(x + ",");
        });
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        return buffer.toString();
    }

    private String getUpdateSql(QueryVO queryVO){

        return "";
    }

    private String getDeleteSql(QueryVO queryVO){

        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/test/update",method = RequestMethod.GET)
    public FrontResult testUpdate(){
        tableService.testUpdate();
        return FrontResult.success("操作成功");

    }



    @Test
    public void test() {
        QueryVO queryVO = new QueryVO();
        queryVO.setSqlType("select");
        queryVO.setTableName("users");
        Map map = new HashMap();
        map.put("username","guonl");
        map.put("mobile","18221825926");
        queryVO.setParaMap(map);
        String selectSql = getSelectSql(queryVO);
        System.out.println(selectSql);

    }
}
