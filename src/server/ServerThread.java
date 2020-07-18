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
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class ServerThread extends Thread {
	protected Socket socketSender;
	private BufferedReader inSender = null;
	private PrintWriter outSender = null;
	protected Socket socketReceiver;
	private BufferedReader inReceiver = null;
	private PrintWriter outReceiver = null;
	private String inLine, outLine;
	private boolean runningServerThread = true;
	private String serverName;
	private Logger log;
	private BlockingQueue<String> commandsQueue;

	public ServerThread(Socket s, Logger log) throws IOException {
		this.commandsQueue = new LinkedBlockingQueue<>();
		this.socketReceiver = s;
		this.serverName = null;
		this.log = log;
		inReceiver = new BufferedReader(new InputStreamReader(socketReceiver.getInputStream()));
		outSender = new PrintWriter(socketReceiver.getOutputStream(),true);
	}

	public void run() {
		try {
			

			while (runningServerThread) {
				// WRITE commands to Client
				outLine=commandsQueue.poll();
				if (outLine != null) {
					outSender.write(outLine+"\n");
					outSender.flush();
					log.debug("printed: " + outLine);
					outLine = null;
					// Read response from Client
					log.debug("waiting response");
					log.debug("Response: "+inReceiver.readLine());
				}
			}
		} catch (IOException e) {
			log.error(e);
		}
	}

	public void close() {
		runningServerThread = false;
	}

	public Queue<String> getCommandsQueue() {
		return commandsQueue;
	}

}
