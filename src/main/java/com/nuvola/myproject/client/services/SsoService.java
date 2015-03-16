package com.nuvola.myproject.client.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.gwtplatform.dispatch.rest.shared.RestAction;

import static com.nuvola.myproject.shared.ResourcePaths.SIGNIN_CORPORATE;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path(SIGNIN_CORPORATE)
public interface SsoService {
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    RestAction<Void> casLogin();
}
