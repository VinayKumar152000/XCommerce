package com.example.demo.bo;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBo {

	private int id;
	private String name;
	private List<CategoryBo> categories;
	private List<SubCategoryBo> subcategories;
}
