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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

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
    // No need for csrf between back end servers. (no cookies/basic auth)
    http.csrf()
        .ignoringAntMatchers("/matrix/**", "/attachment/**", "/twilio/**");

    http.antMatcher("/attachment/**")
        .antMatcher("/twilio/**")
        .addFilterAfter(twilioAuthenticationFilter, AnonymousAuthenticationFilter.class);

    http.authorizeRequests()
        .antMatchers("/twilio/**")
        .authenticated()
        .and()
        .httpBasic();
  }

  @Bean
  FilterRegistrationBean<Filter> twilioFilterRegistration() {
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(twilioAuthenticationFilter);
    registrationBean.addUrlPatterns("/twilio/**");
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); //set precedence
    return registrationBean;
  }
}
