package com.manoj.microservices.limitsservice;

import com.manoj.microservices.limitsservice.bean.Limits;
import com.manoj.microservices.limitsservice.configuration.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {
	
	@Autowired
	Configuration configuration;
	
	@GetMapping("/limits")
	public Limits retrieveLimits()
	{
		//return new Limits(1,1000);
		return new Limits(configuration.getMinimum(),configuration.getMaximum());
		
	}

}
