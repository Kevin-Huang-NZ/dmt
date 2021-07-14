package mahara.common.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pagination {
	Logger logger = LoggerFactory.getLogger(Pagination.class);
	private int pageNo = 1;
	private int nextPage = 0;
	private int previousPage = 0;
	private int totalPages = 0;
	private long totalElements = 0;
	private boolean first = false;
	private boolean last = false;
	private PageNumber[] pages = new PageNumber[5];

	public Pagination(int pageNo, int totalPages, long totalElements) {// , Page page) {
		this.pageNo = pageNo;
		this.nextPage = pageNo + 1;
		this.previousPage = pageNo - 1;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		if (this.pageNo == 1) {
			this.first = true;
		}
		if (this.pageNo == totalPages) {
			this.last = true;
		}
		PageNumber[] tempPages = new PageNumber[9];
		int j = 0;
		for (int i = 0; i < 9; i++) {
			j = i + this.pageNo - 4;
			if (j < 1 || j > this.totalPages) {
				tempPages[i] = new PageNumber();
			} else {
				tempPages[i] = new PageNumber(j, (i == 4));
			}
		}
		int startIndex = 2;
		if (this.first) {
			startIndex = 4;
		} else if (this.pageNo == 2) {
			startIndex = 3;
		} else if (this.last) {
			startIndex = 0;
		} else if (this.pageNo == totalPages - 1) {
			startIndex = 1;
		}
		System.arraycopy(tempPages, startIndex, this.pages, 0, 5);
		logger.debug("------------------------------------------------------------------------------------");
		for (PageNumber page : this.pages) {
			logger.debug("{}-------------------------{}", page.getPageNumber(), page.isCurrent());
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public boolean isFirst() {
		return first;
	}

	public boolean isLast() {
		return last;
	}

	public PageNumber[] getPages() {
		return pages;
	}

//	public static void main(String[] args) {
//		Pagination pagination = new Pagination(1, 1, 100);
//		pagination = new Pagination(1, 2, 100);
//		pagination = new Pagination(2, 2, 100);
//		
//		pagination = new Pagination(1, 3, 100);
//		pagination = new Pagination(2, 3, 100);
//		pagination = new Pagination(3, 3, 100);
//		
//		pagination = new Pagination(1, 4, 100);
//		pagination = new Pagination(2, 4, 100);
//		pagination = new Pagination(3, 4, 100);
//		pagination = new Pagination(4, 4, 100);
//		
//		pagination = new Pagination(1, 5, 100);
//		pagination = new Pagination(2, 5, 100);
//		pagination = new Pagination(3, 5, 100);
//		pagination = new Pagination(4, 5, 100);
//		pagination = new Pagination(5, 5, 100);
//		
//		
//		pagination = new Pagination(1, 6, 100);
//		pagination = new Pagination(2, 6, 100);
//		pagination = new Pagination(3, 6, 100);
//		pagination = new Pagination(4, 6, 100);
//		pagination = new Pagination(5, 6, 100);
//		pagination = new Pagination(6, 6, 100);
//		
//		
//		pagination = new Pagination(1, 7, 100);
//		pagination = new Pagination(2, 7, 100);
//		pagination = new Pagination(3, 7, 100);
//		pagination = new Pagination(4, 7, 100);
//		pagination = new Pagination(5, 7, 100);
//		pagination = new Pagination(6, 7, 100);
//		pagination = new Pagination(7, 7, 100);
//		
//		
//		pagination = new Pagination(1, 8, 100);
//		pagination = new Pagination(2, 8, 100);
//		pagination = new Pagination(3, 8, 100);
//		pagination = new Pagination(4, 8, 100);
//		pagination = new Pagination(5, 8, 100);
//		pagination = new Pagination(6, 8, 100);
//		pagination = new Pagination(7, 8, 100);
//		pagination = new Pagination(8, 8, 100);
//		
//	}
}
