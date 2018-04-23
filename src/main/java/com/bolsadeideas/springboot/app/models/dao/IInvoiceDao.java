package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {

	//Si utilizamos el método findById() de la interfaz CrudRepository
	//al tener varias relaciones, se realizan demasiadas consultas
	//a la base de datos. Para evitarlo, y mejorar el rendimiento general
	//de la aplicación, personalizamos la query usando joins
	@Query("select i from Invoice i join fetch i.client c join fetch i.lines l join fetch l.product where i.id=?1")
	public Invoice fetchByIdWithClientWithInvoiceLineWithProduct(Long id);
	
}
