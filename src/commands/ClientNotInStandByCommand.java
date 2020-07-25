package commands;

import server.Client;

public class ClientNotInStandByCommand implements Command {
	
	private Client client;

	public ClientNotInStandByCommand(Client _client) {
		this.client=_client;
	}

	@Override
	public String execute(String args) {
//		client.setClientName("Client NOT in standby");
		client.setInStandby(false);
		client.getModel().getMvsController().getListViewClients().refresh();
		return "";
	}
}
