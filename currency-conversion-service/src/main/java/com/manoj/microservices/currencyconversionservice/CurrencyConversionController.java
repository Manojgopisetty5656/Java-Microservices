package com.manoj.microservices.currencyconversionservice;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;




//Rest template mapping was not shown in zipkin so adding this

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration
{
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder)
	{
		return builder.build();
	}
}




@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,@PathVariable String to,@PathVariable double quantity) {
		HashMap<String, String> uriVariables=new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,uriVariables);
		CurrencyConversion currencyConversion=responseEntity.getBody();
		double total=quantity*currencyConversion.getConversionMultiple();
		
		return new CurrencyConversion(currencyConversion.getId(),from,to,quantity,currencyConversion.getConversionMultiple(),total,currencyConversion.getEnvironment() + " Rest Template");
		
		}
	
	
	//using feign
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,@PathVariable String to,@PathVariable double quantity) {
		
		CurrencyConversion currencyConversion=proxy.retrieveExchangeValue(from, to);
		double total=quantity*currencyConversion.getConversionMultiple();
	
		return new CurrencyConversion(currencyConversion.getId(),from,to,quantity,currencyConversion.getConversionMultiple(),total,currencyConversion.getEnvironment() + " feign");
		
		}
	
	
	

}
