package commands;

import command.AbstractCommand;
import server.Client;

public class SetClientNameCommand extends AbstractCommand {
	
	public SetClientNameCommand(Client _client) {
		super(_client);
	}
	
	@Override
	public String execute(String args) {
		client.setClientName(args);
		return "executed: "+client.getClientName()+" -> name setted.";
	}

}
