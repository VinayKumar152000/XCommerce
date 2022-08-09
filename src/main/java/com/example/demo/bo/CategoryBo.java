package com.example.demo.bo;

import lombok.AllArgsConstructor;
import java.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBo {
	private String id;
	private String name;
	private String description;
	private List<SubCategoryBo> subcategories;
}
