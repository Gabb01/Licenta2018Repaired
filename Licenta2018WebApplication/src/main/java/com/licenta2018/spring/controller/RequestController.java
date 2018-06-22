package com.licenta2018.spring.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.licenta2018.spring.model.CustomUserDetail;
import com.licenta2018.spring.model.Request;
import com.licenta2018.spring.model.User;
import com.licenta2018.spring.repository.RequestRepository;
import com.licenta2018.spring.repository.RequestTypeRepository;
import com.licenta2018.spring.repository.UserRepository;
import com.licenta2018.spring.utility.PdfRequestView;
import com.licenta2018.spring.utility.RequestNotFoundException;

@Controller
@RequestMapping(value="/requests")
public class RequestController {

    @Autowired
    UserRepository users;
    
    @Autowired
    RequestTypeRepository requestTypes;
    
    @Autowired
    RequestRepository requests;
    
    /*
     * GET METHOD
     * Path : /requests/new (From header)
     * Handles the form to fill the data and create a new request.
     * Returns requests/create.html view
     */
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String createNewRequest(Model model) {
    	Request r = new Request();    	
    	model.addAttribute("request", r);
    	model.addAttribute("requestTypes", requestTypes.findAll());
    	return "requests/create";
    }
    
    /*
     * POST METHOD
     * Properly creates the request in SQL Database and redirects to /requests page
     */
	@RequestMapping(method=RequestMethod.POST)
	public String onSaveRequest(@ModelAttribute Request request, Model model) {
		Date date = new Date();
		request.setUser(getCurrentUser());
		request.setIssueDate(date);
		request.setApprovalDate(null);
		request.setApprovalStatus(false);
			
		requests.save(request);
		model.addAttribute("request", request);
		return "redirect:/requests";
	}
	
	@RequestMapping(value="{id}/remove", method=RequestMethod.GET)
	public String onDeleteRequest(@PathVariable("id") long id, Model model) {
		Request request = requests.findOne(id);    	
		if( request == null )
			throw new RequestNotFoundException();    	
		requests.delete(request);
		model.addAttribute("requests", requests.findAll());
		return "redirect:/users/me";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("requests", requests.findAll());
		return "requests/index";
	}
	
	@RequestMapping(value = "{id}/approve", method = RequestMethod.GET)
	public String onApproveRequest(@PathVariable("id") long id, Model model)
	{
		Date approvalDate = new Date();
		Request request = requests.findOne(id);
		if(request == null)
			throw new RequestNotFoundException();
		request.setApprovalStatus(true);
		request.setApprovalDate(approvalDate);
		requests.save(request);
		model.addAttribute("requests", requests.findAll());
		return "redirect:/users/me";
	}
	
	@RequestMapping(value = "{id}/download/request", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport(@PathVariable("id") long id) throws IOException {

        Request request = requests.findOne(id);

        ByteArrayInputStream bis = PdfRequestView.citiesReport(getCurrentUser(), request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=request.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
	private User getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();    	    
		CustomUserDetail myUser= (CustomUserDetail) auth.getPrincipal(); 
		return myUser.getUser();
	}
}
