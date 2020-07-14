package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import controller.MainViewServerController;


public class ThreadedServer extends Thread {
	
	private static int port;
	private ArrayList<ServerThread> serversList = null;
	static boolean runningThreadedServer = true;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private String serverName;
	private int i=1;
	public static final Logger log = Logger.getLogger(ModelServer.class.getName());
	private MainViewServerController mvsController;

	public MainViewServerController getMvsController() {
		return mvsController;
	}

	public ThreadedServer(int _port, MainViewServerController _mvsController) {
		serversList = new ArrayList<ServerThread>();
		port=_port;
		mvsController=_mvsController;
	}

	@Override
	public void run() {
		log.debug("ThreadedServer started, waiting for clients...");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (runningThreadedServer) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}
			// new thread for a client
			serverName="Client_"+i;
			ServerThread server = new ServerThread(socket, serverName, log);
			log.debug("Client_"+i+" connected");
			serversList.add(server);
			//update observable list in the controller
			String prefixClient="";
			if(i>9)
			prefixClient="Client";
			else
				prefixClient="Client0";
			mvsController.getClientObservableList().add(new Client(prefixClient+i,socket.getInetAddress().toString()));
			
			server.start();
			i++;
		}
	}

	public void close() throws InterruptedException {
		for(ServerThread s: serversList) {
			s.join(1); 
		}
		runningThreadedServer=false;
		
	}

}
