package com.nuvola.myproject.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(ApplicationPresenter.class).asEagerSingleton();
        bind(ApplicationPresenter.MyProxy.class).asEagerSingleton();
        bind(ApplicationPresenter.MyView.class).to(ApplicationView.class);
    }
}
