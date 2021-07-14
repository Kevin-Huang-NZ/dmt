package mahara.web.response.error;

public class BusinessException extends Exception implements CommonError {
	private static final long serialVersionUID = 1L;
	private CommonError commonError;
	private String specialMsg = "";

	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}

	public BusinessException(CommonError commonError, String errMsg) {
		super();
		this.commonError = commonError;
		this.specialMsg = errMsg;
//		this.commonError.setErrMsg(errMsg);
	}

	@Override
	public int getErrCode() {
		return this.commonError.getErrCode();
	}

	@Override
	public String getErrMsg() {
		if ("".equals(this.specialMsg)) {
			return this.commonError.getErrMsg();
		} else {
			return this.specialMsg;
		}
	}

//	@Override
//	public CommonError setErrMsg(String errMsg) {
//		this.commonError.setErrMsg(errMsg);
//		return this;
//	}

}
