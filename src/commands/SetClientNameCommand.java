package commands;

import server.Client;

public class SetClientNameCommand implements Command {
	
	private Client client;
	
	
	public SetClientNameCommand(Client _client) {
		super();
		this.client = _client;
	}
	
	@Override
	public String execute(String args) {
		client.setClientName(args);
		return null;
	}

}
