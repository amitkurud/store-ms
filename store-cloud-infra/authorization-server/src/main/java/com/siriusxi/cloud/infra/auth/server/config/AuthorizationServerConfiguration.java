package com.siriusxi.cloud.infra.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;

/**
 * An instance of Legacy Authorization Server (spring-security-oauth2) that uses a single,
 * not-rotating key and exposes a JWK endpoint.
 *
 * <p>See <a target="_blank"
 * href="https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/">
 * Spring Security OAuth Autoconfig's documentation</a> for additional details.
 *
 * @author Mohamed Taman
 * @since 5.0
 */
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter ***REMOVED***

  private final AuthenticationManager authenticationManager;
  private final KeyPair keyPair;
  private final boolean jwtEnabled;

  @Autowired
  public AuthorizationServerConfiguration(
      AuthenticationConfiguration authenticationConfiguration,
      KeyPair keyPair,
      @Value("$***REMOVED***security.oauth2.authorizationserver.jwt.enabled:true***REMOVED***") boolean jwtEnabled)
      throws Exception ***REMOVED***

    this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    this.keyPair = keyPair;
    this.jwtEnabled = jwtEnabled;
***REMOVED***

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception ***REMOVED***

    /*
     Password is prefixed with ***REMOVED***noop***REMOVED*** to indicate to DelegatingPasswordEncoder that
     NoOpPasswordEncoder should be used.

     This is not safe for production, but makes reading in samples easier.

     Normally passwords should be hashed using BCrypt.
    */
    String[] grantTypes = ***REMOVED***"code", "authorization_code", "implicit", "password"***REMOVED***;
    var redirectUris = "http://my.redirect.uri";
    var secret = "***REMOVED***noop***REMOVED***secret";

    clients
        .inMemory()
        // Client that can only get products
        .withClient("reader")
        .authorizedGrantTypes(grantTypes)
        .redirectUris(redirectUris)
        .secret(secret)
        .scopes("product:read")
        .accessTokenValiditySeconds(600_000_000)
        .and()
        // Client that can get and add products
        .withClient("writer")
        .authorizedGrantTypes(grantTypes)
        .redirectUris(redirectUris)
        .secret(secret)
        .scopes("product:read", "product:write")
        .accessTokenValiditySeconds(600_000_000)
        .and()
        // With just password client
        .withClient("noscopes")
        .authorizedGrantTypes(grantTypes)
        .redirectUris(redirectUris)
        .secret(secret)
        .scopes("none")
        .accessTokenValiditySeconds(600_000_000);
***REMOVED***

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) ***REMOVED***

    endpoints.authenticationManager(this.authenticationManager).tokenStore(tokenStore());

    if (this.jwtEnabled) ***REMOVED***
      endpoints.accessTokenConverter(accessTokenConverter());
***REMOVED***
***REMOVED***

  @Bean
  public TokenStore tokenStore() ***REMOVED***
    if (this.jwtEnabled) ***REMOVED***
      return new JwtTokenStore(accessTokenConverter());
***REMOVED*** else ***REMOVED***
      return new InMemoryTokenStore();
***REMOVED***
***REMOVED***

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() ***REMOVED***
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setKeyPair(this.keyPair);

    DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
    accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
    converter.setAccessTokenConverter(accessTokenConverter);

    return converter;
***REMOVED***
***REMOVED***