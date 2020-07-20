package commands;

public class ClientNotInStandByCommand implements Command {

	@Override
	public String execute() {
		System.out.println("NOT in standby");
		return "";
	}
}
