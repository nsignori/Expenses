package budget;

import java.util.HashMap;

public class CatPurchase {
	private String category;
	private HashMap<String, Double> prices = new HashMap<String, Double>();
	
	public CatPurchase(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public HashMap<String, Double> getPrices() {
		return prices;
	}
}
