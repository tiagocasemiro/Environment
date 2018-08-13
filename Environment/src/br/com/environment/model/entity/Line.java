package br.com.environment.model.entity;

import br.com.environment.model.EnvironmentManager;

public class Line {
	private final String content;
	private Path path;
	private Variable variable;
		
	public Line(String content) {
		super();
		this.content = content;
		
		Variable variable = new Variable();
		String[] pieces = content.split("=");
		if(pieces.length == 2){
			variable.setName(pieces[0].replace("export", "").replaceAll(" ", ""));
			variable.setValue(pieces[1].replace("\"", ""));		
			if(variable.getName().equals(EnvironmentManager.PATH)) {
				Path path = new Path();
	    		path.setName(variable.getName());
	    		path.setValue(variable.getValue());
	    		path.setAdditionalPath(variable.getAdditionalPath());		    		
	    		this.path = path;	    			
			} else {
				this.variable = variable;
			}
		}
	}

	public String getContent() {
		return content;
	}

	public Path getPath() {
		return path;
	}

	public Variable getVariable() {
		return variable;
	}		
}