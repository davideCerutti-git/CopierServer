package commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientInStandByCommand implements Command {

	@Override
	public String execute() {
		System.out.println("in standby");
		return "";
	}
}
