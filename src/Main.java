import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.*;
import util.DBUtil;

/**
 * <h1>Inventory Management System</h1>
 *
 * This is a very simple inventory management system created for managing inventories
 * (Actually Storage management System).
 *
 * This is specially designed for single, small warehouses. using this program you can
 * add new items, edit the details of the items, change the quantity of the items and also
 * remove the item from the database if it is not necessary.
 *
 * @author Vikum Dheemantha
 * @version 0.1
 * @since 2019-01-30
 */
public class Main extends Application {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String initSQL = "CREATE TABLE IF NOT EXISTS items (ID VARCHAR(32) NOT NULL, PRIMARY KEY(ID), NAME VARCHAR(32) NOT NULL, LOCATION VARCHAR(32) NOT NULL, QUANTITY INT NOT NULL,DESCRIPTION VARCHAR(50))";
		DBUtil.dbExecuteUpdate(initSQL);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("views/mainWindow.fxml"));
		primaryStage.setTitle("Simple IMS");
		primaryStage.setScene(new Scene(root));
		primaryStage.setMinHeight(380);
		primaryStage.setMinWidth(640);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("IMS icon.png")));
		primaryStage.show();
	}
}
