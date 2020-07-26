package server;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
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


	public Client(Socket _socket, ModelServer _model, Logger _logger,int _portClientNode) {
		this.clientName="none";
		this.clientAddress="---.---.---.---";
		this.clientStatus = "none";
		this.clientStatusInteger = 0;
		this.logger=_logger;
		this.model = _model;
		this.commandsQueue = new LinkedBlockingQueue<>();
		this.setUpCommands();
		this.serverNode = new MasterServerNode(_socket, logger, model, _portClientNode,this);
		this.serverNode.start();
	}
	
	public void closeServer() {
		this.serverNode.close();
		Platform.runLater(() -> model.getClientObservableList().remove(this));
	}
	
	public void closeClient() {
		this.clientNode.close();
		Platform.runLater(() -> model.getClientObservableList().remove(this));
	}

	private void setUpCommands() {
		commandRegister = new CommandRegister();
		commandRegister.register("in standby", new ClientInStandByCommand(this));
		commandRegister.register("not in standby", new ClientNotInStandByCommand(this));
		commandRegister.register("set name", new SetClientNameCommand(this));
	}
	
	//Setters:

	public void setServerNode(MasterServerNode serverNode) {
		this.serverNode = serverNode;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public void setClientStatusInteger(int clientStatusInteger) {
		this.clientStatusInteger = clientStatusInteger;
	}

	public void setInStandby(boolean value) {
		this.inStandby = value;
	}
	
	public void setClientNode(MasterClientNode clientNode) {
		this.clientNode = clientNode;
	}

	
	//Getters:
	
	public boolean getInStandby() {
		return inStandby;
	}

	public MasterClientNode getClientNode() {
		return clientNode;
	}

	public MasterServerNode getServerNode() {
		return serverNode;
	}

	public String getClientStatus() {
		return clientStatus;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public String getClientAddress() {
		return clientAddress;
	}
	
	public int getClientStatusInteger() {
		return clientStatusInteger;
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

}
