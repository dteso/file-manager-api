package com.kumonosu.filemanagerapi.app.service;

import java.util.List;

import com.kumonosu.filemanagerapi.app.dto.folder.FolderInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.folder.FolderListResponseDto;
import com.kumonosu.filemanagerapi.app.entity.FolderInfo;

public interface FolderInfoService {

	public FolderInfo create(String path, FolderInfo dto);

	public FolderInfo getFolderByName(String folderName);

	public List<FolderInfoResponseDto> getFolderByParentFolderId(Long parentFolderId);

	public List<FolderListResponseDto> getStructureByFolderPath(String path);

	public FolderInfo getFolderById(Long id);

	boolean processPath(String path, FolderInfo folder);

	public List<FolderListResponseDto> getStructure();

}
