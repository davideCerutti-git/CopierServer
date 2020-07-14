import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class ThreadedServer extends Thread {
	
	private static int port;
	private ArrayList<ServerThread> serversList = null;
	static boolean runningThreadedServer = true;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private String serverName;
	private int i=1;
	public static final Logger log = Logger.getLogger(ModelServer.class.getName());

	public ThreadedServer(int _port) {
		serversList = new ArrayList<ServerThread>();
		port=_port;
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
			server.start();
			i++;
		}
	}

}
