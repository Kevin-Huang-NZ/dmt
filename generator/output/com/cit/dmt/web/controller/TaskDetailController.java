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

import com.cit.dmt.core.model.TaskDetail;
import com.cit.dmt.core.service.TaskDetailService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/taskdetail")
public class TaskDetailController extends BaseController {

	Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

	@Autowired
	private TaskDetailService taskDetailService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, TaskDetail criteria) throws BusinessException {

		Page<TaskDetail> searchResult = this.taskDetailService.selectPaged(crp, criteria);

		ListData<TaskDetail> wrapper = new ListData<TaskDetail>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		TaskDetail taskDetail = this.taskDetailService.selectById(id);
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(taskDetail);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkCreate(taskDetail);

		int result = this.taskDetailService.save(taskDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkUpdate(taskDetail);

		int result = this.taskDetailService.update(taskDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.taskDetailService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(TaskDetail taskDetail) throws BusinessException {
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(taskDetail.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(taskDetail.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, taskDetail.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(TaskDetail taskDetail) throws BusinessException {
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (taskDetail.getUserName() != null) {
//			if (isEmpty(taskDetail.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(taskDetail.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
