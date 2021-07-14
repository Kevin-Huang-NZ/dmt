package com.cit.dmt.core.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cit.dmt.core.model.SysPage;
import com.github.pagehelper.Page;

public interface SysPageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int insert(SysPage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int insertSelective(SysPage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    SysPage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int updateByPrimaryKeySelective(SysPage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_page
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int updateByPrimaryKey(SysPage record);

    SysPage selectByPageName(@Param("pageName") String pageName, @Param("notThisId") Long notThisId);
	Page<SysPage> selectPaged(Map<String, Object> criteria);
}