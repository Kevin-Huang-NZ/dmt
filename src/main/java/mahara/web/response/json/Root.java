package mahara.web.response.json;

import lombok.Data;

@Data
public class Root {
	public static final String RESPONSE_STATUS_OK = "ok";
	public static final String RESPONSE_STATUS_ERROR = "error";
	/**
	 * ok / error
	 */
	private String status;
	/**
	 * status是success时，用于判断是否有数据.
	 * 0-无数据 / 1-有数据
	 */
//	private String hasData;
	/**
	 * status是success，data返回前端需要的json
	 * status是failure，data返回通用错误信息
	 */
	private Object data;
//	private Object dataEx;

	public static Root create(Object data) {
		return Root.create(Root.RESPONSE_STATUS_OK, data);
	}

//	public static Root create(Object data, Object dataEx) {
//		return Root.create(Root.RESPONSE_STATUS_OK, data, dataEx);
//	}

	public static Root create(String status, Object data) {
		Root type = new Root();
		type.setData(data);
		type.setStatus(status);
//		type.setHasData("1");
		return type;
	}

//	public static Root create(String status, Object data, Object dataEx) {
//		Root type = new Root();
//		type.setData(data);
//		type.setData(dataEx);
//		type.setStatus(status);
////		type.setHasData("1");
//		return type;
//	}

//	public static Root create(Object data, String status, String hasData) {
//		Root type = new Root();
//		type.setData(data);
//		type.setStatus(status);
//		type.setHasData(hasData);
//		return type;
//	}
}
