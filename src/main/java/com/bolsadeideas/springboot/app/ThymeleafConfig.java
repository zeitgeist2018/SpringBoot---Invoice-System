package com.bolsadeideas.springboot.app;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {
	@Bean
	public SpringTemplateEngine springTemplateEngine()
	{
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.addTemplateResolver(htmlTemplateResolver());
	    return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver htmlTemplateResolver()
	{
	    SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
	    emailTemplateResolver.setPrefix("classpath:/templates");
	    emailTemplateResolver.setSuffix(".html");
	   // emailTemplateResolver.setTemplateMode(StandardTemplateModeHandlers.HTML5.getTemplateModeName());
	    emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
	    return emailTemplateResolver;
	}
}
