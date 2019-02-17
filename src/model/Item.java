package model;

import javafx.beans.property.*;

public class Item {
	private StringProperty itemId;
	private StringProperty name;
	private StringProperty location;
	private IntegerProperty quantity;
	private StringProperty description ;

	public Item() {
		itemId = new SimpleStringProperty();
		name = new SimpleStringProperty();
		location = new SimpleStringProperty();
		quantity = new SimpleIntegerProperty();
		description = new SimpleStringProperty();
	}

	public String getItemId() {
		return itemId.get();
	}

	public StringProperty itemIdProperty() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId.set(itemId);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getLocation() {
		return location.get();
	}

	public StringProperty locationProperty() {
		return location;
	}

	public void setLocation(String location) {
		this.location.set(location);
	}

	public int getQuantity() {
		return quantity.get();
	}

	public IntegerProperty quantityProperty() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	public String getDescription() {
		return description.get();
	}

	public StringProperty descriptionProperty() {
		return description;
	}

	public void setDescription(String description) {
		this.description.set(description);
	}
}
