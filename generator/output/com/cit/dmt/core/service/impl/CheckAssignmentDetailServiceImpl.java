package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CheckAssignmentDetailMapper;
import com.cit.dmt.core.model.CheckAssignmentDetail;
import com.cit.dmt.core.service.CheckAssignmentDetailService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckAssignmentDetailServiceImpl implements CheckAssignmentDetailService {
	Logger logger = LoggerFactory.getLogger(CheckAssignmentDetailService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckAssignmentDetailMapper mapper;
	
	@Override
	public Page<CheckAssignmentDetail> selectPaged(CommonRequestParam crp, CheckAssignmentDetail qp) throws BusinessException {
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
		
		Page<CheckAssignmentDetail> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CheckAssignmentDetail selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException {
//		CheckAssignmentDetail checkConflict = mapper.selectByPhone(checkAssignmentDetail.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(checkAssignmentDetail);
	}

	@Override
	public int update(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException {
		CheckAssignmentDetail checkExist = mapper.selectByPrimaryKey(checkAssignmentDetail.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (checkAssignmentDetail.getPhone() != null) {
//			CheckAssignmentDetail checkConflict = mapper.selectByPhone(checkAssignmentDetail.getPhone(), checkAssignmentDetail.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(checkAssignmentDetail);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CheckAssignmentDetail checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

