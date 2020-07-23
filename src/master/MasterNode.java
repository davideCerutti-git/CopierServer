package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

import server.Client;
import server.ModelServer;

public class MasterNode extends Thread {

	static boolean runningThread = true;
	private ServerSocket serverNodeSocket = null;
	private MasterServerNode serverNode;
	private Socket socketServerNode = null;
	private int portClientNode;
	private Logger logger;
	static private ModelServer model;

	public MasterNode(ModelServer _model, int _port, int _portServerFrom, Logger _log) throws IOException {
		this.serverNodeSocket = new ServerSocket(_port);
		MasterNode.model = _model;
		this.logger = _log;
		this.portClientNode = _portServerFrom;
	}

	@Override
	public void run() {
		logger.info("MasterNode started, waiting for slaves...");

		while (runningThread) {
			try {
				socketServerNode = serverNodeSocket.accept();
				Client client = new Client(socketServerNode.getInetAddress().toString(), serverNode,model);
				serverNode = new MasterServerNode(socketServerNode, logger, model, portClientNode,client);
//				
//				serverNode.setClient(client);
				logger.info("Slave connected");
				// update observable list in the model
				model.getClientObservableList().add(client);
				serverNode.start();
			} catch (SocketException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	public void close() {
		runningThread = false;
	}

}
