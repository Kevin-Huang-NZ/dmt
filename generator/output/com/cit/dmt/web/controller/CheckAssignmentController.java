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
import com.github.pagehelper.Page;

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
		this.checkUpdate(checkAssignment);

		int result = this.checkAssignmentService.update(checkAssignment, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.checkAssignmentService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CheckAssignment checkAssignment) throws BusinessException {
		if (checkAssignment == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
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

	
	private void checkUpdate(CheckAssignment checkAssignment) throws BusinessException {
		if (checkAssignment == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (checkAssignment.getUserName() != null) {
//			if (isEmpty(checkAssignment.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(checkAssignment.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
