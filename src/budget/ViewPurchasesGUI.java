package budget;

import java.io.File;
import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ViewPurchasesGUI extends GUI {
	private Stage stgReciept = new Stage();
	private TableView<Purchase> tvPurchases = new TableView<Purchase>();

	public ViewPurchasesGUI() {
		super("View Purchases", 1000, 800, true);

		TableColumn<Purchase, Integer> tcId = new TableColumn<Purchase, Integer>("Id");
		TableColumn<Purchase, LocalDate> tcDate = new TableColumn<Purchase, LocalDate>("Date");
		TableColumn<Purchase, String> tcCategory = new TableColumn<Purchase, String>("Category");
		TableColumn<Purchase, String> tcItems = new TableColumn<Purchase, String>("Items");
		TableColumn<Purchase, String> tcPurchaseLocation = new TableColumn<Purchase, String>("Location");
		TableColumn<Purchase, Double> tcPrice = new TableColumn<Purchase, Double>("Price");
		TableColumn<Purchase, String> tcPurchaseMethod = new TableColumn<Purchase, String>("Payment Method");
		TableColumn<Purchase, Boolean> tcViewReciept = new TableColumn<Purchase, Boolean>("View Reciept");

		tcId.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
		tcDate.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDate>("date"));
		tcCategory.setCellValueFactory(new PropertyValueFactory<Purchase, String>("category"));
		tcItems.setCellValueFactory(new PropertyValueFactory<Purchase, String>("items"));
		tcPurchaseLocation.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseLocation"));
		tcPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("price"));
		tcPurchaseMethod.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseMethod"));

		// Sets up the TableColumn that contains the DeleteButtonCell
		tcViewReciept.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Purchase, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Purchase, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		tcViewReciept.setCellFactory(new Callback<TableColumn<Purchase, Boolean>, TableCell<Purchase, Boolean>>() {
			@Override
			public TableCell<Purchase, Boolean> call(TableColumn<Purchase, Boolean> p) {
				return new ViewButtonCell();
			}

		});

		tvPurchases.getColumns().add(tcId);
		tvPurchases.getColumns().add(tcDate);
		tvPurchases.getColumns().add(tcCategory);
		tvPurchases.getColumns().add(tcItems);
		tvPurchases.getColumns().add(tcPurchaseLocation);
		tvPurchases.getColumns().add( tcPrice);
		tvPurchases.getColumns().add(tcPurchaseMethod);
		tvPurchases.getColumns().add(tcViewReciept);

		tvPurchases.getItems().setAll(Main.getPurchases());
		tvPurchases.setPrefWidth(900);
		tvPurchases.setPrefHeight(750);

		gpMain.add(tvPurchases, 0, 0);
	}

	private class ViewButtonCell extends TableCell<Purchase, Boolean> {
		final Button cellButton = new Button("View");

		public ViewButtonCell() {
			cellButton.setOnAction(e -> changeView(this));
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if(super.getTableRow() != null && super.getTableRow().getIndex() > 0) {
				if(!(new File("D:\\Pictures\\Expense Receipts\\" + (super.getTableRow().getIndex() + 1) + ".jpg")).exists()) {
					cellButton.setDisable(true);
				}
			}
			if (!empty) {
				setGraphic(cellButton);
			} else {
				setGraphic(null);
			}
		}
	}

	private void changeView(ViewButtonCell button) {
		int id = ((Purchase) button.getTableView().getItems().get(button.getIndex())).getId();

		stgReciept.close();

		GUI recieptGUI = new ViewRecieptGUI(id);
		stgReciept.setScene(recieptGUI.getScene());
		stgReciept.setTitle(recieptGUI.getName());

		int[] dim = recieptGUI.getDim(); // 0 = width, 1 = height
		int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
		stgReciept.setX((screenWidth / 2) - (dim[0] / 2));
		stgReciept.setY((screenHeight / 2) - (dim[1] / 2));
		stgReciept.show();
	}
}
