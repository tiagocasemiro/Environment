package br.com.environment.view.terminal;

public enum Command {
	CREATE("--create"),
	DELETE("--delete"),
	CREATE_ON_PATH("--createOnPath"),
	LIST("--list"),
	VERSION("--version"),
	INVALID_COMMAND(null);
	
	private String value;
	
	Command(String command) {
		value = command;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Command getCommand(String command) {
		if(CREATE.value.equals(command)) {
			return Command.CREATE;
		} else if(DELETE.value.equals(command)) {
			return Command.DELETE;
		} else if(CREATE_ON_PATH.value.equals(command)) {
			return Command.CREATE_ON_PATH;
		} else if(LIST.value.equals(command)) {
			return Command.LIST;
		} else if(VERSION.value.equals(command)) {
			return Command.VERSION;
		}
		
		return INVALID_COMMAND;
	}
}
