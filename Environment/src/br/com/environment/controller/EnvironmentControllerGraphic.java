package br.com.environment.controller;

import java.io.IOException;
import java.util.Map;

import br.com.environment.model.EnvironmentManager;
import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class EnvironmentControllerGraphic {
	
	public EnvironmentControllerGraphic() {
		EnvironmentManager.init();		
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
		return EnvironmentManager.listAllVariables();
	}
	
	public String version() {
		return EnvironmentManager.version();
	}
}
