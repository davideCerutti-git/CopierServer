package server;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import master.MasterServerNode;

public class Client {

	private String clientName;
	private String clientStatus;
	private String clientAddress;
	private int clientStatusInteger;
	private MasterServerNode sTh;
	private BlockingQueue<String> commandsQueue;

	public Client() {
		this(null,null);
	}

	public Client(String address, MasterServerNode server) {
		this.commandsQueue = new LinkedBlockingQueue<>();
		clientName = null;
		clientAddress = address;
		clientStatus = "none";
		clientStatusInteger = 0;
		sTh=server;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getClientStatusInteger() {
		return clientStatusInteger;
	}

	public void setClientStatusInteger(int clientStatusInteger) {
		this.clientStatusInteger = clientStatusInteger;
	}
	
	public MasterServerNode getsTh() {
		return sTh;
	}
	public Queue<String> getCommandsQueue() {
		return commandsQueue;
	}

}
