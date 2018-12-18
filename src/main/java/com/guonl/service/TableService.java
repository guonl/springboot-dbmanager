package com.guonl.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.guonl.dao.BlackTableRuleMapper;
import com.guonl.dao.SqlLogRecordMapper;
import com.guonl.exception.MySqlException;
import com.guonl.factory.my.SqlFactory;
import com.guonl.po.BlackTableRule;
import com.guonl.po.BlackTableRuleExample;
import com.guonl.po.SqlLogRecord;
import com.guonl.util.SqlTypeEnum;
import com.guonl.util.WebUtils;
import com.guonl.vo.BlackTableRuleVo;
import com.guonl.vo.FrontResult;
import com.guonl.vo.SqlExecuteVO;
import com.guonl.xml.TableBlackBuilder;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by guonl
 * Date 2018/11/21 8:54 PM
 * Description:
 */
@Service
public class TableService extends BaseService {

    private static Logger logger = LoggerFactory.getLogger(TableService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlLogRecordMapper sqlLogRecordMapper;
    @Autowired
    private BlackTableRuleMapper blackTableRuleMapper;

    public List<String> getAllTable() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("show tables");
        List<String> tableList = Lists.newArrayList();
        list.forEach(x -> {
            x.forEach((k, v) -> {
                tableList.add((String) v);
            });
        });
        boolean xmlFlag = false;//是否从xml中读取
        List<String> tables = null;
        if (xmlFlag) {
            tables = TableBlackBuilder.getTables();
        } else {
            List<BlackTableRule> blackTableList = getBlackTableList();
            tables = blackTableList.stream().filter(x -> x.getIsBlack() == 1).map(x -> x.getbTableName()).collect(Collectors.toList());
        }
        List<String> collect = Lists.newArrayList();
        if (tables != null) {
            List<String> finalTables = tables;
            collect = tableList.stream().filter(x -> !finalTables.contains(x)).collect(Collectors.toList());
        }
        return collect;
    }

    public List<String> getTableColumn(String tableName) {
        String sql = String.format("desc %s", tableName);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> field = list.stream().map(x -> (String) x.get("Field")).collect(Collectors.toList());
        boolean xmlFlag = false;//是否从xml中读取
        List<String> columns = null;
        if (xmlFlag) {
            columns = TableBlackBuilder.getColumns(tableName);
        } else {
            columns = getBlackTableField(tableName);
        }
        if (columns != null) {
            List<String> finalColumns = columns;
            field = field.stream().filter(x -> !finalColumns.contains(x)).collect(Collectors.toList());
        }
        return field;
    }

    public FrontResult sqlPreview(SqlExecuteVO sqlExecuteVO) {
        FrontResult result = new FrontResult(200, "请求成功");
        SqlFactory sqlFactory = null;
        try {
            sqlFactory = instanceSqlFactory(sqlExecuteVO);
        } catch (MySqlException e) {
            logger.error("实例化sql工厂失败，失败原因：", e);
            return FrontResult.error(-1, e.getMessage());
        }
        String sql = sqlFactory.getSql();
        Object[] sqlParam = sqlFactory.getSqlParam();
        Map<String, Object> resultMap = new HashMap<>();
        int count = preSelect(sqlFactory);
        resultMap.put("sql", sql);
        resultMap.put("sqlParam", Arrays.toString(sqlParam));
        resultMap.put("count", count);
        result.setDataMap(resultMap);
        return result;
    }

    public FrontResult executeSql(SqlExecuteVO sqlExecuteVO, HttpServletRequest request) {
        String message = "";
        try {
            SqlFactory sqlFactory = null;
            sqlFactory = instanceSqlFactory(sqlExecuteVO);
            String sql = sqlFactory.getSql();
            Object[] sqlParam = sqlFactory.getSqlParam();
            String tableName = sqlFactory.getTableName();
            Map<String, Object> resultMap = new HashMap<>();
            int updateCount = jdbcTemplate.update(sql, sqlParam);
            message = String.format("执行成功，操作的表为：%s，共操作了%d条", tableName, updateCount);
            this.recordSqlLog(request, sqlFactory);
        } catch (MySqlException e) {
            logger.error("实例化sql工厂失败，失败原因：", e);
            return FrontResult.error(-1, e.getMessage());
        } catch (DataAccessException e) {
            logger.error("sql操作异常：", e);
            return FrontResult.error(-1, "sql执行失败！！！");
        }
        return FrontResult.success(message);
    }

