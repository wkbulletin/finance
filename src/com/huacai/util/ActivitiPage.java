package com.huacai.util;

import com.huacai.web.common.Page;

/**
 * 分页工具
 * 
 */
public class ActivitiPage {

  public static int[] init(Page page, int curPage, int pageSize) {
	page.setPageSize(pageSize);
	page.setPageNo(curPage);  
    int firstResult = page.getFirst() - 1;
    int maxResults = page.getPageSize();
    return new int[] {firstResult, maxResults};
  }

}
