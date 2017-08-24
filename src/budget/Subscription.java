package budget;

import java.time.LocalDate;

public class Subscription extends Purchase {
	private LocalDate endDate;
	private int recurrenceRate;
	
	public Subscription(LocalDate date, String catagory, String items, String placeBought, double price, String purchaseLocation, int recurrenceRate) {
		super(date, catagory, items, placeBought, price, purchaseLocation);
		this.endDate = null;
		this.recurrenceRate = recurrenceRate;
	}
	
	public Subscription(int id, LocalDate date, String catagory, String items, String placeBought, double price, String purchaseLocation, LocalDate endDate, int recurrenceRate) {
		super(id, date, catagory, items, placeBought, price, purchaseLocation);
		this.endDate = endDate;
		this.recurrenceRate = recurrenceRate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	public int getRecurrenceRate() {
		return recurrenceRate;
	}
}