package com.guonl.factory.my;

import com.guonl.po.Users;
import com.guonl.util.SqlTypeEnum;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/29 2:49 PM
 * Description:
 */
public class TestMyDemo {

    @Test
    public void testttribute(){
        //获取参数类
        Users users = new Users();
        users.setMobile("1234567");
        users.setName("guonl");
        Class cls = users.getClass();
        //将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
        Field[] fields = cls.getDeclaredFields();
        for(int i = 0;i < fields.length; i ++){
            Field f = fields[i];
            f.setAccessible(true);
            try {
                //f.getName()得到对应字段的属性名，f.get(o)得到对应字段属性值,f.getGenericType()得到对应字段的类型
                System.out.println("属性名："+f.getName()+"；属性值："+f.get(users)+";字段类型：" + f.getGenericType());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUpdate(){
        String tableName = "users";
        Map<String,Object> setMap = new HashMap<>();
        Map<String,Object> whereMap = new HashMap<>();
        setMap.put("username","guonl");
        setMap.put("openid","abc-123");
        setMap.put("email","983220871");
        setMap.put("gender","0");
        setMap.put("birthday","2018-08-08");
        whereMap.put("id","2");
        whereMap.put("mobile","18221825926");
        SqlFactory sqlFactory = new SqlFactory(tableName,setMap,whereMap, SqlTypeEnum.UPDATE);

        System.out.println("sql语句：" + sqlFactory.getSql());
        System.out.println("sql参数：" + Arrays.toString(sqlFactory.getSqlParam()));



    }



}
