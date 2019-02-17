package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import  model.ItemDAO;
import  model.Item;
import util.Exp;

public class EditItemController {

	private String itemId;

	public TextField txtID;
	public TextField txtName;
	public TextField txtLocation;
	public TextArea txtDiscription;
	public TextField txtQuantity;

	@FXML
	public void initialize () {
		System.out.println(itemId);
	}

	public void handleSave(ActionEvent actionEvent) {
		Node source = (Node) actionEvent.getSource();
		//get the stage of the source
		Stage stage = (Stage) source.getScene().getWindow();
		try {
			ItemDAO.updateItem(txtID.getText(),txtName.getText(), txtLocation.getText(), txtDiscription.getText(), Integer.parseInt(txtQuantity.getText()));
		} catch (SQLException e) {
			Exp.displayException(e);
		} catch (ClassNotFoundException e) {
			Exp.displayException(e);
		} finally {
			stage.close();
		}


	}

	public void handleClose(ActionEvent actionEvent) {
		//get the source
		Node source = (Node) actionEvent.getSource();
		//get the stage of the source
		Stage stage = (Stage) source.getScene().getWindow();
		//Close the window
		stage.close();
	}

	public void setItemId(String itemId) {
		System.out.println(itemId);
		Item item;
		try {
			item = ItemDAO.getItemById(itemId);
			txtID.setText(item.getItemId());
			txtName.setText(item.getName());
			txtLocation.setText(item.getLocation());
			txtDiscription.setText(item.getDescription());
			txtQuantity.setText(Integer.toString(item.getQuantity()));
		} catch (SQLException e) {
			Exp.displayException(e);
		} catch (ClassNotFoundException e) {
			Exp.displayException(e);

		}
	}
}
