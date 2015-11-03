package com.notemedown.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloWorldController {
	private static Logger logger = LoggerFactory.getLogger(HelloWorldController.class); 
	
	@RequestMapping(method = RequestMethod.GET)
	public String printHello(ModelMap model) {
		if (logger.isDebugEnabled())
			logger.debug("test message");
		
		model.addAttribute("message", "Spring MVC - Hello World");
		return "hello";
	}
}