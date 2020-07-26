package master;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.log4j.Logger;
import server.Client;
import server.ModelServer;

public class MasterServerNode extends Thread {
	
	//Owns:
	private BufferedReader inStream = null;
	private PrintWriter outStream = null;
	
	//Utils:
	private Socket socketServerNode;
	private String outLine;
	private boolean runningThread = true;
	private Logger logger;
	
	//Refs:
	private ModelServer model;
	private Client client;

	public MasterServerNode(Socket _socket, Logger _logger, ModelServer _model, int _portClientNode, Client _client) {
		this.runningThread = true;
		this.client = _client;
		setupInitalCommands();
		this.model = _model;
		this.socketServerNode = _socket;
		this.logger = _logger;
		try {
			this.inStream = new BufferedReader(new InputStreamReader(socketServerNode.getInputStream()));
			this.outStream = new PrintWriter(socketServerNode.getOutputStream(), true);
		} catch (Exception e) {
			logger.error(e);
		}
		client.setClientNode(new MasterClientNode(socketServerNode, model, logger, _portClientNode, client));
		client.getClientNode().start();
	}

	public void run() {
		while (runningThread) {
			try {
				// WRITE commands to Client
				outLine = client.getCommandsQueue().poll();
				if (outLine != null) {
					logger.info("writing outline");
					outStream.write(outLine + "\n");
					outStream.flush();
					logger.info("to client: printed: " + outLine);
					// Read response from Client
					logger.info("waiting response");
					logger.info("Response: " + inStream.readLine());
					outLine = null;
				}
			} catch (Exception e) {
				logger.error(e);
				runningThread = false;
			}
		}

		logger.debug("closing server node...");
		client.closeClient();
	}
	
	private void setupInitalCommands() {
		client.getCommandsQueue().add("get name");
	}

	public void close() {
		runningThread = false;
	}

}
