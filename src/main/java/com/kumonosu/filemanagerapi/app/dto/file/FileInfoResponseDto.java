package com.kumonosu.filemanagerapi.app.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FileInfoResponseDto {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "originalFilename")
	private String originalFilename;

	@JsonProperty(value = "url")
	private String url;

	@JsonProperty(value = "createdTime")
	private String createdTime;

}
