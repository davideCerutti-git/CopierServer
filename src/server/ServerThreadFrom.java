package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

import commands.Command;

public class ServerThreadFrom extends Thread {
	protected Socket sFrom;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Logger log;
	static private boolean runningThread = true, connectedToServer = false;
	private String strLine;
	private ModelServer m;
	private String serverAddress;
	private int port;

	public ServerThreadFrom(Socket sTo, ModelServer _m, Logger _log, int _port) throws IOException {
		this.serverAddress = sTo.getInetAddress().toString().split("/")[1];
		this.m = _m;
		this.log = _log;
		this.port = _port;
	}

	public void run() {
		while (runningThread) {
			while (!connectedToServer && runningThread) {
				try {
					log.info("tring to connect to sClent " + serverAddress);
					this.sFrom = new Socket(serverAddress, port);
					connectedToServer = true;
					log.info("connected to sClient");
				} catch (IOException e) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						log.error(e1);
					}
				}
			}
			try {
				this.in = new BufferedReader(new InputStreamReader(sFrom.getInputStream()));
				this.out = new PrintWriter(sFrom.getOutputStream(), true);
			} catch (IOException e) {
				log.error(e);
			}
			try {
				while (true) {
					strLine = in.readLine();
					log.info("received: " + strLine);
					Command c = m.getCommandRegister().getCommandByName(strLine);
					out.write(c.execute() + "\n");
					out.flush();
				}
			} catch (UnknownHostException e) {
				log.error("Don’t know about host " + serverAddress);
				System.exit(1);
			} catch (IOException e) {
				log.info("Lost connection to: " + serverAddress);
				connectedToServer = false;
				runningThread = false;
			}
		}
		log.info("EchoClient: closing...");
		out.close();
		try {
			in.close();
			sFrom.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

}
