package commands;

import server.Client;

public class ClientNotInStandByCommand implements Command {
	
	private Client client;

	public ClientNotInStandByCommand(Client _client) {
		this.client=_client;
	}

	@Override
	public String execute() {
		client.setClientName("Client NOT in standby");
		client.getModel().getMvsController().getListViewClients().refresh();
		return "";
	}
}
