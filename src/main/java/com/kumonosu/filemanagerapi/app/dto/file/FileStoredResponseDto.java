package com.kumonosu.filemanagerapi.app.dto.file;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FileStoredResponseDto {

	@JsonProperty(value = "itemCount")
	private Integer itemCount;

	@JsonProperty(value = "fileList")
	private List<FileStoredDto> fileList;

}
