package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CheckAssignmentMapper;
import com.cit.dmt.core.model.CheckAssignment;
import com.cit.dmt.core.service.CheckAssignmentService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckAssignmentServiceImpl implements CheckAssignmentService {
	Logger logger = LoggerFactory.getLogger(CheckAssignmentService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckAssignmentMapper mapper;
	
	@Override
	public Page<CheckAssignment> selectPaged(CommonRequestParam crp, CheckAssignment qp) throws BusinessException {
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
		
		Page<CheckAssignment> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CheckAssignment selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CheckAssignment checkAssignment, User loginUser) throws BusinessException {
//		CheckAssignment checkConflict = mapper.selectByPhone(checkAssignment.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(checkAssignment);
	}

	@Override
	public int update(CheckAssignment checkAssignment, User loginUser) throws BusinessException {
		CheckAssignment checkExist = mapper.selectByPrimaryKey(checkAssignment.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (checkAssignment.getPhone() != null) {
//			CheckAssignment checkConflict = mapper.selectByPhone(checkAssignment.getPhone(), checkAssignment.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(checkAssignment);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CheckAssignment checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

