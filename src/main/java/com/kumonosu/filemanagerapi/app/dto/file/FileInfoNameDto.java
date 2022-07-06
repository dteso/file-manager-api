package com.kumonosu.filemanagerapi.app.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfoNameDto {

	@JsonProperty(value = "name")
	private String name;
}
