package server;

import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import commands.*;
import controller.MainViewServerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import master.MasterNode;
import settings.Settings;

public class ModelServer {
	private static Settings settings;
	private static int serverPort, portServerFrom;
	private static MasterNode ts;
	private MainViewServerController mvsController;
	private ObservableList<Client> clientsObservableList;
	public static final Logger log = Logger.getLogger(ModelServer.class.getName());
	private CommandRegister commandRegister ;
	
	public ModelServer(MainViewServerController _mvsController) throws IOException {
		readSettings();
		setUpCommands();
		clientsObservableList = FXCollections.observableArrayList();
		ts=new MasterNode(this,serverPort, portServerFrom,log);
		ts.start();
	}

	private void setUpCommands() {
		commandRegister= new CommandRegister();
		commandRegister.register("in standby", new ClientInStandByCommand());
		commandRegister.register("not in standby", new ClientNotInStandByCommand());
	}
	
	private void readSettings() {
		settings = new Settings();
		try {
			settings.load(new FileReader("properties/settings.cfg"));
		} catch (IOException e) {
			log.error(e);
		}
		serverPort = Integer.parseInt(settings.getProperty("copierServerPort"));
		portServerFrom = Integer.parseInt(settings.getProperty("portServerFrom"));
	}
	
	public void setController(MainViewServerController mainController) {
		mvsController = mainController;

	}

	public void close() {
		for(Client c: clientsObservableList) {
			c.getsTh().close();
		}
		ts.close();
	}

	public MainViewServerController getMvsController() {
		return mvsController;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public ObservableList<Client> getClientObservableList() {
		return clientsObservableList;
	}

	public CommandRegister getCommandRegister() {
		return commandRegister;
	}
	
}
