package budget;

public class EditPurchasesGUI extends GUI {

	public EditPurchasesGUI(int id) {
		super("Edit Purchase", 500, 800, true);

		btnBack.setOnAction(e -> Main.setStage("ViewPurchases"));
	}

}
