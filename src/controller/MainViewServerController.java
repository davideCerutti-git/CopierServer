/**
 * Sample Skeleton for 'MainViewServer.fxml' Controller Class
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import server.Client;
import server.ModelServer;

public class MainViewServerController implements Initializable {

	private ModelServer model;

	@FXML // fx:id="imageViewServer"
	private ImageView imageViewServer; // Value injected by FXMLLoader

	@FXML // fx:id="labelNameServer"
	private Label labelNameServer; // Value injected by FXMLLoader

	@FXML // fx:id="labelAddressServer"
	private Label labelAddressServer; // Value injected by FXMLLoader

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="buttonSync"
	private Button buttonSync; // Value injected by FXMLLoader

	@FXML // fx:id="labelStatus"
	private Label labelStatus; // Value injected by FXMLLoader

	@FXML // fx:id="listViewClients"
	private ListView<Client> listViewClients; // Value injected by FXMLLoader

	private ObservableList<Client> clientObservableList;
	private ContextMenu cMenu;

	public MainViewServerController() {
		clientObservableList = FXCollections.observableArrayList();
		//clientObservableList.addAll(new Client("Client01", "192.168.0.1"), new Client("Client02", "192.168.0.2"));
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert buttonSync != null : "fx:id=\"buttonSync\" was not injected: check your FXML file 'MainViewServer.fxml'.";
		assert labelStatus != null : "fx:id=\"labelStatus\" was not injected: check your FXML file 'MainViewServer.fxml'.";
		assert listViewClients != null : "fx:id=\"listViewClients\" was not injected: check your FXML file 'MainViewServer.fxml'.";
		
	}

	public void setModel(ModelServer modelServer) {
		model = modelServer;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewClients.setItems(clientObservableList);
		listViewClients.setCellFactory(listViewClients -> new ListViewCellsClientController(model));
//		labelNameServer.setText(model.getSettings().getProperty("copierServerName"));
//		labelAddressServer.setText(model.getSettings().getProperty("copierServerIpAddress"));
	}

	public ObservableList<Client> getClientObservableList() {
		return clientObservableList;
	}

	@FXML
	void closeProgram(ActionEvent event) throws InterruptedException {
		model.close();
		// System.exit(0);
	}
}
