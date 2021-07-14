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

import com.cit.dmt.core.model.CheckAssignmentDetail;
import com.cit.dmt.core.model.FinalTrans;
import com.cit.dmt.core.service.CheckAssignmentDetailService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/checkassignmentdetail")
public class CheckAssignmentDetailController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CheckAssignmentDetailController.class);

	@Autowired
	private CheckAssignmentDetailService checkAssignmentDetailService;
	
//	private final String[] CHECK_STATUS = {"0", "1"};
	
	private final String[] ADOPTION_STATUS = {"1", "2"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CheckAssignmentDetail criteria) throws BusinessException {

		Page<FinalTrans> searchResult = this.checkAssignmentDetailService.selectPaged(crp, criteria);

		ListData<FinalTrans> wrapper = new ListData<FinalTrans>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		FinalTrans finalTrans = this.checkAssignmentDetailService.selectById(id);
		if (finalTrans == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(finalTrans);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		this.checkCreate(checkAssignmentDetail);

		int result = this.checkAssignmentDetailService.save(checkAssignmentDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		this.checkUpdate(checkAssignmentDetail);

		int result = this.checkAssignmentDetailService.update(checkAssignmentDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.checkAssignmentDetailService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@PostMapping(value = { "/uchr" })
	public Root updateCheckResult(@RequestBody CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		this.checkCheckResult(checkAssignmentDetail);

		int result = this.checkAssignmentDetailService.updateCheckResult(checkAssignmentDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/uas" })
	public Root updateAdoptionById(@RequestBody CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		this.checkUpdateAdoptionById(checkAssignmentDetail);

		int result = this.checkAssignmentDetailService.updateAdoptionById(checkAssignmentDetail.getId(), checkAssignmentDetail.getAdoptionStatus(), authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/uasb" })
	public Root updateAdoptionBatch(@RequestBody CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		this.checkUpdateAdoptionAll(checkAssignmentDetail);

		int result = this.checkAssignmentDetailService.updateAdoptionAll(checkAssignmentDetail, authContext.get());

		return Root.create(result);
	}
	
	private void checkCheckResult(CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		if (checkAssignmentDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (checkAssignmentDetail.getId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}

		if (isEmpty(checkAssignmentDetail.getCheckResult())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"核对结果不能为空。");
		}
		
		if (overLength(checkAssignmentDetail.getCheckResult(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"核对结果超长，最多200个字符。");
		}
		
		if (overLength(checkAssignmentDetail.getCheckMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"核对备注超长，最多500个字符。");
		}
	}
	
	private void checkUpdateAdoptionById(CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		if (checkAssignmentDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (checkAssignmentDetail.getId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}

		if (isEmpty(checkAssignmentDetail.getAdoptionStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"采纳状态不能为空。");
		}
		if (!Arrays.asList(ADOPTION_STATUS).contains(checkAssignmentDetail.getAdoptionStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"采纳状态参数非法。");
		}

	}
	
	private void checkUpdateAdoptionAll(CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		if (checkAssignmentDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (checkAssignmentDetail.getProjectId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (checkAssignmentDetail.getTaskId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (checkAssignmentDetail.getRoundId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}

		if (isEmpty(checkAssignmentDetail.getAdoptionStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"采纳状态不能为空。");
		}
		if (!Arrays.asList(ADOPTION_STATUS).contains(checkAssignmentDetail.getAdoptionStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"采纳状态参数非法。");
		}
	}
	
	private void checkCreate(CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		if (checkAssignmentDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(checkAssignmentDetail.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(checkAssignmentDetail.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, checkAssignmentDetail.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CheckAssignmentDetail checkAssignmentDetail) throws BusinessException {
		if (checkAssignmentDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (checkAssignmentDetail.getUserName() != null) {
//			if (isEmpty(checkAssignmentDetail.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(checkAssignmentDetail.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
