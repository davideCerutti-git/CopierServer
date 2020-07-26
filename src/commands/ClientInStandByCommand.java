package commands;

import command.AbstractCommand;
import server.Client;

public class ClientInStandByCommand extends AbstractCommand {

	public ClientInStandByCommand(Client _client) {
		super(_client);
	}

	@Override
	public String execute(String args) {
		client.setInStandby(true);
		client.getModel().getMvsController().getListViewClients().refresh();
		return "executed: "+client.getClientName()+" -> in standby.";
	}
}
