package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")	//De forma predeterminada Spring utiliza 
	public String login(Model model, 
			Principal principal,
			RedirectAttributes flash,
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout) {	//la ruta /login como vista del formulario de login
		if(principal != null) {	//El usuario ya ha iniciado sesión
			flash.addFlashAttribute("info", "Ya has iniciado sesión");
			return "redirect:/";
		}
		if(error != null) {
			model.addAttribute("error", "Usuario y/o password incorrecto");
		}
		if(logout != null) {
			model.addAttribute("info", "Has cerrado sesión");
		}
		return "/login";
	}

}
