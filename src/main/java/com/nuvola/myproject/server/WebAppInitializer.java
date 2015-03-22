package com.nuvola.myproject.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import com.nuvola.myproject.server.spring.WebConfig;
import com.nuvola.myproject.shared.ResourcePaths;

public class WebAppInitializer implements WebApplicationInitializer {
    private static final String SECURITY_FILTER_NAME = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;
    private static final String DISPATCHER_SERVLET_NAME = AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext rootContext = createRootContext(servletContext);

        configureSpringMvc(servletContext, rootContext);
        configureSpringSecurity(servletContext, rootContext);
    }

    private WebApplicationContext createRootContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));

        return context;
    }

    private void configureSpringMvc(ServletContext servletContext, WebApplicationContext rootContext) {
        DispatcherServlet dispatcher = new DispatcherServlet(rootContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet(DISPATCHER_SERVLET_NAME, dispatcher);
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/access/login");
        servlet.addMapping("/access/logout");
        servlet.addMapping(ResourcePaths.ROOT + "/*");
    }

    private void configureSpringSecurity(ServletContext servletContext, WebApplicationContext rootContext) {
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

        DelegatingFilterProxy proxy = new DelegatingFilterProxy(SECURITY_FILTER_NAME, rootContext);

        FilterRegistration.Dynamic filter = servletContext.addFilter(SECURITY_FILTER_NAME, proxy);
        filter.setAsyncSupported(true);
        filter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, DISPATCHER_SERVLET_NAME);
    }
}
