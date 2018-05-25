package net.michir.config.keycloak;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Access token implementation for OAuth2.
 *
 * @author michir
 */
public class TokenAuthentication implements Authentication {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 8693521345558148891L;

	private String token;

	private String username;

	private boolean authenticated;

	private transient Object userDetails;

	private Collection<? extends GrantedAuthority> grantAuthorities;

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return userDetails;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPrincipal(Object principal) {
		this.userDetails = principal;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (obj instanceof TokenAuthentication) {
			return this.getToken() == ((TokenAuthentication) obj).getToken();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return token.hashCode();
	}

	public void setName(String username) {
		this.username = username;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantAuthorities;
	}

	@Override
	public Object getDetails() {
		return userDetails;
	}

	public void setDetails(Object userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) {
		this.authenticated = isAuthenticated;
	}

	public void setAuthorities(
			Collection<? extends GrantedAuthority> grantAuthorities) {
		this.grantAuthorities = grantAuthorities;
	}
}
