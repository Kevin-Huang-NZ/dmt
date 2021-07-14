package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.ProjectMapper;
import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.service.ProjectService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class ProjectServiceImpl implements ProjectService {
	Logger logger = LoggerFactory.getLogger(ProjectService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private ProjectMapper mapper;
	
	@Override
	public Page<Project> selectPaged(CommonRequestParam crp, Project qp) throws BusinessException {
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
			if (!StringUtils.isEmpty(qp.getProjectName())) {
				criteria.put("projectName", "%" + qp.getProjectName() + "%");
			}
			if (!StringUtils.isEmpty(qp.getStatus())) {
				criteria.put("status", qp.getStatus());
			}
		}
		
		Page<Project> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public Project selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(Project project, User loginUser) throws BusinessException {
		Project checkConflict = mapper.selectByName(project.getProjectName(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "项目名称重复。");
		}
		return mapper.insert(project);
	}

	@Override
	public int update(Project project, User loginUser) throws BusinessException {
		Project checkExist = mapper.selectByPrimaryKey(project.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (project.getProjectName() != null) {
			Project checkConflict = mapper.selectByName(project.getProjectName(), project.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "项目名称重复。");
			}
		}
		return mapper.updateByPrimaryKeySelective(project);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		Project checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

