package com.nuvola.myproject.client.login;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.nuvola.myproject.client.NameTokens;
import com.nuvola.myproject.client.login.LoginPresenter.MyProxy;
import com.nuvola.myproject.client.login.LoginPresenter.MyView;
import com.nuvola.myproject.client.services.SsoService;
import com.nuvola.myproject.client.services.UserService;
import com.nuvola.myproject.client.util.CurrentUser;

public class LoginPresenter extends Presenter<MyView, MyProxy> implements LoginUiHandlers {
    interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
    }

    @NameToken(NameTokens.LOGIN)
    @ProxyStandard
    @NoGatekeeper
    interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    private final PlaceManager placeManager;
    private final RestDispatch dispatcher;
    private final UserService userService;
    private final SsoService ssoService;
    private final CurrentUser currentUser;

    @Inject
    LoginPresenter(EventBus eventBus,
                   MyView view,
                   MyProxy proxy,
                   PlaceManager placeManager,
                   RestDispatch dispatcher,
                   UserService userService,
                   SsoService ssoService,
                   CurrentUser currentUser) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.userService = userService;
        this.ssoService = ssoService;
        this.currentUser = currentUser;

        getView().setUiHandlers(this);
    }

    @Override
    public void login(String login, String password) {
        sendLoginRequest(login, password);
    }

    @Override
    public void casLogin() {
        sendCasLoginRequest();
    }

    private void sendCasLoginRequest() {
        dispatcher.execute(ssoService.casLogin(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                onLoginSuccess();
            }

            @Override
            public void onFailure(Throwable caught) {
                onLoginFailure();
            }
        });
    }

    private void sendLoginRequest(String username, String password) {
        dispatcher.execute(userService.login(username, password), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                onLoginSuccess();
            }

            @Override
            public void onFailure(Throwable caught) {
                onLoginFailure();
            }
        });
    }

    private void onLoginSuccess() {
        currentUser.setLoggedIn(true);
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.HOME).build();
        placeManager.revealPlace(placeRequest);
    }

    private void onLoginFailure() {
        Window.alert("Wrong login or password.");
    }
}
