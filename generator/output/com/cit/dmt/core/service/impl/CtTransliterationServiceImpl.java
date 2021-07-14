package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CtTransliterationMapper;
import com.cit.dmt.core.model.CtTransliteration;
import com.cit.dmt.core.service.CtTransliterationService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CtTransliterationServiceImpl implements CtTransliterationService {
	Logger logger = LoggerFactory.getLogger(CtTransliterationService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CtTransliterationMapper mapper;
	
	@Override
	public Page<CtTransliteration> selectPaged(CommonRequestParam crp, CtTransliteration qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
//		if (qp != null) {
//			if (!StringUtils.isEmpty(qp.getPhone())) {
//				criteria.put("phone", qp.getPhone() + "%");
//			}
//		}
		
		Page<CtTransliteration> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CtTransliteration selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CtTransliteration ctTransliteration, User loginUser) throws BusinessException {
//		CtTransliteration checkConflict = mapper.selectByPhone(ctTransliteration.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(ctTransliteration);
	}

	@Override
	public int update(CtTransliteration ctTransliteration, User loginUser) throws BusinessException {
		CtTransliteration checkExist = mapper.selectByPrimaryKey(ctTransliteration.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (ctTransliteration.getPhone() != null) {
//			CtTransliteration checkConflict = mapper.selectByPhone(ctTransliteration.getPhone(), ctTransliteration.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(ctTransliteration);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CtTransliteration checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

