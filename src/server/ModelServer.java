package server;

import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;
import controller.MainViewServerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import master.MasterNode;
import settings.Settings;

public class ModelServer {
	
	//Utils:
	public static final Logger logger = Logger.getLogger(ModelServer.class.getName());
	
	//Settings:
	private static Settings settings;
	private static int masterServerNodePort, masterClentNodePort;
	
	//Owns:
	private static MasterNode masterNode;
	private MainViewServerController mvsController;
	private ObservableList<Client> clientsList;
	
	
	
	public ModelServer(MainViewServerController _mvsController) throws IOException {
		readSettings();
		clientsList = FXCollections.observableArrayList();
		masterNode=new MasterNode(this,masterServerNodePort, masterClentNodePort,logger);
		masterNode.start();
	}

	private void readSettings() {
		
		//Load settings:
		settings = new Settings();
		try {
			settings.load(new FileReader("properties/settings.cfg"));
		} catch (IOException e) {
			logger.error(e);
		}
		
		//Read settings:
		masterServerNodePort = Integer.parseInt(settings.getProperty("masterServerNodePort"));
		masterClentNodePort = Integer.parseInt(settings.getProperty("masterClentNodePort"));
	}

	public void close() {
		masterNode.close();
	}

	//Setters:
	public void setController(MainViewServerController mainController) {
		mvsController = mainController;
	}
	
	//Getters:
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

}
