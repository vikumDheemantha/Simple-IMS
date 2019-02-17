package controllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;
import model.*;
import util.Exp;

import javax.xml.validation.Validator;
import java.sql.*;

/**
 * <h1>Main window Controller</h1>
 *
 * This is the main controller for the mainWindow.fxml file. This will handle all the main
 * window's functions
 *
 * @author Vikum Dheeemantha
 * @version 0.1
 * @see views/mainWindow.fxml
 */
public class MainWindowController {
	public TextField txtSearch;
	public TextField txtDisID;
	public TableView tblResult;
	public TableColumn<Item, String> colId;
	public TableColumn<Item, String> colName;
	public TableColumn<Item, String> colLocation;
	public TableColumn<Item, Integer> colQuantity;
	public ToggleGroup toogleSearch;
	public RadioButton radioName;
	public RadioButton radioLocation;
	public RadioButton radioID;
	public TextField txtDisName;
	public TextField txtDisLocation;
	public TextArea txtDisDiscription;
	public Button btnEditQnt;
	public TextField txtDisQuantity;
	public Button btnEditAll;
	public Button btnRemoveItem;

	public ObservableList<Item> filteredItems = null;
	Item selectedItem = null;

	@FXML
	private void initialize () {
		System.out.println("initialized");
		colId.setCellValueFactory(cellData-> cellData.getValue().itemIdProperty());
		colName.setCellValueFactory(cellData-> cellData.getValue().nameProperty());
		colLocation.setCellValueFactory(cellData-> cellData.getValue().locationProperty());
		colQuantity.setCellValueFactory(cellData-> cellData.getValue().quantityProperty().asObject());
		tblResult.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change c) {
				selectedItem = (Item) tblResult.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					txtDisID.setText(selectedItem.getItemId());
					txtDisName.setText(selectedItem.getName());
					txtDisLocation.setText(selectedItem.getLocation());
					txtDisQuantity.setText(Integer.toString(selectedItem.getQuantity()));
					txtDisDiscription.setText(selectedItem.getDescription());
				} else {
					clearRightPane();
				}
			}
		});

		try {
			//Get all item information
			ObservableList<Item> itemData = ItemDAO.getAllItems();
			//Populate Employees on TableView
			populateItems(itemData);
			txtSearch.clear();
		} catch (SQLException e){
//			System.out.println("Error occurred while getting employees information from DB.\n" + e);
			Exp.displayException(e);
		} catch (ClassNotFoundException e) {
			Exp.displayException(e);
		}
	}

	/**
	 * this is a method for handling the actions of the search button and the
	 * key press enter of the txtSearch text field.
	 *
	 *
	 * @param actionEvent
	 */
	public void handleSearch(ActionEvent actionEvent) {
		RadioButton radio = (RadioButton) toogleSearch.getSelectedToggle();
		String searchString = txtSearch.getText();
		ObservableList<Item> itemList;
		if(radio == radioID){
			try {
				itemList = ItemDAO.searchItemById(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		} else if(radio == radioName){
			try {
				itemList = ItemDAO.searchItemByName(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		} else {
			try {
				itemList = ItemDAO.searchItemByLocation(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		}



	}

	/**
	 * This method will display another form window to add new item to the database
	 * form will handle separately from the form's controller
	 * @see AddNewItemController
	 * @param actionEvent
	 * @throws Exception
	 */
	public void handleAddNew(ActionEvent actionEvent) throws Exception {
		Stage newItemWindow = new Stage();
		newItemWindow.setTitle("Add new Item");
		Parent root = FXMLLoader.load(getClass().getResource("/views/addNewItem.fxml"));
		newItemWindow.setScene(new Scene(root));
		newItemWindow.initModality(Modality.APPLICATION_MODAL);
		newItemWindow.setResizable(false);
//		newItemWindow.getIcons().add(new Image(MainWindowController.class.getResourceAsStream("../IMS icon.png")));
		newItemWindow.showAndWait();

		try {
			refreshTable();
		} catch (SQLException e){
//			System.out.println("Error occurred while getting employees information from DB.\n" + e);
			Exp.displayException(e);
		} catch (ClassNotFoundException e) {
			Exp.displayException(e);
		}


	}

	/**
	 * This method is for display edit window when user click edit button in the
	 * right side to edit an item's information. Editing the database will be handled
	 * by the separate controller for edit item
	 * @see EditItemController
	 * @param actionEvent
	 * @throws Exception
	 */
	public void handleEditAll(ActionEvent actionEvent) throws Exception {
		String id = txtDisID.getText();
		Item preSelected = selectedItem;

		if(!id.isEmpty() && id != null){
			Stage newItemWindow = new Stage();
			newItemWindow.setTitle("Edit Item");
			FXMLLoader loader = new  FXMLLoader(getClass().getResource("/views/editItem.fxml"));
			newItemWindow.setScene(new Scene(loader.load()));
			newItemWindow.initModality(Modality.APPLICATION_MODAL);
//			newItemWindow.getIcons().add(new Image(MainWindowController.class.getResourceAsStream("../IMS icon.png")));
			newItemWindow.setResizable(false);

			EditItemController controller = loader.<EditItemController>getController();
			controller.setItemId(id);

			newItemWindow.showAndWait();
			refreshTable();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("There is no item to Edit");
			alert.setContentText("Please select the item before edit !");

			alert.showAndWait();
		}
	}

	/**
	 * This method for remove an item for users wish. item that want to remove
	 * will be permanently removed from the database
	 * @param actionEvent
	 */
	public void handleRemoveItem(ActionEvent actionEvent) {
		String id = txtDisID.getText();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Are you sure, you want to remove this item?");
		alert.setContentText("this item will remove permanently from the database.");

		if(!id.isEmpty() && id != null){
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					ItemDAO.deleteItemById(id);
					refreshTable();
				} catch (SQLException e) {
					Exp.displayException(e);
				} catch (ClassNotFoundException e) {
					Exp.displayException(e);
				}
			}
		} else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("There is no selected item to Remove");
			alert.setContentText("Please select the item before remove !");

			alert.showAndWait();
		}
	}

	public void handleImport(ActionEvent actionEvent) {
		/**
		 * TODO: write a code for allowing user to import set of data from SQL or csv file
		 */
	}

	public void handleExport(ActionEvent actionEvent) {
		//TODO: make a method that allowing user to export data in the database to sql or csv
	}

	public void handleClose(ActionEvent actionEvent) {
		System.exit(0);
	}

	private void populateItems (ObservableList<Item> itemList) throws ClassNotFoundException {
		//Set items to the employeeTable
		tblResult.setItems(itemList);
	}


	public void clearTable(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Are you sure, you want to clean the Database?");
		alert.setContentText("Data will be removed permanently.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				ItemDAO.clearTable();
				refreshTable();

			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			} catch (SQLException e) {
				Exp.displayException(e);
			}
		} else {
			//just do nothing
		}

	}
	
	private void clearRightPane(){
		txtDisID.clear();
		txtDisDiscription.clear();
		txtDisLocation.clear();
		txtDisQuantity.clear();
		txtDisName.clear();
	}

	public void handleEditQt(ActionEvent actionEvent) {
		String currentQuentity = txtDisQuantity.getText();
		String id = txtDisID.getText();
		Item preSelected = selectedItem;

		TextInputDialog dialog = new TextInputDialog(currentQuentity);
		dialog.setTitle("Change Quantity");
		dialog.setHeaderText("Change quantity of the item "+id+".");
		dialog.setContentText("Please enter new quantity:");

		if(!id.isEmpty() && id != null){
			System.out.println(id);
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				try {
					ItemDAO.updateQuantity(txtDisID.getText(), Integer.parseInt(result.get()));
					refreshTable();
				} catch (SQLException e) {
					Exp.displayException(e);
				} catch (ClassNotFoundException e) {
					Exp.displayException(e);
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("There is no item to Edit");
			alert.setContentText("Please select the item before edit !");

			alert.showAndWait();
		}
	}

	private void refreshTable() throws ClassNotFoundException, SQLException {
		RadioButton radio = (RadioButton) toogleSearch.getSelectedToggle();
		String searchString = txtSearch.getText();
		ObservableList<Item> itemList;
		if(radio == radioID){
			try {
				itemList = ItemDAO.searchItemById(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		} else if(radio == radioName){
			try {
				itemList = ItemDAO.searchItemByName(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		} else {
			try {
				itemList = ItemDAO.searchItemByLocation(searchString);
				populateItems(itemList);
			} catch (SQLException e) {
				Exp.displayException(e);
			} catch (ClassNotFoundException e) {
				Exp.displayException(e);
			}
		}

	}

	public void handleAbout(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Simple Inventory Management System \n V0.1 ALPHA");
		Label label = new Label("Developed by: Vikum Dheemantha Gunasekara");
		TextArea textArea = new TextArea("Product Version:\n" +
				"Simple Inventory Management System 0.1.0 ALPHA\n" +
				"\n" +
				"Build Information\n" +
				"Version 0.1.0 ALPHA\n" +
				"Date: 2019-02-17\n" +
				"Java Version: 10.0.1+10, Oracle Corporation\n" +
				"\n" +
				"Copyright (c) 2019, Vikum Dheemantha\n" +
				"vikum.dheemantha@gmail.com\n" +
				"\n" +
				"=============================================\n" +
				"This product includes the following software:\n" +
				"\n" +
				"HyperSQL Database\n" +
				"Copyright (c) 2001-2018, The HSQL Development Group\n");
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
//		expContent.setHgap(10);

		// Set expandable Exception into the dialog pane.
//		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setContent(expContent);

		alert.showAndWait();
	}
}
