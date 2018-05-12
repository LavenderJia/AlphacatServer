package com.alphacat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This navigation controller may need editing later. 
 * Page names should change according to the front end. 
 * <p>
 * TODO delete this label when names have been correctly changed. 
 *
 * @auther yyx
 */
@Controller
public class NavigationController {

	@RequestMapping(value={"/", "/index"})
	public String toIndexPage() {
		return "index";
	}

    @RequestMapping("/login")
    public String toLoginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String toRegisterPage() {
        return "register";
    }

    @RequestMapping("/register/publisher")
    public String toP_RegisterPage() {
        return "publisherRegister";
    }

    @RequestMapping("/register/worker")
    public String toW_RegisterPage() {
        return "workerRegister";
    }

    @RequestMapping("/example")
    public String toExamplePage() {
        return "example";
    }

    @RequestMapping("/contact")
    public String toContactPage() {
        return "contact";
    }

}
