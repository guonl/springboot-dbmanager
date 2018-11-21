package com.guonl.service.impl;

import com.tntxia.dbmanager.ExecuteResult;
import com.tntxia.dbmanager.rowmapper.BeanRowMapper;
import com.tntxia.dbmanager.rowmapper.MapRowMapper;
import com.tntxia.dbmanager.rowmapper.RowMapper;
import com.tntxia.dbmanager.util.JdbcUtils;
import com.tntxia.dbmanager.util.XMLUtil;
import com.tntxia.jdbc.PageBean;
import com.tntxia.jdbc.SQLExecutorInterface;
import com.tntxia.jdbc.annotation.TableEntity;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.sql.DataSource;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by guonl
 * Date 2018/11/16 10:20 AM
 * Description:
 */
//@Component
public class GuoDBManager implements SQLExecutorInterface {

    private DataSource dataSource;

    public GuoDBManager() {
    }

    public GuoDBManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> queryForMap(String sql, Object[] params) throws Exception {
        return (Map)this.queryForObject(sql, params, (RowMapper)(new MapRowMapper()));
    }

    public Map<String, Object> queryForMap(String sql, Object[] params, boolean isForceLowerCase) throws Exception {
        return (Map)this.queryForObject(sql, params, (RowMapper)(new MapRowMapper(isForceLowerCase)));
    }

    public Map<String, Object> queryForMap(String sql, boolean isForceLowerCase) throws Exception {
        return this.queryForMap(sql, (Object[])null, isForceLowerCase);
    }

    public Object queryForObject(String sql, Object[] params, RowMapper rowMapper) throws Exception {
        List<Object> list = this.queryForList(sql, params, rowMapper);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public Object queryForObject(String sql, Object[] params, Class c) throws Exception {
        return this.queryForObject(sql, params, (RowMapper)(new BeanRowMapper(c)));
    }

    public List<Object> queryForList(String sql, Object[] params, RowMapper rowMapper) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList res = new ArrayList();

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();

            while(rs.next()) {
                res.add(rowMapper.mapRow(rs, 0));
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            throw var12;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return res;
    }

    public int queryForInt(String sql, Object[] params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        int i;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }

            i = rs.getInt(1);
        } catch (Exception var10) {
            var10.printStackTrace();
            throw var10;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return i;
    }

    public int queryForInt(String sql) throws Exception {
        return this.queryForInt(sql, (Object[])null);
    }

    public List<Object> queryForList(String sql, boolean isForceLowerCase) throws Exception {
        return this.queryForList(sql, (Object[])null, (RowMapper)(new MapRowMapper(isForceLowerCase)));
    }

    public List<Object> queryForList(String sql, Object[] params, boolean isForceLowerCase) throws Exception {
        return this.queryForList(sql, params, (RowMapper)(new MapRowMapper(isForceLowerCase)));
    }

    public List<Object> queryForList(String sql, List<Object> params, boolean isForceLowerCase) throws Exception {
        Object[] paramsArr = new Object[params.size()];

        for(int i = 0; i < params.size(); ++i) {
            paramsArr[i] = params.get(i);
        }

        return this.queryForList(sql, paramsArr, isForceLowerCase);
    }

    public List<Object> queryForList(String sql, Object[] params, Class c) throws Exception {
        return this.queryForList(sql, params, (RowMapper)(new BeanRowMapper(c)));
    }

    public List<Object> queryForList(String sql, Class c) throws Exception {
        return this.queryForList(sql, (Object[])null, (RowMapper)(new BeanRowMapper(c)));
    }

