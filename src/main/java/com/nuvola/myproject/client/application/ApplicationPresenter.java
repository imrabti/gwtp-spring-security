package com.nuvola.myproject.client.application;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.nuvola.myproject.client.NameTokens;
import com.nuvola.myproject.client.application.ApplicationPresenter.MyProxy;
import com.nuvola.myproject.client.application.ApplicationPresenter.MyView;
import com.nuvola.myproject.client.services.UserService;
import com.nuvola.myproject.shared.model.User;

public class ApplicationPresenter extends Presenter<MyView, MyProxy> {
    @ProxyStandard
    @NameToken(NameTokens.HOME)
    interface MyProxy extends ProxyPlace<ApplicationPresenter> {
    }

    interface MyView extends View {
        void displayUserName(String userName);
    }

    private final RestDispatch dispatch;
    private final UserService userService;

    @Inject
    ApplicationPresenter(EventBus eventBus,
                         MyView view,
                         MyProxy proxy,
                         RestDispatch dispatch,
                         UserService userService) {
        super(eventBus, view, proxy, RevealType.Root);
        this.dispatch = dispatch;
        this.userService = userService;
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        dispatch.execute(userService.getCurrentUser(), new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error during getting connected user info...");
            }

            @Override
            public void onSuccess(User result) {
                getView().displayUserName(result.getLogin());
            }
        });
    }
}
