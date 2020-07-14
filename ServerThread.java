import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServerThread extends Thread {
	protected Socket socket;
	private InputStream inp = null;
	private BufferedReader brinp = null;
	private DataOutputStream out = null;
	private String line;
	private boolean runningServerThread=true;
	private String serverName;
	private Logger log;

    public ServerThread(Socket clientSocket, String _serverName, Logger _log) {
        this.socket = clientSocket;
        this.serverName=_serverName;
        log=_log;
    }

    public void run() {
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        	log.debug(e);
        }
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        while (runningServerThread) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	log.debug("Recieved: "+ line);
//                    out.writeBytes(line + "\n\r");
//                    out.flush();
                }
            } catch (IOException e) {
               // e.printStackTrace();
                log.debug("Connection lost with: "+ serverName);
                runningServerThread=false;
                //return;
            }
        }
    }
}
