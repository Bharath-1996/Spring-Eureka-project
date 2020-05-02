package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;




@Controller
public class TradeController {

	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/")
	public String indexpage()
	{
		
		return "index";
		
	}
	@RequestMapping("/hello")
	@ResponseBody
	public String hello()
	{
		String regservice="register-service";
		List<ServiceInstance> instances=discoveryClient.getInstances(regservice);
		URI uri=instances.get(0).getUri();
		String url=uri.toString()+"/users";
		ResponseEntity<String> result=restTemplate.getForEntity(url, String.class);
		if(result.getStatusCode()==HttpStatus.OK)
		return result.getBody();
		else
			return null;
		
	}
	@RequestMapping("/Trade")
	public String indexpge()
	{
		
		return "Check";
		
	}
	  
	
	
	
}
