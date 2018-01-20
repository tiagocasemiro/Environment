package br.com.environment.model.entity;

import java.util.Arrays;
import java.util.List;

public class Path extends Variable{
	private List<String> path;

	public List<String> getPath() {
		return path;
	}
	
	public void setValue(String value) {
		super.setValue(value);
		generatePath();
	}
	
	private void generatePath(){
		path = Arrays.asList(getValue().split(":"));
	}
}