    private SqlFactory instanceSqlFactory(SqlExecuteVO sqlExecuteVO) throws MySqlException {
        String setJson = sqlExecuteVO.getSetJson();
        String whereJson = sqlExecuteVO.getWhereJson();
        Gson gson = new Gson();
        Map<String, Object> setMap = gson.fromJson(setJson, Map.class);
        Map<String, Object> whereMap = gson.fromJson(whereJson, Map.class);
        if (setMap == null || setMap.isEmpty()) {
            throw new MySqlException(-1, "set参数为空！！！");
        }
        if (whereMap == null || whereMap.isEmpty()) {
            throw new MySqlException(-1, "where参数为空！！！");
        }
        String tableName = sqlExecuteVO.getTableName();
        SqlTypeEnum sqlType = sqlExecuteVO.getSqlType();
        SqlFactory sqlFactory = new SqlFactory(tableName, setMap, whereMap, sqlType);
        return sqlFactory;
    }

    /**
     * 记录日志
     *
     * @param request
     * @param sqlFactory
     */
    private void recordSqlLog(HttpServletRequest request, SqlFactory sqlFactory) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser(); //获取浏览器信息 
        OperatingSystem os = userAgent.getOperatingSystem(); //获取操作系统信息
        SqlLogRecord sqlLogRecord = new SqlLogRecord();
        sqlLogRecord.setTableName(sqlFactory.getTableName());
        sqlLogRecord.setExecuteSql(sqlFactory.getSql());
        sqlLogRecord.setSqlPara(Arrays.toString(sqlFactory.getSqlParam()));
        sqlLogRecord.setSqlType(sqlFactory.getTypeEnum().toString());
        sqlLogRecord.setOperatorIp(WebUtils.getRemoteAddr(request));
        sqlLogRecord.setOperatorInfo(os.toString() + "&&" + browser.toString());
        sqlLogRecordMapper.insertSelective(sqlLogRecord);
    }

    /**
     * 查询影响的条数
     *
     * @param sqlFactory
     * @return
     */
    private int preSelect(SqlFactory sqlFactory) {
        String sql = sqlFactory.getSql();
        Object[] sqlParam = sqlFactory.getSqlParam();
        ArrayList<Object> list = Lists.newArrayList(sqlParam);
        String tableName = sqlFactory.getTableName();
        int wIndex = sql.indexOf("where");
        String preStr = sql.substring(0, wIndex);
        String lastStr = sql.substring(wIndex - 1, sql.length());
        String[] split = preStr.split("\\?");
        System.out.println(split.length - 1);
        for (int i = 0; i < split.length - 1; i++) {
            list.remove(0);
        }
        String selectSql = "select count(1) from " + tableName + lastStr;
        Object[] selectParam = list.toArray();
        int count = jdbcTemplate.queryForObject(selectSql, selectParam, Integer.class);
        return count;
    }

    public List<BlackTableRuleVo> blacklist(BlackTableRuleVo blackTableRuleVo) {
        BlackTableRuleExample example = new BlackTableRuleExample();
        BlackTableRuleExample.Criteria criteria = example.createCriteria().andIsDelEqualTo(Boolean.FALSE);
        List<BlackTableRule> allBlackTable = blackTableRuleMapper.selectByExample(example);
        String btableName = blackTableRuleVo.getbTableName();
        if (StringUtils.isNotBlank(btableName)) {
            criteria.andBTableNameLike("%" + btableName.trim() + "%");
        }
        Integer isBlack = blackTableRuleVo.getIsBlack();
        if (isBlack != null) {
            criteria.andIsBlackEqualTo(isBlack);
        }
        criteria.andParentIdEqualTo(0);
        startPage(blackTableRuleVo);
        List<BlackTableRule> blackTableRules = blackTableRuleMapper.selectByExample(example);
        setPageInfo(blackTableRuleVo, blackTableRules);
        List<BlackTableRuleVo> resultList = new ArrayList<>();
        blackTableRules.forEach(x -> {
            BlackTableRuleVo vo = new BlackTableRuleVo();
            BeanUtils.copyProperties(x, vo);
            List<String> fieldList = new ArrayList<>();
            allBlackTable.forEach(y -> {
                if (y.getParentId() == x.getId()) {
                    fieldList.add(y.getBlackField());
                }
            });
            vo.setFieldList(fieldList);
            resultList.add(vo);
        });
        return resultList;
    }


    public FrontResult<List<String>> showAllTables(Integer flag) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("show tables");
        List<String> tableList = Lists.newArrayList();
        list.forEach(x -> {
            x.forEach((k, v) -> {
                tableList.add((String) v);
            });
        });
        List<BlackTableRule> blackTableList = getBlackTableList();
        List<String> collect = blackTableList.stream().map(x -> x.getbTableName()).collect(Collectors.toList());
        if (flag == 0) {//全部
            return FrontResult.success(tableList);
        } else if (flag == 1) {//黑名单的表
            return FrontResult.success(collect);
        } else {//白名单的表
            tableList.removeAll(collect);
            return FrontResult.success(tableList);
        }
    }

    //查询所有的黑名单的表 -- 从数据库
    private List<BlackTableRule> getBlackTableList() {
        BlackTableRuleExample example = new BlackTableRuleExample();
        example.createCriteria().andIsDelEqualTo(Boolean.FALSE).andParentIdEqualTo(0);
        List<BlackTableRule> blackTableRules = blackTableRuleMapper.selectByExample(example);
        return blackTableRules;
    }

    //查询黑名单配置的字段 -- 从数据库
    private List<String> getBlackTableField(String tableName) {
        BlackTableRuleExample example = new BlackTableRuleExample();
        BlackTableRuleExample.Criteria criteria = example.createCriteria().andIsDelEqualTo(Boolean.FALSE);
        criteria.andBTableNameEqualTo(tableName);
        criteria.andBlackFieldIsNotNull();
        List<BlackTableRule> blackTableRules = blackTableRuleMapper.selectByExample(example);
        List<String> fieldList = blackTableRules.stream().map(x -> x.getBlackField()).collect(Collectors.toList());
        return fieldList;
    }

    public FrontResult blackSave(BlackTableRuleVo blackTableRuleVo) {
        BlackTableRule po = new BlackTableRule();
        BeanUtils.copyProperties(blackTableRuleVo, po);
        po.setParentId(0);
        int i = blackTableRuleMapper.insertSelective(po);//保存父类
        int count = 0;
        String fieldStr = blackTableRuleVo.getFieldStr();
        if (blackTableRuleVo.getIsBlack() == 0 && StringUtils.isNotBlank(fieldStr)) {
            String[] split = fieldStr.split(",");
            for (String s : split) {
                BlackTableRule rule = new BlackTableRule();
                rule.setParentId(po.getId());
                rule.setbTableName(po.getbTableName());
                rule.setIsBlack(0);
                rule.setBlackField(s);
                blackTableRuleMapper.insertSelective(rule);
                count++;
            }
        }
        return FrontResult.success(i + count);
    }


    public FrontResult getTableField(String tableName) {
        String sql = String.format("desc %s", tableName);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> field = list.stream().map(x -> (String) x.get("Field")).collect(Collectors.toList());
        return FrontResult.success(field);
    }

    @Transactional
    public FrontResult delBlack(Integer id) {
        BlackTableRule blackTableRule = blackTableRuleMapper.selectByPrimaryKey(id);
        blackTableRule.setIsDel(true);
        int i = blackTableRuleMapper.updateByPrimaryKeySelective(blackTableRule);
        Integer isBlack = blackTableRule.getIsBlack();
        int count = 0;
        if (isBlack == 0) {
            BlackTableRule rule = new BlackTableRule();
            BlackTableRuleExample example = new BlackTableRuleExample();
            BlackTableRuleExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            rule.setIsDel(true);
            count = blackTableRuleMapper.updateByExampleSelective(rule, example);
        }
        return FrontResult.success(i + count);
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
        //INSERT INTO 表名称 VALUES (值1, 值2,...
        // .)
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
