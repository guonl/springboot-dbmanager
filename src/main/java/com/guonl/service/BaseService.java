package com.guonl.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guonl.util.Page;
import java.util.List;

/**
 * Created by guonl
 * Date 2018/12/18 5:34 PM
 * Description:
 */
public class BaseService {

	public <T> void setPageInfo(Page p, List<T> list) {
		PageInfo page = new PageInfo(list);
		p.setRct((int) page.getTotal());
		p.setPn(page.getPageNum());
		p.setPs(page.getPageSize());
	}

	public void startPage(Page p) {
		PageHelper.startPage(p.getPn(), p.getPs());
	}

	public void startPageOrderBy(Page p, String orderBy) {
		PageHelper.startPage(p.getPn(), p.getPs(), orderBy);
	}

	public void startPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
	}
}
