package commands;

import command.AbstractCommand;
import server.Client;

public class ClientNotInStandByCommand extends AbstractCommand {

	public ClientNotInStandByCommand(Client _client) {
		super(_client);
	}

	@Override
	public String execute(String args) {
		client.setInStandby(false);
		client.getModel().getMvsController().getListViewClients().refresh();
		return "executed: "+client.getClientName()+" -> NOT in standby.";
	}
}
