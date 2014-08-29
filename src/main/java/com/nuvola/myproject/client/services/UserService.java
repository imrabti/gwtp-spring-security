package com.nuvola.myproject.client.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.gwtplatform.dispatch.rest.shared.RestAction;
import static com.nuvola.myproject.shared.Parameters.PASSWORD;
import static com.nuvola.myproject.shared.Parameters.USERNAME;
import static com.nuvola.myproject.shared.ResourcePaths.User.ROOT;
import static com.nuvola.myproject.shared.ResourcePaths.User.LOGIN;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path(ROOT)
public interface UserService {
    @POST
    @Path(LOGIN)
    @Consumes(APPLICATION_FORM_URLENCODED)
    RestAction<Void> login(@FormParam(USERNAME) String username, @FormParam(PASSWORD) String password);

    @DELETE
    @Path(LOGIN)
    RestAction<Void> logout();

    @GET
    @Path(LOGIN)
    RestAction<Boolean> isCurrentUserLoggedIn();
}
