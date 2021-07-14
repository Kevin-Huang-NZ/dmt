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

import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.service.SysRoleFunService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/sysrolefun")
public class SysRoleFunController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SysRoleFunController.class);

	@Autowired
	private SysRoleFunService sysRoleFunService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, SysRoleFun criteria) throws BusinessException {

		Page<SysRoleFun> searchResult = this.sysRoleFunService.selectPaged(crp, criteria);

		ListData<SysRoleFun> wrapper = new ListData<SysRoleFun>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		SysRoleFun sysRoleFun = this.sysRoleFunService.selectById(id);
		if (sysRoleFun == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(sysRoleFun);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody SysRoleFun sysRoleFun) throws BusinessException {
		this.checkCreate(sysRoleFun);

		int result = this.sysRoleFunService.save(sysRoleFun, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody SysRoleFun sysRoleFun) throws BusinessException {
		this.checkUpdate(sysRoleFun);

		int result = this.sysRoleFunService.update(sysRoleFun, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.sysRoleFunService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(SysRoleFun sysRoleFun) throws BusinessException {
		if (sysRoleFun == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(sysRoleFun.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(sysRoleFun.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, sysRoleFun.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(SysRoleFun sysRoleFun) throws BusinessException {
		if (sysRoleFun == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (sysRoleFun.getUserName() != null) {
//			if (isEmpty(sysRoleFun.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(sysRoleFun.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
