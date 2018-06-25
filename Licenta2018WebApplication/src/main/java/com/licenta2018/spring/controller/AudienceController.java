package com.licenta2018.spring.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.licenta2018.spring.model.CustomUserDetail;
import com.licenta2018.spring.model.Audience;
import com.licenta2018.spring.model.User;
import com.licenta2018.spring.repository.AudienceRepository;
import com.licenta2018.spring.repository.UserRepository;
import com.licenta2018.spring.utility.RequestNotFoundException;

@Controller
@RequestMapping(value="/audiences")
public class AudienceController {
	@Autowired
	AudienceRepository audiences;
	
	@Autowired
	UserRepository users;
	
	/*
     * GET METHOD
     * Path : /audiences/new (From header)
     * Handles the form to fill the data and create a new audience.
     * Returns audiences/create.html view
     */
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String createNewRequest(Model model) {
    	Audience audience = new Audience();    	
    	model.addAttribute("audience", audience);
    	return "audiences/create";
    }
    
    /*
     * POST METHOD
     * Properly creates the audience in SQL Database and redirects to /audiences page
     */
	@RequestMapping(method=RequestMethod.POST)
	public String onSaveRequest(@ModelAttribute Audience audience, Model model) {
		audience.setUser(getCurrentUser());
		audience.setApprovalDate(null);
		audience.setApprovalStatus(false);
		audience.setIssuerName(getCurrentUser().getName());
		audiences.save(audience);
		model.addAttribute("audience", audience);
		return "redirect:/audiences";
	}
	
	@RequestMapping(value = "{id}/approve", method = RequestMethod.GET)
	public String onApproveRequest(@PathVariable("id") long id, Model model)
	{
		Date approvalDate = new Date();
		Audience audience = audiences.findOne(id);
		if(audience == null)
			throw new RequestNotFoundException();
		audience.setApprovalStatus(true);
		audience.setApprovalDate(approvalDate);
		audiences.save(audience);
		model.addAttribute("audiences", audiences.findAll());
		return "redirect:/users/me";
	}
	
	@RequestMapping(value = "{id}/reject", method = RequestMethod.GET)
	public String onRejectRequest(@PathVariable("id") long id, Model model)
	{
		Audience audience = audiences.findOne(id);
		if(audience == null)
			throw new RequestNotFoundException();
		audience.setReject(true);
		audiences.save(audience);
		model.addAttribute("requests", audiences.findAll());
		return "redirect:/users/me";
	}
	
	@RequestMapping(value="{id}/remove", method=RequestMethod.GET)
	public String onDeleteRequest(@PathVariable("id") long id, Model model) {
		Audience audience = audiences.findOne(id);    	
		if( audience == null )
			throw new RequestNotFoundException();    	
		audiences.delete(audience);
		model.addAttribute("audiences", audiences.findAll());
		return "redirect:/users/me";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("audiences", audiences.findAll());
		return "audiences/index";
	}
    
	private User getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();    	    
		CustomUserDetail myUser= (CustomUserDetail) auth.getPrincipal(); 
		return myUser.getUser();
	}
}
