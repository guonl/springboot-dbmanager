package com.guonl.factory.my;

import com.guonl.util.ManagerUtil;
import com.guonl.util.SqlTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guonl
 * Date 2018/11/29 10:59 AM
 * Description:
 */
public class SqlFactory {

    private static Logger logger = LoggerFactory.getLogger(SqlFactory.class);

    /**
     * 需要操作的表名
     **/
    private String tableName;

    /**
     * 生成的sql语句
     **/
    private String sql;//最终生成的sql

    /**
     * 参数值
     **/
    private List objParam = new ArrayList();//最后拼接的参数

    /**
     * 存放更新值的po
     */
    private Object setObj = null;

    /**
     * 存放查询条件的po
     */
    private Object whereObj = null;

    /**
     * sql类型
     */
    private SqlTypeEnum typeEnum;


    /**
     * @param tableName 需要操作的表名
     * @param setMap    前端传过来的set集
     * @param whereMap  前端传过来的where集
     * @param type      sql类型
     */
    public SqlFactory(String tableName, Map<String, Object> setMap, Map<String, Object> whereMap, SqlTypeEnum type) {

        try {
            this.tableName = tableName;
            this.setObj = ManagerUtil.transPo(ManagerUtil.getInstancePO(tableName), setMap);
            this.whereObj = ManagerUtil.transPo(ManagerUtil.getInstancePO(tableName),whereMap);
            this.typeEnum = type;
            distribute(typeEnum);
            logger.info("sql工厂初始化完成……");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("sql工厂初始化错误……", e);
        }
    }

    /**
     * 分发函数
     *
     * @param typeEnum
     */
    private void distribute(SqlTypeEnum typeEnum) {
        switch (typeEnum) {
            case SELECT:
                getSelectSql();
                break;
            case INSERT:
                getInsertSql();
                break;
            case UPDATE:
                getUpdateSql();
                break;
            case DELETE:
                getDeleteSql();
                break;
            default:
                logger.error("SQL类型错误……");
                break;
        }

    }

    /**
     * 生成查询sql
     */
    private void getSelectSql() {

    }


    /**
     * 生成insert语句
     */
    private void getInsertSql() {

    }

    /**
     * 生成删除语句
     */
    private void getDeleteSql() {

    }


    /**
     * 生成update语句
     */
    private void getUpdateSql() {
        StringBuffer buffer = new StringBuffer();
        if (setObj == null) {
            return;
        }
        String tableName = ManagerUtil.po2Db(setObj.getClass().getSimpleName());
        buffer.append("update " + tableName + " ");
        Field[] fields = setObj.getClass().getDeclaredFields();
        try {
            //封装set
            buffer.append("set ");
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(setObj);
                if (null != value) {
                    String name = ManagerUtil.po2Db(field.getName());//得到字段名
                    buffer.append(name + "=?,");
                    objParam.add(value);
                }
            }
            if(buffer.toString().endsWith(",")){
                buffer.deleteCharAt(buffer.length()-1);
            }
            //封装where
            if (whereObj == null) {// 字段属性都是null
                logger.info("update的where条件为null");
                buffer.append("where 1=1");
            } else {
                buffer.append(" where ");
                Field[] declaredFields = whereObj.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    Object value = field.get(whereObj);
                    if (null != value) {
                        String name = ManagerUtil.po2Db(field.getName());//得到字段名
                        buffer.append(name + "=? ");
                        buffer.append("and ");
                        objParam.add(value);
                    }
                }
            }
            if(buffer.toString().endsWith("and ")){
                sql = buffer.substring(0, buffer.length() - 4).toString();
            }else {
                sql = buffer.toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error("生成update语句错误：", e);
        }
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getSqlParam() {
        return objParam.toArray();
    }

    public void setSqlParam(List objParam) {
        this.objParam = objParam;
    }

    public SqlTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(SqlTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
