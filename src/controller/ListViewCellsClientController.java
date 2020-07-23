/**
 * Sample Skeleton for 'ListViewCellsClient.fxml' Controller Class
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
	static private int count=0;
	private int index=0;
	private ModelServer model;
	
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

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert lableNameClient != null : "fx:id=\"lableNameClient\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert labelAddressClient != null : "fx:id=\"labelAddressClient\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
		assert imageViewClientStatus != null : "fx:id=\"imageViewClientStatus\" was not injected: check your FXML file 'ListViewCellsClient.fxml'.";
	}

	public ListViewCellsClientController(ModelServer _model) {
		this.model=_model;
		this.index=ListViewCellsClientController.count-1;
		ListViewCellsClientController.count++;
		cMenu = makeContextMenuClient();
		this.setContextMenu(cMenu);
		loadFXML();
	}

	private ContextMenu makeContextMenuClient() {
		ContextMenu contextMenu=new ContextMenu();
		MenuItem menuItem1 = new MenuItem("File list");
		menuItem1.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/treeStructureIcon16.png"))));
		MenuItem menuItem2 = new MenuItem("Connect");
		MenuItem menuItem3 = new MenuItem("Disconnect");
		MenuItem menuItem4 = new MenuItem("Update");
		menuItem4.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/updateIcon16.png"))));
		menuItem3.setOnAction((event) -> {
			lableNameClient.setText(" Disconnected");
		});
		
		menuItem4.setOnAction((event) -> {
			System.out.println(index);
			model.getClientObservableList().get(index).getCommandsQueue().add("get name");
		});
		menuItem2.setOnAction((event) -> {
			model.getClientObservableList().get(0).setClientName("pippo");
			updateItem(model.getClientObservableList().get(0),false);
//			lableNameClient.setText(" Connected");
		});
		menuItem1.setOnAction((event) -> {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FileListView.fxml"));
			Parent root;
			try {
				root = loader.load();
    		FileListViewController fileListViewController = (FileListViewController) loader.getController();
//    		fileListViewController.setModel(model);
    		Image icon = new Image(getClass().getResourceAsStream("/treeStructureIcon96.png"));
    		stage.getIcons().add(icon);
				stage.setTitle("List files");
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
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
			lableNameClient.setText(_client.getClientName());
			labelAddressClient.setText(_client.getClientAddress());
			setGraphic(mainPane);
		}
	}

}
