package com.kumonosu.filemanagerapi.app.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.kumonosu.filemanagerapi.app.dto.FileInfoResponseDto;

public interface StorageService {

	void init();

	FileInfoResponseDto store(MultipartFile file);

	Stream<Path> loadAll();

	Resource load(String filename);

	void deleteAll();

	void delete(String filename);
}