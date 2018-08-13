package br.com.environment.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.environment.model.entity.Line;
import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class EnvironmentManager {
	public static String PATH = "PATH";
	public static String BASH_PROFILE ="/Users/" + System.getProperty("user.name") + "/.bash_profile"; 
		
	public static Map<String, Variable> variables = new HashMap<String, Variable>();
	public static Path path = new Path();
	public static final String VERSION = "Environment app, Version v0.2.0";
	
	public static void removeVariableFromPath(Path path, Variable variable) {
		StringBuffer finalPath = new StringBuffer();		
		String name = "$" + variable.getName();		
		String nameWithAdditionalPath = "";
				
		if(variable.getAdditionalPath() != null){
			nameWithAdditionalPath = "$" + variable.getName() + File.separator + variable.getAdditionalPath();
		}
		
		for (int index = 0; index < path.getPath().size(); index++) {			
			if(!path.getPath().get(index).equals(name) && !path.getPath().get(index).equals("") && !path.getPath().get(index).equals(nameWithAdditionalPath) &&	!path.getPath().get(index).contains(name + File.separator)){
				if(index > 0){
					finalPath.append(":");
				}
				
				finalPath.append(path.getPath().get(index));				
			}	
		}
		
		path.setValue(finalPath.toString());		
	}

	public static void putVariableOnPath(Variable path, Variable variable) {
		String value = path.getValue();
						
		if(variable.getAdditionalPath() != null) {
			char[] additionalPath = variable.getAdditionalPath().toCharArray();
			String characterVaidation = String.valueOf(additionalPath[0]);
			
			if(additionalPath.length > 0 && characterVaidation.equals(File.separator)){
				variable.setAdditionalPath(variable.getAdditionalPath().substring(1, variable.getAdditionalPath().length()));
			}			
			value = value + File.pathSeparator + "$" + variable.getName() + File.separator + variable.getAdditionalPath(); 			
		} else {
			value = value + File.pathSeparator + "$" + variable.getName();			
		}
		
		path.setValue(value);
	}

	public static String montFile(Variable path, Map<String, Variable> variables) {
		StringBuffer environment = new StringBuffer();
		
		environment.append("export ");
		environment.append(path.getName());
	    environment.append("=");
	    environment.append("\"");
	    environment.append(path.getValue());
	    environment.append("\"");	   
						
		for(Map.Entry<String, Variable> entry : variables.entrySet()) {	
			environment.append(System.lineSeparator());
			environment.append("export ");
		    environment.append(entry.getKey());
		    environment.append("=");
		    environment.append("\"");
		    environment.append(entry.getValue().getValue());
		    environment.append("\"");		    
		}
		
		return environment.toString();
	}

	public static String montBashProfile(Variable path, Map<String, Variable> variables) {
		StringBuffer environment = new StringBuffer();
										
		for(Map.Entry<String, Variable> entry : variables.entrySet()) {		
			environment.append("export ");
		    environment.append(entry.getKey());
		    environment.append("=");
		    environment.append("\"");
		    environment.append(entry.getValue().getValue());
		    environment.append("\"");
		    environment.append(System.lineSeparator());
		}
		
		environment.append(System.lineSeparator());
		environment.append("export ");
		environment.append(path.getName());
	    environment.append("=");
	    environment.append("\"");
	    environment.append(path.getValue());
	    environment.append("\"");
	    environment.append(System.lineSeparator());
		
		return environment.toString();
	}

	public static void generateBashProfile() throws IOException, InterruptedException {	
		String bashRcBody = FileManager.readFile(BASH_PROFILE);
		String bashFile = montBashProfile(EnvironmentManager.path, EnvironmentManager.variables);
		
		StringBuilder bashRcWithNewVariables = new StringBuilder();
		bashRcWithNewVariables.append(bashRcBody);
		bashRcWithNewVariables.append(System.lineSeparator());
		bashRcWithNewVariables.append(System.lineSeparator());
		bashRcWithNewVariables.append("### DarkSide - system of mananger environment variable ###");
		bashRcWithNewVariables.append(System.lineSeparator());
		bashRcWithNewVariables.append(bashFile);	
								
		System.out.println("source " + BASH_PROFILE);
		
		FileManager.writeFile(BASH_PROFILE, bashRcWithNewVariables.toString());			 
		Process process = Runtime.getRuntime().exec("source " + BASH_PROFILE);         
	    process.waitFor();
	    	   	       
	    FileManager.writeFile(BASH_PROFILE, bashRcBody);	   
	}

	public static void init() {
		FileManager.readFileLineByLine(BASH_PROFILE, new LineListener() {		
			@Override
			public void onLineListener(String l) {
				Line line = new Line(l);
						
				if(line.getVariable() != null) {					
					EnvironmentManager.variables.put(line.getVariable().getName(), line.getVariable());
				} 	
								
				if(line.getPath() != null) {
					EnvironmentManager.path = line.getPath();	
				}
			}
									
			@Override
			public void onFinish() {
				for (Map.Entry<String, Variable> entry : EnvironmentManager.variables.entrySet()) {
					if(EnvironmentManager.path != null && EnvironmentManager.path.getPath() != null) {
						for(String value: EnvironmentManager.path.getPath()){
							if(value.contains("$" + entry.getKey() + File.separator) || value.equals("$" + entry.getKey())
									|| value.contains("${" + entry.getKey() + "}" + File.separator) || value.equals("${" + entry.getKey() + "}")){
								value = value.replace("$" + entry.getKey(), "").replace("${" + entry.getKey() + "}", "");
								
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
			}
		});		
	}	
}
