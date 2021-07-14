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

import com.cit.dmt.core.model.CtRoman;
import com.cit.dmt.core.service.CtRomanService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ctroman")
public class CtRomanController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtRomanController.class);

	@Autowired
	private CtRomanService ctRomanService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtRoman criteria) throws BusinessException {

		Page<CtRoman> searchResult = this.ctRomanService.selectPaged(crp, criteria);

		ListData<CtRoman> wrapper = new ListData<CtRoman>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtRoman ctRoman = this.ctRomanService.selectById(id);
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctRoman);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtRoman ctRoman) throws BusinessException {
		this.checkCreate(ctRoman);

		int result = this.ctRomanService.save(ctRoman, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtRoman ctRoman) throws BusinessException {
		this.checkUpdate(ctRoman);

		int result = this.ctRomanService.update(ctRoman, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctRomanService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtRoman ctRoman) throws BusinessException {
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(ctRoman.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(ctRoman.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, ctRoman.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CtRoman ctRoman) throws BusinessException {
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (ctRoman.getUserName() != null) {
//			if (isEmpty(ctRoman.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(ctRoman.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
