package mahara.web.param;

import org.apache.commons.lang3.StringUtils;

public class CommonRequestParam {
	/**
	 * 页码，从1开始
	 */
	private int pageNum = 1;
	/**
	 * 页面大小
	 */
	private int pageSize = 10;

	private String orderBy = "id";
	private String ascDesc = "desc";

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getAscDesc() {
		return ascDesc;
	}

	public void setAscDesc(String ascDesc) {
		this.ascDesc = ascDesc;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrder() {
		if (StringUtils.isEmpty(this.orderBy)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(this.orderBy).append(" ").append(this.ascDesc);
		return sb.toString();
	}
}
