package net.michir.config.keycloak;

import org.keycloak.RSATokenVerifier;
import org.keycloak.exceptions.TokenVerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeycloakUserDetailsService implements UserDetailsService {

    /**
     * To cache public keys.
     */
    private static Map<String, PublicKey> keys = new java.util.concurrent.ConcurrentHashMap<>();

	@Autowired
	RestTemplate restTemplate;

	@Autowired
    KeycloakPublicKeyService keycloakPublicKeyService;

    @Value("${keycloak.realmUrl}")
    private String realmUrl;

	@Override
	public UserDetails loadUserByUsername(String jwt)
			throws UsernameNotFoundException {
		try {
            RSATokenVerifier rsaTokenVerifier = RSATokenVerifier.create(jwt);
            PublicKey publicKey = keycloakPublicKeyService.publicKey(rsaTokenVerifier.getHeader().getKeyId());

            AccessToken accessToken = rsaTokenVerifier.realmUrl(realmUrl)
                    .publicKey(publicKey)
                    .verify().getToken();

            List<SimpleGrantedAuthority> authorities = accessToken.getRealmAccess().getRoles()
                    .stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());

            return new User(accessToken.getEmail(), "changed", authorities);

        } catch (TokenVerificationException e) {
            throw new UsernameNotFoundException("Invalid token signature (or expired)! "+jwt, e);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error validating token"+jwt, e);
        }
	}
}
