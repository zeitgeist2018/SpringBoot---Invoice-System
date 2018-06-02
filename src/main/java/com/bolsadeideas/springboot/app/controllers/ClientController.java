package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("client")
public class ClientController {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	//@Qualifier("clientDao") Si tenemos varias implementaciones
	//de la interfaz, indicamos cual queremos usar dando su nombre
	//Si solo tenemos una, usamos @Autowired
	@Autowired
	private IClientService clientService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;
	
	
	//Método para cargar imágenes
	//El parametro {filename:.+} lo escribimos como expresión regular
	//ya que si no Spring eliminaría la extensión del mismo (del archivo)
	//y la necesitamos para buscar el fichero en el equipo.
	//Cuando la vista intenta cargar la imagen desde la carpeta uploads
	//este método es llamado
	@Secured("ROLE_USER")
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		Resource resource = null;
		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	//@Secured("ROLE_USER")
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		//Client client = clientService.findOne(id);
		Client client = clientService.fetchByIdWithInvoice(id);
		if(client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/clientes";
		}else {
			model.put("client", client);
			model.put("title", "Detalles cliente - " + client.getName());
		}
		return "/ver";
	}

	@RequestMapping(value= {"/clientes", "/"}, method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, 
			Model model, 
			Authentication authentication,
			HttpServletRequest request,
			Locale locale) {
		if(authentication != null) {
			log.info("El usuario actual es " + authentication.getName());
		}
		/*
		 * También podemos obtener acceso a authentication sin inyectarlo, a través del
		 * método estático SecurityContextHolder.getContext().getAuthentication();
		 * Ésto nos permite utilizarla en cualquier clase
		 */
		
		//Comprobamos si el usuario tiene el rol necesario para este recurso
		if(hasRole("ROLE_ADMIN")) {
			log.info("El usuario tiene el role necesario para acceder a éste recurso");
		}else {
			log.error("El usuario NO tiene el role necesario para acceder a éste recurso");
		}
		//De esta manera podemos hacer lo mismo, sin tener que implementar el método hasRole()
		/*SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("Usando SecurityContextHolderAwareRequestWrapper: El usuario tiene el role necesario para acceder a éste recurso");
		}else {
			log.info("Usando SecurityContextHolderAwareRequestWrapper: El usuario NO tiene el role necesario para acceder a éste recurso");
		}*/
		//Esta es otra manera más de hacer lo mismo, pero utilizando el objeto request inyectado al método
		if(request.isUserInRole("ROLE_ADMIN")) {
			log.info("Usando HttpServletRequest: El usuario tiene el role necesario para acceder a éste recurso");
		}else {
			log.info("Usando HttpServletRequest: El usuario NO tiene el role necesario para acceder a éste recurso");
		}
		
		//Pageable pageRequest = new PageRequest(page, 5);
		Pageable pageRequest = PageRequest.of(page, 3);	//Spring Boot 2
		Page<Client> clients = clientService.findAll(pageRequest);
		PageRender<Client> render = new PageRender<>("/clientes", clients);
		model.addAttribute("title", messageSource.getMessage("text.list.title", null, locale));
		model.addAttribute("clients", clients);
		model.addAttribute("page", render);
		return "/list";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form")
	public String crear(Map<String, Object> model) {
		Client client = new Client();
		model.put("title", "Formulario de cliente");
		model.put("client", client);
		return "/form";
	}

	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")	//La anotación @PreAuthorize es igual que @Secured, solo que permite más control
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		if(id > 0) {
			Client client = clientService.findOne(id);
			if(client != null) {
				model.put("title", "Editar cliente");
				model.put("client", client);
				return "/form";
			}else {
				flash.addFlashAttribute("error", "El ID no es válido");
				return "redirect:/clientes";
			}
		}else {
			flash.addFlashAttribute("error", "El ID tiene que ser positivo");
			return "redirect:/clientes";
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus sessionStatus) {
		if(result.hasErrors()) {
			model.addAttribute("title", "Formulario de cliente");
			return "/form";
		}
		if(!photo.isEmpty()) {
			/*
			 * Podrían utilizarse las dos lineas siguientes para 
			 * guardar las imagenes subidas a una carpeta dentro
			 * de la estructura del proyecto, pero ésto está 
			 * desaconsejado. Lo mejor es desacoplarla y guardar
			 * los archivos en una carpeta externa.
			 */
			//Path resources = Paths.get("src/main/resources/static/uploads");
			//String rootPath = resources.toFile().getAbsolutePath();
			//String rootPath = "C://Temp//uploads";

			//Añadimos al nombre de la imagen un "unique id"
			//para asegurarnos de que no se repiten

			//Si el usuario ya tenía foto, eliminamos la antigua
			if(client.getId() != null && client.getId() > 0
					&& client.getPhoto() != null
					&& client.getPhoto().length() > 0) {
				uploadFileService.delete(client.getPhoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(photo);
			}catch(IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute(
					"info", 
					"Imagen subida correctamente (" + uniqueFileName + ")");
			client.setPhoto(uniqueFileName);
		}
		String message = client.getId() != null ? "Cliente editado con éxito" : "Cliente creado con éxito";
		clientService.save(client);
		sessionStatus.setComplete();
		flash.addFlashAttribute("success", message);
		return "redirect:clientes";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash, Map<String, Object> model) {
		if(id > 0) {
			Client client = clientService.findOne(id);
			clientService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
			if(uploadFileService.delete(client.getPhoto())) {
				flash.addFlashAttribute("info", "Foto " + client.getPhoto() + " eliminada con éxito");
			}
		}
		return "redirect:/clientes";
	}

	/*
	 * Éste método permite tener más control sobre los roles del usuario, pudiendo acceder a cada uno de ellos
	 */
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null) {
			Authentication auth = context.getAuthentication();
			if(auth != null) {
				Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
				/*for(GrantedAuthority authority : authorities) {
					if(role.equals(authority.getAuthority())) {
						return true;
					}
				}*/
				return authorities.contains(new SimpleGrantedAuthority(role));	//Esta forma es más escueta que utilizando el for
			}
		}
		return false;
	}
}
