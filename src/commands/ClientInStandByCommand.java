package commands;

public class ClientInStandByCommand implements Command {

	@Override
	public String execute() {
		System.out.println("in standby");
		return "";
	}
}
