package server;

import java.io.FileReader;
import java.io.IOException;
import controller.MainViewServerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import settings.Settings;

public class ModelServer {
	private Settings settings;
	private static int serverPort;
	private long commiterSleepTime;
	private static String pathServeriFix, pathLocalCommit;
	private ThreadedServer ts;
	private MainViewServerController mvsController;
	private ObservableList<Client> clientObservableList;
	
	public ModelServer(MainViewServerController _mvsController) {
		readSettings();
		clientObservableList = FXCollections.observableArrayList();
		clientObservableList.addAll(new Client("Client01", "192.168.0.1",null), new Client("Client02", "192.168.0.2",null));
		ts=new ThreadedServer(this,serverPort,_mvsController);
		ts.start();
	}
	
	private void readSettings() {
		settings = new Settings();
		try {
			settings.load(new FileReader("properties/settings.cfg"));
		} catch (IOException e) {
			//log.error(e);
		}
		serverPort = Integer.parseInt(settings.getProperty("copierServerPort"));
		pathServeriFix=settings.getProperty("pathServeriFix");
		pathLocalCommit=settings.getProperty("pathLocalCommit");
		commiterSleepTime=(long)Integer.parseInt(settings.getProperty("commiterSleepTime"));
	}
	
	public void setController(MainViewServerController mainController) {
		mvsController = mainController;

	}

	public void close() throws InterruptedException {
		ts.close();
		ts.join(100);
	}

	public MainViewServerController getMvsController() {
		return mvsController;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public ObservableList<Client> getClientObservableList() {
		return clientObservableList;
	}
	
}
