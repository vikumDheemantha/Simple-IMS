package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import  util.DBUtil;
import java.sql.*;

public class ItemDAO {
	public static Item getItemById(String id) throws SQLException, ClassNotFoundException {
		String selectStmt = "SELECT * FROM items WHERE id = '" + id+"';";
		try {
			//Get ResultSet from dbExecuteQuery method
			ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

			//Send ResultSet to the getItemFromList method and get Item object
			Item item = getItemFromRes(rsEmp);

			//Return employee object
			return item;
		} catch (SQLException e) {
			System.out.println("While searching an item with " + id+ " id, an error occurred: " + e);
			//Return exception
			throw e;

		}
	}

	private static Item getItemFromRes(ResultSet rs) throws SQLException
	{
		Item item = null;
		if (rs.next()) {
			item = new Item();
			item.setItemId(rs.getString("ID"));
			item.setName(rs.getString("NAME"));
			item.setLocation(rs.getString("LOCATION"));
			item.setDescription(rs.getString("DESCRIPTION"));
			item.setQuantity(rs.getInt("QUANTITY"));
		}
		return item;
	}

	private static ObservableList<Item> getItemList(ResultSet rs) throws SQLException, ClassNotFoundException {
		//Declare a observable List which comprises of Employee objects
		ObservableList<Item> itemList = FXCollections.observableArrayList();

		while (rs.next()) {
			Item item = new Item();
			item.setItemId(rs.getString("ID"));
			item.setName(rs.getString("NAME"));
			item.setLocation(rs.getString("LOCATION"));
			item.setDescription(rs.getString("DESCRIPTION"));
			item.setQuantity(rs.getInt("QUANTITY"));
			//Add employee to the ObservableList
			itemList.add(item);
		}
		//return empList (ObservableList of Employees)
		return itemList;
	}

	public static ObservableList<Item> getAllItems() throws SQLException, ClassNotFoundException {
		//Declare a SELECT statement
		String selectStmt = "SELECT * FROM items";

		//Execute SELECT statement
		try {
			//Get ResultSet from dbExecuteQuery method
			ResultSet resItems = DBUtil.dbExecuteQuery(selectStmt);

			//Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Item> itemList = getItemList(resItems);

			//Return employee object
			return itemList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			//Return exception
			throw e;
		}
	}

	public static ObservableList<Item> searchItemByName(String name) throws SQLException, ClassNotFoundException {
		//Declare a SELECT statement
		String selectStmt = "SELECT * FROM items WHERE NAME like '%"+name+"%'";

		//Execute SELECT statement
		try {
			//Get ResultSet from dbExecuteQuery method
			ResultSet resItems = DBUtil.dbExecuteQuery(selectStmt);

			//Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Item> itemList = getItemList(resItems);

			//Return employee object
			return itemList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			//Return exception
			throw e;
		}
	}

	public static ObservableList<Item> searchItemByLocation(String location) throws SQLException, ClassNotFoundException {
		//Declare a SELECT statement
		String selectStmt = "SELECT * FROM items WHERE LOCATION like '%"+location+"%'";

		//Execute SELECT statement
		try {
			//Get ResultSet from dbExecuteQuery method
			ResultSet resItems = DBUtil.dbExecuteQuery(selectStmt);

			//Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Item> itemList = getItemList(resItems);

			//Return employee object
			return itemList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			//Return exception
			throw e;
		}
	}

	public static ObservableList<Item> searchItemById(String id) throws SQLException, ClassNotFoundException {
		//Declare a SELECT statement
		String selectStmt = "SELECT * FROM items WHERE ID like '%"+id+"%'";

		//Execute SELECT statement
		try {
			//Get ResultSet from dbExecuteQuery method
			ResultSet resItems = DBUtil.dbExecuteQuery(selectStmt);

			//Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Item> itemList = getItemList(resItems);

			//Return employee object
			return itemList;
		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			//Return exception
			throw e;
		}
	}

	public static void updateItem (String id, String name, String location, String description, int quantity) throws SQLException, ClassNotFoundException {
		//Declare a UPDATE statement
		String updateStmt =
						"   UPDATE items\n" +
						"      SET NAME = '" + name+ "'\n" +
						"      , LOCATION = '" + location+ "'\n" +
						"      , DESCRIPTION = '" + description+ "'\n" +
						"      , QUANTITY = " + quantity+ "\n" +
						"    WHERE ID= '" + id + "';";

		//Execute UPDATE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while UPDATE Operation: " + e);
			throw e;
		}
	}

	public static void deleteItemById (String id) throws SQLException, ClassNotFoundException {
		//Declare a DELETE statement
		String updateStmt =
						"   DELETE FROM items\n" +
						"         WHERE ID ='"+ id+"';";

		//Execute UPDATE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while DELETE Operation: " + e);
			throw e;
		}
	}

	public static void insertItem (String id, String name, String location, String description, int quantity) throws SQLException, ClassNotFoundException {
		//Declare a DELETE statement
		String updateStmt =
				"INSERT INTO items\n" +
						"(ID, NAME, LOCATION, QUANTITY, DESCRIPTION)\n" +
						"VALUES\n" +
						"('"+id+"','"+name+"', '"+location+"', "+quantity+", '"+ description+"');";

		//Execute DELETE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while INSERT Operation: " + e);
			throw e;
		}
	}

	public static void clearTable() throws ClassNotFoundException, SQLException {
		String clearStmt = "DELETE FROM items";
		try {
			DBUtil.dbExecuteUpdate(clearStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while DELETE Operation: " + e);
			throw e;
		}

	}

	public static void updateQuantity(String id, int quantity) throws SQLException, ClassNotFoundException {
		//Declare a UPDATE statement
		String updateStmt =
				"   UPDATE items\n" +
						"      SET QUANTITY = " + quantity+ "\n" +
						"    WHERE ID= '" + id + "';";

		//Execute UPDATE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while UPDATE Operation: " + e);
			throw e;
		}
	}
}
