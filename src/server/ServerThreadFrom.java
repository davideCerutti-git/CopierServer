package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import commands.Command;

public class ServerThreadFrom extends Thread {
	protected Socket sFrom;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Logger log;
	private boolean stopThread;
	private boolean connectedToServer = false;
	private String strLine;
	private ModelServer m;
	private String serverAddress;
	private int port;
	private Client cl;

	public ServerThreadFrom(Socket sTo, ModelServer _m, Logger _log, int _port, Client client) throws IOException {
		this.serverAddress = sTo.getInetAddress().toString().split("/")[1];
		this.m = _m;
		this.log=_log;
		this.port=_port;
		this.cl=client;
	}

	public void run() {
		while (!stopThread) {
			while (!connectedToServer && !stopThread) {
				try {
					log.debug("tring to connect to sClent "+serverAddress);
					this.sFrom = new Socket(serverAddress, port);
					connectedToServer = true;
					log.debug("connected to sClent");
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
					//log.debug("received: "+strLine);
					Command c = m.getCommandRegister().getCommandByName(strLine);
					out.write(c.execute() + "\n");
					out.flush();
				}
			} catch (UnknownHostException e) {
				System.err.println("Don’t know about host " + serverAddress);
				System.exit(1);
			} catch (IOException e) {
				log.debug("Lost connection to: " + serverAddress);
				connectedToServer = false;
				stopThread=true;
			}
		}
		log.debug("EchoClient: closing...");
		out.close();
		try {
			in.close();
			sFrom.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

}
