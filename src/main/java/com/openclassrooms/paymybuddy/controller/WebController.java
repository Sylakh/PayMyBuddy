package com.openclassrooms.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

	@GetMapping("/login")
	public String loginPage() {
		// custom logic before showing login page...

		return "login";
	}
}
