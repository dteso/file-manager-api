package com.kumonosu.filemanagerapi.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;
import com.kumonosu.filemanagerapi.app.dto.file.mapper.FileInfoMapper;
import com.kumonosu.filemanagerapi.app.dto.folder.FolderListResponseDto;
import com.kumonosu.filemanagerapi.app.entity.FolderInfo;
import com.kumonosu.filemanagerapi.app.repository.FolderRepository;
import com.kumonosu.filemanagerapi.app.service.FolderInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class FolderInfoServiceImpl implements FolderInfoService {

	@Autowired
	FolderRepository folderRepository;

	@Override
	public FolderInfo create(String path, FolderInfo folder) {
		if (processPath(path, folder)) {
			return folderRepository.save(folder);
		}
		log.error(">>>>>> ERROR. Invalid directory...");
		throw new RuntimeException("Not a valid directory selected");
	}

	@Override
	public FolderInfo getFolderByName(String folderName) {
		return folderRepository.findFolderInfoByName(folderName);
	}

	@Override
	public FolderInfo getFolderById(Long id) {
		return folderRepository.findFolderInfoById(id);
	}

	@Override
	public boolean processPath(String path, FolderInfo folder) {
		if (path == null) {
			return true;
		}
		String firstFolderName = "";
		String[] pathFolders = null;
		FolderInfo currentFolder = new FolderInfo();

		if (!path.contains("/")) {
			firstFolderName = path;
		} else {
			pathFolders = path.split("/");
			firstFolderName = pathFolders[0];
		}

		currentFolder = getFolderByName(firstFolderName);

		if (currentFolder == null || (currentFolder != null && !currentFolder.getParentFolderId().equals(0L))) {
			return false;
		}

		if (pathFolders != null) {
			for (int index = 0; index < pathFolders.length; index++) {
				FolderInfo childFolder = getFolderByName(pathFolders[index]);
				if (childFolder == null) {
					return false;
				} else if (index > 0 && pathFolders[index] != null) {
					if (!currentFolder.getId().equals((childFolder).getParentFolderId())) {
						return false;
					}
					currentFolder = childFolder;
				}
			}
		}
		folder.setParentFolderId(currentFolder.getId());
		folder.setPath(path + "/" + folder.getName());
		return true;
	}

	@Override
	public List<FolderListResponseDto> getStructure() {

		List<FolderListResponseDto> responseList = new ArrayList<FolderListResponseDto>();
		List<FolderInfo> folderInfoList = folderRepository.findAll();

		folderInfoList.stream().forEach(folder -> {
			List<FileInfoResponseDto> itemList = new ArrayList<>();
			FolderListResponseDto response = new FolderListResponseDto();
			folder.getFileList().stream()
					.forEach(fileInfo -> itemList.add(FileInfoMapper.fromEntityToResponse(fileInfo)));
			response.setId(folder.getId());
			response.setName(folder.getName());
			response.setParentFolder(folder.getParentFolderId());
			response.setFiles(itemList);
			response.setPath(folder.getPath());
			responseList.add(response);
		});

		return responseList;
	}
}
