package com.guonl.factory.test01;


import java.util.Date;

/**
 * Created by guonl
 * Date 2018/11/27 4:02 PM
 * Description:
 */
public class Person {

    private String name;
    private String pass;
    private int id;
    private Date Bir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBir() {
        return Bir;
    }

    public void setBir(Date bir) {
        Bir = bir;
    }
}
