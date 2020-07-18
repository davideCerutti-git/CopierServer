package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

public class ThreadedServer extends Thread {

	static boolean runningThreadedServer = true;
	private ServerSocket serverSocket = null;
	private ServerThread serverThread;
	private Socket s = null;
	private int i = 1;
	private Logger log;
	private ModelServer model;

	public ThreadedServer(ModelServer _model, int _port, Logger _log) throws IOException {
		serverSocket = new ServerSocket(_port);
		model = _model;
		log=_log;
	}

	@Override
	public void run() {
		log.debug("ThreadedServer started, waiting for clients...");

		while (runningThreadedServer) {
			try {
				s = serverSocket.accept();
			} catch (IOException e) {
				log.error(e);
			}
			// new thread for a client
			try {
				serverThread = new ServerThread(s, log);
				log.debug("Client connected");
				// update observable list in the model
				model.getClientObservableList().add(new Client(s.getInetAddress().toString(), serverThread));
				serverThread.start();
			} catch (SocketException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

	}

	public void close() {
		runningThreadedServer = false;
	}

}
