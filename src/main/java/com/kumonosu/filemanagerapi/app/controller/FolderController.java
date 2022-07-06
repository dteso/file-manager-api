package com.kumonosu.filemanagerapi.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.folder.FolderInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.folder.FolderListResponseDto;
import com.kumonosu.filemanagerapi.app.entity.FolderInfo;
import com.kumonosu.filemanagerapi.app.service.FolderInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "files-api", tags = { "folder" }, description = "Folder Operations")
@RestController
@RequestMapping("/folder")
public class FolderController {

	@Autowired
	private FolderInfoService folderService;

	@ApiOperation(value = "Creates a bew Folder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "File saved successfully", response = FileInfoResponseDto.class) })
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestParam("folder") String folder, @RequestParam("path") String path) {
		FolderInfoResponseDto responseDto = new FolderInfoResponseDto();
		FolderInfo folderDto = new FolderInfo();
		folderDto.setName(folder);
		try {
			folderService.create(path, folderDto);
		} catch (Exception e) {
			log.error(">>>>> ERROR >>>>>>>  Error creating folder");
			responseDto.setError(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.CONTENT_TYPE, "application/json")
					.body(responseDto);
		}
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json")
				.body(responseDto);
	}

	@ApiOperation(value = "Get the folder structure")
	@GetMapping("/get-structure")
	public ResponseEntity<List<FolderListResponseDto>> getListFiles() {
		List<FolderListResponseDto> folderStructure = folderService.getStructure();
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json")
				.body(folderStructure);
	}

}
