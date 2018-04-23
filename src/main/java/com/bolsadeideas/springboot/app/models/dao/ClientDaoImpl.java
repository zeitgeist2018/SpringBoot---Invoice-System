package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Client;

//@Repository("clientDao") Si tenemos varias implementaciones, utilizamos
//un nombre para cada una. Si no, lo hacemos así
@Repository
public class ClientDaoImpl implements IClientDao{

	@PersistenceContext
	private EntityManager em;


	@Override
	public Client findOne(Long id) {
		return em.find(Client.class, id);
	}
	
	@Override
	public List<Client> findAll() {
		return em.createQuery("from Client").getResultList();
	}

	@Override
	public void save(Client client) {
		if(client.getId() != null && client.getId() > 0) {
			em.merge(client);
		} else {
			em.persist(client);
		}
	}

	@Override
	public void delete(Long id) {
		if(id != null && id > 0) {
			em.remove(findOne(id));
		}
	}
}
