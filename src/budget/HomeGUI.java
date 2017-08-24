package budget;

import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeGUI extends GUI {
	private Button btnAddPurchase = new MyButton("Add Purchase");
	private Button btnManageSubscriptions = new MyButton("Manage Subscriptions");
	private Button btnViewPurchases = new MyButton("View Purchases");
	private Button btnViewGraphs = new MyButton("View Graphs");
			
	public HomeGUI() {
		super("Home", 300, 500, false);
		
		btnAddPurchase.setOnAction(e -> Main.setStage("AddPurchase"));		
		btnManageSubscriptions.setOnAction(e -> Main.setStage("ManageSubscriptions"));
		btnViewPurchases.setOnAction(e -> Main.setStage("ViewPurchases"));
		btnViewGraphs.setOnAction(e -> Main.setStage("ViewGraphs"));
		
		btnAddPurchase.setAlignment(Pos.CENTER);
		
		gpMain.add(btnAddPurchase, 0, 0);
		gpMain.add(btnManageSubscriptions, 0, 1);
		gpMain.add(btnViewPurchases, 0, 2);
		gpMain.add(btnViewGraphs, 0, 3);

		gpMain.add(new Label("This week: $" + Main.getAmountSpentSince(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() + 1))), 0, 4);
		gpMain.add(new Label("In the last week: $" + Main.getAmountSpentSince(LocalDate.now().minusWeeks(1))), 0, 5);
		gpMain.add(new Label("This month: $" + Main.getAmountSpentSince(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()))), 0, 6);
		gpMain.add(new Label("In the last month: $" + Main.getAmountSpentSince(LocalDate.now().minusMonths(1))), 0, 7);
	}
}