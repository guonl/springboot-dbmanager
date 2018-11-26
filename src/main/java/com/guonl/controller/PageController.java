package com.guonl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by guonl
 * Date 2018/11/21 4:53 PM
 * Description:
 */
@Controller
@RequestMapping("/dbm")
public class PageController {


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }


    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String admin(){
        return "dbm/admin";
    }

}
