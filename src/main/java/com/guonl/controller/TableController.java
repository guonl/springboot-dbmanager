package com.guonl.controller;

import com.guonl.service.TableService;
import com.guonl.vo.FrontResult;
import com.guonl.vo.SqlExecuteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

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

    /**
     * 查询所有的表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all-tables",method = RequestMethod.POST)
    public FrontResult getTableList(){
        List<String> allTables = tableService.getAllTable();
        return FrontResult.success(allTables);

    }

    /**
     * 根据表名查询表中的字段
     * @param tableName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/table-column",method = RequestMethod.POST)
    public FrontResult getTableColumn(String tableName){
        List<String> column = tableService.getTableColumn(tableName);
        return FrontResult.success(column);
    }


    /**
     * sql预览
     * @param sqlExecuteVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sql-pre",method = RequestMethod.POST)
    public FrontResult sqlPreview(SqlExecuteVO sqlExecuteVO){
        FrontResult result = tableService.sqlPreview(sqlExecuteVO);
        return FrontResult.success(result);
    }

    /**
     * 执行
     * @param sqlExecuteVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/execute-sql",method = RequestMethod.POST)
    public FrontResult executeSql(SqlExecuteVO sqlExecuteVO){
        FrontResult result = tableService.executeSql(sqlExecuteVO);
        return FrontResult.success(result);
    }








}
