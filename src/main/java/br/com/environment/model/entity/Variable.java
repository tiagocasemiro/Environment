package br.com.environment.model.entity;

public class Variable {
	private String name;
	private String value;
	private String additionalPath;
	private boolean onPath = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
	public String getAdditionalPath() {
		return additionalPath;
	}
	public void setAdditionalPath(String additionalPath) {
		this.additionalPath = additionalPath;
	}		
	public Boolean isOnPath() {
		return onPath;
	}
	public void setOnPath(Boolean isOnPath) {
		this.onPath = isOnPath;
	}

	@Override
	public String toString() {
		return "Variable{" +
				"name='" + name + '\'' +
				", value='" + value + '\'' +
				", additionalPath='" + additionalPath + '\'' +
				", onPath=" + onPath +
				'}';
	}
}

/*
 * Original file
  
PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games"
LC_ALL="pt_BR.UTF-8"


 * 
 * */
 