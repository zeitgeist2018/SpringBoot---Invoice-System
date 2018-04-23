package com.bolsadeideas.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {

	//Este método sirve para ser llamado por los botones de cambiar de idioma de
	//la barra de navegación. De esta manera, los otros parámetros que pudiese
	//haber en la URL se mantienen, ya que si se añade diréctamente el parámetro
	//?lang=idioma con un href, el resto de parámetros se eliminan
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String lastUrl = request.getHeader("referer");	//El header 'referer' entrega la url de la última página
		return "redirect:".concat(lastUrl);
	}

}
