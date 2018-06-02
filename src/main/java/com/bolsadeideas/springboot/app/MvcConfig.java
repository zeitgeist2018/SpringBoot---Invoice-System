package com.bolsadeideas.springboot.app;


import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@Configuration
//public class MvcConfig extends WebMvcConfigurerAdapter{	//Spring Boot 1.5.10
public class MvcConfig implements WebMvcConfigurer {	//Spring Boot 2.0.1.RELEASE

	private final Logger log = LoggerFactory.getLogger(getClass());

	//Opción para poder cargar las imágenes desde el directorio
	//También se puede hacer en el controlador
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		String resourcesPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		registry.addResourceHandler("/uploads/**")
			.addResourceLocations(resourcesPath);
		log.info("resourcesPath: " + resourcesPath);
	}*/

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403")
		.setViewName("error_403");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		//Utilizamos el algoritmo BCrypt, que es el más potente actualmente
		//ya que el resultado de la encriptación es diferente para una misma 
		//contraseña
		return new BCryptPasswordEncoder();
	}

	//Este método se encarga de guardar, en este caso en la sesión (aunque
	//se puede guardar en cookies, etc) el "locale".
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es", "ES"));
		return localeResolver;
	}
	
	//Este método se encarga de interceptar, de ahí su nombre, cada vez que se
	//cambia el idioma
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");	//Nombre que queramos para el parámetro del lenguaje en la url
		return interceptor;
	}

	//Esto es necesario para registrar nuestro interceptor en la aplicación
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	
}
