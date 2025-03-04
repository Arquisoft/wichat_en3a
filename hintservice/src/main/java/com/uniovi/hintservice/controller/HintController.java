package com.uniovi.hintservice.controller;

import com.uniovi.hintservice.service.LLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HintController {

	@Autowired
	private LLMService llmService;

	@RequestMapping(value="/hint", method = RequestMethod.GET)
	public String askHint(String hint){
		return null;
	}

}
