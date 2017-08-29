package budget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBConnectionManager {
	// Sets address of DB based on location
	private static final String DB_COMP_NAME = "localhost";

	private static DBConnectionManager db = null;
	private static String dbURL = "jdbc:mysql://" + DB_COMP_NAME + "/expenses";

	private static final String user = "expenseApp";
	private static final String password = "co3oNEDIROGOPogoB8Wixiz4YEvan2";
	private Connection conn;

	private PreparedStatement psGetCategories;
	private PreparedStatement psGetPurchaseLocations;
	private PreparedStatement psGetPurchaseMethods;
	private PreparedStatement psAddCategory;
	private PreparedStatement psAddLocation;
	private PreparedStatement psAddMethod;
	private PreparedStatement psAddPurchase;
	private PreparedStatement psGetPurchase;
	private PreparedStatement psAddSubscription;
	private PreparedStatement psGetPurchases;
	private PreparedStatement psDeletePurchase;
	private PreparedStatement psGetAmountSince;
	private PreparedStatement psGetPurchasesSince;

	private DBConnectionManager() throws ClassNotFoundException, SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Class.forName("com.mysql.jdbc.Driver");

		conn = DriverManager.getConnection(dbURL, user, password);

		makePS();
	}

	// Create all the PreparedStatements
	private void makePS() {
		try {
			psGetCategories = conn.prepareStatement("SELECT * FROM expenses.`categories`;");
			psGetPurchaseLocations = conn.prepareStatement("SELECT * FROM expenses.`purchase-locations`;");
			psGetPurchaseMethods = conn.prepareStatement("SELECT * FROM expenses.`purchase-methods`;");
			psAddCategory = conn.prepareStatement("INSERT INTO expenses.`categories` VALUES (?);");
			psAddLocation = conn.prepareStatement("INSERT INTO expenses.`purchase-locations` VALUES (?);");
			psAddMethod = conn.prepareStatement("INSERT INTO expenses.`purchase-methods` VALUES (?);");
			psAddPurchase = conn.prepareStatement("INSERT INTO expenses.`purchases` (Date, Category, Items, PurchaseLocation, Price, PurchaseMethod) VALUES (?, ?, ?, ?, ?, ?);");
			psGetPurchase = conn.prepareStatement("SELECT Id FROM expenses.`purchases` WHERE Date = ? AND Category = ? AND Items = ? AND PurchaseLocation = ? AND Price = ? AND PurchaseMethod = ?;");
			psAddSubscription = conn.prepareStatement("INSERT INTO expenses.`subscriptions` (StartDate, Category, Items, PurchaseLocation, Price, PurchaseMethod, RecurranceRate) VALUES (?, ?, ?, ?, ?, ?, ?);");
			psGetPurchases = conn.prepareStatement("SELECT * FROM expenses.`purchases`;");
			psDeletePurchase = conn.prepareStatement("DELETE FROM expenses.`purchases` WHERE Id=?;");
			psGetAmountSince = conn.prepareStatement("SELECT sum(Price) FROM purchases WHERE (Date > ?);");
			psGetPurchasesSince = conn.prepareStatement("SELECT Date, Price, Category FROM purchases WHERE (Date > ?);");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static DBConnectionManager getInstance() {
		if (db == null) {
			try {
				db = new DBConnectionManager();
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println("DB CONNECTION ERROR");
				e.printStackTrace();
			}
		}
		return db;
	}

	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<String>();
		try {
			ResultSet rs = psGetCategories.executeQuery();
			while (rs.next()) {
				categories.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	public ArrayList<String> getPurchaseLocations() {
		ArrayList<String> places = new ArrayList<String>();
		try {
			ResultSet rs = psGetPurchaseLocations.executeQuery();
			while (rs.next()) {
				places.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return places;
	}

	public ArrayList<String> getPurchaseMethods() {
		ArrayList<String> methods = new ArrayList<String>();
		try {
			ResultSet rs = psGetPurchaseMethods.executeQuery();
			while (rs.next()) {
				methods.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return methods;
	}

	public void addCategory(String category) {
		try {		
			psAddCategory.setString(1, category);
			psAddCategory.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addLocation(String location) {
		try {		
			psAddLocation.setString(1, location);
			psAddLocation.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addMethod(String method) {
		try {		
			psAddMethod.setString(1, method);
			psAddMethod.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int addPurchase(Purchase purchase) {
		try {
			psAddPurchase.setDate(1, java.sql.Date.valueOf(purchase.getDate()));
			psAddPurchase.setString(2, purchase.getCategory());
			psAddPurchase.setString(3, purchase.getItems());
			psAddPurchase.setString(4, purchase.getPurchaseLocation());
			psAddPurchase.setDouble(5, purchase.getPrice());
			psAddPurchase.setString(6, purchase.getPurchaseMethod());
			psAddPurchase.executeUpdate();

			psGetPurchase.setDate(1, java.sql.Date.valueOf(purchase.getDate()));
			psGetPurchase.setString(2, purchase.getCategory());
			psGetPurchase.setString(3, purchase.getItems());
			psGetPurchase.setString(4, purchase.getPurchaseLocation());
			psGetPurchase.setDouble(5, purchase.getPrice());
			psGetPurchase.setString(6, purchase.getPurchaseMethod());
			ResultSet rs = psGetPurchase.executeQuery();

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void addSubscription(Subscription subscription) {
		try {
			psAddSubscription.setDate(1, java.sql.Date.valueOf(subscription.getDate()));
			psAddSubscription.setString(2, subscription.getCategory());
			psAddSubscription.setString(3, subscription.getItems());
			psAddSubscription.setString(4, subscription.getPurchaseLocation());
			psAddSubscription.setDouble(5, subscription.getPrice());
			psAddSubscription.setString(6, subscription.getPurchaseMethod());
			psAddSubscription.setInt(7, subscription.getRecurrenceRate());
			psAddSubscription.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Purchase> getPurchases() {
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		try {
			ResultSet rs = psGetPurchases.executeQuery();
			while (rs.next()) {
				purchases.add(new Purchase(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getString(7)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return purchases;
	}

	public double getAmountSpentSince(LocalDate date) {
		double amount = -1;
		try {
			psGetAmountSince.setDate(1, java.sql.Date.valueOf(date));

			ResultSet rs = psGetAmountSince.executeQuery();
			rs.next();
			
			amount = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amount;
	}

	public ArrayList<Purchase> getPurchasesSince(int numOfDays) {
		ArrayList<Purchase> prices = new ArrayList<Purchase>();
		try {
			psGetPurchasesSince.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(numOfDays)));

			ResultSet rs = psGetPurchasesSince.executeQuery();
			
			while (rs.next()) {
				LocalDate date = rs.getDate(1).toLocalDate();
				double price = rs.getDouble(2);
				String category = rs.getString(3);
				
				prices.add(new Purchase(date, category, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return prices;
	}
}