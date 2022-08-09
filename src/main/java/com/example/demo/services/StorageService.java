package com.example.demo.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

	public boolean imageUpload(MultipartFile image, String uploadDir) throws Exception {

		boolean flag = false;

		File f = new File(uploadDir);
		if (!f.exists()) {
			f.mkdir();
		}

		String name = image.getOriginalFilename();
//		String randomId = UUID.randomUUID().toString();
//		String filename = randomId.concat(name.substring(name.lastIndexOf(".")));
		try {
			Files.copy(image.getInputStream(), Paths.get(uploadDir + File.separator + name),
					StandardCopyOption.REPLACE_EXISTING);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public boolean imageDelete(String image, String dir) throws Exception {

		Path path = Paths.get(dir + File.separator + image);
		boolean fileDeleted = Files.deleteIfExists(path);
		if (fileDeleted) {
			return true;
		} else {
			return false;
		}
	}
}
