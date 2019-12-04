package com.jizhi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.service.PropertyService;

@RestController
@RequestMapping("property")
public class PropertyController {
	
	@Autowired
	private PropertyService propertyService;
}
