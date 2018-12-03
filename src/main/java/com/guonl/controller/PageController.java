package com.guonl.controller;

import com.guonl.service.TableService;
import com.guonl.vo.FrontResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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


    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String admin(){
        return "dbm/admin";
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
