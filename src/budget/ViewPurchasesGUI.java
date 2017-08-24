package budget;

import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ViewPurchasesGUI extends GUI {
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
		TableColumn<Purchase, TableColumn<Purchase, Boolean>> tcManagePurchase = new TableColumn<Purchase, TableColumn<Purchase, Boolean>>("Manage Purchase");

		TableColumn<Purchase, Boolean> tcEditPurchase = new TableColumn<Purchase, Boolean>("Edit Purchase");
		TableColumn<Purchase, Boolean> tcDeletePurchase = new TableColumn<Purchase, Boolean>("Delete Purchase");

		tcId.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
		tcDate.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDate>("date"));
		tcCategory.setCellValueFactory(new PropertyValueFactory<Purchase, String>("category"));
		tcItems.setCellValueFactory(new PropertyValueFactory<Purchase, String>("items"));
		tcPurchaseLocation.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseLocation"));
		tcPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("price"));
		tcPurchaseMethod.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseMethod"));

		// Sets up the TableColumn that contains the DeleteButtonCell
		tcEditPurchase.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Purchase, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Purchase, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		tcEditPurchase.setCellFactory(new Callback<TableColumn<Purchase, Boolean>, TableCell<Purchase, Boolean>>() {

			@Override
			public TableCell<Purchase, Boolean> call(TableColumn<Purchase, Boolean> p) {
				return new EditButtonCell();
			}

		});

		// Sets up the TableColumn that contains the DeleteButtonCell
		tcDeletePurchase.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Purchase, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Purchase, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		tcDeletePurchase.setCellFactory(new Callback<TableColumn<Purchase, Boolean>, TableCell<Purchase, Boolean>>() {

			@Override
			public TableCell<Purchase, Boolean> call(TableColumn<Purchase, Boolean> p) {
				return new DeleteButtonCell();
			}

		});

		tcManagePurchase.getColumns().add(tcEditPurchase);
		tcManagePurchase.getColumns().add(tcDeletePurchase);

		tvPurchases.getColumns().add(tcId);
		tvPurchases.getColumns().add(tcDate);
		tvPurchases.getColumns().add(tcCategory);
		tvPurchases.getColumns().add(tcItems);
		tvPurchases.getColumns().add(tcPurchaseLocation);
		tvPurchases.getColumns().add( tcPrice);
		tvPurchases.getColumns().add(tcPurchaseMethod);
		tvPurchases.getColumns().add(tcManagePurchase);

		tvPurchases.getItems().setAll(Main.getPurchases());

		gpMain.add(tvPurchases, 0, 0);
	}

	// Creates a DeleteButtonCell that deletes the selected student
	private class DeleteButtonCell extends TableCell<Purchase, Boolean> {
		final Button cellButton = new Button("Delete");

		public DeleteButtonCell() {

			cellButton.setOnAction(e -> {
				Main.deletePurchase(((Purchase) DeleteButtonCell.this.getTableView().getItems().get(DeleteButtonCell.this.getIndex())).getId());

				tvPurchases.getItems().setAll(Main.getPurchases());
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			} else {
				setGraphic(null);
			}
		}
	}

	// Creates a DeleteButtonCell that deletes the selected student
	private class EditButtonCell extends TableCell<Purchase, Boolean> {
		final Button cellButton = new Button("Edit");

		public EditButtonCell() {

			cellButton.setOnAction(e -> {
				Main.setStage("EditPurchase " + ((Purchase) EditButtonCell.this.getTableView().getItems().get(EditButtonCell.this.getIndex())).getId());

				tvPurchases.getItems().setAll(Main.getPurchases());
			});
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			} else {
				setGraphic(null);
			}
		}
	}
}
