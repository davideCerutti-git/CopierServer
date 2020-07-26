package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

import commands.Command;
import server.Client;
import server.ModelServer;

public class MasterClientNode extends Thread {
	protected Socket socketClientNode;
	private BufferedReader inStream = null;
	private PrintWriter outStream = null;
	private Logger logger;
	private boolean runningThread = true, connectedToServer = false;
	private String strLine;
	@SuppressWarnings("unused")
	private ModelServer model;
	private String addressClientNode;
	private int portClientNode;
	private Client client;

	public MasterClientNode(Socket _socketServerNode, ModelServer _model, Logger _logger, int _portClientNode, Client _client) {
		this.runningThread = true;
		this.connectedToServer = false;
		this.addressClientNode = _socketServerNode.getInetAddress().toString().split("/")[1];
		this.model = _model;
		this.logger = _logger;
		this.portClientNode = _portClientNode;
		this.client=_client;
		
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
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						logger.error(e1);
						connectedToServer = false;
						runningThread = false;
					}
				}
			}
			try {
				this.inStream = new BufferedReader(new InputStreamReader(socketClientNode.getInputStream()));
				this.outStream = new PrintWriter(socketClientNode.getOutputStream(), true);
				while (runningThread) {
					strLine = inStream.readLine();
					logger.info("received: " + strLine);
					String args=strLine.split(":")[1].trim();
					String strCmd=strLine.split(":")[0].trim();
					Command cmd = client.getCommandRegister().getCommandByName(strCmd);
					outStream.write(cmd.execute(args) + "\n");
					outStream.flush();
				}
				inStream.close();
				outStream.close();
				socketClientNode.close();
			} catch (UnknownHostException e) {
				logger.error("Don’t know about host " + addressClientNode);
				connectedToServer = false;
				runningThread = false;
			} catch (IOException e) {
				logger.info("Lost connection to: " + addressClientNode);
				connectedToServer = false;
				runningThread = false;
			}
		}
		
		logger.info("closing client node...");
		client.closeServer();
	}

	public void close() {
		connectedToServer = false;
		runningThread = false;
	}

}
