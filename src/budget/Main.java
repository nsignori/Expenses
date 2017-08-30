package budget;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;

public class Main {
	private static DBConnectionManager db = DBConnectionManager.getInstance();

	public static void main(String[] args) {
		Application.launch(SetUp.class);
	}

	public static void setStage(String input) {
		String key = input;
		String param = null;
		
		if(input.contains(" ")) {
			key = input.substring(0, input.indexOf(" "));
			param = input.substring(input.indexOf(" ") + 1);
		}
		
		switch(key) {
		case "Home":
			SetUp.setStage(new HomeGUI());
			break;
		case "AddPurchase":
			SetUp.setStage(new AddPurchaseGUI());
			break;
		case "ViewPurchases":
			SetUp.setStage(new ViewPurchasesGUI());
			break;
		case "ViewGraphs":
			SetUp.setStage(new GraphsGUI());
			break;
		default:
			System.out.println("Key not found: " + key + param);
			break;
		}
	}

	public static void exit() {
		db.close();
		System.exit(0);
	}
	
	public static double getAmountSpentSince(LocalDate date) {
		return db.getAmountSpentSince(date);
	}

	public static ArrayList<Purchase> getPurchasesSince(int numOfDays) {
		return db.getPurchasesSince(numOfDays);
	}

	public static ArrayList<String> getCategories() {
		return db.getCategories();
	}

	public static ArrayList<String> getPurchaseLocations() {
		return db.getPurchaseLocations();
	}

	public static ArrayList<String> getPurchaseMethods() {
		return db.getPurchaseMethods();
	}

	public static void addCategory(String category) {
		db.addCategory(category);
	}

	public static void addLocation(String location) {
		db.addLocation(location);
	}

	public static void addMethod(String method) {
		db.addMethod(method);
	}

	public static int addPurchase(Purchase purchase) {
		return db.addPurchase(purchase);
	}

	public static void addSubscription(Subscription subscription) {
		db.addSubscription(subscription);
	}

	public static ArrayList<Purchase> getPurchases() {
		return db.getPurchases();
	}
}