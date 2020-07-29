package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import controller.MainViewServerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import settings.Settings;

public class ModelServer {

	// Singleton instance
	private static ModelServer instance = null; // references to instance

	// Utils:
	public static final Logger logger = Logger.getLogger(ModelServer.class.getName());

	// Settings:
	private static Settings settings;
	private static int masterServerNodePort, masterClentNodePort;

	// Owns:
	private static MasterNode masterNode;
	private MainViewServerController mvsController;
	private ObservableList<Client> clientsList;
	static private List<Item> fileList = new ArrayList<>();

	private ModelServer(MainViewServerController _mvsController) { // Constructor
		readSettings();
		clientsList = FXCollections.observableArrayList();
		try {
			masterNode = new MasterNode(this, masterServerNodePort, masterClentNodePort, logger);
		} catch (IOException e) {
			logger.error(e);
		}
		masterNode.start();
	}

	public static ModelServer getIstance(MainViewServerController _mainController) {
		if (instance == null)
			instance = new ModelServer(_mainController);
		return instance;
	}

	private void readSettings() {

		// Load settings:
		settings = new Settings();
		try {
			settings.load(new FileReader("properties/settings.cfg"));
		} catch (IOException e) {
			logger.error(e);
		}

		// Read settings:
		masterServerNodePort = Integer.parseInt(settings.getProperty("masterServerNodePort"));
		masterClentNodePort = Integer.parseInt(settings.getProperty("masterClentNodePort"));
	}

	public void listFile() throws IOException, ClassNotFoundException {
		search(new File("C:/testCopier"));
		
		for (Item item : fileList) {
			logger.info((item.toString()));
			logger.info(Item.deSerializeObjectFromString(Item.serializeObjectToString(item)));
			logger.info(Item.serializeObjectToString(item)+"\n\n");
		}
	}

	public static void search(File folder) {
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				String[] dir = f.list();
				if (dir.length > 0)
					search(f);
				else {
					fileList.add(new Item(f.getName(), f.getAbsolutePath(), f.lastModified(), false));
				}
			}

			if (f.isFile()) {
				fileList.add(new Item(f.getName(), f.getAbsolutePath(), f.lastModified(), false));
			}
		}
	}

	public void close() {
		masterNode.close();
	}

	// Setters:
	public void setController(MainViewServerController mainController) {
		mvsController = mainController;
	}

	// Getters:
	public MainViewServerController getMvsController() {
		return mvsController;
	}

	public Settings getSettings() {
		return settings;
	}

	public ObservableList<Client> getClientObservableList() {
		return clientsList;
	}

	public Logger getLogger() {
		return ModelServer.logger;
	}

	public static List<Item> getFileList() {
		return fileList;
	}



}
