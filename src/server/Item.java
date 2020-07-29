package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Item implements Serializable , Comparable<Item>{

	private static final long serialVersionUID = 4794682805606231339L;
	// Fields:
	private String name;
	private String absoluteName;
	private long lastEdit;
	private boolean isFile;

	public Item(String _name, String _absoluteName, long _lastEdit, boolean _isFile) {
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

	public long getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(long lastEdit) {
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
		return this.name + " " + this.absoluteName + " "
				+ Instant.ofEpochMilli(this.lastEdit).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}

	public String toStringTest() {
		return this.isFile
				? "FILE:		 " + this.name + " - " + this.absoluteName + " - "
						+ Instant.ofEpochMilli(this.lastEdit).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
				: "DIRECTORY: " + this.name + " - " + this.absoluteName + " - "
						+ Instant.ofEpochMilli(this.lastEdit).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}

	@Override
	public int compareTo(Item o) {
		 if(this.lastEdit==((Item)o).getLastEdit())
			 return 0;
		 if(this.lastEdit>((Item)o).getLastEdit())
			 return 1;
		 return -1;
	}
	
	public static String serializeObjectToString(Item o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public static Item deSerializeObjectFromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return (Item) o;
	}

}
