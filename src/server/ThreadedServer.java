package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

public class ThreadedServer extends Thread {

	static boolean runningThread = true;
	private ServerSocket serverSocket = null;
	private ServerThreadTo serverThreadTo;
	private Socket s = null;
	private int portServerFrom;
	private Logger log;
	static private ModelServer model;

	public ThreadedServer(ModelServer _model, int _port, int _portServerFrom, Logger _log) throws IOException {
		this.serverSocket = new ServerSocket(_port);
		ThreadedServer.model = _model;
		this.log = _log;
		this.portServerFrom = _portServerFrom;
	}

	@Override
	public void run() {
		log.info("ThreadedServer started, waiting for clients...");

		while (runningThread) {
			try {
				s = serverSocket.accept();

				serverThreadTo = new ServerThreadTo(s, log, model, portServerFrom);
				Client client = new Client(s.getInetAddress().toString(), serverThreadTo);
				log.info("Client connected");
				// update observable list in the model
				model.getClientObservableList().add(client);
				serverThreadTo.start();
			} catch (SocketException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	public void close() {
		runningThread = false;
	}

}
