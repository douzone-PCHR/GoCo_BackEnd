package com.pchr.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRestController {
	
	@GetMapping(value = "/test")
	public String test() {
		System.out.println("ㅇㅇㅇ");
		return "ok";
	}
	
}
