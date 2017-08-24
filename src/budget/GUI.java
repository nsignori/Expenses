package budget;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public abstract class GUI {
	public Button enterBtn;
	public Node focus;

	public String name;
	public int width;
	public int height;

	private GridPane gpRoot = new GridPane();
	public GridPane gpMain = new GridPane();

	public Button btnBack = new MyButton("Back");
	public Label lblError = new Label("");

	public GUI(String name, int width, int height, boolean showBackButton) {
		this.name = name;
		this.width = width;
		this.height = height;

		gpRoot.setAlignment(Pos.CENTER);
		
		gpMain.setAlignment(Pos.CENTER);
		gpMain.setHgap(5);
		gpMain.setVgap(5);
		
		if(showBackButton) {
			gpRoot.add(btnBack, 0, 0);
			btnBack.setOnAction(e -> Main.setStage("Home"));
		}
		
		lblError.setTextFill(Color.RED);
		
		gpRoot.add(gpMain, 0, 1, 5, 1);
		gpRoot.add(lblError, 0, 2, 5, 1);
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Scene getScene() {
		Scene scene = new Scene(gpRoot, width, height);
		if(enterBtn != null) {
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER) {
						enterBtn.fire();
					}
				}
			});
		}
		return scene;
	}

	public int[] getDim() {
		return new int[] { width, height };
	}

	public Node getFocusElement() {
		return focus;
	}
}