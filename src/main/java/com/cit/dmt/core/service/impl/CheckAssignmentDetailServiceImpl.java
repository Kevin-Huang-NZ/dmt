package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CheckAssignmentDetailMapper;
import com.cit.dmt.core.dao.TaskDetailMapper;
import com.cit.dmt.core.model.CheckAssignmentDetail;
import com.cit.dmt.core.model.FinalTrans;
import com.cit.dmt.core.service.CheckAssignmentDetailService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckAssignmentDetailServiceImpl implements CheckAssignmentDetailService {
	Logger logger = LoggerFactory.getLogger(CheckAssignmentDetailService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckAssignmentDetailMapper mapper;
	@Autowired
	private TaskDetailMapper taskDetailMapper;
	
	@Override
	public Page<FinalTrans> selectPaged(CommonRequestParam crp, CheckAssignmentDetail qp) throws BusinessException {
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
			if (!NumberUtils.isNull(qp.getProjectId())) {
				criteria.put("projectId", qp.getProjectId());
			}
			if (!NumberUtils.isNull(qp.getTaskId())) {
				criteria.put("taskId", qp.getTaskId());
			}
			if (!NumberUtils.isNull(qp.getRoundId())) {
				criteria.put("roundId", qp.getRoundId());
			}
			if (!NumberUtils.isNull(qp.getUserId())) {
				criteria.put("userId", qp.getUserId());
			}
			if (!StringUtils.isEmpty(qp.getCheckStatus())) {
				criteria.put("checkStatus", qp.getCheckStatus());
			}
			if (!StringUtils.isEmpty(qp.getAdoptionStatus())) {
				criteria.put("adoptionStatus", qp.getAdoptionStatus());
			}
		}
		
		Page<FinalTrans> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public FinalTrans selectById(Long id) {
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
		FinalTrans checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int updateCheckResult(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException {
		FinalTrans checkExist = mapper.selectByPrimaryKey(checkAssignmentDetail.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
//		logger.info("终译任务专家id：{}；当前登录用户id：{}", checkExist.getUserId(), loginUser.getId());
		if (checkExist.getUserId() == null || checkExist.getUserId().compareTo(loginUser.getId()) != 0) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		return mapper.updateCheckResult(checkAssignmentDetail.getId(), checkAssignmentDetail.getCheckResult(), checkAssignmentDetail.getCheckMemo());
	}

	@Override
	@Transactional
	public int updateAdoptionById(Long id, String adoptionStatus, User loginUser) throws BusinessException {
		FinalTrans checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		if (!StringUtils.equals(checkExist.getCheckStatus(), "1")) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "还未核对，不能采纳/拒绝。");
		}
		if (StringUtils.equals(adoptionStatus, "1")) {
			taskDetailMapper.finalTrans(checkExist.getTaskDetailId(), checkExist.getCheckResult());	
		}
		return mapper.updateAdoptionById(id, adoptionStatus);
	}

	@Override
	@Transactional
	public int updateAdoptionAll(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException {

		if (StringUtils.equals(checkAssignmentDetail.getAdoptionStatus(), "1")) {
			taskDetailMapper.finalTransBatch(checkAssignmentDetail.getProjectId(), checkAssignmentDetail.getTaskId(), checkAssignmentDetail.getRoundId(), checkAssignmentDetail.getUserId());	
		}
		return mapper.updateAdoptionAll(checkAssignmentDetail.getProjectId(), checkAssignmentDetail.getTaskId(), checkAssignmentDetail.getRoundId(), checkAssignmentDetail.getUserId(), checkAssignmentDetail.getAdoptionStatus());
	}

}

