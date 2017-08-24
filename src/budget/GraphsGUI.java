package budget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GraphsGUI extends GUI {
	private Button btnViewWeek = new Button("Week");
	private Button btnViewMonth = new Button("Month");
	private Button btnViewYear = new Button("Year");
	private Button btnViewAllTime = new Button("All Time");
	
	private Stage stgGraph = new Stage();

	private CheckBox cbByCategory = new CheckBox("Break Down by Categories");

	public GraphsGUI() {
		super("Graphs", 700, 600, true);

		gpMain.add(btnViewWeek, 0, 0);
		gpMain.add(btnViewMonth, 0, 1);
		gpMain.add(btnViewYear, 0, 2);
		gpMain.add(btnViewAllTime, 0, 3);
		btnViewWeek.setOnAction(e -> changeView(e));
		btnViewMonth.setOnAction(e -> changeView(e));
		btnViewYear.setOnAction(e -> changeView(e));
		btnViewAllTime.setOnAction(e -> changeView(e));
		
		gpMain.add(cbByCategory, 0, 4);
		
		stgGraph.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent t) {
				btnViewWeek.setDisable(false);
				btnViewMonth.setDisable(false);
				btnViewYear.setDisable(false);
				btnViewAllTime.setDisable(false);
			}
		});
	}
	
	private void changeView(ActionEvent e) {
		stgGraph.close();
		
		btnViewWeek.setDisable(false);
		btnViewMonth.setDisable(false);
		btnViewYear.setDisable(false);
		btnViewAllTime.setDisable(false);
		
		((Button)(e.getSource())).setDisable(true);
		
		GUI graphGUI = new ActualGraphGUI(((Button)(e.getSource())).getText(), cbByCategory.isSelected());
		stgGraph.setScene(graphGUI.getScene());
		stgGraph.setTitle(graphGUI.getName());
		
		int[] dim = graphGUI.getDim(); // 0 = width, 1 = height
		int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
		stgGraph.setX((screenWidth / 2) - (dim[0] / 2));
		stgGraph.setY((screenHeight / 2) - (dim[1] / 2));
		stgGraph.show();
	}
}
