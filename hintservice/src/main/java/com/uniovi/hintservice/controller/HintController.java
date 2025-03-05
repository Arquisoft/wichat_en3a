package com.uniovi.hintservice.controller;

import com.uniovi.hintservice.service.GenAI;
import com.uniovi.hintservice.service.LLMService;
import com.uniovi.hintservice.service.MultiModal;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class HintController {
	@Autowired
	private GenAI genAI;

	@PostMapping("/test")
	public String askHint(@RequestParam String userMessage, @RequestParam String answerQuestion, Model model) {
		// Llamar al servicio para obtener la respuesta de la IA
//		String result = llmService.askLLM(userMessage, answerQuestion);
//
//		// Añadir la respuesta al modelo
//		model.addAttribute("result", result);

		// Retornar la misma vista con la respuesta
		return "redirect:/hint";  // El nombre de la vista (el archivo HTML)
	}

	@GetMapping("/hint")
	public String showHint(Model model) throws IOException, HttpException {
//		String userMessage="¿Donde está la torre eiffel?";
//		String answerQuestion="Francia";
//		String result = llmService.askLLM(userMessage, answerQuestion);

		String result = genAI.ask();

		// Añadir la respuesta al modelo
		//String result= MultiModal.ask();
		model.addAttribute("result", result);
		// Mostrar la respuesta desde el modelo
		return "/hint";  // Asegúrate de tener un archivo hint.html en resources/templates
	}
}
