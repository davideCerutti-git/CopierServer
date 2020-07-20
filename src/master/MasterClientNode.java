package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

import commands.Command;
import server.ModelServer;

public class MasterClientNode extends Thread {
	protected Socket socketClientNode;
	private BufferedReader inStream = null;
	private PrintWriter outStream = null;
	private Logger logger;
	static private boolean runningThread = true, connectedToServer = false;
	private String strLine;
	private ModelServer model;
	private String addressClientNode;
	private int portClientNode;

	public MasterClientNode(Socket _socketServerNode, ModelServer _model, Logger _logger, int _portClientNode) throws IOException {
		this.addressClientNode = _socketServerNode.getInetAddress().toString().split("/")[1];
		this.model = _model;
		this.logger = _logger;
		this.portClientNode = _portClientNode;
	}

	public void run() {
		while (runningThread) {
			while (!connectedToServer && runningThread) {
				try {
					logger.info("Tring to connect to ClientNode " + addressClientNode);
					this.socketClientNode = new Socket(addressClientNode, portClientNode);
					connectedToServer = true;
					logger.info("connected to sClient");
				} catch (IOException e) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						logger.error(e1);
					}
				}
			}
			try {
				this.inStream = new BufferedReader(new InputStreamReader(socketClientNode.getInputStream()));
				this.outStream = new PrintWriter(socketClientNode.getOutputStream(), true);
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				while (true) {
					strLine = inStream.readLine();
					logger.info("received: " + strLine);
					Command c = model.getCommandRegister().getCommandByName(strLine);
					outStream.write(c.execute() + "\n");
					outStream.flush();
				}
			} catch (UnknownHostException e) {
				logger.error("Don’t know about host " + addressClientNode);
				System.exit(1);
			} catch (IOException e) {
				logger.info("Lost connection to: " + addressClientNode);
				connectedToServer = false;
				runningThread = false;
			}
		}
		logger.info("EchoClient: closing...");
		outStream.close();
		try {
			inStream.close();
			socketClientNode.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
