package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.util.List;

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
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/sysrolefun")
public class SysRoleFunController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SysRoleFunController.class);

	@Autowired
	private SysRoleFunService sysRoleFunService;
	
	@GetMapping(value = { "/l" })
	public Root list(String roleNo) throws BusinessException {

		List<SysRoleFun> searchResult = this.sysRoleFunService.selectByRoleNo(roleNo);

		ListData<SysRoleFun> wrapper = new ListData<SysRoleFun>();
		wrapper.setDataList(searchResult); 
		
		return Root.create(wrapper);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody SysRoleFun sysRoleFun) throws BusinessException {
		this.checkCreate(sysRoleFun);

		int result = this.sysRoleFunService.save(sysRoleFun, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody SysRoleFun sysRoleFun) throws BusinessException {
		this.checkCreate(sysRoleFun);
		
		int result = this.sysRoleFunService.deleteByNo(sysRoleFun, authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(SysRoleFun sysRoleFun) throws BusinessException {
		if (sysRoleFun == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(sysRoleFun.getRoleNo())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(sysRoleFun.getRoleNo(), 1)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		
		if (isEmpty(sysRoleFun.getFunNo())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(sysRoleFun.getFunNo(), 80)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
//		if (!patternCheck(PATTERN_MOBILE, sysRoleFun.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
//		}
	}

	
//	private void checkUpdate(SysRoleFun sysRoleFun) throws BusinessException {
//		if (sysRoleFun == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
////		if (sysRoleFun.getUserName() != null) {
////			if (isEmpty(sysRoleFun.getUserName())) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
////			}
////			if (overLength(sysRoleFun.getUserName(), 50)) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
////			}
////		}
//	}
}
