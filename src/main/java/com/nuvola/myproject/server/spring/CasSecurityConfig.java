package com.nuvola.myproject.server.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.oauth2.OAuth2Template;

import com.nuvola.myproject.server.security.AuthFailureHandler;
import com.nuvola.myproject.server.security.AuthSuccessHandler;
import com.nuvola.myproject.server.security.HttpAuthenticationEntryPoint;
import com.nuvola.myproject.server.security.HttpLogoutSuccessHandler;
import com.nuvola.myproject.server.security.NuvolaCasDetailsService;
import com.nuvola.myproject.server.security.SimpleSignInAdapter;
import com.nuvola.myproject.server.security.corp.CorporateConnectionFactory;
import com.nuvola.myproject.shared.ResourcePaths;

@Configuration
@Import(PropertiesConfig.class)
@EnableWebSecurity
public class CasSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PATH = ResourcePaths.ROOT + ResourcePaths.SIGNIN_CORPORATE;

    private
    @Value("${appclientid}")
    String appClientId;
    private
    @Value("${appclientsecret}")
    String appClientSecret;
    private
    @Value("${authloginuri}")
    String authLoginUri;
    private
    @Value("${userauthorizationuri}")
    String userAuthorizationUri;
    private
    @Value("${accesstokenuri}")
    String accessTokenUri;

    @Autowired
    private HttpAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private NuvolaCasDetailsService userDetailsService;
    @Autowired
    private AuthSuccessHandler authSuccessHandler;
    @Autowired
    private AuthFailureHandler authFailureHandler;
    @Autowired
    private HttpLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    OAuth2Template restTemplate() {
        return new OAuth2Template(corporateProvider().getClientId(),
                corporateProvider().getClientSecret(),
                corporateProvider().getUserAuthorizationUri(),
                corporateProvider().getAccessTokenUri());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new ShaPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    @Scope(value="singleton", proxyMode= ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new CorporateConnectionFactory(
                restTemplate()));

        return registry;
    }

    @Bean
    @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator());
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ProviderSignInController controller = new ProviderSignInController(connectionFactoryLocator(),
                usersConnectionRepository(), new SimpleSignInAdapter(new HttpSessionRequestCache()));
        // controller.setSignInUrl(LOGIN_PATH);
        return controller;
    }

    @Bean
    AuthorizationCodeResourceDetails corporateProvider() {
        AuthorizationCodeResourceDetails codeResourceDetails = new AuthorizationCodeResourceDetails();
        codeResourceDetails.setClientId(appClientId);
        codeResourceDetails.setClientSecret(appClientSecret);
        codeResourceDetails.setGrantType("authorization_code");
        codeResourceDetails.setTokenName("bearer");
        codeResourceDetails.setAuthenticationScheme(AuthenticationScheme.header);
        codeResourceDetails.setClientAuthenticationScheme(AuthenticationScheme.header);
        codeResourceDetails.setAccessTokenUri(accessTokenUri);
        codeResourceDetails.setUserAuthorizationUri(userAuthorizationUri);
        codeResourceDetails.setUseCurrentUri(true);
        codeResourceDetails.setPreEstablishedRedirectUri("http://localhost:8090/");
        return codeResourceDetails;
    }

    @Bean
    AuthorizationCodeResourceDetails googleProvider() {
        return null;
    }

    @Bean
    AuthorizationCodeResourceDetails facebookProvider() {
        return null;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authenticationProvider(authenticationProvider())
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin()
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                .sessionManagement()
                .maximumSessions(1);

        http.authorizeRequests()
                .antMatchers(LOGIN_PATH).permitAll()
                .anyRequest().authenticated();
    }
}
