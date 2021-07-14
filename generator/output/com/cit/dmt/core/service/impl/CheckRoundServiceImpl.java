package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CheckRoundMapper;
import com.cit.dmt.core.model.CheckRound;
import com.cit.dmt.core.service.CheckRoundService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckRoundServiceImpl implements CheckRoundService {
	Logger logger = LoggerFactory.getLogger(CheckRoundService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckRoundMapper mapper;
	
	@Override
	public Page<CheckRound> selectPaged(CommonRequestParam crp, CheckRound qp) throws BusinessException {
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
		
		Page<CheckRound> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CheckRound selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CheckRound checkRound, User loginUser) throws BusinessException {
//		CheckRound checkConflict = mapper.selectByPhone(checkRound.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(checkRound);
	}

	@Override
	public int update(CheckRound checkRound, User loginUser) throws BusinessException {
		CheckRound checkExist = mapper.selectByPrimaryKey(checkRound.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (checkRound.getPhone() != null) {
//			CheckRound checkConflict = mapper.selectByPhone(checkRound.getPhone(), checkRound.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(checkRound);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CheckRound checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

