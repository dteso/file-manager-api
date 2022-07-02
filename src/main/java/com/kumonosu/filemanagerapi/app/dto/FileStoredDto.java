package com.kumonosu.filemanagerapi.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FileStoredDto {

	@JsonProperty(value = "filename")
	private String filaname;

	@JsonProperty(value = "path")
	private String path;

}
