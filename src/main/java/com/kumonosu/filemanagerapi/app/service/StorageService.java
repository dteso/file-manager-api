package com.kumonosu.filemanagerapi.app.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.kumonosu.filemanagerapi.app.entity.FileInfo;

public interface StorageService {

	void init();

	FileInfo store(String folder, MultipartFile file);

	Stream<Path> loadAll();

	Resource load(String filename);

	void deleteAll();

	void delete(String filename);

	FileInfo getFileByName(String name);
}