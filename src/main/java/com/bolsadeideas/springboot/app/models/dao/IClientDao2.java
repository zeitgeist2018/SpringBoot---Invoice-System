package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Client;

/*
 * Extendiendo de CrudRepository(Modelo, Tipo de PK) podemos
 * ahorrarnos el implementar los métodos típicos de CRUD.
 * De esta manera no necesitamos implementar la clase
 * ClientDaoImpl
 */
//public interface IClientDao2 extends CrudRepository<Client, Long> {

//Podemos extender de PagingAndSortingRepository para facilitar
//la implementación de un sistema de paginación
public interface IClientDao2 extends PagingAndSortingRepository<Client, Long> {
	
	@Query("select c from Client c left join fetch c.invoices i where c.id=?1")
	public Client fetchByIdWithInvoice(Long id);
	
}
