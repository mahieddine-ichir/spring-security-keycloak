package net.michir.config.keycloak;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JWT Filter.
 *
 * @author michir
 */
public class TokenFilterBean extends GenericFilterBean {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			logger.info("User already authenticated!");
			chain.doFilter(request, response);
			return;
		}

		// authenticate
		String header = request.getHeader("Authorization");
		if (!StringUtils.isEmpty(header) && header.startsWith("Bearer ")) {
			try {
				logger.debug("JWT Token "+header);
				// extract token value
				String token = header.split("\\s")[1].trim();
				// validate token
				TokenAuthentication tokenAuthentication = new TokenAuthentication();
				tokenAuthentication.setToken(token);

				Authentication authentication = authenticationManager.authenticate(tokenAuthentication);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (AuthenticationException ex) {
				logger.debug("Authentication failed on header "+header, ex);
				SecurityContextHolder.clearContext();
			}
		}
		chain.doFilter(request, response);
		return;
	}
}
