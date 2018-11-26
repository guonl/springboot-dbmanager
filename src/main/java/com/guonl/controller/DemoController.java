package com.guonl.controller;

import com.guonl.exception.MySqlException;
import com.guonl.vo.BaseResponse;
import com.guonl.vo.OperateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by guonl
 * Date 2018/11/22 3:53 PM
 * Description:
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private PlatformTransactionManager transactionManager;

    private static Connection con = null;


    @RequestMapping("/test")
    @ResponseBody

    public BaseResponse uploadDB(@RequestBody List<OperateLog> operateLogs) {
        BaseResponse ret = new BaseResponse();
        List<String> insertSqls = operateLogs.stream().filter(t ->
                "insert".equals(t.getType())
        ).map(OperateLog::getStatement).collect(Collectors.toList());
        List<String> updateSqls = operateLogs.stream().filter(t ->
                "update".equals(t.getType())
        ).map(OperateLog::getStatement).collect(Collectors.toList());
        List<String> deleteSqls = operateLogs.stream().filter(t ->
                "delete".equals(t.getType())
        ).map(OperateLog::getStatement).collect(Collectors.toList());
//        定义事务隔离级别，传播行为
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        事务状态类，通过PlatformTransactionManager的getTransaction方法根据事务定义获取；获取事务状态后，Spring根据传播行为来决定如何开启事务
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            con = jdbcTemplate.getDataSource().getConnection();
//            这里设置是没有效果的[是不能实现事物操作的]
//            con.setAutoCommit(false);
//            执行顺序不能错
            execSQL(insertSqls);
            execSQL(updateSqls);
            execSQL(deleteSqls);
//            提交status中绑定的事务
            transactionManager.commit(transactionStatus);
            ret.setStatus(true);
//            这里设置是没有效果的[是不能实现事物操作的]
//            con.commit();
        } catch (Exception e) {
            try {
//                这里设置是没有效果的[是不能实现事物操作的]
//                con.rollback();
//                提交status中绑定的事务
                transactionManager.rollback(transactionStatus);
            } catch (Exception e1) {
                logger.error("上传数据,回滚报错，", e1);
            }
            logger.error("上传数据,SQL报错，", e);
            if (e instanceof MySqlException) {
                ret.setData(e);
            } else {
                ret.setMsg("未知错误");
            }
            ret.setStatus(false);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                logger.error("上传数据,关闭链接报错，", e);
            }
        }

        return ret;
    }

    public void execSQL(List<String> sqls) throws MySqlException {
        for (String sql : sqls) {
            try {
                jdbcTemplate.update(sql);
            } catch (Exception e) {
//                这是我自定义的异常类
                MySqlException exception = new MySqlException();
                String tableName = matchSql(sql);
                String info = matchInfo(sql);
                if (null != tableName) {
                    if (tableName.equals("t_jzw_fangjian")) {
                        exception.setTableName("房间");
                        exception.setInfo(info);
                    } else {
                        exception.setTableName("人员");
                        exception.setInfo(info);
                    }
                } else {
                    exception.setTableName("未知错误");
                }
                throw exception;
            }

        }
    }


    /**
     * 表名获取
     *
     * @param sql lowcase
     * @return
     */
    public static String matchSql(String sql) {
        Matcher matcher = null;
        //SELECT 列名称 FROM 表名称
        //SELECT * FROM 表名称
        if (sql.startsWith("select")) {
            matcher = Pattern.compile("select\\s.+from\\s(.+)where\\s(.*)").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
        if (sql.startsWith("insert")) {
            matcher = Pattern.compile("insert\\sinto\\s(.+)\\(.*\\)\\s.*").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        if (sql.startsWith("update")) {
            matcher = Pattern.compile("update\\s(.+)set\\s.*").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //DELETE FROM 表名称 WHERE 列名称 = 值
        if (sql.startsWith("delete")) {
            matcher = Pattern.compile("delete\\sfrom\\s(.+)where\\s(.*)").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    /**
     * @describe: 错误SQL获取错误的参数
     * @params:
     * @Author: Kanyun
     * @Date: 2018/7/20 11:31
     */
    public static String matchInfo(String sql) {
        String[] infos = sql.split(" ");
        String regex = "^[\\u4e00-\\u9fa5]*$";
        Pattern p = Pattern.compile(regex);
        List info = new ArrayList();
        for (String s : infos) {
            Matcher m = p.matcher(s);
            if (m.find()) {
                info.add(s);
            }
        }
        return info.toString();

    }
}
