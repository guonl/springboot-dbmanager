package com.guonl.service;

import com.google.common.collect.Lists;
import com.guonl.exception.MySqlException;
import com.guonl.vo.FrontResult;
import com.guonl.vo.QueryVO;
import com.guonl.xml.TableBlackBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by guonl
 * Date 2018/11/21 8:54 PM
 * Description:
 */
@Service
public class TableService {

    private static Logger logger = LoggerFactory.getLogger(TableService.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    private PlatformTransactionManager transactionManager;

    private static Connection con = null;

    public List<String> getAllTable() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("show tables");
        List<String> tableList = Lists.newArrayList();
        list.forEach(x -> {
            x.forEach((k, v) -> {
                tableList.add((String) v);
            });
        });
        List<String> tables = TableBlackBuilder.getTables();
        List<String> collect = Lists.newArrayList();
        if (tables != null) {
            collect = tableList.stream().filter(x -> !tables.contains(x)).collect(Collectors.toList());
        }
        return collect;
    }

    public List<String> getTableColumn(String tableName) {
        String sql = String.format("desc %s", tableName);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> field = list.stream().map(x -> (String) x.get("Field")).collect(Collectors.toList());
        List<String> columns = TableBlackBuilder.getColumns(tableName);
        if (columns != null) {
            field = field.stream().filter(x -> !columns.contains(x)).collect(Collectors.toList());
        }
        return field;
    }

    public FrontResult sqlExecute(QueryVO queryVO) {
        String sql = queryVO.getSql();
        String tableName = queryVO.getTableName();
        Matcher matcher = matchSql(sql);
//        matcher.find() find是部分匹配，matches是全匹配
        if (!matcher.matches()) {
            return FrontResult.error(-1, "sql的格式错误");
        }
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
            execSQL(sql);
//            提交status中绑定的事务
            transactionManager.commit(transactionStatus);
//            这里设置是没有效果的[是不能实现事物操作的]
//            con.commit();
        } catch (Exception e) {
//            这里设置是没有效果的[是不能实现事物操作的]
//            con.rollback();
//            提交status中绑定的事务
            try {
                transactionManager.rollback(transactionStatus);
            } catch (TransactionException e1) {
                logger.error("事务回滚错误", e);
                return FrontResult.error(-1,"事务回滚错误");
            }

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("数据库连接关闭错误", e);
            }
        }

        return null;
    }

    public void execSQL(String sql) throws MySqlException {
        try {
            if(sql.startsWith("insert")){
                jdbcTemplate.queryForList(sql);
            }else {
                jdbcTemplate.update(sql);
            }
        } catch (Exception e) {
            MySqlException exception = new MySqlException();
            throw exception;
        }
    }

    /**
     * 判断sql的格式是否正确
     *
     * @param sql
     * @return
     */
    private Matcher matchSql(String sql) {
        Matcher matcher = null;
        //SELECT 列名称 FROM 表名称
        //SELECT * FROM 表名称
        if (sql.startsWith("select")) {
            matcher = Pattern.compile("select\\s.+from\\s(.+)where\\s(.*)").matcher(sql);
        }
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
        if (sql.startsWith("insert")) {
            matcher = Pattern.compile("insert\\sinto\\s(.+)\\(.*\\)\\s.*").matcher(sql);
        }
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        if (sql.startsWith("update")) {
            matcher = Pattern.compile("update\\s(.+)set\\s.*").matcher(sql);
        }
        //DELETE FROM 表名称 WHERE 列名称 = 值
        if (sql.startsWith("delete")) {
            matcher = Pattern.compile("delete\\sfrom\\s(.+)where\\s(.*)").matcher(sql);
        }
        return matcher;
    }
}
