package br.com.environment.controller;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import static br.com.environment.model.EnvironmentManager.*;

import br.com.environment.model.EnvironmentManager;
import br.com.environment.model.entity.Variable;
import br.com.environment.model.entity.Line;
import br.com.environment.model.entity.Path;

public class EnvironmentControllerGraphic {
	
	public EnvironmentControllerGraphic(String bashFile) {
		init(bashFile);
	}
		
	public void create(Variable variable) throws IOException, InterruptedException, Exception {
		if(createVariable(variable)) {
			generateBashProfile();
			sourceBashProfile();
		} 
	}
	
	public void delete(Variable variable) throws Exception {
		if(removeVariable(variable)){
			removeVariableFromPath(variable);
			generateBashProfile();
			sourceBashProfile();
		} 
	}
	
	public void createOnPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(createVariable(variable)) {
			putVariableOnPath(variable);
			generateBashProfile();
			sourceBashProfile();
		}
	}
	
	public void update(Variable variable) throws InterruptedException, IOException, Exception {
		if(removeVariable(variable)){
			if(createVariable(variable)) {
				generateBashProfile();
				sourceBashProfile();
			} 		
		} 
	}
	
	public void addToPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(existVariableByName(variable.getName())) {
			putVariableOnPath(variable);
			generateBashProfile();
			sourceBashProfile();
		}
	}

	public void removeFromPath(Variable variable) throws InterruptedException, IOException, Exception {
		if(existVariableByName(variable.getName())) {
			removeVariableFromPath(variable);
			generateBashProfile();
			sourceBashProfile();
		} 	
	}
	
	public Path getPath() {
		return EnvironmentManager.getPath();
	}
	
	public String savePath(Path path) throws IOException, InterruptedException {
		EnvironmentManager.savePath(path);
		generateBashProfile();
		sourceBashProfile();
		
		return null;
	}
	
	public Map<String, Variable> list() {
		Map<String, Variable> variables = new HashMap<>();
		Map<String, Line> lines = listAllVariables();
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
