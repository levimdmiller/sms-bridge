package ca.levimiller.smsbridge.config;

import ca.levimiller.smsbridge.util.MatrixUtil;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.factory.jaxrs.JaxRsRequestFactory;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "matrix")
@Validated
@Getter
@Setter
public class MatrixConfig {

  /**
   * Because I misconfigured my matrix server years ago and didn't include the subdomain.
   */
//  @NotEmpty
  private String serverName;
  /**
   * Url to Matrix server.
   */
  @NotEmpty
  private String url;
  /**
   * Token for Authorization on Matrix server.
   */
  @NotEmpty
  private String asToken;
  /**
   * Token for Home Matrix server.
   */
  @NotEmpty
  private String hsToken;

  @PostConstruct
  public void init() {
    if(StringUtils.isEmpty(serverName)) {
      try {
        serverName = new URL(url).getHost();
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("ServerName is not set, and failed to extract "
            + "host from the server url.", e);
      }
    }
  }

  @Bean
  MatrixUtil matrixUtil() {
    return new MatrixUtil();
  }

  @Bean
  AppServiceClient matrixClient() {
    return new AppServiceClient.Builder()
        .requestFactory(new JaxRsRequestFactory(url))
        .accessToken(asToken)
        .build();
  }
}
