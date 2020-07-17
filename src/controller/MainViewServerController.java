/**
 * Sample Skeleton for 'MainViewServer.fxml' Controller Class
 */

package controller;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
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

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert buttonSync != null : "fx:id=\"buttonSync\" was not injected: check your FXML file 'MainViewServer.fxml'.";
		assert labelStatus != null : "fx:id=\"labelStatus\" was not injected: check your FXML file 'MainViewServer.fxml'.";
		assert listViewClients != null : "fx:id=\"listViewClients\" was not injected: check your FXML file 'MainViewServer.fxml'.";
	}

	public void setModel(ModelServer modelServer) {
		model = modelServer;
		listViewClients.setItems(model.getClientObservableList());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listViewClients.setCellFactory(listViewClients -> new ListViewCellsClientController(model));
		try {
			labelNameServer.setText(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		buttonSync.setOnAction((event) -> {
			for( Client c: model.getClientObservableList()) {
				c.getsTh().getCommandsQueue().add("print time");
			}
		});
	}

	@FXML
	void closeProgram(ActionEvent event) throws InterruptedException {
		model.close();
		// System.exit(0);
	}

}
