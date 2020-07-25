package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import server.Client;
import server.ModelServer;

public class MasterServerNode extends Thread {
	protected Socket socketServerNode;
	private BufferedReader inStream = null;
	private PrintWriter outStream = null;
	private String outLine;
	static private boolean runningThread = true;
	private Logger logger;
	private MasterClientNode clientNode;
	private ModelServer model;
	private Client client;

	public MasterServerNode(Socket _socket, Logger _log, ModelServer _model, int _portClientNode, Client _client) {
		runningThread = true;
		this.client = _client;
		client.getCommandsQueue().add("get name");
		this.model = _model;
		if(_socket==null)System.out.println("_socket = null");
		this.socketServerNode = _socket;
		this.logger = _log;
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

	public void close() {
		runningThread = false;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
