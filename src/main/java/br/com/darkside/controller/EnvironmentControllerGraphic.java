package br.com.darkside.controller;



import br.com.darkside.EnvironmentManager;
import br.com.darkside.entity.Line;
import br.com.darkside.entity.Path;
import br.com.darkside.entity.Variable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentControllerGraphic {
	
	public EnvironmentControllerGraphic(String bashFile) {
		EnvironmentManager.init(bashFile);
	}
		
	public void create(Variable variable) throws IOException, InterruptedException, Exception {
		if(EnvironmentManager.createVariable(variable)) {							
			EnvironmentManager.generateBashProfile();
			EnvironmentManager.sourceBashProfile();
		} 
	}
	
	public void delete(Variable variable) throws Exception {
		if(EnvironmentManager.removeVariable(variable)){			
			EnvironmentManager.removeVariableFromPath(variable);
			EnvironmentManager.generateBashProfile();
			EnvironmentManager.sourceBashProfile();
		} 
	}
	
	public void createOnPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(EnvironmentManager.createVariable(variable)) {			
			EnvironmentManager.putVariableOnPath(variable);			
			EnvironmentManager.generateBashProfile();	
			EnvironmentManager.sourceBashProfile();
		}
	}
	
	public void update(Variable variable) throws InterruptedException, IOException, Exception {
		if(EnvironmentManager.removeVariable(variable)){			
			if(EnvironmentManager.createVariable(variable)) {				
				EnvironmentManager.generateBashProfile();	
				EnvironmentManager.sourceBashProfile();
			} 		
		} 
	}
	
	public void addToPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(EnvironmentManager.existVariableByName(variable.getName())) {
			EnvironmentManager.putVariableOnPath(variable);
			EnvironmentManager.generateBashProfile();
			EnvironmentManager.sourceBashProfile();
		}
	}

	public void removeFromPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(EnvironmentManager.existVariableByName(variable.getName())) {						
			EnvironmentManager.removeVariableFromPath(variable);
			EnvironmentManager.generateBashProfile();	
			EnvironmentManager.sourceBashProfile();
		} 	
	}
	
	public Path getPath() {
		return EnvironmentManager.getPath();
	}
	
	public String savePath(Path path) throws IOException, InterruptedException {
		EnvironmentManager.savePath(path);	
		EnvironmentManager.generateBashProfile();
		EnvironmentManager.sourceBashProfile();
		
		return null;
	}
	
	public Map<String, Variable> list() {
		Map<String, Variable> variables = new HashMap<>();
		Map<String, Line> lines = EnvironmentManager.listAllVariables();
		for(Map.Entry<String, Line> entry : lines.entrySet()) {
			if(entry.getValue().getVariable() != null) {
				variables.put(entry.getKey(), entry.getValue().getVariable());
			}
		}
		
		return variables;
	}
	
	public String version() {
		return EnvironmentManager.version();
	}
}
