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

public class ServerThreadTo extends Thread {
	protected Socket sTo;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String inLine, outLine;
	private boolean runningServerThread = true;
	private String serverName;
	private Logger log;
	private BlockingQueue<String> commandsQueue;
	private ServerThreadFrom sFrom;
	private ModelServer m;
	private Client c;

	public ServerThreadTo(Socket _s, Logger log, ModelServer _m, int portServerFrom, Client _c) throws IOException {
		this.commandsQueue = new LinkedBlockingQueue<>();
		this.c = _c;
		this.m = _m;
		this.sTo = _s;
		this.serverName = null;
		this.log = log;
		this.in = new BufferedReader(new InputStreamReader(sTo.getInputStream()));
		this.out = new PrintWriter(sTo.getOutputStream(), true);
		this.sFrom = new ServerThreadFrom(sTo, m, log, portServerFrom,c);
		this.sFrom.start();
	}

	public void run() {
		try {
			while (runningServerThread) {
				// WRITE commands to Client
				outLine = commandsQueue.poll();
				if (outLine != null) {
					out.write(outLine + "\n");
					out.flush();
					log.debug("printed: " + outLine);
					outLine = null;
					// Read response from Client
					log.debug("waiting response");
					log.debug("Response: " + in.readLine());
				}
			}
		} catch (IOException e) {
			log.error(e);
			runningServerThread = false;
		}
	}

	public void close() {
		runningServerThread = false;
	}

	public Queue<String> getCommandsQueue() {
		return commandsQueue;
	}

}
