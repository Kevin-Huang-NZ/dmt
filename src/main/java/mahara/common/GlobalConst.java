package mahara.common;

public class GlobalConst {
//	public static final String SESSION_LOGIN_TYPE = "X_LOGIN_TYPE";
//	public static final String LOGIN_TYPE_STAFF = "STAFF";
//	public static final String LOGIN_TYPE_MEMBER = "MEMBER";
//	public static final String LOGIN_TYPE_USER = "USER";
	public static final String SESSION_LOGIN_USER = "X_LOGIN_USER";
	
	/*
	 * redis keys
	 */
	public static final String KEY_ROLE_FUNS = "KEY_ROLE_FUNS";
	public static final String KEY_STS_UPDATE_REASON = "KEY_STS_UPDATE_REASON";

	public static final String ROLE_SYS_ADMINISTRATOR = "Z";
	public static final String ROLE_STAFF_MANAGER = "M";
	public static final String ROLE_STAFF_AUDITOR = "A";
//	public static final String ROLE_OUTSOURCE_AUDITOR = "O";
	public static final String ROLE_SHARE_USER = "S";
	
	public static final String UPLOADED_FILE_ACCESS_URL_PREFIX = "/static/uploaded/image/";
}
