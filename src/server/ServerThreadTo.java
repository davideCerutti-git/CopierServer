package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class ServerThreadTo extends Thread {
	protected Socket socketTo;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String outLine;
	static private boolean runningThread = true;
	private Logger log;
	private BlockingQueue<String> commandsQueue;
	private ServerThreadFrom sFrom;
	private ModelServer model;

	public ServerThreadTo(Socket _socket, Logger log, ModelServer _model, int portServerFrom) throws IOException {
		this.commandsQueue = new LinkedBlockingQueue<>();
		this.model = _model;
		this.socketTo = _socket;
		this.log = log;
		this.in = new BufferedReader(new InputStreamReader(socketTo.getInputStream()));
		this.out = new PrintWriter(socketTo.getOutputStream(), true);
		this.sFrom = new ServerThreadFrom(socketTo, model, log, portServerFrom);
		this.sFrom.start();
	}

	public void run() {
		try {
			while (runningThread) {
				// WRITE commands to Client
				outLine = commandsQueue.poll();
				if (outLine != null) {
					out.write(outLine + "\n");
					out.flush();
					log.info("printed: " + outLine);
					// Read response from Client
					log.info("waiting response");
					log.info("Response: " + in.readLine());
					outLine = null;
				}
			}
		} catch (IOException e) {
			log.error(e);
			runningThread = false;
		}
	}

	public void close() {
		runningThread = false;
	}

	public Queue<String> getCommandsQueue() {
		return commandsQueue;
	}

}
