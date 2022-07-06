package com.kumonosu.filemanagerapi.app.dto.file;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@NoArgsConstructor
@Data
public class FileInfoRequestDto {

	private String folder;

	private MultipartFile file;

}
