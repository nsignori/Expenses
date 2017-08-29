package budget;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewRecieptGUI extends GUI {
	public ViewRecieptGUI(int id) {
		super("Reciept", 600, 900, false);

		ImageView ivReciept = new ImageView(new Image("file:\\D:\\Pictures\\Expense Receipts\\" + id + ".jpg"));
		ivReciept.setFitWidth(550);
		ivReciept.setFitHeight(850);
		
		ivReciept.setPreserveRatio(true);
		
		gpMain.add(ivReciept, 0, 0);
	}
}
