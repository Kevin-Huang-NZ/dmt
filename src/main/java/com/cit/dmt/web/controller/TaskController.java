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
		sw.start("罗马转写");
		this.taskService.execRoman(id.getId());
		sw.stop();
		logger.info(sw.prettyPrint());
		return Root.create(null);
	}

	@PostMapping(value = { "/et" })
	public Root execTrans(@RequestBody IdWrap id) throws BusinessException {
		StopWatch sw = new StopWatch();
		sw.start("初译");
		this.taskService.execTrans(id.getId());
		sw.stop();
		logger.info(sw.prettyPrint());
		return Root.create(null);
	}

	private void checkCreate(Task task) throws BusinessException {
		if (task == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}

		if (isEmpty(task.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"项目不能为空。");
		}
		

		if (isEmpty(task.getTaskName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务名称不能为空。");
		}
		if (overLength(task.getTaskName(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务名称超长，最多100个字符。");
		}
		
		if (isEmpty(task.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
		}
		if (overLength(task.getLanguageCode(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种参数非法。");
		}

		if (isEmpty(task.getNeedRoman())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母转换不能为空。");
		}
		if (!Arrays.asList(NEED_ROMAN).contains(task.getNeedRoman())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母转换参数非法。");
		}

		if (isEmpty(task.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务状态不能为空。");
		}
		if (!Arrays.asList(TASK_STATE).contains(task.getStatus())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务状态参数非法。");
		}
		
		if (overLength(task.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"备注超长，最多500个字符。");
		}
	}

	
//	private void checkUpdate(Task task) throws BusinessException {
//		if (task == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
//		}
//		
//		if (task.getProjectId() != null) {
//			if (isEmpty(task.getProjectId())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"项目不能为空。");
//			}
//		}
//
//		if (task.getTaskName() != null) {
//			if (isEmpty(task.getTaskName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务名称不能为空。");
//			}
//			if (overLength(task.getTaskName(), 100)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务名称超长，最多100个字符。");
//			}
//		}
//
//		if (task.getLanguageCode() != null) {
//			if (isEmpty(task.getLanguageCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
//			}
//			if (overLength(task.getLanguageCode(), 10)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种参数非法。");
//			}
//		}
//
//		if (task.getNeedRoman() != null) {
//			if (isEmpty(task.getNeedRoman())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母转换不能为空。");
//			}
//			if (!Arrays.asList(NEED_ROMAN).contains(task.getNeedRoman())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母转换参数非法。");
//			}
//		}
//
//		if (task.getStatus() != null) {
//			if (isEmpty(task.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务状态不能为空。");
//			}
//			if (!Arrays.asList(TASK_STATE).contains(task.getStatus())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"任务状态参数非法。");
//			}
//		}
//
//		if (task.getMemo() != null) {
//			if (overLength(task.getMemo(), 500)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"备注超长，最多500个字符。");
//			}
//		}
//	}
}
