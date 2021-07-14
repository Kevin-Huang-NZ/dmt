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

import com.cit.dmt.core.model.SysFun;
import com.cit.dmt.core.service.SysFunService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/sysfun")
public class SysFunController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SysFunController.class);

	@Autowired
	private SysFunService sysFunService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, SysFun criteria) throws BusinessException {

		Page<SysFun> searchResult = this.sysFunService.selectPaged(crp, criteria);

		ListData<SysFun> wrapper = new ListData<SysFun>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		SysFun sysFun = this.sysFunService.selectById(id);
		if (sysFun == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(sysFun);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody SysFun sysFun) throws BusinessException {
		this.checkCreate(sysFun);

		int result = this.sysFunService.save(sysFun, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody SysFun sysFun) throws BusinessException {
		this.checkUpdate(sysFun);

		int result = this.sysFunService.update(sysFun, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.sysFunService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(SysFun sysFun) throws BusinessException {
		if (sysFun == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(sysFun.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(sysFun.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, sysFun.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(SysFun sysFun) throws BusinessException {
		if (sysFun == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (sysFun.getUserName() != null) {
//			if (isEmpty(sysFun.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(sysFun.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
