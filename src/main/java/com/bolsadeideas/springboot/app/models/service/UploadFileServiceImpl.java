package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/*
 * El objetivo de este servicio es desacoplar 
 * todo el código de manejo de subida de archivos
 * que teníamos en el controlador, evitando código
 * repetido
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final String UPLOADS_FOLDER = "uploads";
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Resource load(String filename) throws MalformedURLException {
		//Cargamos la ruta de la imagen requerida
		Path photoPath = getPath(filename);
		log.info("photoPath: " + photoPath);
		Resource resource = null;
		resource = new UrlResource(photoPath.toUri());
		if(!resource.exists() || !resource.isReadable()) {
			//Si se intenta cargar una imagen que no existe lanzamos una excepción
			throw new RuntimeException("Error: No se puede cargar la imagen: " + photoPath.toString());
		} 
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String photoName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); 
		Path _rootPath = getPath(photoName);
		log.info("_rootPath: " + _rootPath);
		//Copiar foto a ruta dentro del proyecto
		Files.copy(file.getInputStream(), _rootPath);
		return photoName;
	}

	@Override
	public boolean delete(String filename) {
		//Borramos la imagen del cliente
		Path rootPath = getPath(filename);
		File file = rootPath.toFile();
		if(file.exists() && file.canRead()) {
			if(file.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return  Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));
	}

}
