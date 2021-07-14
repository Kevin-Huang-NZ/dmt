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

import com.cit.dmt.core.model.CtTransliteration;
import com.cit.dmt.core.service.CtTransliterationService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/cttransliteration")
public class CtTransliterationController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtTransliterationController.class);

	@Autowired
	private CtTransliterationService ctTransliterationService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtTransliteration criteria) throws BusinessException {

		Page<CtTransliteration> searchResult = this.ctTransliterationService.selectPaged(crp, criteria);

		ListData<CtTransliteration> wrapper = new ListData<CtTransliteration>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtTransliteration ctTransliteration = this.ctTransliterationService.selectById(id);
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctTransliteration);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtTransliteration ctTransliteration) throws BusinessException {
		this.checkCreate(ctTransliteration);

		int result = this.ctTransliterationService.save(ctTransliteration, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtTransliteration ctTransliteration) throws BusinessException {
		this.checkUpdate(ctTransliteration);

		int result = this.ctTransliterationService.update(ctTransliteration, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctTransliterationService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtTransliteration ctTransliteration) throws BusinessException {
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(ctTransliteration.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(ctTransliteration.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, ctTransliteration.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CtTransliteration ctTransliteration) throws BusinessException {
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (ctTransliteration.getUserName() != null) {
//			if (isEmpty(ctTransliteration.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(ctTransliteration.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
