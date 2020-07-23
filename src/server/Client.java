package server;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import commands.ClientInStandByCommand;
import commands.ClientNotInStandByCommand;
import commands.CommandRegister;
import master.MasterServerNode;

public class Client {

	private String clientName;
	private String clientStatus;
	private String clientAddress;
	private int clientStatusInteger;
	private MasterServerNode sTh;
	private BlockingQueue<String> commandsQueue;
	private CommandRegister commandRegister ;
	private ModelServer model;

	public Client() {
		this(null,null,null);
	}

	public Client(String address, MasterServerNode server, ModelServer _model) {
		this.commandsQueue = new LinkedBlockingQueue<>();
		model=_model;
		clientName ="Client NOT in standbyy";
		clientAddress = address;
		clientStatus = "none";
		clientStatusInteger = 0;
		setUpCommands();
		sTh=server;
	}
	
	private void setUpCommands() {
		commandRegister= new CommandRegister();
		commandRegister.register("in standby", new ClientInStandByCommand(this));
		commandRegister.register("not in standby", new ClientNotInStandByCommand(this));
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

}
