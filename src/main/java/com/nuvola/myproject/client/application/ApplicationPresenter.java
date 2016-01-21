package com.nuvola.myproject.client.application;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.nuvola.myproject.client.NameTokens;
import com.nuvola.myproject.client.application.ApplicationPresenter.MyProxy;
import com.nuvola.myproject.client.application.ApplicationPresenter.MyView;
import com.nuvola.myproject.client.security.CurrentUser;
import com.nuvola.myproject.client.services.UserService;
import com.nuvola.myproject.shared.model.User;

public class ApplicationPresenter extends Presenter<MyView, MyProxy> implements ApplicationUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.HOME)
    interface MyProxy extends ProxyPlace<ApplicationPresenter> {
    }

    interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
        void displayUserName(String userName);
    }

    private final RestDispatch dispatch;
    private final UserService userService;
    private final CurrentUser currentUser;
    private final PlaceManager placeManager;

    @Inject
    ApplicationPresenter(EventBus eventBus,
                         MyView view,
                         MyProxy proxy,
                         RestDispatch dispatch,
                         UserService userService,
                         CurrentUser currentUser,
                         PlaceManager placeManager) {
        super(eventBus, view, proxy, RevealType.Root);

        this.dispatch = dispatch;
        this.userService = userService;
        this.currentUser = currentUser;
        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void logout() {
        dispatch.execute(userService.logout(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error during logging out the connected user...");
            }

            @Override
            public void onSuccess(Void result) {
                PlaceRequest placeRequest = new PlaceRequest.Builder()
                        .nameToken(NameTokens.getLogin())
                        .build();
                currentUser.setLoggedIn(false);
                placeManager.revealPlace(placeRequest);
            }
        });
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
