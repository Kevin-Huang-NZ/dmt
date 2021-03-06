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

import com.cit.dmt.core.model.CheckRound;
import com.cit.dmt.core.service.CheckRoundService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/checkround")
public class CheckRoundController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CheckRoundController.class);

	@Autowired
	private CheckRoundService checkRoundService;
	private final String[] CHECK_ROUND_STATUS = {"0", "1", "9"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CheckRound criteria) throws BusinessException {

		Page<CheckRound> searchResult = this.checkRoundService.selectPaged(crp, criteria);

		ListData<CheckRound> wrapper = new ListData<CheckRound>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CheckRound checkRound = this.checkRoundService.selectById(id);
		if (checkRound == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(checkRound);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CheckRound checkRound) throws BusinessException {
		this.checkCreate(checkRound);

		int result = this.checkRoundService.save(checkRound, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CheckRound checkRound) throws BusinessException {
		this.checkCreate(checkRound);

		int result = this.checkRoundService.update(checkRound, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.checkRoundService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@PostMapping(value = { "/assign" })
	public Root assign(@RequestBody CheckRound checkRound) throws BusinessException {
		this.checkAssign(checkRound);
		this.checkRoundService.assign(checkRound, authContext.get());
		return Root.create(null);
	}
	
	private void checkCreate(CheckRound checkRound) throws BusinessException {
		if (checkRound == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(checkRound.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		
		if (isEmpty(checkRound.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		
		if (checkRound.getRoundName() != null) {
			if (isEmpty(checkRound.getRoundName())) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
			}
			if (overLength(checkRound.getRoundName(), 100)) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????100????????????");
			}
		}
		if (!patternCheck(PATTERN_YYYY_MM_DD, checkRound.getStartDate())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????");
		}

		if (!patternCheck(PATTERN_YYYY_MM_DD, checkRound.getDueDate())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
		}
		
		if (checkRound.getStartDate().compareTo(checkRound.getDueDate()) > 0) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????????????????");
		}

		if (isEmpty(checkRound.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
		}
		if (!Arrays.asList(CHECK_ROUND_STATUS).contains(checkRound.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
		}
		
		if (overLength(checkRound.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
		}
	
	}
	
	private void checkAssign(CheckRound checkRound) throws BusinessException {
		if (checkRound == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(checkRound.getId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(checkRound.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(checkRound.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
	
	}

	
//	private void checkUpdate(CheckRound checkRound) throws BusinessException {
//		if (checkRound == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
//		
//		if (isEmpty(checkRound.getProjectId())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//		}
//		
//		if (isEmpty(checkRound.getTaskId())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//		}
//		
//		if (checkRound.getRoundName() != null) {
//			if (isEmpty(checkRound.getRoundName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
//			}
//			if (overLength(checkRound.getRoundName(), 100)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????100????????????");
//			}
//		}
//		if (!patternCheck(PATTERN_YYYY_MM_DD, checkRound.getStartDate())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????");
//		}
//
//		if (!patternCheck(PATTERN_YYYY_MM_DD, checkRound.getDueDate())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
//		}
//		
//		if (checkRound.getStartDate().compareTo(checkRound.getDueDate()) > 0) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"??????????????????????????????????????????");
//		}
//
//		if (isEmpty(checkRound.getStatus())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
//		}
//		if (!Arrays.asList(CHECK_ROUND_STATUS).contains(checkRound.getStatus())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
//		}
//		
//		if (overLength(checkRound.getMemo(), 500)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
//		}
//	}
}
