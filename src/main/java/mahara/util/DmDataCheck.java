package mahara.util;

public class DmDataCheck {
	
	public static ValidationResult checkDmName(String val) {
		if (CheckUtil.isEmpty(val)) {
			return new ValidationResult("标准名称不能为空。");
		}

		if (CheckUtil.overLength(val, 100)) {
			return new ValidationResult("标准名称超长，最多100字符。");
		}
		return  new ValidationResult();
	}

	public static ValidationResult checkDmOtherName(String val) {
		if (CheckUtil.overLength(val, 200)) {
			return new ValidationResult("别名超长，最多200字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmShortName(String val) {
		if (CheckUtil.overLength(val, 50)) {
			return new ValidationResult("简称超长，最多50字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmLangSpell(String val) {
		if (CheckUtil.overLength(val, 200)) {
			return new ValidationResult("少数民族语书写超长，最多200字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmPhonics(String val) {
		if (CheckUtil.isEmpty(val)) {
			return new ValidationResult("罗马化拼写不能为空。");
		}

		if (CheckUtil.overLength(val, 200)) {
			return new ValidationResult("罗马化拼写超长，最多200字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmLanguage(String val) {
		if (CheckUtil.overLength(val, 50)) {
			return new ValidationResult("地名语种超长，最多50字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmType(String val) {
		if (CheckUtil.isEmpty(val)) {
			return new ValidationResult("地名类别不能为空。");
		}

		if (CheckUtil.overLength(val, 50)) {
			return new ValidationResult("地名类别超长，最多50字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmTime(String val) {
		if (CheckUtil.isEmpty(val)) {
			return new ValidationResult("使用时间不能为空。");
		}

		if (CheckUtil.overLength(val, 10)) {
			return new ValidationResult("使用时间超长，最多10字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmSrc(String val) {
		if (CheckUtil.overLength(val, 65535)) {
			return new ValidationResult("来历超长，最多65535字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmMeaning(String val) {
		if (CheckUtil.overLength(val, 65535)) {
			return new ValidationResult("含义超长，最多65535字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmHistory(String val) {
		if (CheckUtil.overLength(val, 65535)) {
			return new ValidationResult("历史沿革超长，最多65535字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmSummary(String val) {
		if (CheckUtil.overLength(val, 2000)) {
			return new ValidationResult("地理实体概况超长，最多2000字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkDmReference(String val) {
		if (CheckUtil.overLength(val, 200)) {
			return new ValidationResult("资料来源超长，最多200字符。");
		}
		return  new ValidationResult();
	}
	
	public static ValidationResult checkCountyName(String val) {
		if (CheckUtil.overLength(val, 100)) {
			return new ValidationResult("县级政区超长，最多100字符。");
		}
		return  new ValidationResult();
	}
}
