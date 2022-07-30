package com.kumonosu.filemanagerapi.app.controller;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.file.FileStoredDto;
import com.kumonosu.filemanagerapi.app.dto.file.FileStoredResponseDto;
import com.kumonosu.filemanagerapi.app.dto.file.mapper.FileInfoMapper;
import com.kumonosu.filemanagerapi.app.service.StorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "files-api", tags = { "files" }, description = "Files Operations")
@RestController
@RequestMapping("/files")
public class FileController {

	private final static String JSON_EXT = ".json";

	@Autowired
	StorageService storageService;

	@ApiOperation(value = "Uploads attached file on request")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "File saved successfully", response = FileInfoResponseDto.class) })
	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file, @RequestParam("path") String path) {
		FileInfoResponseDto responseDto = new FileInfoResponseDto();
		log.info("Path: " + path);
		try {
			responseDto = FileInfoMapper.fromEntityToResponse(storageService.store(path, file));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.CONTENT_TYPE, "application/json")
					.body(responseDto);
		}
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json")
				.body(responseDto);
	}

	@ApiOperation(value = "Gets a file as a resource directly on the response")
	@GetMapping("/show/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		try {
			Resource file = storageService.load(filename);

			String ext = filename.split("\\.")[1];
			if (ext.equalsIgnoreCase(JSON_EXT)) {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(file);
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png; image/jpeg; image/jpg")
						.body(file);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@ApiOperation(value = "Gets file info from db using filename")
	@GetMapping("/get/{filename}")
	@ResponseBody
	public ResponseEntity<FileInfoResponseDto> getFileFromDb(@PathVariable String filename) {
		FileInfoResponseDto responseDto = new FileInfoResponseDto();
		try {
			responseDto = FileInfoMapper.fromEntityToResponse(storageService.getFileByName(filename));
			if (responseDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@ApiOperation(value = "Download the the file with filename established as parameter")
	@GetMapping("/download/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> download(@PathVariable String filename) {
		try {
			Resource file = storageService.load(filename);

			String ext = filename.split("\\.")[1];

			if (file == null) {
				return ResponseEntity.noContent().build();
			}

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file.getFile()));

			if (ext.equalsIgnoreCase(".json")) {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(file);
			} else {
				HttpHeaders headers = getHeadersForDownload(filename, file, ext);
				return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	private HttpHeaders getHeadersForDownload(String filename, Resource file, String ext) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename()
				+ "\"; filename*=utf-8 '' \"" + file.getFilename() + "\"");
		headers.add(filename, ext);
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return headers;
	}

	@ApiOperation(value = "Gets a list of all stored files")
	@GetMapping("/list-all")
	public ResponseEntity<FileStoredResponseDto> getListFiles() {
		FileStoredResponseDto fileStoredResponse = new FileStoredResponseDto();
		FileStoredDto responseDto = new FileStoredDto();
		List<FileStoredDto> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

			responseDto.setFilaname(filename);
			responseDto.setPath(url);

			return responseDto;
		}).collect(Collectors.toList());

		fileStoredResponse.setItemCount(fileInfos.size());
		fileStoredResponse.setFileList(fileInfos);

		return ResponseEntity.status(HttpStatus.OK).body(fileStoredResponse);
	}

	@ApiOperation(value = "Deletes a file by his name")
	@DeleteMapping("/delete/{filename}")
	public ResponseEntity<?> deleteFileByName(@PathVariable String filename) {
		storageService.delete(filename);
		return ResponseEntity.status(HttpStatus.OK).body(filename);
	}

	@ApiOperation(value = "Remove all files")
	@DeleteMapping("/delete-all")
	public ResponseEntity<?> deleteAllFiles() {
		storageService.deleteAll();
		return ResponseEntity.status(HttpStatus.OK).body("ALL FILES DELETED");
	}

}