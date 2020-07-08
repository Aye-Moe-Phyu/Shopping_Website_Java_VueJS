package com.example.demo.Model;

public class Products {
	private String id;
    private String name;
    private String detail;
    public String brand;
    private String category;
    private int price;
    private int image_count;
    private int quantity;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getImage_count() {
		return image_count;
	}
	public void setImage_count(int image_count) {
		this.image_count = image_count;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "Products [id=" + id + ", name=" + name + ", detail=" + detail + ", brand=" + brand + ", category="
				+ category + ", price=" + price + ", image_count=" + image_count + ", quantity=" + quantity + "]";
	}
	public Products(String id, String name, String detail, String brand, String category, int price, int image_count,
			int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.brand = brand;
		this.category = category;
		this.price = price;
		this.image_count = image_count;
		this.quantity = quantity;
	}
	public Products() {
		// TODO Auto-generated constructor stub
	}
    
}
