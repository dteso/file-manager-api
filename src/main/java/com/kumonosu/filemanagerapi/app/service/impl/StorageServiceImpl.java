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

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.kumonosu.filemanagerapi.app.controller.FileController;
import com.kumonosu.filemanagerapi.app.dto.file.FileInfoDto;
import com.kumonosu.filemanagerapi.app.dto.file.mapper.FileInfoMapper;
import com.kumonosu.filemanagerapi.app.entity.FileInfo;
import com.kumonosu.filemanagerapi.app.entity.FolderInfo;
import com.kumonosu.filemanagerapi.app.repository.FileRepository;
import com.kumonosu.filemanagerapi.app.service.FolderInfoService;
import com.kumonosu.filemanagerapi.app.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FolderInfoService folderService;

	private final static String ROOT_FOLDER = "public";

	private final Path root = Paths.get(ROOT_FOLDER);

	@Override
	public void init() {
		try {
			if (!Files.exists(root)) {
				Files.createDirectory(root);
			} else {
				log.info("El directorio ya está creado: " + root.getName(0));
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public FileInfo store(String path, MultipartFile file) {

		final UUID corrIdUuid = UUID.randomUUID();
		FileInfo fileInfoDb = new FileInfo();

		try {

			String ext = file.getOriginalFilename().split("\\.")[1];
			Files.copy(file.getInputStream(), this.root.resolve(corrIdUuid.toString() + "." + ext));

			String[] splittedPath = path.split("/");
			String folderName = splittedPath[splittedPath.length - 1];

			FolderInfo folderInfo = new FolderInfo();

			if (folderService.processPath(path, folderInfo)) {
				folderInfo = folderService.getFolderByName(folderName);
				FileInfoDto fileInfoDto = setFileInfoDtoDb(file, corrIdUuid.toString(), ext, folderInfo);
				fileInfoDb = fileRepository.save(FileInfoMapper.fromDtoToEntity(fileInfoDto));
			} else {
				log.error("Error: Not a valid directory");
				throw new RuntimeException("Not a valid directory");
			}

		} catch (FileAlreadyExistsException alreadyExistsException) {
			log.error("Error: File already exists.");
			throw new RuntimeException("Error: File already exists " + alreadyExistsException.getMessage());
		} catch (SizeLimitExceededException sizeLimitException) {
			log.error("Error: File size limit exceeded");
			throw new RuntimeException("Error: File size limit exceeded" + sizeLimitException.getMessage());
		} catch (Exception e) {
			log.error("Could not store the file. Error: " + e.getMessage());
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
		return fileInfoDb;
	}

	private FileInfoDto setFileInfoDtoDb(MultipartFile file, String uuid, String ext, FolderInfo folderInfo) {

		FileInfoDto fileInfoDto = new FileInfoDto();
		String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", uuid + "." + ext).build()
				.toString();

		fileInfoDto.setName(uuid + '.' + ext);
		fileInfoDto.setFilaname(file.getOriginalFilename());
		fileInfoDto.setType("FILE_TYPE");
		fileInfoDto.setExtension(ext);
		fileInfoDto.setPath(url);
		fileInfoDto.setCreatedTime(LocalDate.now().toString());
		fileInfoDto.setFolder(folderInfo);

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
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public FileInfo getFileByName(String name) {
		return fileRepository.findFileInfoByName(name);
	}

	@Override
	public void delete(String filename) {
		try {
			fileRepository.deleteFileInfoByName(filename);
			Files.delete(this.root.resolve(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll() {
		fileRepository.deleteAll();
		FileSystemUtils.deleteRecursively(root.toFile());
		init();
	}

}