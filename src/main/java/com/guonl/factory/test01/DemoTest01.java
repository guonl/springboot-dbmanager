package com.guonl.factory.test01;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/28 2:42 PM
 * Description:
 */
public class DemoTest01 {

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Person te = new Person();
        te.setName("张三");
        te.setPass("123456");
        te.setId(123);
//        te.setBir(new Date());
        System.out.println("********添删改********");
        SqlFactory sf = new SqlFactory(te);
        String sql = sf.createUpdateSql("update");
        Object[] oo = sf.getSqlParams();
        System.out.println(sql);
        System.out.println(Arrays.toString(oo));

//        System.out.println("********查询********");
//        SqlFactory sf2 = new SqlFactory(te);//1
//        Map<String, Object> ma = new HashMap<String, Object>();
//        ma.put("userName", "张三");
//        ma.put("userPass", new Time(new Date().getTime()));
//        String qsql = sf2.createQuerySql(ma);//2
//        System.out.println(qsql);
//        Object[] oo2 = sf2.getSqlParams();//3
//        System.out.println(Arrays.toString(oo2));

    }

}
