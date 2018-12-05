package com.guonl.controller;

import com.guonl.service.TableService;
import com.guonl.util.SqlTypeEnum;
import com.guonl.vo.FrontResult;
import com.guonl.vo.SqlQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/21 4:53 PM
 * Description:
 */
@Controller
@RequestMapping("/dbm")
public class PageController {

    @Autowired
    private TableService tableService;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }


    /**
     * 主界面
     * @param model
     * @param sqlQueryVO
     * @return
     */
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String admin(Model model, SqlQueryVO sqlQueryVO){
        model.addAttribute("sqlQueryVO", sqlQueryVO);
        return "dbm/admin";
    }

    @RequestMapping(value = "/blacklist",method = RequestMethod.GET)
    public String blacklist(Model model, SqlQueryVO sqlQueryVO){
        model.addAttribute("sqlQueryVO", sqlQueryVO);
        return "dbm/blacklist";
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

    @ResponseBody
    @RequestMapping(value = "/test-db",method = RequestMethod.GET)
    public FrontResult testDB(){
        FrontResult result = new FrontResult();
        String tableName = "users";
        Map<String,Object> setMap = new HashMap<>();
        Map<String,Object> whereMap = new HashMap<>();
        setMap.put("username","guonl");
        setMap.put("openid","abc-123");
        setMap.put("email","983220871");
        setMap.put("gender","0");
        setMap.put("birthday","2018-08-08");
//        whereMap.put("id","2");
//        whereMap.put("mobile","18221825926");
        whereMap.put("name","guonl");

        int i = tableService.testUpdate(tableName, setMap, whereMap);
        result.setCode(200);
        result.setMessage("成功执行了:" + i + "条");
        result.setDataMap(i);
        return result;
    }


}
