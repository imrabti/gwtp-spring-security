package com.nuvola.myproject.client.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.nuvola.myproject.shared.ResourcePaths;

public class ServiceModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new RestDispatchAsyncModule.Builder().build());
    }

    @Provides
    @RestApplicationPath
    String getApplicationPath() {
        String baseUrl = GWT.getHostPageBaseURL();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl + ResourcePaths.ROOT;
    }
}