    public ExecuteResult execute(String sql) {
        ResultSet rs = null;
        Statement stat = null;
        ExecuteResult result = new ExecuteResult();
        Connection conn = null;

        try {
            conn = this.dataSource.getConnection();
            stat = conn.createStatement();
            boolean r = stat.execute(sql);
            if (r) {
                rs = stat.getResultSet();

                try {
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    List<Map<String, Object>> cols = new ArrayList();

                    HashMap item;
                    for(int i = 1; i <= colCount; ++i) {
                        item = new HashMap();
                        String colName = meta.getColumnName(i);
                        item.put("label", colName);
                        item.put("field", colName);
                        cols.add(item);
                    }

                    result.setCols(cols);
                    ArrayList list = new ArrayList();

                    while(true) {
                        if (!rs.next()) {
                            result.setList(list);
                            break;
                        }

                        item = new HashMap();

                        for(int i = 1; i <= colCount; ++i) {
                            String colName = meta.getColumnName(i);
                            item.put(colName, rs.getString(i));
                        }

                        list.add(item);
                    }
                } catch (SQLException var18) {
                    var18.printStackTrace();
                }
            }

            result.setSuccess(true);
        } catch (SQLException var19) {
            result.setSuccess(false);
            result.setException(var19);
            var19.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

        return result;
    }

    public void executeUpdate(String sql) {
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException var8) {
            var8.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

    }

    public void executeUpdate(String sql, Object[] params) {
        List<Object> p = new ArrayList();
        Object[] var4 = params;
        int var5 = params.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object o = var4[var6];
            p.add(o);
        }

        this.executeUpdate(sql, (List)p);
    }

    public void executeUpdate(String sql, List<Object> params) {
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            for(int i = 0; i < params.size(); ++i) {
                Object param = params.get(i);
                if (param instanceof Integer) {
                    ps.setInt(i + 1, (Integer)param);
                } else if (param instanceof java.util.Date) {
                    java.util.Date date = (Date)param;
                    ps.setTimestamp(i + 1, new Timestamp(date.getTime()));
                } else if (param instanceof Boolean) {
                    ps.setString(i + 1, param + "");
                } else {
                    ps.setString(i + 1, (String)param);
                }
            }

            ps.executeUpdate();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

    }

