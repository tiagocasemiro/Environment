package br.com.environment.controller;

import java.io.IOException;
import java.util.Map;

import br.com.environment.model.EnvironmentManager;
import br.com.environment.model.FileManager;
import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class EnvironmentControllerGraphic {
	
	public EnvironmentControllerGraphic() {
		EnvironmentManager.init();		
	}
		
	public String create(Variable variable) throws IOException, InterruptedException, Exception {
		Variable result = EnvironmentManager.variables.put(variable.getName(), variable);				
		if(result == null) {							
			FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile();
		} else {
			return "Variável já existente";
		}
		
		return null;
	}
	
	public String delete(Variable variable) throws Exception {
		Variable result = EnvironmentManager.variables.remove(variable.getName());
		if(result != null){			
			EnvironmentManager.removeVariableFromPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));					
		} else {
			return "Variável não encontrada";
		}
		return null;
	}
	
	public String createOnPath(Variable variable) throws InterruptedException, IOException, Exception {
		Variable result = EnvironmentManager.variables.put(variable.getName(), variable);
		if(result == null) {		
			EnvironmentManager.putVariableOnPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile();	
		} else {
			return "Variável já existente";
		}	
		
		return null;
	}
	
	public String update(Variable variable) throws InterruptedException, IOException, Exception {
		Variable result = EnvironmentManager.variables.remove(variable.getName());
		if(result != null){	
			result = EnvironmentManager.variables.put(variable.getName(), variable);	
			if(result == null) {
				FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
				EnvironmentManager.generateBashProfile();				
			} else {
				return "Não foi possível alterar o conteúdo da variável. Verifique se o comando SUDO foi usado corretamente";
			}			
		} else {
			return "Variável não encontrada";
		}	
		
		return null;
	}
	
	public String addToPath(Variable variable) throws InterruptedException, IOException, Exception {
		Variable result = EnvironmentManager.variables.get(variable.getName());
		
		if(result != null) {						
			EnvironmentManager.putVariableOnPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile();	
		} else {
			return "Variável não encontrada";
		}
				
		return null;
	}

	public String removeFromPath(Variable variable) throws InterruptedException, IOException, Exception {
		variable = EnvironmentManager.variables.get(variable.getName());
		
		if(variable != null) {						
			EnvironmentManager.removeVariableFromPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
		} else {
			return "Variável não encontrada";
		}
				
		return null;		
	}

	public String listToString() throws Exception {
		return EnvironmentManager.montBashProfile(EnvironmentManager.path, EnvironmentManager.variables);	
	}
	
	public Path getPath() {
		return EnvironmentManager.path;
	}
	
	public String savePath(Path path) throws IOException, InterruptedException {
		EnvironmentManager.path = path;		
		FileManager.writeFile(EnvironmentManager.BASH_PROFILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
		EnvironmentManager.generateBashProfile();
		
		return null;
	}
	
	public Map<String, Variable> list() {
		return EnvironmentManager.variables;
	}
	
	public String version() {
		return EnvironmentManager.VERSION;
	}
}
