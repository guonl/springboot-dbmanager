package com.guonl.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by guonl
 * Date 2018/11/15 6:08 PM
 * Description: 从spring中获取datasource
 * 参考文章： https://blog.csdn.net/qq_20916555/article/details/80852144
 *
 * 引申：springboot如何获取多数据源
 * 参考文章：https://blog.csdn.net/hnhygkx/article/details/78598788
 *
 */
@Component
public class DataSourceUtil {

    public DataSourceUtil() {
    }

    private static Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);

    @Autowired
    private ApplicationContext applicationContext;

    private DataSource dataSource;

    private Connection connection;



    public Connection getConnInstance() {
        try {
            // 获取配置的数据源
            if(connection == null){
                synchronized (DataSourceUtil.class){
                    if(connection == null){
                        dataSource = getDataSourceInstance();
                        connection = dataSource.getConnection();
                        connection.setAutoCommit(false);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sequence Service get connection failed:" + e.getMessage(), e);
        }
        return connection;
    }


    public DataSource getDataSourceInstance(){
        if(dataSource == null){
            synchronized (DataSourceUtil.class){
                if(dataSource == null){
                    dataSource = applicationContext.getBean(DataSource.class);
                }
            }
        }
        return dataSource;
    }


    protected void closeConnection(Connection conn) {
        closeConnection(conn, false);
    }

    protected void closeConnection(Connection conn, boolean rollBack) {
        try {
            if (rollBack) conn.rollback();
            else conn.commit();
        } catch (SQLException e1) {
            e1.printStackTrace();
            logger.error("Sequence Service commit/rollback(" + rollBack + ") failed:" + e1.getMessage(), e1);
        }
        try {
            conn.close();
        } catch (SQLException e) {
        }
    }


}
