package com.kumonosu.filemanagerapi;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.kumonosu.filemanagerapi.app.service.StorageService;

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
			System.out.println("Inicializando storage");
		    storageService.init();
		}
	}
}
