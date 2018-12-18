package com.guonl.page;

import com.guonl.util.Page;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class PageElementProcessor extends AbstractMarkupSubstitutionElementProcessor {
	/** page number */
	private int pn = 5;
	/** page size */
	private int ps = 10;
	/** record count */
	private int rct = 100;
	/** synchronized */
	private boolean sync = true;

	protected PageElementProcessor() {
		super("page");
	}

	@Override
	protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {
		Page page = (Page) arguments.getExpressionSelectionEvaluationRoot();
		// final String ps = element.getAttributeValue("ps");
		// final String pn = element.getAttributeValue("pn");
		// final String rct = element.getAttributeValue("rct");
		// this.pn = Integer.parseInt(pn);
		//
		// this.ps = Integer.parseInt(ps);
		//
		// this.rct = Integer.parseInt(rct);
		this.pn = page.getPn();
		this.ps = page.getPs();
		this.rct = page.getRct();
		final Element container = new Element("div");
		final List<Node> nodes = new ArrayList<>();
		final Text text = new Text(toHtml(), null, null, true);
		container.addChild(text);
		nodes.add(container);
		return nodes;
	}

	@Override
	public int getPrecedence() {
		return 0;
	}

	private String toHtml() {
		StringBuilder sb = new StringBuilder();
		// 计算总页数
		int pageCount = (rct + ps - 1) / ps;
		if (rct == 0) {
			sb.append("<div class=\"text-center\">暂无数据!</div>");
		} else {
			// 页号越界处理
			if (pn > pageCount) {
				pn = pageCount;
			}
			if (pn < 1) {
				pn = 1;
			}
			// 当前页参数隐藏域
			sb.append("<input type=\"hidden\" id=\"pn\" name=\"pn\" value=\"" + pn + "\">");
			// 分页样式(bootstrap样式)
			// 输出统计数据
			// sb.append("<div class=\"page-total\"><label>共" + rct + "条，" +
			// pageCount + "页</label></div>");
			sb.append("<nav class=\"text-right\">");
			sb.append("<ul class=\"pagination\">");
			// 上一页
			if (pn != 1) {
				// sb.append("<li><a href=\"javascript:formQuery(1)\"
				// aria-label=\"Home\" title=\"首页\"><span
				// aria-hidden=\"true\">&laquo;</span></a></li>");
				sb.append("<li><a href=\"javascript:formQuery(" + (pn - 1)
						+ ")\" aria-label=\"Previous\" title=\"上一页\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
			}
			// 如果总页数小于等于10页，则全显示，否则断开显示
			if (pageCount <= 10) {
				for (int i = 1; i <= pageCount; i++) {
					if (pn == i) {
						// 当前页号不需要点击事件
						sb.append("<li class=\"active\"><span>" + i + "</span></li>");
					} else {
						sb.append("<li><a href=\"javascript:formQuery(" + i + ");\"><span>" + i + "</span></a></li>");
					}
				}
			} else {
				// 如果前面页数超过5页,前面显示3页，后面显示"..."
				int start = 1;
				if (pn > 5) {
					start = pn;
					for (int i = 1; i <= 3; i++) {
						sb.append("<li><a href=\"javascript:formQuery(" + i + ")\"><span>" + i + "</span></a></li>");
					}
					// ...时取两个数的中位数进行点击
					int middle = getMiddleNum(3, pn);
					sb.append("<li><a href=\"javascript:formQuery(" + middle + ")\" title=\"中间第" + middle
							+ "页\"><span>&hellip;</span></a></li>");
				}
				// 显示当前页附近的页
				int end = pn + 3;
				if (end > pageCount) {
					end = pageCount;
				}
				for (int i = start; i <= end; i++) {
					if (pn == i) {
						// 当前页号不需要点击事件
						sb.append("<li class=\"active\"><span>" + i + "</span></li>");
					} else {
						sb.append("<li><a href=\"javascript:formQuery(" + i + ");\"><span>" + i + "</span></a></li>");
					}
				}
				// 如果后面页数过多,显示"..."
				if (end < pageCount - 3) {
					// ...时取两个数的中位数进行点击
					int middle = getMiddleNum(end, pageCount - 3);
					sb.append("<li><a href=\"javascript:formQuery(" + middle + ")\" title=\"中间第" + middle
							+ "页\"><span>&hellip;</span></a></li>");
					// sb.append("<li><span>&hellip;</span></li>");
				}
				if (end < pageCount - 2) {
					sb.append("<li><a href=\"javascript:formQuery(" + (pageCount - 2) + ")\"><span>" + (pageCount - 2)
							+ "</span></a></li>");
				}
				if (end < pageCount - 1) {
					sb.append("<li><a href=\"javascript:formQuery(" + (pageCount - 1) + ")\"><span>" + (pageCount - 1)
							+ "</span></a></li>");
				}
				if (end < pageCount) {
					sb.append("<li><a href=\"javascript:formQuery(" + pageCount + ")\"><span>" + pageCount
							+ "</span></a></li>");
				}
			}
			// 下一页
			if (pn != pageCount) {
				sb.append("<li><a href=\"javascript:formQuery(" + (pn + 1)
						+ ")\" aria-label=\"Next\" title=\"下一页\"><span aria-hidden=\"true\">&raquo;</span></a></li>");
				// sb.append("<li><a
				// href=\"javascript:formQuery("+pageCount+")\"
				// aria-label=\"Last\" title=\"末页\"><span
				// aria-hidden=\"true\">&raquo;</span></a></li>");
			}
			sb.append("</ul>");
			sb.append("</nav>");
			// sync==true同步时，调用如下 js
			// 方法直接提交表单，sync==false异步时调用页面自定义的formQuery()方法
			if (sync) {
				// 生成提交表单的js，页面上的<p:page>标签需要用id为form-query的表单包住
				sb.append("<script type=\"text/javascript\">");
				sb.append("  function formQuery(pn){");
				sb.append("    if(pn > " + pageCount + "){");
				sb.append("       pn = " + pageCount + ";");
				sb.append("    }");
				sb.append("    if(pn < 1){pn = 1;}");
				sb.append("    document.getElementById('form-query').pn.value=pn;");
				sb.append("    document.getElementById('form-query').submit();");
				sb.append("  };");
				sb.append("$(function(){");
				sb.append(" var btn = document.getElementById('btn-submit');");
				sb.append("  btn.addEventListener('click',function(){");
				sb.append("  document.getElementById('form-query').pn.value=1; ");
				sb.append(" },false);})");
				sb.append("</script>");
			}
		}
		return sb.toString();
	}

	/**
	 * 取两个数的中位数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private int getMiddleNum(int min, int max) {
		ArrayList<Integer> arr = new ArrayList<>();
		int mi = min;
		int count = max - min + 1;
		int[] arr2 = new int[count];
		for (int i = 0; i < count; i++) {
			arr2[i] = mi++;
		}
		for (int element : arr2) {
			arr.add(element);
		}
		Integer j;
		Collections.sort(arr);
		if (arr2.length > 2 && arr2.length % 2 == 0) {
			j = (arr.get(arr2.length / 2) + arr.get(arr2.length / 2 + 1)) / 2;
		} else {
			j = arr.get(arr2.length / 2);
		}
		return j == null ? min : j;
	}

}
