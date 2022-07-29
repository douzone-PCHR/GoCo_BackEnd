package com.pchr.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardRestController {
	@GetMapping(value = "/")
	public String test() {
		return "test";
	}

}
