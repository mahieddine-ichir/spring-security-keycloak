package net.michir.config.keycloak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		UserDetails userDetails = userDetailsService.loadUserByUsername(tokenAuthentication.getToken());

		// authenticate token (expired ?)
		tokenAuthentication.setDetails(userDetails);
		tokenAuthentication.setAuthorities(userDetails.getAuthorities());
		tokenAuthentication.setPrincipal(userDetails);
		tokenAuthentication.setName(userDetails.getUsername());
		tokenAuthentication.setAuthenticated(true);

		return tokenAuthentication;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return (TokenAuthentication.class.isAssignableFrom(arg0));
	}
}
