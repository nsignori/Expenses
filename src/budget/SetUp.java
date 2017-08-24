package budget;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SetUp extends Application {
	public static Stage stage = new Stage();
	private static int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	private static int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();

	@Override
	public void start(Stage arg0) throws Exception {
		stage.getIcons().add(new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("money-bag.png")));

		// Sets close action
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent t) {
				Main.exit();
			}
		});

		setStage(new HomeGUI());
	}

	public static void setStage(GUI scene) {
		stage.setScene(scene.getScene());
		stage.setTitle(scene.getName());
		int[] dim = scene.getDim(); // 0 = width, 1 = height
		stage.setX((screenWidth / 2) - (dim[0] / 2));
		stage.setY((screenHeight / 2) - (dim[1] / 2));
		stage.show();
		if(scene.getFocusElement() != null) {
			scene.getFocusElement().requestFocus();
		}
	}
}