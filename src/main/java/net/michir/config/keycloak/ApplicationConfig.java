package net.michir.config.keycloak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper()
			.setSerializationInclusion(Include.NON_NULL)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
