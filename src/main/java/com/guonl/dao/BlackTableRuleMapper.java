package com.guonl.dao;

import com.guonl.po.BlackTableRule;
import com.guonl.po.BlackTableRuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlackTableRuleMapper {
    long countByExample(BlackTableRuleExample example);

    int deleteByExample(BlackTableRuleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlackTableRule record);

    int insertSelective(BlackTableRule record);

    List<BlackTableRule> selectByExample(BlackTableRuleExample example);

    BlackTableRule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlackTableRule record, @Param("example") BlackTableRuleExample example);

    int updateByExample(@Param("record") BlackTableRule record, @Param("example") BlackTableRuleExample example);

    int updateByPrimaryKeySelective(BlackTableRule record);

    int updateByPrimaryKey(BlackTableRule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table black_table_rule
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BlackTableRule selectOneByExample(BlackTableRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table black_table_rule
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsert(@Param("list") List<BlackTableRule> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table black_table_rule
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsertSelective(@Param("list") List<BlackTableRule> list, @Param("selective") BlackTableRule.Column ... selective);
}