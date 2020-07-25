package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketServer;

import commands.ClientInStandByCommand;
import commands.ClientNotInStandByCommand;
import commands.CommandRegister;
import commands.SetClientNameCommand;
import javafx.application.Platform;
import master.MasterClientNode;
import master.MasterServerNode;

public class Client {

	private String clientName;
	private String clientStatus;
	private String clientAddress;
	private boolean inStandby;
	private int clientStatusInteger;
	private MasterServerNode sTh;
	private BlockingQueue<String> commandsQueue;
	private CommandRegister commandRegister;
	private ModelServer model;
	private MasterServerNode serverNode;
	private MasterClientNode clientNode;
	private Logger logger;
//	private int portClientNode;

//	public Client() {
//		this(null, null, null,0);
//	}

//	public Client(ServerSocket socketServerNode, ModelServer _model,Logger _logger, int _portClientNode) {
//		try {
//			portClientNode=_portClientNode;
//			logger=_logger;
//			model = _model;
//			this.commandsQueue = new LinkedBlockingQueue<>();
//			clientStatus = "none";
//			clientStatusInteger = 0;
//			setUpCommands();
//			Socket socket = socketServerNode.accept();
//			clientAddress = socket.getInetAddress().toString();
//			serverNode = new MasterServerNode(socket, logger, model, portClientNode, this);
//			Platform.runLater(() -> model.getClientObservableList().add(this));
//			serverNode.start();
//		} catch (IOException e) {
//			logger.error(e);
//		}
//	}

	public Client(Socket _socket, ModelServer _model, Logger _logger,int _portClientNode) {
		clientStatus = "none";
		clientStatusInteger = 0;
		logger=_logger;
		model = _model;
		this.commandsQueue = new LinkedBlockingQueue<>();
		setUpCommands();
		
		this.serverNode = new MasterServerNode(_socket, logger, model, _portClientNode,this);
		this.serverNode.start();
	}

	private void setUpCommands() {
		commandRegister = new CommandRegister();
		commandRegister.register("in standby", new ClientInStandByCommand(this));
		commandRegister.register("not in standby", new ClientNotInStandByCommand(this));
		commandRegister.register("set name", new SetClientNameCommand(this));
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

	public CommandRegister getCommandRegister() {
		return commandRegister;
	}

	public ModelServer getModel() {
		return model;

	}

	public void setInStandby(boolean value) {
		this.inStandby = value;
	}

	public boolean getInStandby() {
		return inStandby;
	}

	public MasterClientNode getClientNode() {
		return clientNode;
	}

	public void setClientNode(MasterClientNode clientNode) {
		this.clientNode = clientNode;
	}

	public void closeClient() {
		this.clientNode.close();
		Platform.runLater(() -> model.getClientObservableList().remove(this));
	}

	public MasterServerNode getServerNode() {
		return serverNode;
	}

	public void setServerNode(MasterServerNode serverNode) {
		this.serverNode = serverNode;
	}

	public void closeServer() {
		this.serverNode.close();
		Platform.runLater(() -> model.getClientObservableList().remove(this));
	}
	
	

	
}
