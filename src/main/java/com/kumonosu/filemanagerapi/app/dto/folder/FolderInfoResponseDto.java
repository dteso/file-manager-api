package com.kumonosu.filemanagerapi.app.dto.folder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FolderInfoResponseDto {

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "parentFolderId")
	private Long parentFolderId;

	@JsonProperty(value = "error")
	private String error;

}
