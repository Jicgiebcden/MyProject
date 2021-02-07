package com.huangym.springredis.controller;

public class Test {

	private Long id;
	
	private String name;
	
	private Integer age;
	
	private Double price;
	
	private Boolean isTrue;
	
	public Test() {
		
	}
	
	public Test(Long id, String name, Integer age, Double price, Boolean isTrue) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.price = price;
		this.isTrue = isTrue;
	}
	
	private Long get() {
		return this.id;
	}

	public boolean compare(Test t) {
		// 可以访问本类的私有方法
		Long other = t.get();
		System.out.println(other);
		// 可以直接访问本类的私有变量
		if (this.id != t.id) {
			return false;
		}
		if (this.age != t.age) {
			return false;
		}
		if (this.isTrue != t.isTrue) {
			return false;
		}
		return true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}
	
}
