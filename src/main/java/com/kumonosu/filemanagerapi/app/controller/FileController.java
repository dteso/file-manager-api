package com.kumonosu.filemanagerapi.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kumonosu.filemanagerapi.app.entity.FileInfo;
import com.kumonosu.filemanagerapi.app.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

	@Autowired
	private StorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<?> handleFileUpload(@RequestParam("image") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		System.out.println("UPLOAD IMAGE ---> " + file.getOriginalFilename());
		try {
			log.info("File Size: " + file.getSize());
			storageService.store(file);
		} catch (Exception e) {
			log.error("Error uploading image");
			return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.CONTENT_TYPE, "application/json")
					.body("KO");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/get/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		try {
			Resource file = storageService.load(filename);

			// TODO: Split not working???? filename.split(".") returns error
			String ext = filename.substring(filename.length() - 5, filename.length());

			// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
			// file.getFilename() + "\"") //Para generar descarga
			if (ext.equalsIgnoreCase(".json")) {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(file);
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png; image/jpeg; image/jpg")
						.body(file);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/list-all")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}
	
	@DeleteMapping("/delete/{filename}")
	public ResponseEntity<?> deleteFile(@PathVariable String filename) {
			storageService.delete(filename);
		return ResponseEntity.status(HttpStatus.OK).body(filename);
	}
	
	@DeleteMapping("/delete-all")
	public ResponseEntity<?> deleteFiles() {
			storageService.deleteAll();
		return ResponseEntity.status(HttpStatus.OK).body("ALL FILES DELETED");
	}

}