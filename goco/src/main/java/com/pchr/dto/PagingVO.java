package com.pchr.dto;

import lombok.Getter;

@Getter
public class PagingVO {
	private int page;
	private int perPageNum;
	
	public PagingVO() {
		this.page = 1;	//현재 페이지
		this.perPageNum = 10;	// 페이지당 보여줄 게시글 수
		
	}
	
	public int getPageStart() {	// 페이지 게시글 시작 번호
		return (this.page -1)*perPageNum;
	}
	
	public void setPage(int page) {
		if(page<=0) {
			this.page =1;
		}else {
			
			this.page = page;
		}
	}
	
	public void setPerPageNum(int pageCount) {
		
		int cnt = this.perPageNum;
		if(pageCount != cnt) {
			this.perPageNum = cnt;
		}else {
			this.perPageNum = pageCount;
		}
	}
	
}
