package ca.levimiller.smsbridge.security;

import javax.inject.Inject;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final Filter twilioAuthenticationFilter;

  @Inject
  public WebSecurityConfig(
      @Qualifier("twilioFilter") Filter twilioAuthenticationFilter) {
    this.twilioAuthenticationFilter = twilioAuthenticationFilter;
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v2/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**");
  }
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // disable csrf for twilio as it uses a generated token to verify the server.
    http.csrf()
        .ignoringAntMatchers("/twilio/**");
  }

  @Bean
  public FilterRegistrationBean<Filter> twilioFilterRegistration() {
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(twilioAuthenticationFilter);
    registrationBean.addUrlPatterns("/twilio/*");
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); //set precedence
    return registrationBean;
  }
}
