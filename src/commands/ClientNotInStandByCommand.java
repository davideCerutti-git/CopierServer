package commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

import controller.MainViewServerController;

public class ClientNotInStandByCommand implements Command {

	@Override
	public String execute() {
		System.out.println("NOT in standby");
		return "";
	}
}
