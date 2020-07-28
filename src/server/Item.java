package server;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Item implements Serializable{

	// Fields:
	private String name;
	private String absoluteName;
	private LocalDateTime lastEdit;
	private boolean isFile;

	public Item(String _name, String _absoluteName, LocalDateTime _lastEdit, boolean _isFile) {
		super();
		this.name = _name;
		this.absoluteName = _absoluteName;
		this.lastEdit = _lastEdit;
		this.isFile = _isFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbsoluteName() {
		return absoluteName;
	}

	public void setAbsoluteName(String absoluteName) {
		this.absoluteName = absoluteName;
	}

	public LocalDateTime getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(LocalDateTime lastEdit) {
		this.lastEdit = lastEdit;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	@Override
	public String toString() {
		return this.name + " " + this.absoluteName + " " + this.lastEdit.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	
	public String toStringTest() {
		return this.isFile ? "FILE:		 " + this.name + " - " + this.absoluteName + " - " + this.lastEdit.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) :
							 "DIRECTORY: " + this.name + " - " + this.absoluteName + " - " + this.lastEdit.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	
}
