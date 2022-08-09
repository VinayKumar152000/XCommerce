package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="subcategory_info")
public class SubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "subcategory_id")
	private int id;
	private String name;
	private String description;
	
	@ManyToMany(mappedBy = "subcategories")
	private List<Category> categories;
	
	@ManyToMany(mappedBy = "subcategories")
	private List<Product> products;

}
