package mahara.common.pagination;

public class PageNumber {

	private int pageNumber = 0;
	private boolean current = false;

	public PageNumber() {

	}

	public PageNumber(int pageNumber, boolean current) {
		this.pageNumber = pageNumber;
		this.current = current;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
