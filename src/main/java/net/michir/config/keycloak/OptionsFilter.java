package net.michir.config.keycloak;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

public class OptionsFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
		resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE, PATCH");
		resp.addHeader("Access-Control-Allow-Headers", "accept, authorization, content-type, x-requested-with");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Max-Age", "1");
		resp.addHeader("Access-Control-Expose-Headers", "Location");

		if (req.getMethod().equalsIgnoreCase("options")) {
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(request, response);
		}
	}
}
