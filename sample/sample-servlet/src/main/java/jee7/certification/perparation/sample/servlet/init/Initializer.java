package jee7.certification.perparation.sample.servlet.init;

import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import jee7.certification.perparation.sample.servlet.filter.ResponseLoggingFilter;

public class Initializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		FilterRegistration.Dynamic reg = ctx.addFilter(ResponseLoggingFilter.class.getSimpleName(), ResponseLoggingFilter.class.getName());
		reg.addMappingForUrlPatterns(null, false, "/*");
	}
}
