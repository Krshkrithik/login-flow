package com.example.signup.security;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class AuthorizationProviderImp extends AuthorizationServerConfigurerAdapter{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenStore tokenStore;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthorizationProviderImp(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public DefaultTokenServices defaultTokenServices() {

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("krithik")                          // in-memory clientId
                .secret(passwordEncoder.encode("Secret"))  // in-memory clientSecret
                .accessTokenValiditySeconds(-1)              // expire time for access token in seconds
                .refreshTokenValiditySeconds(-1)                // expire time for refresh token - no expiry if negative value
                .scopes("read", "write")                         // scope related to resource server
                .authorizedGrantTypes("password", "refresh_token","authorization_code");      // grant types allowed by authorization server

    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("JWT_KEY");
        return converter;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter())
                .reuseRefreshTokens(false);
    }

    @Bean
    public TokenStore tokenStore(HikariDataSource dataSource) {
       return new JdbcTokenStore(dataSource);
    }

}
