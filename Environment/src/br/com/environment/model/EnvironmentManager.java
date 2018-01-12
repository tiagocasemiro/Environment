package br.com.environment.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class EnvironmentManager {
	public static String PATH = "PATH";
	public static String SYSTEM_START_FILE = "/etc/environment";  
	public static Map<String, Variable> variables = new HashMap<String, Variable>();
	public static Path path = new Path();
	public static final String VERSION = "Environment app, Version 0.1.0";
	
	
	public static Variable variableFromLine(String line) {
		Variable variable = new Variable();
		String[] pieces = line.split("=");
		if(pieces.length == 2){			
			variable.setName(pieces[0].toUpperCase());
			variable.setValue(pieces[1].replace("\"", ""));				
		}else{
			throw new RuntimeException("Erro ao tentar carregar arquivo /etc/Environment");
		}
		
		return variable;
	}

	public static void removeVariableFromPath(Path path, Variable variable) {
		StringBuffer finalPath = new StringBuffer();		
		String name = "$" + variable.getName();		
		String nameWithAdditionalPath = "";
		
		if(variable.getAdditionalPath() != null){
			nameWithAdditionalPath = "$" + variable.getName() + File.separator + variable.getAdditionalPath();
		}
		
		for (int index = 0; index < path.getPath().size(); index++) {			
			if(!path.getPath().get(index).equals(name) && !path.getPath().get(index).equals("") && !path.getPath().get(index).equals(nameWithAdditionalPath)){
				if(index > 0){
					finalPath.append(":");
				}
				
				finalPath.append(path.getPath().get(index));				
			}	
		}		
	}

	public static void putVariableOnPath(Variable path, Variable variable) {
		String value = path.getValue();
		
		char[] additionalPath = variable.getAdditionalPath().toCharArray();
		if(additionalPath.length > 0 && additionalPath[0] == '/'){
			variable.setAdditionalPath(variable.getAdditionalPath().substring(1, variable.getAdditionalPath().length()));
		}
				
		if(variable.getAdditionalPath() != null) {
			value = value + File.pathSeparator + "$" + variable.getName() + File.separator + variable.getAdditionalPath(); 			
		} else {
			value = value + File.pathSeparator + "$" + variable.getName();			
		}
		
		path.setValue(value);
	}

	public static String montFile(Variable path, Map<String, Variable> variables) {
		StringBuffer environment = new StringBuffer();
		
		environment.append(path.getName());
	    environment.append("=");
	    environment.append("\"");
	    environment.append(path.getValue());
	    environment.append("\"");	   
						
		for(Map.Entry<String, Variable> entry : variables.entrySet()) {	
			environment.append(System.lineSeparator());
		    environment.append(entry.getKey());
		    environment.append("=");
		    environment.append("\"");
		    environment.append(entry.getValue().getValue());
		    environment.append("\"");		    
		}
		
		return environment.toString();
	}

	public static String montBashFile(Variable path, Map<String, Variable> variables) {
		StringBuffer environment = new StringBuffer();
				
		environment.append(path.getName());
	    environment.append("=");
	    environment.append("\"");
	    environment.append(path.getValue());
	    environment.append("\"");
	    environment.append(System.lineSeparator());
						
		for(Map.Entry<String, Variable> entry : variables.entrySet()) {				
		    environment.append(entry.getKey());
		    environment.append("=");
		    environment.append("\"");
		    environment.append(entry.getValue().getValue());
		    environment.append("\"");
		    environment.append(System.lineSeparator());
		}
		
		return environment.toString();
	}

	public static void generateBashProfile(Path path, Map<String, Variable> variables) throws IOException, InterruptedException {
		String bash = "/etc/bash.bashrc";
		String bashSupport = "/etc/.environment";
		
		String bashRcBody = FileManager.readFile(bash);
		String bashFile = montBashFile(path, variables);
		
		StringBuilder bashRcWithNewVariables = new StringBuilder();
		bashRcWithNewVariables.append(bashRcBody);
		bashRcWithNewVariables.append(System.lineSeparator());
		bashRcWithNewVariables.append(bashFile);	
						
		FileManager.writeFile(bash, bashRcWithNewVariables.toString());			 
		Process process = Runtime.getRuntime().exec("sudo source " + bash);         
	    process.waitFor();
	       
	    FileManager.writeFile(bash, bashRcBody);
	    FileManager.writeFile(bashSupport, bashFile);
	}

}
