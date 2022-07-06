package com.kumonosu.filemanagerapi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kumonosu.filemanagerapi.app.entity.FolderInfo;

@Repository
public interface FolderRepository extends JpaRepository<FolderInfo, Long> {

	public FolderInfo findFolderInfoByName(String name);

	public FolderInfo findFolderInfoById(Long id);

}