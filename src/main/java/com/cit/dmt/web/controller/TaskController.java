package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.dmt.core.model.Task;
import com.cit.dmt.core.service.TaskService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/task")
public class TaskController extends BaseController {

	Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TaskService taskService;
	
	private final String[] NEED_ROMAN = {"0", "1"};
	
	private final String[] TASK_STATE = {"1", "2", "9"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, Task criteria) throws BusinessException {

		Page<Task> searchResult = this.taskService.selectPaged(crp, criteria);

		ListData<Task> wrapper = new ListData<Task>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		Task task = this.taskService.selectById(id);
		if (task == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(task);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody Task task) throws BusinessException {
		this.checkCreate(task);

		int result = this.taskService.save(task, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody Task task) throws BusinessException {
		this.checkCreate(task);

		int result = this.taskService.update(task, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/er" })
	public Root execRoman(@RequestBody IdWrap id) throws BusinessException {
		StopWatch sw = new StopWatch();
		sw.start("????????????");
		this.taskService.execRoman(id.getId());
		sw.stop();
		logger.info(sw.prettyPrint());
		return Root.create(null);
	}

	@PostMapping(value = { "/et" })
	public Root execTrans(@RequestBody IdWrap id) throws BusinessException {
		StopWatch sw = new StopWatch();
		sw.start("??????");
		this.taskService.execTrans(id.getId());
		sw.stop();
		logger.info(sw.prettyPrint());
		return Root.create(null);
	}

	private void checkCreate(Task task) throws BusinessException {
		if (task == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}

		if (isEmpty(task.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		

		if (isEmpty(task.getTaskName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(task.getTaskName(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????100????????????");
		}
		
		if (isEmpty(task.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(task.getLanguageCode(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}

		if (isEmpty(task.getNeedRoman())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
		}
		if (!Arrays.asList(NEED_ROMAN).contains(task.getNeedRoman())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
		}

		if (isEmpty(task.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (!Arrays.asList(TASK_STATE).contains(task.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		
		if (overLength(task.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
		}
	}

	
//	private void checkUpdate(Task task) throws BusinessException {
//		if (task == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
//		
//		if (task.getProjectId() != null) {
//			if (isEmpty(task.getProjectId())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//			}
//		}
//
//		if (task.getTaskName() != null) {
//			if (isEmpty(task.getTaskName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//			if (overLength(task.getTaskName(), 100)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????100????????????");
//			}
//		}
//
//		if (task.getLanguageCode() != null) {
//			if (isEmpty(task.getLanguageCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//			}
//			if (overLength(task.getLanguageCode(), 10)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
//			}
//		}
//
//		if (task.getNeedRoman() != null) {
//			if (isEmpty(task.getNeedRoman())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
//			}
//			if (!Arrays.asList(NEED_ROMAN).contains(task.getNeedRoman())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????");
//			}
//		}
//
//		if (task.getStatus() != null) {
//			if (isEmpty(task.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//			if (!Arrays.asList(TASK_STATE).contains(task.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
//			}
//		}
//
//		if (task.getMemo() != null) {
//			if (overLength(task.getMemo(), 500)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
//			}
//		}
//	}
}
