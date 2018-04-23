package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.Product;

public interface IClientService {
	public List<Client> findAll();
	
	public Page<Client> findAll(Pageable page);

	public void save(Client client);

	public Client findOne(Long id);

	public void delete(Long id);
	
	public List<Product> findByName(String search);
	
	public Product findProductById(Long id);
	
	public void saveInvoice(Invoice invoice);
	
	public Invoice findInvoiceById(Long id);
	
	public void deleteInvoice(Long id);
	
	public Invoice fetchByIdWithClientWithInvoiceLineWithProduct(Long id);

	public Client fetchByIdWithInvoice(Long id);
}
