package com.guonl.xml;

import com.guonl.cache.LocalCache;
import com.guonl.xml.bean.BlackTables;
import com.guonl.xml.bean.Columns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.AllPermission;
import java.util.List;

/**
 * Created by guonl
 * Date 2018/11/20 2:19 PM
 * Description: 从xml读取配置
 */
public class TableBlackBuilder {

    private static Logger logger = LoggerFactory.getLogger(TableBlackBuilder.class);

    private static final String ALL = "all";//所有

    private static final String BT = "b_table";//表名集合

    private static final String BS= "c_";//单表 BS + tableName

    /**
     * 类一加载就初始化缓存
     */
    static {
        initCache();
    }


    /**
     * 查询所有的黑名单表
     * @return
     */
    public static BlackTables getAll() {
        BlackTables tables = (BlackTables) LocalCache.get(ALL);
        if(tables == null){
            initCache();
            tables = (BlackTables) LocalCache.get(ALL);
        }
        return tables;
    }


    /**
     * 查询黑名单的表
     * @return
     */
    public static List<String> getTables(){
        List<String> tableList = (List<String>) LocalCache.get(BT);
        return tableList;
    }

    /**
     * 查询单个表中的黑名单字段
     * @param tableName
     * @return
     */
    public static List<String> getColumns(String tableName){
        List<String> Columns = (List<String>) LocalCache.get(getColumnKey(tableName));
        return Columns;
    }

    private static void initCache(){
        BlackTables tables = null;
        try {
            // 读取XML文件
            Resource resource = new ClassPathResource("config/TableBlackList.xml");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
            tables = (BlackTables) XmlBuilder.xmlStrToOject(BlackTables.class, buffer.toString());
        } catch (Exception e) {
            logger.error("读取黑名单报错：" + e.getMessage());
            e.printStackTrace();
        }
        if(tables != null){
            LocalCache.put(ALL,tables);//保存总数据
            List<String> tableNameList = tables.getTableName();//黑名单表集合
            List<Columns> columnsList = tables.getColumnsList();//黑名单字段集合
            LocalCache.put(BT,tableNameList);
            columnsList.forEach(x->{
                LocalCache.put(getColumnKey(x.getTableName()),x.getFieldList());
            });
        }
    }

    /**
     * 生成字段集合的key
     * @param tableName
     * @return
     */
    private static String getColumnKey(String tableName){
        return BS + tableName;
    }


}


