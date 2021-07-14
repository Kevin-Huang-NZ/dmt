package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CtCommonMapper;
import com.cit.dmt.core.model.CtCommon;
import com.cit.dmt.core.service.CtCommonService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.ListUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CtCommonServiceImpl implements CtCommonService {
	Logger logger = LoggerFactory.getLogger(CtCommonService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CtCommonMapper mapper;
	
	@Override
	public Page<CtCommon> selectPaged(CommonRequestParam crp, CtCommon qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
		if (qp != null) {
			if (!StringUtils.isEmpty(qp.getCountryCode())) {
				criteria.put("countryCode", qp.getCountryCode());
			}
			if (!StringUtils.isEmpty(qp.getLanguageCode())) {
				criteria.put("languageCode", qp.getLanguageCode());
			}
			if (!StringUtils.isEmpty(qp.getOriginalType())) {
				criteria.put("originalType", qp.getOriginalType());
			}
			if (!StringUtils.isEmpty(qp.getOriginal())) {
				criteria.put("keyWord", "%"+qp.getOriginal()+"%");
			}
		}
		
		Page<CtCommon> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CtCommon selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CtCommon ctCommon, User loginUser) throws BusinessException {
//		CtCommon checkConflict = mapper.selectByPhone(ctCommon.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(ctCommon);
	}

	@Override
	public int update(CtCommon ctCommon, User loginUser) throws BusinessException {
		CtCommon checkExist = mapper.selectByPrimaryKey(ctCommon.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (ctCommon.getPhone() != null) {
//			CtCommon checkConflict = mapper.selectByPhone(ctCommon.getPhone(), ctCommon.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(ctCommon);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CtCommon checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int saveBatch(List<CtCommon> ctCommons, User loginUser) throws BusinessException {
        int totalInsert = 0;
	
	    List<List<CtCommon>> sepratedList = ListUtils.splitListByCapacity(ctCommons, 500);
	    for (List<CtCommon> subList : sepratedList) {
	    	totalInsert += mapper.insertBatch(subList);
	    }
	
	    return totalInsert;
	}

	@Override
	public int deleteBatch(String countryCode, String languageCode, User loginUser) throws BusinessException {
		return mapper.deleteBatch(countryCode, languageCode);
	}

}

