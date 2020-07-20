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

	public MasterServerNode(Socket _socket, Logger _log, ModelServer _model, int _portClientNode) throws IOException {
		
		this.model = _model;
		this.socketServerNode = _socket;
		this.logger = _log;
		this.inStream = new BufferedReader(new InputStreamReader(socketServerNode.getInputStream()));
		this.outStream = new PrintWriter(socketServerNode.getOutputStream(), true);
		this.clientNode = new MasterClientNode(socketServerNode, model, logger, _portClientNode);
		this.clientNode.start();
	}

	public void run() {
		try {
			while (runningThread) {
				// WRITE commands to Client
				outLine = client.getCommandsQueue().poll();
				if (outLine != null) {
					outStream.write(outLine + "\n");
					outStream.flush();
					logger.info("to client: printed: " + outLine);
					// Read response from Client
					logger.info("waiting response");
					logger.info("Response: " + inStream.readLine());
					outLine = null;
				}
			}
		} catch (IOException e) {
			logger.error(e);
			runningThread = false;
		}
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
