package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;

import com.cit.dmt.core.dao.CheckAssignmentDetailMapper;
import com.cit.dmt.core.dao.CheckAssignmentMapper;
import com.cit.dmt.core.dao.CheckRoundMapper;
import com.cit.dmt.core.dao.ProjectMapper;
import com.cit.dmt.core.dao.TaskMapper;
import com.cit.dmt.core.model.CheckAssignment;
import com.cit.dmt.core.model.CheckRound;
import com.cit.dmt.core.model.CheckRoundAssignmentSts;
import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.model.Task;
import com.cit.dmt.core.service.CheckRoundService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CheckRoundServiceImpl implements CheckRoundService {
	Logger logger = LoggerFactory.getLogger(CheckRoundService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CheckRoundMapper mapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private CheckAssignmentMapper caMapper;
	@Autowired
	private CheckAssignmentDetailMapper cadMapper;
	
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
		if (qp != null) {
			if (!NumberUtils.isNull(qp.getProjectId())) {
				criteria.put("projectId", qp.getProjectId());
			}
			if (!NumberUtils.isNull(qp.getTaskId())) {
				criteria.put("taskId", qp.getTaskId());
			}
			if (!StringUtils.isEmpty(qp.getStatus())) {
				criteria.put("status", qp.getStatus());
			}
			if (!StringUtils.isEmpty(qp.getRoundName())) {
				criteria.put("roundName", "%" + qp.getRoundName() + "%");
			}
		}
		
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
		Project project = projectMapper.selectByPrimaryKey(checkRound.getProjectId());
		if (project == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
		}
		Task task = taskMapper.selectByPrimaryKey(checkRound.getTaskId());
		if (task == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "任务不存在。");
		}
		CheckRound checkConflict = mapper.selectByName(checkRound.getProjectId(), checkRound.getTaskId(), checkRound.getRoundName(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "终译轮数名称重复。");
		}
		return mapper.insert(checkRound);
	}

	@Override
	public int update(CheckRound checkRound, User loginUser) throws BusinessException {
		CheckRound checkExist = mapper.selectByPrimaryKey(checkRound.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (checkRound.getRoundName() != null) {
			CheckRound checkConflict = mapper.selectByName(checkRound.getProjectId(), checkRound.getTaskId(), checkRound.getRoundName(), checkRound.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "终译轮数名称重复。");
			}
		}
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

	@Override
	public void assign(CheckRound checkRound, User loginUser) throws BusinessException {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("roundId", checkRound.getId());
		criteria.put("assignStatus", "0");
		Page<CheckAssignment> caList = caMapper.selectPaged(criteria);
		if (caList != null && caList.size() > 0) {
			// 先判断是不是第一次执行实际分配
			Map<String, Object> criteriaCad = new HashMap<String, Object>();
			criteriaCad.put("projectId", checkRound.getProjectId());
			criteriaCad.put("taskId", checkRound.getTaskId());
			criteriaCad.put("roundId", checkRound.getId());
			CheckRoundAssignmentSts craSts = cadMapper.getTaskCount(criteriaCad);
			if (craSts != null && NumberUtils.compares(craSts.getTotal(), 0) == 0) {
				// 是第一次实际分配，将task_detail表中的所有外文地名拷贝到check_assignment_detail表
				cadMapper.insertAllTask(checkRound.getProjectId(), checkRound.getTaskId(), checkRound.getId());
			}
			// 循环分配
			for (CheckAssignment ca : caList) {
				if (NumberUtils.compares(ca.getAmount(), 0) == 1) {
					cadMapper.taskAssign(ca.getProjectId(), ca.getTaskId(), ca.getRoundId(), ca.getUserId(), ca.getAmount());
					ca.setAssignStatus("1");
					caMapper.updateByPrimaryKeySelective(ca);
				}
			}
		}
	}

}

