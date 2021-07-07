package br.com.darkside.entity;

public class Variable {
	private String name;
	private String value;
	private String additionalPath;
	private boolean onPath = false;
	private String oldAdditionalPath;
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

	public String getOldAdditionalPath() {
		return oldAdditionalPath;
	}

	public void setOldAdditionalPath(String oldAdditionalPath) {
		this.oldAdditionalPath = oldAdditionalPath;
	}
}
