package com.guonl.controller;

import com.guonl.po.Users;
import com.guonl.service.TestService;
import com.guonl.vo.FrontResult;
import com.guonl.xml.TableBlackBuilder;
import com.guonl.xml.bean.BlackTables;
import com.guonl.xml.bean.Columns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by guonl
 * Date 2018/11/15 10:42 AM
 * Description: 测试
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("name", "guonl");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/db", method = RequestMethod.GET)
    public FrontResult testDb(Model model) {
        List<Users> list = testService.queryList();
        return FrontResult.success(list);
    }

    @ResponseBody
    @RequestMapping(value = "/tem/{name}", method = RequestMethod.GET)
    public FrontResult testJdbcTemplate(Model model, @PathVariable String name) throws Exception {
        //执行SQL,输出查到的数据
        String tableName = name;
        //查询黑名单
        BlackTables blackTables = TableBlackBuilder.getAll();
        List<String> tableNameList = blackTables.getTableName();//表的限制
        List<Columns> columnsList = blackTables.getColumnsList();//字段的限制
        //判断这张表里面是否有需要排除的字段
        List<String> cTableList = columnsList.stream().map(x->x.getTableName()).collect(Collectors.toList());
        if(!tableNameList.isEmpty() && tableNameList.contains(tableName)){
            return FrontResult.error(-1,"该表在黑名单中，不能操作……");
        }
        String sql = String.format("desc %s",tableName);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<Object> field = list.stream().map(x -> x.get("Field")).collect(Collectors.toList());
        if(cTableList.contains(tableName)){
            Columns columns = columnsList.stream().filter(x -> x.getTableName().equals(tableName)).collect(Collectors.toList()).get(0);
            field = field.stream().filter(x->!columns.getFieldList().contains(x)).collect(Collectors.toList());
        }
        return FrontResult.success(field);
    }






}