    public String createTable(String tableName, Map<String, String> cols) {
        Connection conn = null;
        StringBuilder sb = new StringBuilder();

        try {
            conn = this.dataSource.getConnection();
            sb.append("create table ").append(tableName).append("(");
            boolean firstFlag = true;
            Iterator var6 = cols.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var6.next();
                if (firstFlag) {
                    sb.append((String)entry.getKey()).append(" ").append((String)entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("\n,").append((String)entry.getKey()).append(" ").append((String)entry.getValue());
                }
            }

            sb.append(")");
            Statement stat = null;

            try {
                stat = conn.createStatement();

                try {
                    stat.execute("drop table " + tableName);
                } catch (Exception var28) {
                    System.out.println(tableName + "表不存在");
                }

                stat.execute(sb.toString());
            } catch (SQLException var29) {
                var29.printStackTrace();
            } finally {
                try {
                    if (stat != null) {
                        stat.close();
                    }
                } catch (Exception var27) {
                    var27.printStackTrace();
                }

            }
        } catch (Exception var31) {
            var31.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        return sb.toString();
    }

    public void createTable(String xml) {
        Document doc = XMLUtil.getDoc(xml);
        Element root = doc.getDocumentElement();
        NodeList rootChild = root.getChildNodes();
        String tableName = null;
        NodeList colNodeList = null;

        for(int i = 0; i < rootChild.getLength(); ++i) {
            Node node = rootChild.item(i);
            if (node.getNodeType() == 1) {
                if (node.getNodeName().equals("name")) {
                    tableName = node.getTextContent();
                } else if (node.getNodeName().equals("cols")) {
                    colNodeList = node.getChildNodes();
                }
            }
        }

        Map<String, String> cols = new TreeMap();
        if (colNodeList != null) {
            for(int i = 0; i < colNodeList.getLength(); ++i) {
                Node node = colNodeList.item(i);
                if (node.getNodeType() == 1) {
                    String key = null;
                    String value = null;

                    for(int j = 0; j < node.getChildNodes().getLength(); ++j) {
                        Node node2 = node.getChildNodes().item(j);
                        if (node2.getNodeType() == 1) {
                            if (node2.getNodeName().equals("name")) {
                                key = node2.getTextContent();
                            } else if (node2.getNodeName().equals("type")) {
                                value = node2.getTextContent();
                            }
                        }
                    }

                    cols.put(key, value);
                }
            }
        }

        this.createTable(tableName, cols);
    }

    public void update(String sql, Object[] param) throws Exception {
        Connection conn = null;
        PreparedStatement stat = null;

        try {
            conn = this.dataSource.getConnection();
            stat = conn.prepareStatement(sql);
            if (param != null) {
                for(int i = 0; i < param.length; ++i) {
                    stat.setObject(i + 1, param[i]);
                }
            }

            stat.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
            throw var9;
        } finally {
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

    }

    public void update(String sql) throws Exception {
        this.update(sql, (Object[])null);
    }

    public boolean exist(String sql) {
        return this.exist(sql, (Object[])null);
    }

    public boolean exist(String sql, Object[] params) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        boolean res = false;
        Connection conn = null;

        try {
            conn = this.dataSource.getConnection();
            stat = conn.prepareStatement(sql);
            int count;
            if (params != null) {
                for(count = 0; count < params.length; ++count) {
                    stat.setObject(count + 1, params[count]);
                }
            }

            rs = stat.executeQuery();
            count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            res = count > 0;
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

        return res;
    }

    public int getMaxId(String tableName, String id) {
        Statement stat = null;
        ResultSet rs = null;
        int res = 0;
        Connection conn = null;

        try {
            conn = this.dataSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("select max(" + id + ") from " + tableName);
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

        return res;
    }

    public void executeFile(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            while((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                if (tempString.endsWith(";")) {
                    this.execute(sb.toString());
                    sb = new StringBuffer();
                }
            }
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public void dropTable(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop table ").append(tableName).append(";");
        this.execute(sb.toString());
    }

    public int getCount(String sql, List params) throws Exception {
        Object[] paramsArr = params.toArray();
        return this.getCount(sql, paramsArr);
    }

    public int getCount(String sql, Object[] params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        int i;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }

            i = rs.getInt(1);
        } catch (Exception var10) {
            throw var10;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return i;
    }

    public int getCount(String sql) throws Exception {
        return this.getCount(sql, new Object[0]);
    }

    public double getDouble(String sql) {
        return this.getDouble(sql, (Object[])null);
    }

    public double getDouble(String sql, Object[] params) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                double var13 = rs.getDouble(1);
                return var13;
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return 0.0D;
    }

    public String getString(String sql) {
        return this.getString(sql, (Object[])null);
    }

    public String getString(String sql, Object[] params) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                String var12 = rs.getString(1);
                return var12;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return null;
    }

    private Map<String, Object> getPagingResult(List list, PageBean pageBean, int totalAmount) {
        int page = pageBean.getPage();
        int pageSize = pageBean.getPageSize();
        List rows = new ArrayList();
        if ((page - 1) * pageSize < list.size()) {
            int start = (page - 1) * pageSize;

            for(int i = start; i < list.size(); ++i) {
                rows.add(list.get(i));
            }
        }

        Map<String, Object> res = new HashMap();
        res.put("rows", rows);
        res.put("page", pageBean.getPage());
        res.put("totalAmount", totalAmount);
        res.put("pageSize", pageBean.getPageSize());
        return res;
    }

    public Map<String, Object> queryForPagingResult(Class clazz, PageBean pageBean) throws Exception {
        TableEntity t = (TableEntity)clazz.getAnnotation(TableEntity.class);
        String table = t.table();
        String sql = "select top " + pageBean.getTop() + " * from " + table;
        String sqlCount = "select count(*) from " + table;
        List list = this.queryForList(sql, true);
        int count = this.getCount(sqlCount);
        return this.getPagingResult(list, pageBean, count);
    }

    public BigDecimal queryForBigDecimal(String sql, Object[] params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        BigDecimal var12;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for(int i = 0; i < params.length; ++i) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            if (!rs.next()) {
                return BigDecimal.ZERO;
            }

            var12 = rs.getBigDecimal(1);
        } catch (Exception var10) {
            var10.printStackTrace();
            throw var10;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
        }

        return var12;
    }

    public List<String> getMetaList(String sql, boolean forceLower) {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        ArrayList res = new ArrayList();

        try {
            conn = this.dataSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            for(int i = 1; i <= colCount; ++i) {
                String label = meta.getColumnLabel(i);
                if (!label.equalsIgnoreCase("id") && !label.equalsIgnoreCase("restrain_name")) {
                    if (forceLower) {
                        label = label.toLowerCase();
                    }

                    res.add(label);
                }
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

        return res;
    }

}
