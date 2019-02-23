package com.msh.restdemo.common.client.interceptors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ClientHttpResponse response = execution.execute(request, body);
		log(request, body, response);
		return response;
	}

	private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("logging");
			logger.debug("headers: {}", request.getHeaders());
			logger.debug("{}", new String(body));
			logger.debug("{}", IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
		}
	}
}
