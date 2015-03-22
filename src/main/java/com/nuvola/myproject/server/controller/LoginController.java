package com.nuvola.myproject.server.controller;

import javax.annotation.security.PermitAll;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    @RequestMapping(value = "/access/login", method = RequestMethod.GET)
    @PermitAll
    public ModelAndView signUp(ModelMap model) {
        // model.addAttribute("");
        return new ModelAndView("login");
    }
}
