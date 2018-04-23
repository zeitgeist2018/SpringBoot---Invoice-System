package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClientDao2;
import com.bolsadeideas.springboot.app.models.dao.IInvoiceDao;
import com.bolsadeideas.springboot.app.models.dao.IProductDao;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.Product;

@Service
public class ClientServiceImpl implements IClientService {

	@Autowired
	private IClientDao2 clientDao;

	@Autowired
	private IProductDao productDao;
	
	@Autowired
	private IInvoiceDao invoiceDao;
	

	@Override
	@Transactional(readOnly=true)
	public List<Client> findAll() {
		return (List<Client>) clientDao.findAll();
	}

	@Override
	@Transactional
	public void save(Client client) {
		clientDao.save(client);
	}

	@Override
	@Transactional(readOnly=true)
	public Client findOne(Long id) {
		//return clientDao.findOne(id); //Spring Boot 1.5.10
		return clientDao.findById(id).orElse(null);	//Spring Boot 2
	}
	
	@Override
	@Transactional(readOnly=true)
	public Client fetchByIdWithInvoice(Long id) {
		return clientDao.fetchByIdWithInvoice(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		//clientDao.delete(id);
		clientDao.deleteById(id);	//Spring Boot 2
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Client> findAll(Pageable page) {
		return clientDao.findAll(page);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Product> findByName(String search) {
		return productDao.findByNameLikeIgnoreCase("%" + search + "%");
	}

	@Override
	@Transactional
	public void saveInvoice(Invoice invoice) {
		invoiceDao.save(invoice);
	}

	@Override
	@Transactional(readOnly=true)
	public Product findProductById(Long id) {
		return productDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice findInvoiceById(Long id) {
		return invoiceDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteInvoice(Long id) {
		invoiceDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice fetchByIdWithClientWithInvoiceLineWithProduct(Long id) {
		return invoiceDao.fetchByIdWithClientWithInvoiceLineWithProduct(id);
	}



}
