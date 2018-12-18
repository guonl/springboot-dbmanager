package com.guonl.controller;

import com.guonl.service.TableService;
import com.guonl.vo.BlackTableRuleVo;
import com.guonl.vo.FrontResult;
import com.guonl.vo.SqlExecuteVO;
import com.guonl.vo.SqlQueryVO;
import com.guonl.xml.TableBlackBuilder;
import com.guonl.xml.bean.BlackTables;
import com.guonl.xml.bean.Columns;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/21 4:52 PM
 * Description: 主要是获取表信息接口
 */
@Controller
@RequestMapping("/dbm")
public class TableController {

    @Autowired
    private TableService tableService;

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String admin(Model model, SqlQueryVO sqlQueryVO){
        model.addAttribute("sqlQueryVO", sqlQueryVO);
        return "dbm/admin";
    }

    /* 黑名单展示页面 */
    @RequestMapping(value = "/black-show",method = RequestMethod.GET)
    public String blackShow(Model model, SqlQueryVO sqlQueryVO){
        List<String> whiteTables = tableService.getAllTable();//白名单
        BlackTables allCache = TableBlackBuilder.getAll();
        List<String> blackTables = allCache.getTableName();
        List<Columns> columnsList = allCache.getColumnsList();
        model.addAttribute("whiteTables", whiteTables);
        model.addAttribute("blackTables", blackTables);
        model.addAttribute("blackColumns", columnsList);
        String overview = String.format("一共有%d张表，其中白名单的表有%d张，黑名单的表有%d张。",
                whiteTables.size() + blackTables.size(),whiteTables.size(),blackTables.size());
        model.addAttribute("overview", overview);
        return "dbm/blacklist-view";
    }

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
     * 根据所选择的表和类型展示操作界面
     * @param model
     * @param sqlQueryVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/confirm",method = RequestMethod.POST)
    public FrontResult confirm(Model model, SqlQueryVO sqlQueryVO){
        String tableName = sqlQueryVO.getTableName();
        List<String> tableColumn = tableService.getTableColumn(tableName);
        return FrontResult.success(tableColumn);
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
        return result;
    }

    /**
     * 执行
     * @param sqlExecuteVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/execute-sql",method = RequestMethod.POST)
    public FrontResult executeSql(HttpServletRequest request, SqlExecuteVO sqlExecuteVO){
        FrontResult result = tableService.executeSql(sqlExecuteVO,request);
        return result;
    }

    /********黑名单管理***********/
    @RequestMapping(value = "/black-list",method = {RequestMethod.GET,RequestMethod.POST})
    public String blacklist(Model model, BlackTableRuleVo blackTableRuleVo){
        List<BlackTableRuleVo> list = tableService.blacklist(blackTableRuleVo);
        model.addAttribute("blacklist", list);
        return "dbm/blacklist";
    }

    @RequestMapping(value = "/black-add",method = RequestMethod.GET)
    public String blackAdd(Model model,BlackTableRuleVo blackTableRuleVo){
        model.addAttribute("blackTableRuleVo", blackTableRuleVo);
        model.addAttribute("nav_menu_name", "黑名单管理 > 黑名单添加");
        return "dbm/blacklist-add";
    }

    /**
     * 只查询table，不查询字段
     * @param model
     * @param flag  0-查询所有的表，1-查询黑名单的表，2-查询白名单的表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/show-black-tables",method = RequestMethod.POST)
    public FrontResult showAllTables(Model model,Integer flag){
        FrontResult<List<String>> result = tableService.showAllTables(flag);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/black-save",method = RequestMethod.POST)
    public FrontResult blackSave(Model model,BlackTableRuleVo blackTableRuleVo){
        FrontResult result = tableService.blackSave(blackTableRuleVo);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/get-table-field",method = RequestMethod.POST)
    public FrontResult getTableField(Model model,String tableName){
        FrontResult result = tableService.getTableField(tableName);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/del-black",method = RequestMethod.POST)
    public FrontResult delBlack(Model model,Integer id){
        FrontResult result = tableService.delBlack(id);
        return result;
    }



}
