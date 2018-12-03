package com.guonl.util;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by guonl
 * Date 2018/11/29 4:11 PM
 * Description: 一个转换的工具类
 */
public class ManagerUtil {

    private static Logger logger = LoggerFactory.getLogger(ManagerUtil.class);

    /**
     * 正则表达式 用于匹配属性的第一个字母
     **/
    private static final String REGEX = "[a-zA-Z]";

    /**
     * db字段转化为po字段
     *
     * @param column
     * @return
     */
    public static String db2Po(String column) {
        StringBuilder builder = new StringBuilder();
        String a[] = column.split("_");
        for (String s : a) {
            builder.append(s.substring(0, 1).toUpperCase());
            builder.append(s.substring(1).toLowerCase());
        }
        return builder.toString();
    }


    /**
     * po字段转化为db字段
     *
     * @param column
     * @return
     */
    public static String po2Db(String column) {
        StringBuilder builder = new StringBuilder(column);
        int temp = 0;//定位
        for (int i = 0; i < column.length(); i++) {
            if (Character.isUpperCase(column.charAt(i))) {
                builder.insert(i + temp, "_");
                temp += 1;
            }
        }
        if (builder.toString().toString().startsWith("_")) {
            builder.deleteCharAt(0);
        }
        return builder.toString().toLowerCase();
    }

    /**
     * 根据表名获取po实例
     * 方法一：扫描po路径下面的类进行过滤，
     * 方法二：知道PO的路径，手动拼接 (暂时先用这种)
     *
     * @param tableName
     * @return
     */
    public static Object getInstancePO(String tableName) {
        String path = "com.guonl.po." + db2Po(tableName);
        Object obj = null;
        try {
            Class<?> aClass = Class.forName(path);
            obj = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 将查询条件和需要修改的字段赋值到PO
     * 对po赋初值，方便字段值的调用
     * 1、select，delete 只需要对po赋一次查询条件
     * 2、insert，update  需要对po赋值两次，一个是要赋的值，一次是查询条件
     *
     * @param obj
     * @param para
     */
    public static Object transPo(Object obj, Map<String, Object> para) {
        if (obj == null) {
            return null;
        }
        Field[] field = obj.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        List<Field> fields = Arrays.asList(field);
        //只需要对指定的字段赋值就可以了
        List<String> paraKeyList = Lists.newArrayList();
        para.forEach((k, v) -> {
            paraKeyList.add(k);
        });
        List<Field> collect = fields.stream().filter(x -> paraKeyList.contains(x.getName())).collect(Collectors.toList());
        try {
            for (int i = 0; i < collect.size(); i++) { // 遍历所有属性
                String name = collect.get(i).getName(); // 获取属性的名字
                String type = collect.get(i).getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = obj.getClass().getMethod(toMethodName(name,obj,false));
                    String value = (String) m.invoke(obj); // 调用getter方法获取属性值
                    if (value == null) {
                        m = obj.getClass().getMethod(toMethodName(name,obj,true), String.class);
                        m.invoke(obj, para.get(name));
                    }
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = obj.getClass().getMethod(toMethodName(name,obj,false));
                    Integer value = (Integer) m.invoke(obj);
                    if (value == null) {
                        m = obj.getClass().getMethod(toMethodName(name,obj,true), Integer.class);
                        m.invoke(obj, Integer.valueOf((String)para.get(name)));
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = obj.getClass().getMethod(toMethodName(name,obj,false));
                    Boolean value = (Boolean) m.invoke(obj);
                    if (value == null) {
                        m = obj.getClass().getMethod(toMethodName(name,obj,true), Boolean.class);
                        m.invoke(obj, ((Boolean)para.get(name)).booleanValue());
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = obj.getClass().getMethod(toMethodName(name,obj,false));
                    Date value = (Date) m.invoke(obj);
                    if (value == null) {
                        m = obj.getClass().getMethod(toMethodName(name,obj,true), Date.class);
                        m.invoke(obj, DateUtils.stringToDate((String) para.get(name)));
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将origin属性注入到destination中
     *
     * @param origin      原对象
     * @param destination 目标对象
     * @param <T>
     */
    public <T> void mergeObject(T origin, T destination) {
        if (origin == null || destination == null)
            return;
        if (!origin.getClass().equals(destination.getClass()))
            return;

        Field[] fields = origin.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                Object value = fields[i].get(origin);
                if (null != value) {
                    fields[i].set(destination, value);
                }
                fields[i].setAccessible(false);
            } catch (Exception e) {
                logger.error("转换报错", e);
            }
        }
    }

    private static String toMethodName(String name, Object obj, boolean isSet) {
//        name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(name);
        StringBuffer buffer = new StringBuffer();
        /** 如果是set方法名称 **/
        if (isSet) {
            buffer.append("set");
        } else {
            /** get方法名称 **/
            try {
                Field attributeField = obj.getClass().getDeclaredField(name);
                /** 如果类型为boolean **/
                if (attributeField.getType() == boolean.class || attributeField.getType() == Boolean.class) {
                    buffer.append("is");
                } else {
                    buffer.append("get");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        /** 针对以下划线开头的属性 **/
        if (name.charAt(0) != '_' && m.find()) {
            buffer.append(m.replaceFirst(m.group().toUpperCase()));
        } else {
            buffer.append(name);
        }
        return buffer.toString();
    }


    @Test
    public void test() {
        String tableName = "tb_user_abc";
        String poName = ManagerUtil.db2Po(tableName);
        System.out.println(poName);
        System.out.println(ManagerUtil.po2Db(poName));

    }


}
