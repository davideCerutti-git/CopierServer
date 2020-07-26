package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import javafx.application.Platform;
import server.Client;
import server.ModelServer;

public class MasterNode extends Thread {

	static boolean runningThread = true;
	private ServerSocket masterServerSocket = null;
	private Socket socket = null;
	private int portClientNode;
	private Logger logger;
	static private ModelServer model;

	public MasterNode(ModelServer _model, int _port, int _portServerFrom, Logger _log) throws IOException {
		this.masterServerSocket = new ServerSocket(_port);
		MasterNode.model = _model;
		this.logger = _log;
		this.portClientNode = _portServerFrom;
	}

	@Override
	public void run() {
		logger.info("MasterNode started, waiting for clients...");
		try {
			while (runningThread) {
				// Wait an incoming client
				socket = masterServerSocket.accept();
				Semaphore semaphore = new Semaphore(0);
				Platform.runLater(() -> {
					// Set up the new client
					logger.debug("Creating new client...");
					Client client = new Client(socket, model, logger, portClientNode);
					model.getClientObservableList().add(client);
					client = null;
					semaphore.release();
				});
				
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					logger.error(e);
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}

	}

	public void close() {
		runningThread = false;
	}

}
