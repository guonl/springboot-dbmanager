package com.guonl.util;

/**
 * Created by guonl
 * Date 2018/12/18 5:26 PM
 * Description: 页码工具
 */
public class Page {
	/** max page size */
	private static final int MAX_PS = 100;
	/** page number */
	private int pn = 1;
	/** page size */
	private int ps = 20;
	/** record count */
	private int rct = 0;

	public Page() {
	}

	public Page(int pn, int ps) {
		this.setPn(pn);
		this.setPs(ps);
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		if (pn < 1) {
			this.pn = 1;
			return;
		}
		this.pn = pn;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		if (ps < 1) {
			this.ps = 1;
			return;
		}
		if (ps > MAX_PS) {
			this.ps = MAX_PS;
			return;
		}
		this.ps = ps;
	}

	public int getRct() {
		return rct;
	}

	public void setRct(int rct) {
		this.rct = rct;
	}

	public boolean hasPre() {
		return this.getPn() > 1;
	}

	public int pre() {
		return this.getPn() - 1;
	}

	public int next() {
		return this.getPn() + 1;
	}

	/** mysql limit */
	public int limit() {
		return ps;
	}

	/** mysql offset */
	public int offset() {
		return (pn - 1) * ps;
	}

	/** oracle start rowno */
	public int startRowno() {
		return (pn - 1) * ps + 1;
	}

	/** oracle end rowno */
	public int endRowno() {
		return pn * ps;
	}

	/** solr start */
	public int start() {
		return (pn - 1) * ps + 1;
	}

	/** solr rows */
	public int rows() {
		return ps;
	}


	@Override
	public String toString() {
		return String.format("{pn:%s, ps:%s, rct:%s}", pn, ps, rct);
	}
}