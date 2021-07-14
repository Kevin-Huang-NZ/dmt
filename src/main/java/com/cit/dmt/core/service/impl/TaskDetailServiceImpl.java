package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;

import com.cit.dmt.core.dao.ProjectMapper;
import com.cit.dmt.core.dao.TaskDetailMapper;
import com.cit.dmt.core.dao.TaskMapper;
import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.model.Task;
import com.cit.dmt.core.model.TaskDetail;
import com.cit.dmt.core.service.TaskDetailService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.ListUtils;
import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class TaskDetailServiceImpl implements TaskDetailService {
	Logger logger = LoggerFactory.getLogger(TaskDetailService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private TaskDetailMapper mapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private TaskMapper taskMapper;
	
	@Override
	public Page<TaskDetail> selectPaged(CommonRequestParam crp, TaskDetail qp) throws BusinessException {
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
			if (!StringUtils.isEmpty(qp.getTransStatus())) {
				criteria.put("transStatus", qp.getTransStatus());
			}
			if (!StringUtils.isEmpty(qp.getCheckStatus())) {
				criteria.put("checkStatus", qp.getCheckStatus());
			}
			if (!StringUtils.isEmpty(qp.getOriginal())) {
				criteria.put("original", "%" + qp.getOriginal() + "%");
			}
		}
		
		Page<TaskDetail> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public TaskDetail selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(TaskDetail taskDetail, User loginUser) throws BusinessException {
//		TaskDetail checkConflict = mapper.selectByPhone(taskDetail.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		Project project = projectMapper.selectByPrimaryKey(taskDetail.getProjectId());
		if (project == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
		}
		Task task = taskMapper.selectByPrimaryKey(taskDetail.getTaskId());
		if (task == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "任务不存在。");
		}
		if (StringUtils.equals(task.getNeedRoman(), "1")) {
			taskDetail.setRomanStatus("0");
		} else {
			taskDetail.setRomanStatus("9");
		}
		taskDetail.setTransStatus("0");
		taskDetail.setCheckStatus("0");
		return mapper.insert(taskDetail);
	}

	@Override
	public int update(TaskDetail taskDetail, User loginUser) throws BusinessException {
		TaskDetail checkExist = mapper.selectByPrimaryKey(taskDetail.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (taskDetail.getPhone() != null) {
//			TaskDetail checkConflict = mapper.selectByPhone(taskDetail.getPhone(), taskDetail.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		if (!NumberUtils.equals(taskDetail.getProjectId(), checkExist.getProjectId())) {
			Project project = projectMapper.selectByPrimaryKey(taskDetail.getProjectId());
			if (project == null) {
				throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
			}
		}
		Task task = null;
		if (!NumberUtils.equals(taskDetail.getTaskId(), checkExist.getTaskId())) {
			task = taskMapper.selectByPrimaryKey(taskDetail.getTaskId());
			if (task == null) {
				throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "任务不存在。");
			}
		}
		// 影响翻译结果的字段值更新后，重置状态信息
		if (!StringUtils.equals(taskDetail.getOriginal(), checkExist.getOriginal())
				|| !StringUtils.equals(taskDetail.getGec(), checkExist.getGec())) {
			if (task == null) {
				task = taskMapper.selectByPrimaryKey(taskDetail.getTaskId());
			}
			if (StringUtils.equals(task.getNeedRoman(), "1")) {
				taskDetail.setRomanStatus("0");
			} else {
				taskDetail.setRomanStatus("9");
			}
			taskDetail.setTransStatus("0");
			taskDetail.setCheckStatus("0");
		}
		return mapper.updateByPrimaryKeySelective(taskDetail);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		TaskDetail checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int finalTrans(Long id, String transResult, User loginUser) throws BusinessException {
		TaskDetail checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.finalTrans(id, transResult);
	}

	@Override
	public int saveBatch(Long projectId, Long taskId, List<TaskDetail> taskDetails, User loginUser) throws BusinessException {
		Project project = projectMapper.selectByPrimaryKey(projectId);
		if (project == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
		}
		Task task = taskMapper.selectByPrimaryKey(taskId);
		if (task == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "任务不存在。");
		}

		String romanStatus = "9";
		if (StringUtils.equals(task.getNeedRoman(), "1")) {
			romanStatus = "0";
		}
		
        int totalInsert = 0;
	
	    List<List<TaskDetail>> sepratedList = ListUtils.splitListByCapacity(taskDetails, 500);
	    for (List<TaskDetail> subList : sepratedList) {
	    	totalInsert += mapper.insertBatch(subList, romanStatus);
	    }
	
	    return totalInsert;
	}

	@Override
	public int deleteBatch(Long projectId, Long taskId, User loginUser) throws BusinessException {
		Task task = taskMapper.selectByPrimaryKey(taskId);
		if (task == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "任务不存在。");
		}
		if (StringUtils.equals(task.getStatus(), "2")) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "任务已完成，不能清空数据。");
		}
		return mapper.deleteBatch(projectId, taskId);
	}

}

