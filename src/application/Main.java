package application;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import controller.MainViewServerController;
import javafx.application.Application;
import javafx.stage.Stage;
import server.ModelServer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	private static ModelServer modelServer;
	static SystemTray tray;
	static TrayIcon trayIcon;

	@Override
	public void start(Stage primaryStage) {

		try {
			makeSystemTray();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainViewServer.fxml"));
			Parent root = loader.load();
			MainViewServerController mainController = (MainViewServerController) loader.getController();
			modelServer = new ModelServer(mainController);
			mainController.setModel(modelServer);
			modelServer.setController(mainController);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			Image icon = new Image(getClass().getResourceAsStream("/syncServerIcon40.png"));
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException
	 * @throws AWTException
	 */
	private void makeSystemTray() throws IOException, AWTException {
		// System tray
		trayIcon = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/syncServerIcon16.png")),
				"Server Copier\u00a9");
		tray = SystemTray.getSystemTray();
		PopupMenu popup = new PopupMenu();
		MenuItem close = new MenuItem("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popup.add(close);
		trayIcon.setPopupMenu(popup);
		tray.add(trayIcon);
	}

	public static void main(String[] args) {
		
		launch(args);
		tray.remove(trayIcon);
	}
}
