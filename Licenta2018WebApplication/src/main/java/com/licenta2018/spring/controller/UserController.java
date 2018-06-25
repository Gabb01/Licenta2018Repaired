package com.licenta2018.spring.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.licenta2018.spring.model.Audience;
import com.licenta2018.spring.model.Authority;
import com.licenta2018.spring.model.CustomUserDetail;
import com.licenta2018.spring.model.CustomUserDetailsService;
import com.licenta2018.spring.model.Request;
import com.licenta2018.spring.model.User;
import com.licenta2018.spring.repository.AudienceRepository;
import com.licenta2018.spring.repository.AuthorityRepository;
import com.licenta2018.spring.repository.RequestRepository;
import com.licenta2018.spring.repository.UserRepository;
import com.licenta2018.spring.security.SecurityConfig;
import com.licenta2018.spring.utility.UserNotFoundException;

@Controller
@RequestMapping(value="/users")
public class UserController {

	@Autowired
	UserRepository users;
	
	@Autowired
	RequestRepository requests;
	
	@Autowired
	AudienceRepository audiences;
	
	@Autowired
	AuthorityRepository authorities;

	@Autowired 
	private AuthenticationManager authMgr;

	@Autowired 
	private CustomUserDetailsService customUserDetailsSvc;

	// GET  /users/new			- the form to fill the data for a new user
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String onCreateNewUser(Model model) {
		model.addAttribute("user", new User());
		return "users/create";
	}

	// POST /users         	- creates a new user
	@RequestMapping(method=RequestMethod.POST)
	public String onSaveUser(@ModelAttribute User user, Model model)
	{
		try{
			Authority authority = authorities.findByRole("ROLE_USER");
			user.setAuthority(authority);
			String pass = user.getPassword();
			user.setPassword(SecurityConfig.encoder.encode(user.getPassword()));
			users.save(user);
			UserDetails userDetails = customUserDetailsSvc.loadUserByUsername(user.getUsername());
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, pass, userDetails.getAuthorities());
			authMgr.authenticate(auth);
			SecurityContextHolder.getContext().setAuthentication(auth);
			return "redirect:/users/me";
			 
		}
		catch(Exception e){
			return "error";
		}
	}

	// GET /login
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(Model model) {
		return "index";
	}

	@RequestMapping(value="/me", method=RequestMethod.GET)
	public String showActiveProfile(Model model) throws UserNotFoundException
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();    	    
		CustomUserDetail myUser= (CustomUserDetail) auth.getPrincipal();
		User user = users.findOne(myUser.getUser().getId());
		model.addAttribute("requests", getUserRequests(user.getId(), auth));
		model.addAttribute("audiences", getUserAudiences(user.getId()));
		model.addAttribute("user", user);
		return "users/show";
	}

	public Iterable<Audience> getUserAudiences(long user_id)
	{
		Iterator<Audience> audiencesIterator = audiences.findAll().iterator();
		List<Audience> audiencesList = new ArrayList<Audience>();

		while(audiencesIterator.hasNext())
		{
			Audience b = audiencesIterator.next();
			if(b.getUser().getId() == user_id || user_id == 5)
				audiencesList.add(b);
		}

		return audiencesList;
	}  

	public Iterable<Request> getUserRequests(long user_id, Authentication authentication)
	{
		Iterator<Request> requestsIterator = requests.findAll().iterator();
		List<Request> requestsList = new ArrayList<Request>();

		while(requestsIterator.hasNext())
		{
			Request b = requestsIterator.next();
			
			CustomUserDetail principal = (authentication != null) ? (CustomUserDetail) authentication.getPrincipal() : null;

			if(principal.getUser().getAuthority().getRole().equals("ROLE_CONSTRUCTION_AUTHORIZE") && b.getType().getId() == 1)
				requestsList.add(b);
			
			else if(principal.getUser().getAuthority().getRole().equals("ROLE_ENVIRONMENT_AUTHORIZE") && b.getType().getId() == 2)
				requestsList.add(b);
			
			else if(principal.getUser().getAuthority().getRole().equals("ROLE_INFRASTRUCTURE") && b.getType().getId() == 3)
				requestsList.add(b);
			else
				if(b.getUser().getId() == user_id)
					requestsList.add(b);
			
		}

		return requestsList;
	}     
}
