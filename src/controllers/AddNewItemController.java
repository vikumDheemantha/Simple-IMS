package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import model.ItemDAO;
import util.Exp;

public class AddNewItemController {
	public TextField txtID;
	public TextField txtName;
	public TextField txtLocation;
	public TextArea txtDiscription;
	public TextField txtQuantity;
	public Label errorLbl;

	/**
	 * This method will close the window that controlled by this controller
	 * @param actionEvent
	 */
	public void handleClose(ActionEvent actionEvent) {
		//get the source
		Node source = (Node) actionEvent.getSource();
		//get the stage of the source
		Stage stage = (Stage) source.getScene().getWindow();
		//Close the window
		stage.close();
	}

	public void handleAdd(ActionEvent actionEvent) {
		Node source = (Node) actionEvent.getSource();
		//get the stage of the source
		Stage stage = (Stage) source.getScene().getWindow();
		String id, name, location, discription;
		int qnt;
		id = txtID.getText();
		name = txtName.getText();
		location = txtLocation.getText();
		discription = txtDiscription.getText();
		if(id == null || id.trim().isEmpty() ||
				name == null || name.trim().isEmpty() ||
				location == null || location.trim().isEmpty() ||
				txtQuantity.getText() == null || txtQuantity.getText().trim().isEmpty()
		){
			errorLbl.setVisible(true);
			return;
		}
		qnt = Integer.parseInt(txtQuantity.getText());

		try {
			ItemDAO.insertItem(id,name, location, discription, qnt);
		} catch (SQLException e) {
			Exp.displayException(e);
		} catch (ClassNotFoundException e) {
			Exp.displayException(e);
		} finally {
			stage.close();
		}
	}
}
