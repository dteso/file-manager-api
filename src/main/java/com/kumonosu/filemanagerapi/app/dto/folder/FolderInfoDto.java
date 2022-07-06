package com.kumonosu.filemanagerapi.app.dto.folder;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumonosu.filemanagerapi.app.entity.FileInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FolderInfoDto {

	@NotNull
	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "parentFolderId")
	private Long parentFolderId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "folder")
	private List<FileInfo> fileList;

}
