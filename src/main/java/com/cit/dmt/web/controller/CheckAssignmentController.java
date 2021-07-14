package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.dmt.core.model.CheckAssignment;
import com.cit.dmt.core.service.CheckAssignmentService;
import com.cit.dmt.web.vo.FinalTransAssignmentSts;
import com.github.pagehelper.Page;

import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/checkassignment")
public class CheckAssignmentController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CheckAssignmentController.class);

	@Autowired
	private CheckAssignmentService checkAssignmentService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CheckAssignment criteria) throws BusinessException {

		Page<CheckAssignment> searchResult = this.checkAssignmentService.selectPaged(crp, criteria);

		ListData<CheckAssignment> wrapper = new ListData<CheckAssignment>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CheckAssignment checkAssignment = this.checkAssignmentService.selectById(id);
		if (checkAssignment == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(checkAssignment);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CheckAssignment checkAssignment) throws BusinessException {
		this.checkCreate(checkAssignment);

		int result = this.checkAssignmentService.save(checkAssignment, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CheckAssignment checkAssignment) throws BusinessException {
		this.checkCreate(checkAssignment);

		int result = this.checkAssignmentService.update(checkAssignment, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.checkAssignmentService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	@GetMapping(value = { "/sts" })
	public Root assignmentSts(CheckAssignment criteria) throws BusinessException {
		FinalTransAssignmentSts assignmentSts = new FinalTransAssignmentSts();
		if (criteria != null) {
			if (!NumberUtils.isNull(criteria.getProjectId()) && !NumberUtils.isNull(criteria.getTaskId()) && !NumberUtils.isNull(criteria.getRoundId())) {
				assignmentSts = this.checkAssignmentService.getAssignmentSts(criteria.getProjectId(), criteria.getTaskId(), criteria.getRoundId());
			}
		}

		return Root.create( assignmentSts);
	}

	@PostMapping(value = { "/add" })
	public Root add(@RequestBody CheckAssignment checkAssignment) throws BusinessException {
		this.checkAssignment(checkAssignment);
		this.checkAssignmentService.add(checkAssignment, authContext.get());
		return Root.create(null);
	}

	@PostMapping(value = { "/withdraw" })
	public Root withdraw(@RequestBody CheckAssignment checkAssignment) throws BusinessException {
		this.checkAssignment(checkAssignment);
		this.checkAssignmentService.withdraw(checkAssignment, authContext.get());
		return Root.create(null);
	}
	
	private void checkCreate(CheckAssignment checkAssignment) throws BusinessException {
		if (checkAssignment == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (isEmpty(checkAssignment.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"项目不能为空。");
		}
		
		if (isEmpty(checkAssignment.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务不能为空。");
		}
		
		if (isEmpty(checkAssignment.getRoundId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"终译任务不能为空。");
		}
		
		if (isEmpty(checkAssignment.getUserId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"专家不能为空。");
		}

		if (NumberUtils.compares(checkAssignment.getAmount(), Integer.valueOf(0)) < 0) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确分配数量。");
		}
//		if (isEmpty(checkAssignment.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(checkAssignment.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, checkAssignment.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}
	
	private void checkAssignment(CheckAssignment checkAssignment) throws BusinessException {
		if (checkAssignment == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (isEmpty(checkAssignment.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"项目不能为空。");
		}
		
		if (isEmpty(checkAssignment.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务不能为空。");
		}
		
		if (isEmpty(checkAssignment.getRoundId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"终译任务不能为空。");
		}
		
		if (isEmpty(checkAssignment.getUserId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"专家不能为空。");
		}

		if (NumberUtils.compares(checkAssignment.getAmount(), Integer.valueOf(0)) < 0) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确数量。");
		}
	}

	
//	private void checkUpdate(CheckAssignment checkAssignment) throws BusinessException {
//		if (checkAssignment == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
//		}
////		if (checkAssignment.getUserName() != null) {
////			if (isEmpty(checkAssignment.getUserName())) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
////			}
////			if (overLength(checkAssignment.getUserName(), 50)) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
////			}
////		}
//	}
}
