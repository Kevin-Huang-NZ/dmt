package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.service.ProjectService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;
	
	private final String[] PROJECT_STATUS = {"1", "9"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, Project criteria) throws BusinessException {

		Page<Project> searchResult = this.projectService.selectPaged(crp, criteria);

		ListData<Project> wrapper = new ListData<Project>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		Project project = this.projectService.selectById(id);
		if (project == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(project);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody Project project) throws BusinessException {
		this.checkCreate(project);

		int result = this.projectService.save(project, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody Project project) throws BusinessException {
		this.checkCreate(project);

		int result = this.projectService.update(project, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.projectService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(Project project) throws BusinessException {
		if (project == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(project.getProjectName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(project.getProjectName(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????100????????????");
		}
		
		if (isEmpty(project.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(project.getCountryCode(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}

		if (!patternCheck(PATTERN_YYYY_MM_DD, project.getStartDate())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????");
		}

		if (!patternCheck(PATTERN_YYYY_MM_DD, project.getDueDate())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
		}
		
		if (project.getStartDate().compareTo(project.getDueDate()) > 0) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????????????????");
		}

		if (isEmpty(project.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (!Arrays.asList(PROJECT_STATUS).contains(project.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		
		if (overLength(project.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
		}
	}

	
//	private void checkUpdate(Project project) throws BusinessException {
//		if (project == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
//		if (project.getProjectName() != null) {
//			if (isEmpty(project.getProjectName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//			if (overLength(project.getProjectName(), 100)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????100????????????");
//			}
//		}
//
//		if (project.getCountryCode() != null) {
//			if (isEmpty(project.getCountryCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//			}
//			if (overLength(project.getCountryCode(), 10)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//			}
//		}
//
//		if (project.getStartDate() != null) {
//			if (patternCheck(PATTERN_YYYY_MM_DD, project.getStartDate())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????");
//			}
//		}
//
//		if (project.getDueDate() != null) {
//			if (patternCheck(PATTERN_YYYY_MM_DD, project.getDueDate())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????");
//			}
//		}
//
//		if (project.getProjectName() != null && project.getDueDate() != null) {
//			if (project.getStartDate().compareTo(project.getDueDate()) > 0) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
//			}
//		}
//
//		if (project.getStatus() != null) {
//			if (isEmpty(project.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//			if (!Arrays.asList(PROJECT_STATE).contains(project.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//		}
//
//		if (project.getMemo() != null) {
//			if (overLength(project.getMemo(), 500)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
//			}
//		}
//	}
}
