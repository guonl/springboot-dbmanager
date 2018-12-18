package com.guonl.test;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;

/**
 * Created by guonl
 * Date 2018/12/12 2:42 PM
 * Description: 一个测试的类
 */
public class DemoTest {

    @Test
    public void test() {
        ArrayList<String> list = Lists.newArrayList("123", "456", "789");
//        List<String> list = Arrays.asList("123", "456", "789");
//        ArrayList list = new ArrayList(list);
        Object[] array = list.toArray();
        String tableName = "users";
        String sql = "update users set name=? where id=? ";
        int where = sql.indexOf("where");
        System.out.println(where);
        String str01 = sql.substring(0, where);
        String str02 = sql.substring(where - 1, sql.length());
        System.out.println(str01);
        System.out.println(str02);

        String[] split = str01.split("\\?");
        System.out.println(split.length - 1);
        for (int i = 0; i < split.length - 1; i++) {
            list.remove(0);
        }
        str02 = "select count(1) from " + tableName + str02;
        System.out.println(str02);
        System.out.println(Arrays.toString(list.toArray()));


    }

    @Test
    public void mapKeyDesc() {

        Map<String, Object> map = new HashMap<>();

        Map<String, Object> map2 = new TreeMap<>((a, b) -> b.compareTo(a));
        map.put("1", "rty");
        map.put("2", "jefkl");
        map.put("3", "irlf");
        map.put("4", "tywtyu");
        map.put("5", "nvmn");

        map.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);

        });
        System.out.println("***************");
        map2.putAll(map);
        map2.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);

        });
    }


}
