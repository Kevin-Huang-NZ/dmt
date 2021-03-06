package com.cit.dmt.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cit.dmt.core.model.CtCommon;
import com.github.pagehelper.Page;

public interface CtCommonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    int insert(CtCommon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    int insertSelective(CtCommon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    CtCommon selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    int updateByPrimaryKeySelective(CtCommon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_common
     *
     * @mbg.generated Mon Feb 15 21:20:58 NZDT 2021
     */
    int updateByPrimaryKey(CtCommon record);
    
	Page<CtCommon> selectPaged(Map<String, Object> criteria);
	int insertBatch(List<CtCommon> ctCommons);
	int deleteBatch(@Param("countryCode") String countryCode, @Param("languageCode") String languageCode);
}