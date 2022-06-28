package com.kumonosu.filemanagerapi.app.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kumonosu.filemanagerapi.app.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

	private final static String PUBLIC_FOLDER = "my-storage";

	private final Path root = Paths.get(PUBLIC_FOLDER);

	@Override
	public void init() {
		try {
			if (!Files.exists(root)) {
				Files.createDirectory(root);
			} else {
				System.out.println("El directorio ya está creado: " + root.getName(0));
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public void store(MultipartFile file) {
		try {
			System.out.println(this.root);
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (FileAlreadyExistsException alreadyExistsException) {
			log.info("Error: File already exists.");
			throw new RuntimeException("Error: File already exists " + alreadyExistsException.getMessage());
		} catch (Exception e) {
			log.info("Could not store the file. Error: " + e.getMessage());
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				log.info("Loading... " + filename);
				return resource;
			} else {
				log.info("Could not read the file!");
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void delete(String filename) {
		try {
			Files.delete(this.root.resolve(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
		init();
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
}