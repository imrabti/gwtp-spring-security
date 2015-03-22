package com.nuvola.myproject.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import com.nuvola.myproject.client.application.ApplicationModule;
import com.nuvola.myproject.client.services.ServiceModule;
import com.nuvola.myproject.client.util.CurrentUser;

public class MyModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new DefaultModule.Builder()
                .placeManager(DefaultPlaceManager.class)
                .tokenFormatter(RouteTokenFormatter.class)
                .build());

        bind(CurrentUser.class).asEagerSingleton();

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.getHome());
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.getHome());
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.getHome());

        install(new ServiceModule());
        install(new ApplicationModule());
    }
}
