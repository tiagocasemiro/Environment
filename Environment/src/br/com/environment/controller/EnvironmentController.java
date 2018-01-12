package br.com.environment.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import br.com.environment.model.EnvironmentManager;
import br.com.environment.model.FileManager;
import br.com.environment.model.LineListener;
import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class EnvironmentController {
	
	public EnvironmentController() {
		init();
	}
	
	private void init() {
		FileManager.readFileLineByLine(EnvironmentManager.SYSTEM_START_FILE, new LineListener() {
			@Override
			public void onLineListener(Variable variable) {									
				EnvironmentManager.variables.put(variable.getName(), variable);
			}
									
			@Override
			public void onPathListener(Path path) {
				EnvironmentManager.path = path;
				EnvironmentManager.path.generatePath();
			}
						
			@Override
			public void onFinish() {
				for (Map.Entry<String, Variable> entry : EnvironmentManager.variables.entrySet()) {
					for(String value: EnvironmentManager.path.getPath()){
						if(value.contains(entry.getKey())){
							value = value.replace("$" + entry.getKey() + File.separator, "");
							entry.getValue().setAdditionalPath(value);
						}
					}					
				}				
			}
		});			
	}

	public String create(Variable variable) throws IOException, InterruptedException, Exception {
		Variable result = EnvironmentManager.variables.put(variable.getName(), variable);				
		if(result == null) {							
			FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile(EnvironmentManager.path, EnvironmentManager.variables);
		} else {
			return "Variável já existente";
		}
		
		return null;
	}
	
	public String delete(Variable variable) throws Exception {
		Variable result = EnvironmentManager.variables.remove(variable.getName().toUpperCase());
		if(result != null){			
			EnvironmentManager.removeVariableFromPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));					
		} else {
			return "Variável não encontrada";
		}
		return null;
	}
	
	public String createOnPath(Variable variable) throws InterruptedException, IOException, Exception {
		Variable result = EnvironmentManager.variables.put(variable.getName(), variable);
		if(result == null) {		
			EnvironmentManager.putVariableOnPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile(EnvironmentManager.path, EnvironmentManager.variables);	
		} else {
			return "Variável já existente";
		}	
		
		return null;
	}

	public String list() throws Exception{
		return EnvironmentManager.montBashFile(EnvironmentManager.path, EnvironmentManager.variables);	
	}
	
	public String version(){
		return EnvironmentManager.VERSION;
	}
}
