package server;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client_old {

	private final StringProperty clientName;
	private final StringProperty clientStatus;
	private final StringProperty clientAddress;
	private final IntegerProperty clientStatusInteger;

	public Client_old() {
		this(null, null);
	}

	public Client_old(String name, String address) {
		clientName = new SimpleStringProperty(name);
		clientAddress = new SimpleStringProperty(address);
		clientStatus = new SimpleStringProperty("none");
		clientStatusInteger = new SimpleIntegerProperty(0);
	}

	public String getClientName() {
		return clientName.get();
	}

	public void setClientName(String name) {
		this.clientName.set(name);
	}

	public String getClientAddress() {
		return clientAddress.get();
	}

	public void setClientAddress(String address) {
		this.clientAddress.set(address);
	}

	public String getClientStatus() {
		return clientStatus.get();
	}

	public void setClientStatus(String status) {
		this.clientStatus.set(status);
	}

	public int getClientStatusInteger() {
		return clientStatusInteger.get();
	}

	public void setClientStatusInteger(int statusInteger) {
		this.clientStatusInteger.set(statusInteger);
	}
}
