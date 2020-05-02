package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;




@Controller
public class RegisterController {

	@Autowired
	UserRepository repo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DiscoveryClient discoveryClient;

@Autowired
private LoadBalancerClient loadbalancerclient;
	
	@RequestMapping("/")
	public String indexpage()
	{
		
		return "index";
		
	}
	@RequestMapping("/hello")
	@ResponseBody
	public String hello()
	{
		
		return "welcome";
		
	}
	@RequestMapping(value="/users/register", method=RequestMethod.POST)
	@ResponseBody
    public String Register( User testObj){
		
		repo.save(testObj);
		return "<html><body>Registered Successfully<br><a href='http://localhost:7070/Login.html'>Login</a></body></html>";
		//return "redirect:/";
        //return "login";
    }
	@RequestMapping(value="/users/login", method=RequestMethod.POST)
	@ResponseBody
    public String Login( User testObj,HttpServletRequest request,Model model,HttpServletResponse response){
		
		User u=(User) repo.findByUserid(testObj.getUserid());     //04023324614  1967
		request.getSession().setAttribute("user", u);
		String tradeservice="trade-service";
		if(u.getPassword().contentEquals(testObj.getPassword()))
		{
			/*
			  //using disoveryclient to check the instances
			List<ServiceInstance> instances=discoveryClient.getInstances(tradeservice);
			URI uri=instances.get(0).getUri();
			*/
			
			//using loadbalancerclient to choose instances
			ServiceInstance instance=loadbalancerclient.choose(tradeservice);
			URI uri=URI.create(String.format("http://%s:%s", instance.getHost(),instance.getPort()));
			
			String url=uri.toString()+"/hello";
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ModelAndView model = new ModelAndView("Trade");
	        //model.addAttribute("name",testObj.getUserid().toString());
	        // return model;
			//return "Trade";
		}
		//ModelAndView model = new ModelAndView("Login");
		//return "redirect:/";
        return "Login";
    }
	@RequestMapping(value="/users", method=RequestMethod.GET)
	@ResponseBody
    public Iterable<User> users(){
		
		return repo.findAll();
    }
	
	@HystrixCommand(fallbackMethod = "defaultcountries",
			commandProperties = {
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="5"),
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="5000")
			})
	@RequestMapping(value="/countries", method=RequestMethod.GET)
	@ResponseBody
    public String getcountries(){
		
		String result=restTemplate.getForObject("https://restcountries.eu/rest/v2/all", String.class);
		if(result!=null)
			return result;
		return null;
    }
	public String defaultcountries()
	{
		return "India";
	}
}
