package ca.levimiller.smsbridge.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@ConfigurationProperties(prefix = "matrix")
@Getter
@Setter
public class MatrixConfig {

  /**
   * Because I misconfigured my matrix server years ago and didn't include the subdomain.
   */
  private String serverName;
  /**
   * Url to Matrix server.
   */
  private String url;
  /**
   * Token for Authorization on Matrix server.
   */
  private String asToken;
  /**
   * Token for Home Matrix server.
   */
  private String hsToken;

  @Bean
  @Qualifier("matrixTemplate")
  RestTemplate restTemplate(
      RestTemplateBuilder builder) {
    UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(url)
        .path("/_matrix/client/r0");
    return builder
        .rootUri(uriBuilder.build().toString())
        .interceptors(new TokenAuthenticationInterceptor(asToken))
        .build();
  }
}
