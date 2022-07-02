package com.kumonosu.filemanagerapi.app.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.kumonosu.filemanagerapi.app.controller.FileController;
import com.kumonosu.filemanagerapi.app.dto.FileInfoDto;
import com.kumonosu.filemanagerapi.app.dto.FileInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.mapper.FileInfoMapper;
import com.kumonosu.filemanagerapi.app.entity.FileInfo;
import com.kumonosu.filemanagerapi.app.repository.FileRepository;
import com.kumonosu.filemanagerapi.app.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

	@Autowired
	FileRepository fileRepository;

	private final static String ROOT_FOLDER = "public";

	private final Path root = Paths.get(ROOT_FOLDER);

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
	public FileInfoResponseDto store(MultipartFile file) {

		final UUID corrIdUuid = UUID.randomUUID();
		FileInfoResponseDto fileInfoResponse = new FileInfoResponseDto();

		try {
			String ext = file.getOriginalFilename().split("\\.")[1];
			Files.copy(file.getInputStream(), this.root.resolve(corrIdUuid.toString() + "." + ext));
			FileInfoDto fileInfoDto = setFileInfoDtoDb(file, corrIdUuid.toString(), ext);
			FileInfo fileInfoDb = fileRepository.save(FileInfoMapper.fromDtoToEntity(fileInfoDto));
			fileInfoResponse.setHttpStatus(HttpStatus.OK.name());
			fileInfoResponse.setPath(fileInfoDb.getPath());
			fileInfoResponse.setName(fileInfoDb.getName());
			fileInfoResponse.setId(fileInfoDb.getId());
		} catch (FileAlreadyExistsException alreadyExistsException) {
			log.info("Error: File already exists.");
			throw new RuntimeException("Error: File already exists " + alreadyExistsException.getMessage());
		} catch (Exception e) {
			log.info("Could not store the file. Error: " + e.getMessage());
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
		return fileInfoResponse;
	}

	private FileInfoDto setFileInfoDtoDb(MultipartFile file, String uuid, String ext) {

		FileInfoDto fileInfoDto = new FileInfoDto();
		String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", uuid + "." + ext).build()
				.toString();

		fileInfoDto.setName(uuid + '.' + ext);
		fileInfoDto.setFilaname(file.getOriginalFilename());
		fileInfoDto.setType("FILE_TYPE");
		fileInfoDto.setExtension(ext);
		fileInfoDto.setPath(url);
		fileInfoDto.setCreatedTime(LocalDate.now().toString());

		return fileInfoDto;
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
		fileRepository.deleteAll();
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