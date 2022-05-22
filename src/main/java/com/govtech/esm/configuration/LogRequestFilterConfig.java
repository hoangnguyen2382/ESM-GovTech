package com.govtech.esm.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Log Configuration to capture processing time for each Request.
 * 
 * @author vanhoang.nguyen
 *
 */
@Configuration
public class LogRequestFilterConfig {

	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter = new TimeCommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setIncludeHeaders(true);
		filter.setMaxPayloadLength(10000);
		filter.setBeforeMessagePrefix("Start the request [");
		filter.setAfterMessagePrefix("Complete the request [");
		return filter;
	}

	private class TimeCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {
		private ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

		@Override
		protected void beforeRequest(HttpServletRequest request, String message) {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			stopWatchThreadLocal.set(stopWatch);
			super.beforeRequest(request, message);
		}

		@Override
		protected void afterRequest(HttpServletRequest request, String message) {
			StopWatch stopWatch = stopWatchThreadLocal.get();
			stopWatchThreadLocal.remove();
			stopWatch.stop();
			String durationMessage = " took " + stopWatch.getLastTaskTimeMillis() + "ms";
			super.afterRequest(request, message + durationMessage);
		}
	}
}