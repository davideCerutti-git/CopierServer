/**
 * Sample Skeleton for 'ListViewCellsClient.fxml' Controller Class
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.Client;
import server.ModelServer;

public class ListViewCellsClientController extends ListCell<Client> {

	private FXMLLoader mLLoader;
	private ContextMenu cMenu;
	private Logger logger;
	private Client client;
	private ModelServer model;
	
	private final Image IMAGE_STANDBY  = new Image(getClass().getResourceAsStream("/sleep.png"));
    private final Image IMAGE_NOT_STANDBY  = new Image(getClass().getResourceAsStream("/person.png"));

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="lableNameClient"
	private Label lableNameClient; // Value injected by FXMLLoader

	@FXML // fx:id="labelAddressClient"
	private Label labelAddressClient; // Value injected by FXMLLoader

	@FXML // fx:id="imageViewClientStatus"
	private ImageView imageViewClientStatus; // Value injected by FXMLLoader

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader
	
    @FXML // fx:id="imageViewClientStatusStandBy"
    private ImageView imageViewClientStatusStandBy; // Value injected by FXMLLoader
	

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert lableNameClient != null : "fx:id=\"lableNameClient\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert labelAddressClient != null : "fx:id=\"labelAddressClient\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert imageViewClientStatus != null : "fx:id=\"imageViewClientStatus\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
	}

	public ListViewCellsClientController(ModelServer _model, Logger _logger) {
		this.logger=_logger;
		cMenu = makeContextMenuClient();
		this.setContextMenu(cMenu);
		loadFXML();
	}

	private ContextMenu makeContextMenuClient() {
		ContextMenu contextMenu=new ContextMenu();
		
		MenuItem menuItem1 = new MenuItem("File list");
		MenuItem menuItem2 = new MenuItem("Connect");
		MenuItem menuItem3 = new MenuItem("Disconnect");
		MenuItem menuItem4 = new MenuItem("Get name");
		MenuItem menuItem5 = new MenuItem("Set in startup");
		MenuItem menuItem6 = new MenuItem("Remove startup");
		MenuItem menuItem7 = new MenuItem("Rerstart");
		
		menuItem1.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/treeStructureIcon16.png"))));
		menuItem4.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/updateIcon16.png"))));
		
		menuItem1.setOnAction((event) -> {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FileListView.fxml"));
			Parent root;
			try {
				root = loader.load();
    		FileListViewController fileListViewController = (FileListViewController) loader.getController();
    		fileListViewController.setModel(model);
    		Image icon = new Image(getClass().getResourceAsStream("/treeStructureIcon96.png"));
    		stage.getIcons().add(icon);
				stage.setTitle("List files");
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e) {
				logger.error(e);
			}

		});
		
		menuItem2.setOnAction((event) -> {
//			client.setClientName("pippo");
//			model.getClientObservableList().get(0).setClientName("pippo");
//			updateItem(client,false);
//			lableNameClient.setText(" Connected");
		});
		
		menuItem3.setOnAction((event) -> {
//			lableNameClient.setText(" Disconnected");
		});
		
		menuItem4.setOnAction((event) -> {
			client.getCommandsQueue().add("get name");
		});
		
		menuItem5.setOnAction((event)->{
			client.getCommandsQueue().add("set startup");
		});
		
		menuItem6.setOnAction((event)->{
			client.getCommandsQueue().add("remove startup");
		});
		
		menuItem7.setOnAction((event)->{
			client.getCommandsQueue().add("restart");
		});
		
		contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, menuItem7);
		return contextMenu;
	}

	private void loadFXML() {
		try {
			mLLoader = new FXMLLoader(getClass().getResource("/ListViewCellsClient.fxml"));
			mLLoader.setController(this);
			mLLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(Client _client, boolean empty) {
		super.updateItem(_client, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			this.client=_client;
			this.lableNameClient.setText(_client.getClientName());
			this.labelAddressClient.setText(_client.getClientAddress());
			if(_client.getInStandby())
				this.imageViewClientStatusStandBy.setImage(IMAGE_STANDBY);
			else
				this.imageViewClientStatusStandBy.setImage(IMAGE_NOT_STANDBY);
			setGraphic(mainPane);
		}
	}

}
