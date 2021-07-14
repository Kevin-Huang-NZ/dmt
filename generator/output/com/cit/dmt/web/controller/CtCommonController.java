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

import com.cit.dmt.core.model.CtCommon;
import com.cit.dmt.core.service.CtCommonService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ctcommon")
public class CtCommonController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtCommonController.class);

	@Autowired
	private CtCommonService ctCommonService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtCommon criteria) throws BusinessException {

		Page<CtCommon> searchResult = this.ctCommonService.selectPaged(crp, criteria);

		ListData<CtCommon> wrapper = new ListData<CtCommon>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtCommon ctCommon = this.ctCommonService.selectById(id);
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctCommon);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtCommon ctCommon) throws BusinessException {
		this.checkCreate(ctCommon);

		int result = this.ctCommonService.save(ctCommon, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtCommon ctCommon) throws BusinessException {
		this.checkUpdate(ctCommon);

		int result = this.ctCommonService.update(ctCommon, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctCommonService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtCommon ctCommon) throws BusinessException {
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(ctCommon.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(ctCommon.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, ctCommon.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CtCommon ctCommon) throws BusinessException {
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (ctCommon.getUserName() != null) {
//			if (isEmpty(ctCommon.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(ctCommon.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
