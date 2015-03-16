package com.nuvola.myproject.server.security;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import com.nuvola.myproject.server.service.UserService;
import com.nuvola.myproject.shared.model.User;

public class SimpleSignInAdapter implements SignInAdapter {
    private static final Logger log = LoggerFactory.getLogger(SimpleSignInAdapter.class);
    private final RequestCache requestCache;

    @Autowired
    private UserService userService;
    //@Autowired
    //private PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices;

    @Inject
    public SimpleSignInAdapter(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        User user = userService.getLocalUserByUsername(localUserId);

        Authentication authentication = SignInUtils.signin(user.getLogin());

//		//HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication
//		 // set remember-me cookie
//        tokenBasedRememberMeServices.onLoginSuccess(
//                (HttpServletRequest) request.getNativeRequest(),
//                (HttpServletResponse) request.getNativeResponse(),
//                authentication);

        return extractOriginalUrl(request);
    }

    private String extractOriginalUrl(NativeWebRequest request) {
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        requestCache.removeRequest(nativeReq, nativeRes);
        removeAutheticationAttributes(nativeReq.getSession(false));
        return saved.getRedirectUrl();
    }

    private void removeAutheticationAttributes(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
