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
		this.checkUpdate(checkRound);

		int result = this.checkRoundService.update(checkRound, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.checkRoundService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CheckRound checkRound) throws BusinessException {
		if (checkRound == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(checkRound.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(checkRound.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, checkRound.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CheckRound checkRound) throws BusinessException {
		if (checkRound == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (checkRound.getUserName() != null) {
//			if (isEmpty(checkRound.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(checkRound.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
