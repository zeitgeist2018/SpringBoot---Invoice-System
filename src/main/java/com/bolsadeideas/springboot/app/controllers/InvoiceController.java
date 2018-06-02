package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.InvoiceLine;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@RequestMapping("/factura")
@Secured("ROLE_ADMIN")
@SessionAttributes("invoice")
public class InvoiceController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/form/{clientId}")
	public String crear(@PathVariable(value="clientId") Long clientId, Map<String, Object> model, RedirectAttributes flash) {
		Client client = clientService.findOne(clientId);
		if(client == null) {
			flash.addFlashAttribute("error", "No existe ese cliente");
			return "redirect:/clients";
		}
		Invoice invoice = new Invoice();
		invoice.setClient(client);
		model.put("invoice", invoice);
		model.put("title", "Crear factura");

		return "/invoices/form";
	}

	@GetMapping(value="/cargar-producto/{search}", produces={"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String search){
		return clientService.findByName(search);
	}

	@PostMapping("/form")
	public String save(
			@Valid Invoice invoice, 
			BindingResult result,
			@RequestParam(name="item_id[]", required=false) Long[] itemId, 
			@RequestParam(name="quantity[]", required=false) Integer[] quantities, 
			RedirectAttributes flash,
			SessionStatus status,
			Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Crear Factura");
			return "/invoices/form";
		}
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("title", "Crear Factura");
			model.addAttribute("error", "La factura debe tener productos");
			return "/invoices/form";
		}
		for(int i=0; i<itemId.length; i++) {
			Product product = clientService.findProductById(itemId[i]);
			InvoiceLine line = new InvoiceLine();
			line.setQuantity(quantities[i]);
			line.setProduct(product);
			invoice.addLine(line);
		}
		clientService.saveInvoice(invoice);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con éxito");
		return "redirect:/ver/" + invoice.getClient().getId();
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id,
			Model model,
			RedirectAttributes flash) {
		//Invoice invoice = clientService.findInvoiceById(id);
		Invoice invoice = clientService.fetchByIdWithClientWithInvoiceLineWithProduct(id);
		if(invoice == null) {
			flash.addFlashAttribute("error", "La factura no existe");
			return "redirect:/clientes";
		}
		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Factura: ".concat(invoice.getDescription()));
		return "/invoices/view";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(
			@PathVariable(value="id") Long id,
			RedirectAttributes flash) {
		Invoice invoice = clientService.findInvoiceById(id);
		if(invoice != null) {
			clientService.deleteInvoice(id);
			flash.addFlashAttribute("success", "Factura eliminada con éxito");
			return "redirect:/ver/" + invoice.getClient().getId();
		}else {
			flash.addAttribute("error", "La factura no existe");
			return "redirect:/clientes";
		}
	}

}
