package com.example.demo.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPayload {

	private String name;
	private MultipartFile image;
	//if we want to take nested values as input
//	private Map<String,List<String>> categories;
	private List<String> categories;
	private List<String> subcategories;
}
