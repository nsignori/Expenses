package budget;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddPurchaseGUI extends GUI {
	private DatePicker dpPurchaseDate = new DatePicker();
	private Button btnNextDay = new Button("->");
	private ComboBox<String> cbCategory = new ComboBox<String>();
	private TextField txtItems = new TextField();
	private ComboBox<String> cbPurchaseLocation = new ComboBox<String>();
	private TextField txtPrice = new TextField();
	private ComboBox<String> cbPurchaseMethods = new ComboBox<String>();

	private CheckBox cbSubscription = new CheckBox("Subscription");
	private Label lblLengthOfSubscription = new Label("Length Subscripton in Months: ");
	private Spinner<Integer> spMonth = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50));

	private File file = null;
	private FileChooser fcPic = new FileChooser();
	private Button btnChoosePic = new Button("Attach Receipt");
	private Label lblPicPath = new Label();

	private Button btnEnter = new Button("Enter");

	ObservableList<String> catOps = FXCollections.observableArrayList();
	ObservableList<String> locOps = FXCollections.observableArrayList();
	ObservableList<String> metOps = FXCollections.observableArrayList();

	public AddPurchaseGUI() {
		super("Add Purchase", 500, 800, true);

		dpPurchaseDate.setValue(LocalDate.now());
		dpPurchaseDate.setEditable(false);
		
		btnNextDay.setOnAction(e -> {
			dpPurchaseDate.setValue(dpPurchaseDate.getValue().plusDays(1));
		});

		cbCategory.setPrefWidth(150);
		cbCategory.setEditable(true);
		cbCategory.getItems().addAll(Main.getCategories());
		cbCategory.getEditor().setOnKeyReleased(e -> {
			search("Categories");
			cbCategory.show();

			if(cbCategory.getEditor().getText().length() > 20) {
				lblError.setText("Catagory is restriced to 20 characters.");
				cbCategory.getEditor().setText(cbCategory.getEditor().getText().substring(0, 20));
			} else {
				lblError.setText("");
			}
		});

		txtItems.setPrefWidth(300);
		txtItems.textProperty().addListener(e -> {
			if(txtItems.getText().length() > 100) {
				lblError.setText("Items text field restriced to 100 characters.");
				txtItems.setText(txtItems.getText().substring(0, 100));
			} else {
				lblError.setText("");
			}
		});

		cbPurchaseLocation.setPrefWidth(150);
		cbPurchaseLocation.setEditable(true);
		cbPurchaseLocation.getItems().addAll(Main.getPurchaseLocations());
		cbPurchaseLocation.getEditor().setOnKeyReleased(e -> {
			search("Locations");
			cbPurchaseLocation.show();

			if(cbPurchaseLocation.getEditor().getText().length() > 20) {
				lblError.setText("Purchase Location is restriced to 20 characters.");
				cbPurchaseLocation.getEditor().setText(cbPurchaseLocation.getEditor().getText().substring(0, 20));
			} else {
				lblError.setText("");
			}
		});

		cbPurchaseMethods.setPrefWidth(150);
		cbPurchaseMethods.setEditable(true);
		cbPurchaseMethods.getItems().addAll(Main.getPurchaseMethods());
		cbPurchaseMethods.getEditor().setOnKeyReleased(e -> {
			search("Methods");
			cbPurchaseMethods.show();

			if(cbPurchaseMethods.getEditor().getText().length() > 20) {
				lblError.setText("Purchase Method is restriced to 20 characters.");
				cbPurchaseMethods.getEditor().setText(cbPurchaseMethods.getEditor().getText().substring(0, 20));
			} else {
				lblError.setText("");
			}
		});

		txtPrice.setPrefWidth(150);
		txtPrice.textProperty().addListener(e -> {
			lblError.setText("");
			try {
				if(!txtPrice.getText().equals("")) {
					Double.parseDouble(txtPrice.getText().replaceAll("$", ""));
				}
			} catch (Exception e1) {
				lblError.setText("Price must be a number.");
			}
		});

		btnChoosePic.setOnAction(e -> {
			fcPic.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("JPG", "*.jpg"),
					new FileChooser.ExtensionFilter("PNG", "*.png")
					);
			file = fcPic.showOpenDialog(new Stage());
			if(file != null) {
				lblPicPath.setText("Piture selected: " + file.getAbsolutePath());
			}
		});

		cbSubscription.setOnMouseClicked(e -> {
			spMonth.setVisible(cbSubscription.isSelected());
			lblLengthOfSubscription.setVisible(cbSubscription.isSelected());
		});
		spMonth.setVisible(false);
		lblLengthOfSubscription.setVisible(false);

		btnEnter.setOnAction(e -> submit());

		gpMain.add(new Label("Date of Purchase: "), 0, 0);
		gpMain.add(dpPurchaseDate, 1, 0);
		gpMain.add(btnNextDay, 2, 0);
		gpMain.add(new Label("Category: "), 0, 1);
		gpMain.add(cbCategory, 1, 1);
		gpMain.add(new Label("Description of Items: "), 0, 2, 2, 1);
		gpMain.add(txtItems, 0, 3, 2, 1);
		gpMain.add(new Label("Purchase Location: "), 0, 4);
		gpMain.add(cbPurchaseLocation, 1, 4);
		gpMain.add(new Label("Price: "), 0, 5);
		gpMain.add(txtPrice, 1, 5);
		gpMain.add(new Label("Purchase Method: "), 0, 6);
		gpMain.add(cbPurchaseMethods, 1, 6);
		gpMain.add(btnChoosePic, 0, 7);
		gpMain.add(cbSubscription, 1, 7);
		gpMain.add(lblPicPath, 0, 8, 2, 1);
		gpMain.add(lblLengthOfSubscription, 0, 9);
		gpMain.add(spMonth, 1, 9);
		gpMain.add(btnEnter, 0, 10, 2, 1);

		enterBtn = btnEnter;
	}


	// Searches for information based on category and adds the matching data to search box results
	public void search(String key) {
		ArrayList<String> options = null;
		String searchVal = "";

		if(key.equals("Categories")) {
			catOps = FXCollections.observableArrayList();
			options = Main.getCategories();
			searchVal = cbCategory.getEditor().getText();
		} else if(key.equals("Locations")) {
			locOps = FXCollections.observableArrayList();
			options = Main.getPurchaseLocations();
			searchVal = cbPurchaseLocation.getEditor().getText();
		} else if(key.equals("Methods")) {
			metOps = FXCollections.observableArrayList();
			options = Main.getPurchaseMethods();
			searchVal = cbPurchaseMethods.getEditor().getText();
		}



		for(String temp : options) {
			if(temp.toLowerCase().contains(searchVal.toLowerCase())) {
				if(key.equals("Categories")) {
					catOps.add(temp);
				} else if(key.equals("Locations")) {
					locOps.add(temp);
				} else if(key.equals("Methods")) {
					metOps.add(temp);
				}
			}
		}

		if(key.equals("Categories")) {
			if (catOps.isEmpty()) {
				catOps.add("No Results Found");
			}
			cbCategory.setItems(catOps);
		} else if(key.equals("Locations")) {
			if (locOps.isEmpty()) {
				locOps.add("No Results Found");
			}
			cbPurchaseLocation.setItems(locOps);
		} else if(key.equals("Methods")) {
			if (metOps.isEmpty()) {
				metOps.add("No Results Found");
			}
			cbPurchaseMethods.setItems(metOps);
		}
	}

	public void submit() {
		boolean keepGoing = true;
		String category = cbCategory.getSelectionModel().getSelectedItem();
		String location = cbPurchaseLocation.getSelectionModel().getSelectedItem();
		String method = cbPurchaseMethods.getSelectionModel().getSelectedItem();

		if(category == null || location == null || method == null || txtItems.getText().equals("") || txtPrice.getText().equals("")) {
			lblError.setText("Fill in all options.");
		} else {
			if(!Main.getCategories().contains(category)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Add New Category");
				alert.setHeaderText("Would you like to add a new category?");
				alert.setContentText(category + " is not currently a category. Click \"Ok\" to add it to the database, or \"Cancel\" to choose an existing category.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != ButtonType.OK){
					keepGoing = false;
				} else {
					Main.addCategory(category);
				}
			}
			if(keepGoing && !Main.getPurchaseLocations().contains(location)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Add New Location");
				alert.setHeaderText("Would you like to add a new location?");
				alert.setContentText(location + " is not currently a purchase location. Click \"Ok\" to add it to the database, or \"Cancel\" to choose an existing location.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != ButtonType.OK){
					keepGoing = false;
				} else {
					Main.addLocation(location);
				}
			}
			if(keepGoing && !Main.getPurchaseMethods().contains(method)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Add New Method");
				alert.setHeaderText("Would you like to add a new purchase method?");
				alert.setContentText(method + " is not currently a purchase method. Click \"Ok\" to add it to the database, or \"Cancel\" to choose an existing purchase method.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != ButtonType.OK){
					keepGoing = false;
				} else {
					Main.addMethod(method);
				}
			}

			if(keepGoing) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				if(!cbSubscription.isSelected()) {
					alert.setHeaderText("Purchase submitted successfully.");
					alert.setContentText("Purchase of " + txtItems.getText() + " has been recorded.");
					alert.showAndWait();
					int id = Main.addPurchase(new Purchase(dpPurchaseDate.getValue(), category, txtItems.getText(), location, (int)(Double.parseDouble(txtPrice.getText()) * 100) / 100.0, method));

					if(file != null && id != -1) {
						try {
							Files.move(file.toPath(), new File("D:\\Pictures\\Expense Receipts\\" + id + ".jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
							clear();
						} catch (IOException e) {
							lblError.setText("Receipt picture could not be moved.");
						}
					} else {
						clear();
					}
				} else {
					alert.setHeaderText("Purchase submitted successfully.");
					alert.setContentText("Purchase of " + txtItems.getText() + " has been recorded.");
					alert.showAndWait();
					Main.addSubscription(new Subscription(dpPurchaseDate.getValue(), category, txtItems.getText(), location, (int)(Double.parseDouble(txtPrice.getText()) * 100) / 100.0, method, spMonth.getValue()));
					clear();
				}
			}
		}
	}

	private void clear() {
		cbCategory.getSelectionModel().clearSelection();
		cbCategory.getEditor().clear();
		txtItems.clear();
		cbPurchaseLocation.getSelectionModel().clearSelection();
		cbPurchaseLocation.getEditor().clear();
		txtPrice.clear();
		cbPurchaseMethods.getSelectionModel().clearSelection();
		cbPurchaseMethods.getEditor().clear();
		cbSubscription.setSelected(false);
		spMonth.getEditor().clear();
		file = null;
		lblPicPath.setText("");
	}
}