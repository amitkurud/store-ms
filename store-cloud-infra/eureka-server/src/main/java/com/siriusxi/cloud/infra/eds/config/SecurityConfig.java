package com.siriusxi.cloud.infra.eds.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter ***REMOVED***

  private final String username;
  private final String password;

  @Autowired
  public SecurityConfig(
      @Value("$***REMOVED***app.eureka.username***REMOVED***") String username,
      @Value("$***REMOVED***app.eureka.password***REMOVED***") String password) ***REMOVED***
    this.username = username;
    this.password = "***REMOVED***noop***REMOVED***".concat(password);
***REMOVED***

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception ***REMOVED***
    auth.inMemoryAuthentication()
        .withUser(username)
        .password(password)
        .authorities("USER");
***REMOVED***

  @Override
  protected void configure(HttpSecurity http) throws Exception ***REMOVED***
    http
        // Disable CRCF to allow services to register themselves with Eureka
        .csrf()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic();
***REMOVED***
***REMOVED***
