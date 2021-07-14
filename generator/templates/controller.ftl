package [=controllerPkg];

import static mahara.util.CheckUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import [=modelPkg].[=targetName];
import [=servicePkg].[=targetName]Service;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/[=lowerTargetName]")
public class [=targetName]Controller extends BaseController {

	Logger logger = LoggerFactory.getLogger([=targetName]Controller.class);

	@Autowired
	private [=targetName]Service [=lcTargetName]Service;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, [=targetName] criteria) throws BusinessException {

		Page<[=targetName]> searchResult = this.[=lcTargetName]Service.selectPaged(crp, criteria);

		ListData<[=targetName]> wrapper = new ListData<[=targetName]>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		[=targetName] [=lcTargetName] = this.[=lcTargetName]Service.selectById(id);
		if ([=lcTargetName] == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create([=lcTargetName]);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody [=targetName] [=lcTargetName]) throws BusinessException {
		this.checkCreate([=lcTargetName]);

		int result = this.[=lcTargetName]Service.save([=lcTargetName], authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody [=targetName] [=lcTargetName]) throws BusinessException {
		this.checkUpdate([=lcTargetName]);

		int result = this.[=lcTargetName]Service.update([=lcTargetName], authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.[=lcTargetName]Service.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate([=targetName] [=lcTargetName]) throws BusinessException {
		if ([=lcTargetName] == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty([=lcTargetName].getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength([=lcTargetName].getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, [=lcTargetName].getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate([=targetName] [=lcTargetName]) throws BusinessException {
		if ([=lcTargetName] == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if ([=lcTargetName].getUserName() != null) {
//			if (isEmpty([=lcTargetName].getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength([=lcTargetName].getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
