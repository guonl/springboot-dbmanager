package com.guonl.page;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lenovo
 *
 */
@Component
public class GuonlDialect extends AbstractDialect {

	@Override
	public String getPrefix() {
		return "guonl";
	}

	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<>();
		processors.add(new PageElementProcessor());
		return processors;
	}

}
