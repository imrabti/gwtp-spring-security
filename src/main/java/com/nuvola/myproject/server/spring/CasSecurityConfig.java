package com.nuvola.myproject.server.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.SpringSocialConfigurer;

import com.nuvola.myproject.server.security.AuthFailureHandler;
import com.nuvola.myproject.server.security.AuthSuccessHandler;
import com.nuvola.myproject.server.security.HttpAuthenticationEntryPoint;
import com.nuvola.myproject.server.security.HttpLogoutSuccessHandler;
import com.nuvola.myproject.server.security.NuvolaCasDetailsService;
import com.nuvola.myproject.server.security.SimpleConnectionSignUp;
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
    private DataSource dataSource;
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
    @Scope(value="singleton", proxyMode= ScopedProxyMode.INTERFACES)
    public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
        SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
        registry.addConnectionFactory(new CorporateConnectionFactory(restTemplate()));

        return registry;
    }

    @Bean
    @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository connectionRepository =
                new JdbcUsersConnectionRepository(dataSource, socialAuthenticationServiceLocator(), Encryptors.noOpText());
        connectionRepository.setConnectionSignUp(new SimpleConnectionSignUp());
        return connectionRepository;
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ProviderSignInController controller = new ProviderSignInController(socialAuthenticationServiceLocator(),
                usersConnectionRepository(), new SimpleSignInAdapter(new HttpSessionRequestCache(),
                userDetailsService));
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
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().loginPage("/access/login")
                .permitAll()
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                .logout().logoutUrl("/access/logout")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .apply(new SpringSocialConfigurer()
                        .postLoginUrl("/home")
                        .alwaysUsePostLoginUrl(true));

        http.authorizeRequests()
                .antMatchers(LOGIN_PATH).permitAll()
                .anyRequest().authenticated();
    }
}
