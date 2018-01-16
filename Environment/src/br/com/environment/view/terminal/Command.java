package br.com.environment.view.terminal;

public enum Command {
	CREATE("--create"),
	DELETE("--delete"),
	CREATE_ON_PATH("--createOnPath"),
	LIST("--list"),
	VERSION("--version"),
	UPDATE("--update"),
	ADD_TO_PATH("--addToPath"),
	REMOVE_FROM_PAH("--removeFromPath"),
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
		} else if(UPDATE.value.equals(command)){
			return Command.UPDATE;
		} else if(ADD_TO_PATH.value.equals(command)){
			return Command.ADD_TO_PATH;
		} else if(REMOVE_FROM_PAH.value.equals(command)){
			return Command.REMOVE_FROM_PAH;
		}
		
		return INVALID_COMMAND;
	}
}
