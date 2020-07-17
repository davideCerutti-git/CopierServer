package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public class ServerThread extends Thread {
	protected Socket socket;
	private BufferedReader in = null;
	private DataOutputStream out = null;
	private String inLine, outLine;
	private boolean runningServerThread = true;
	private String serverName;
	private Logger log;
	private Queue<String> commandsQueue;

	
	public ServerThread(Socket clientSocket, String _serverName, Logger log) throws SocketException {
		this.commandsQueue = new LinkedList<>();
		this.socket = clientSocket;
		this.serverName = _serverName;
		this.log = log;
		this.socket.setSoTimeout(3*1000);
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			log.error(e);
		}
		try {
			// TODO necessario uno sleep?
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			log.error(e);
		}
		while (runningServerThread) {

			// WRITE commands to Client
			outLine = commandsQueue.poll();
			if (outLine != null) {
				try {
					out.writeBytes(outLine);
				} catch (IOException e) {
					log.error(e);
				}
				//Read response from Client
				try {
					inLine = in.readLine();
					executeCommand(inLine);
				} catch (SocketTimeoutException e) {
					log.error("SocketTimeout Exception: command "+"\""+outLine+"\""+" without response");
					commandsQueue.add(outLine);
				}catch (IOException e) {
					log.error(e);
				}
				
			}
		}
	}

	private boolean executeCommand(String responseString) {
		//Example command:
		// echo: unknown command
		log.debug(responseString);
		
		switch (responseString.split(":")[1].trim()) {
		
		case "done":
			log.debug("command executed");
			return true;
		
		case "error":
			log.error("command failed");
			return false;
		
		case "unknown command":
			log.error("unknown command");
			return false;
				
		default:
			log.error("unknown response");
		}
		return false;
	}

	public void close() {
		runningServerThread = false;
	}
	
	public Queue<String> getCommandsQueue() {
		return commandsQueue;
	}


}
