package commands;

import server.Client;

public class ClientInStandByCommand implements Command {
	
	private Client client;

	public ClientInStandByCommand(Client _client) {
		this.client=_client;
	}

	@Override
	public String execute(String args) {
//		client.setClientName("Client in standby");
		client.setInStandby(true);
		client.getModel().getMvsController().getListViewClients().refresh();
		return "";
	}
}
