package com.nuvola.myproject.client;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.nuvola.myproject.client.services.UserService;
import com.nuvola.myproject.client.util.CurrentUser;

public class BootstrapperImpl implements Bootstrapper {
    private final String unauthorizedPlace;
    private final CurrentUser currentUser;
    private final PlaceManager placeManager;
    private final RestDispatch dispatcher;
    private final UserService userService;

    @Inject
    BootstrapperImpl(PlaceManager placeManager,
                     CurrentUser currentUser,
                     RestDispatch dispatcher,
                     UserService userService,
                     @UnauthorizedPlace String unauthorizedPlace) {
        this.currentUser = currentUser;
        this.dispatcher = dispatcher;
        this.userService = userService;
        this.unauthorizedPlace = unauthorizedPlace;
        this.placeManager = placeManager;
    }

    @Override
    public void onBootstrap() {
        checkIfUserIsLoggedIn();
    }

    private void checkIfUserIsLoggedIn() {
        dispatcher.execute(userService.isCurrentUserLoggedIn(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                navigate(false);
            }

            @Override
            public void onSuccess(Boolean isCurrentUserLoggedIn) {
                navigate(isCurrentUserLoggedIn);
            }
        });
    }

    private void navigate(Boolean isCurrentUserLoggedIn) {
        currentUser.setLoggedIn(isCurrentUserLoggedIn);

        if (isCurrentUserLoggedIn) {
            placeManager.revealCurrentPlace();
        } else {
            gotoLogin();
        }
    }

    private void gotoLogin() {
        Window.open(GWT.getHostPageBaseURL() + "access/login", "_self", null);
    }
}
