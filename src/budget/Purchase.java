package budget;

import java.time.LocalDate;

public class Purchase {
	private int id;
	private LocalDate date;
	private String category;
	private String items;
	private String purchaseLocation;
	private double price;
	private String purchaseMethod;
	
	public Purchase(LocalDate date, String category, String items, String purchaseLocation, double price, String purchaseMethod) {
		this.id = -1;
		this.date = date;
		this.category = category;
		this.items = items;
		this.purchaseLocation = purchaseLocation;
		this.price = price;
		this.purchaseMethod = purchaseMethod;
	}

	public Purchase(int id, LocalDate date, String catagory, String items, String purchaseLocation, double price, String purchaseMethod) {
		this.id = id;
		this.date = date;
		this.category = catagory;
		this.items = items;
		this.purchaseLocation = purchaseLocation;
		this.price = price;
		this.purchaseMethod = purchaseMethod;
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getCategory() {
		return category;
	}

	public String getItems() {
		return items;
	}

	public String getPurchaseLocation() {
		return purchaseLocation;
	}

	public double getPrice() {
		return price;
	}

	public String getPurchaseMethod() {
		return purchaseMethod;
	}
}