package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/*
 * Crear una interfaz para un servicio como ésta
 * no es estríctamente necesario, pero de esta manera
 * aseguramos un código más escalable y mantenible en 
 * el futuro, ya que podríamos crear varias implementaciones
 * del servicio que respeten la misma estructura. 
 */
public interface IUploadFileService {

	public Resource load(String filename) throws MalformedURLException;

	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;
}
