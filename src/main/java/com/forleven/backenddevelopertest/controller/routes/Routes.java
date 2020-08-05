package com.forleven.backenddevelopertest.controller.routes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class Routes {
	@GetMapping({"/api", "/api/docs"})
	public String api() {
		return "redirect:/swagger-ui.html";
	}
}
