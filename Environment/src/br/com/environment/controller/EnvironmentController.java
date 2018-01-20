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
			}
						
			@Override
			public void onFinish() {
				for (Map.Entry<String, Variable> entry : EnvironmentManager.variables.entrySet()) {
					for(String value: EnvironmentManager.path.getPath()){
						if(value.contains("$" + entry.getKey() + File.separator) || value.equals("$" + entry.getKey())){
							value = value.replace("$" + entry.getKey(), "");
							
							if(value.startsWith("/")){
								value = value.substring(1, value.length());
							}
							
							if(!"".equals(value)){
								entry.getValue().setAdditionalPath(value);
							}
							entry.getValue().setOnPath(true);
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
		Variable result = EnvironmentManager.variables.remove(variable.getName());
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
	
	public String update(Variable variable) throws InterruptedException, IOException, Exception {
		Variable result = EnvironmentManager.variables.remove(variable.getName());
		if(result != null){	
			result = EnvironmentManager.variables.put(variable.getName(), variable);	
			if(result == null) {
				FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
				EnvironmentManager.generateBashProfile(EnvironmentManager.path, EnvironmentManager.variables);				
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
			FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));			
			EnvironmentManager.generateBashProfile(EnvironmentManager.path, EnvironmentManager.variables);	
		} else {
			return "Variável não encontrada";
		}
				
		return null;
	}

	public String removeFromPath(Variable variable) throws InterruptedException, IOException, Exception {
		variable = EnvironmentManager.variables.get(variable.getName());
		
		if(variable != null) {						
			EnvironmentManager.removeVariableFromPath(EnvironmentManager.path, variable);
			FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
		} else {
			return "Variável não encontrada";
		}
				
		return null;		
	}

	public String listToString() throws Exception {
		return EnvironmentManager.montBashFile(EnvironmentManager.path, EnvironmentManager.variables);	
	}
	
	public Path getPath() {
		return EnvironmentManager.path;
	}
	
	public String savePath(Path path) throws IOException, InterruptedException {
		EnvironmentManager.path = path;		
		FileManager.writeFile(EnvironmentManager.SYSTEM_START_FILE, EnvironmentManager.montFile(EnvironmentManager.path, EnvironmentManager.variables));
		EnvironmentManager.generateBashProfile(EnvironmentManager.path, EnvironmentManager.variables);
		
		return null;
	}
	
	public Map<String, Variable> list() {
		return EnvironmentManager.variables;
	}
	
	public String version() {
		return EnvironmentManager.VERSION;
	}
}
