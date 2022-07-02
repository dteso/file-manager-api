package com.kumonosu.filemanagerapi;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.kumonosu.filemanagerapi.app.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class FilemanagerapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilemanagerapiApplication.class, args);
	}

	class StorageStarter {
		@Resource
		StorageService storageService;

		@Bean
		void initFolder() {
			log.info("------------------- Inicializando storage --------------");
			storageService.init();
		}
	}
}
