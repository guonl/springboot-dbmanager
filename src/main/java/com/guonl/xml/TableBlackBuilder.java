package com.guonl.xml;

import com.guonl.xml.bean.BlackTables;
import com.guonl.xml.bean.Columns;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by guonl
 * Date 2018/11/20 2:19 PM
 * Description:
 */
public class TableBlackBuilder {

    public static BlackTables getBlackTables() throws Exception {
        // 读取XML文件
        Resource resource = new ClassPathResource("config/TableBlackList.xml");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) !=null) {
            buffer.append(line);
        }
        br.close();
        // XML转为Java对象
        BlackTables tables = (BlackTables)XmlBuilder.xmlStrToOject(BlackTables.class, buffer.toString());
        return tables;
    }

    //TODO 使用单例模式将这个列表放入到本地的缓存中



    public static void main(String[] args) throws Exception {
        BlackTables blackTables = getBlackTables();
        List<String> tableName = blackTables.getTableName();
        List<Columns> columnsList = blackTables.getColumnsList();

    }


}
