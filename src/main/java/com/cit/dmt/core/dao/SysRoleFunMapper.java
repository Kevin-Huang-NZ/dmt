package com.cit.dmt.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.model.SysRoleFunIJFun;

public interface SysRoleFunMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int insert(SysRoleFun record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int insertSelective(SysRoleFun record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    SysRoleFun selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int updateByPrimaryKeySelective(SysRoleFun record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_fun
     *
     * @mbg.generated Mon Jan 18 20:26:22 NZDT 2021
     */
    int updateByPrimaryKey(SysRoleFun record);
    
    List<SysRoleFunIJFun> loadSysRoleFun();
    List<SysRoleFun> selectByRoleNo(String roleNo);
    List<SysRoleFun> selectByNo(@Param("roleNo") String roleNo, @Param("funNo") String funNo);
    int deleteByRoleNo(String roleNo);
    int deleteByFunNo(String funNo);
    int deleteByNo(@Param("roleNo") String roleNo, @Param("funNo") String funNo);
    int deleteByPageName(String pageName);
}