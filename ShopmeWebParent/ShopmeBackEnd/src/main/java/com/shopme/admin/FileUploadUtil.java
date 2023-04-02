package com.shopme.admin;

import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;


public class FileUploadUtil {
	//private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
	
	//saves file/image, creates dir if does not exist
	//Saves data in multipartfile, Ovverrides a file w same name
	public static void saveFile(String uploadDir, 
			String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}
	
	//Adjusts so that only latest photo is kept if another one is uploaded
	//on same user profile
public static void cleanDir(String dir) {
	Path dirPath = Paths.get(dir);
	try {
		Files.list(dirPath).forEach(file ->{
			if(!Files.isDirectory(file)) {
				try {
				Files.delete(file);
			}catch (IOException ex){
				//LOGGER.error("Could not delete file " + file);
				System.out.println("Could not delete file " + file);
			}
			}
		});	
}catch(IOException ex) {
	//LOGGER.error("Could not delete file " + dirPath);
	System.out.println("Could not list directory " + dirPath);
}
}

//removing cat
//Remove all files from dir, delet dir itself
public static void removeDir(String dir) {
	cleanDir(dir);
	try {
		Files.delete(Paths.get(dir));
	}catch (IOException e) {
		//LOGGER.error("Could nor remove directory: " + dir);
		System.out.println("Could not remove directory " + dir);
	}
}
}
