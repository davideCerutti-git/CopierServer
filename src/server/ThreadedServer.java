package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

public class ThreadedServer extends Thread {

	static boolean runningThreadedServer = true;
	private ServerSocket serverSocket = null;
	private ServerThreadTo serverThreadTo;
	private Socket s = null;
	private int i = 1, portServerFrom;
	private Logger log;
	private ModelServer model;

	public ThreadedServer(ModelServer _model, int _port, int _portServerFrom, Logger _log ) throws IOException {
		serverSocket = new ServerSocket(_port);
		model = _model;
		log=_log;
		this.portServerFrom=_portServerFrom;
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
				
				Client c=new Client(s.getInetAddress().toString(), serverThreadTo);
				serverThreadTo = new ServerThreadTo(s, log, model, portServerFrom, c);
				log.debug("Client connected");
				// update observable list in the model
				model.getClientObservableList().add(c);
				serverThreadTo.start();
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
