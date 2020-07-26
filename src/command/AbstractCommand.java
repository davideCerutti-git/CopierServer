package command;

import server.Client;

public abstract class AbstractCommand implements CommandInterface {

	protected Client client;

	protected AbstractCommand(Client _client) {
		this.client = _client;
	}

}
