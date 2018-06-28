package com.tenmax.controller;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody String admin() {
		System.out.println("a login!");
		return "get a";
	}
	
	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	public @ResponseBody String provider() {
		System.out.println("b login!");
		return "get b";
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody String user() {
		System.out.println("c login!");
		return "get c";
	}
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public @ResponseBody String about() {
		System.out.println("about!");
		return "about";
	}
	//==================== method annotation ====================
	@DenyAll
	@RequestMapping(value = "/deny", method = RequestMethod.GET)
	public @ResponseBody String deny() {
		System.out.println("all user cant get this!");
		return "---";
	}
	
	@RolesAllowed({"ADMIN","PROVIDER"})
	@RequestMapping(value = "/adminAT", method = RequestMethod.GET)
	public @ResponseBody String adminAT() {
		System.out.println("a login !");
		return "a login";
	}

	@PermitAll
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody String all() {
		System.out.println("everyone can login !");
		return "everyone can login";
	}

	@PermitAll
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public @ResponseBody String error() {
		System.out.println("error page!");
		return "error page";
	}

	@RequestMapping(value="/logout1", method = RequestMethod.GET)
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "LogoutSuccess。default redirect:/login?logout";
	}
}
