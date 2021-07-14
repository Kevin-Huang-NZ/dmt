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
import com.cit.dmt.core.dao.CheckAssignmentMapper;
import com.cit.dmt.core.dao.TaskDetailMapper;
import com.cit.dmt.core.model.CheckAssignment;
import com.cit.dmt.core.model.CheckRoundAssignmentSts;
import com.cit.dmt.core.service.CheckAssignmentService;
import com.cit.dmt.web.vo.FinalTransAssignmentSts;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckAssignmentServiceImpl implements CheckAssignmentService {
	Logger logger = LoggerFactory.getLogger(CheckAssignmentService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckAssignmentMapper mapper;
	@Autowired
	private TaskDetailMapper taskDetailMapper;
	@Autowired
	private CheckAssignmentDetailMapper cadMapper;
	
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
		}
		
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
		CheckAssignment checkConflict = mapper.selectByUserId(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "同一个终译轮数，一个专家只能有一个分配数量。");
		}
		Integer assignedAmount = mapper.getAssignedAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId());
		Integer total = taskDetailMapper.getTaskDetailAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId());
		if (assignedAmount == null) {
			assignedAmount = 0;
		}
		assignedAmount = assignedAmount + checkAssignment.getAmount();
		if (NumberUtils.compares(assignedAmount, total) == 1) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "分配总数量超过了外语地名总数。");
		}
		checkAssignment.setAssignStatus("0");
		checkAssignment.setDone(0);
		return mapper.insert(checkAssignment);
	}

	@Override
	public int update(CheckAssignment checkAssignment, User loginUser) throws BusinessException {
		CheckAssignment checkExist = mapper.selectByPrimaryKey(checkAssignment.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (StringUtils.equals(checkExist.getAssignStatus(), "1") ) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "已实际分配，不可以更新。");
		}
		
		CheckAssignment checkConflict = mapper.selectByUserId(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), checkAssignment.getId());
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "同一个终译轮数，一个专家只能有一个分配数量。");
		}
		Integer assignedAmount = mapper.getAssignedAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId());
		Integer total = taskDetailMapper.getTaskDetailAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId());
		if (assignedAmount == null) {
			assignedAmount = 0;
		}
		assignedAmount = assignedAmount - checkExist.getAmount() + checkAssignment.getAmount();
		if (NumberUtils.compares(assignedAmount, total) == 1) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "分配总数量超过了外语地名总数。");
		}
		return mapper.updateByPrimaryKeySelective(checkAssignment);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CheckAssignment checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (StringUtils.equals(checkExist.getAssignStatus(), "1") ) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "已实际分配，不可以删除。");
		}
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public FinalTransAssignmentSts getAssignmentSts(Long projectId, Long taskId, Long roundId) {
		FinalTransAssignmentSts finalTransAssignmentSts = new FinalTransAssignmentSts();
		
		Integer assigned = mapper.getAssignedAmount(projectId, taskId, roundId);
		if (assigned == null) {
			assigned = 0;
		}
		Integer total = taskDetailMapper.getTaskDetailAmount(projectId, taskId);
		if (total == null) {
			total = 0;
		}
		
		Integer remained = total - assigned;
		if (remained < 0) {
			remained = 0;
		}
		finalTransAssignmentSts.setTotal(total);
		finalTransAssignmentSts.setAssigned(assigned);
		finalTransAssignmentSts.setRemained(remained);
		return finalTransAssignmentSts;
	}

	@Override
	@Transactional
	public void add(CheckAssignment checkAssignment, User loginUser) throws BusinessException {
		Integer assignedAmount = mapper.getAssignedAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId());
		Integer total = taskDetailMapper.getTaskDetailAmount(checkAssignment.getProjectId(), checkAssignment.getTaskId());
		if (assignedAmount == null) {
			assignedAmount = 0;
		}
		assignedAmount = assignedAmount + checkAssignment.getAmount();
		if (NumberUtils.compares(assignedAmount, total) == 1) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "分配总数量超过了外语地名总数。");
		}
		CheckAssignment existAssignment = mapper.selectByUserId(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), null);
		if (existAssignment == null) {
			checkAssignment.setAssignStatus("1");
			checkAssignment.setDone(0);
			mapper.insert(checkAssignment);
		} else {
			existAssignment.setAmount(existAssignment.getAmount() + checkAssignment.getAmount());
			mapper.updateByPrimaryKeySelective(existAssignment);
		}
		cadMapper.taskAssign(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), checkAssignment.getAmount());
	}

	@Override
	@Transactional
	public void withdraw(CheckAssignment checkAssignment, User loginUser) throws BusinessException {
		Map<String, Object> criteriaCad = new HashMap<String, Object>();
		criteriaCad.put("projectId", checkAssignment.getProjectId());
		criteriaCad.put("taskId", checkAssignment.getTaskId());
		criteriaCad.put("roundId", checkAssignment.getRoundId());
		criteriaCad.put("userId", checkAssignment.getUserId());
		CheckRoundAssignmentSts craSts = cadMapper.getTaskCount(criteriaCad);
		if (craSts.getWaitingCheck() == null || craSts.getWaitingCheck().compareTo(checkAssignment.getAmount()) < 0) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "撤回数量超过了该专家未终译完的数量。");
		}
		CheckAssignment existAssignment = mapper.selectByUserId(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), null);
		existAssignment.setAmount(existAssignment.getAmount() - checkAssignment.getAmount());
		mapper.updateByPrimaryKeySelective(existAssignment);
		cadMapper.withdrawAssign(checkAssignment.getProjectId(), checkAssignment.getTaskId(), checkAssignment.getRoundId(), checkAssignment.getUserId(), "0",checkAssignment.getAmount());
	}

}

