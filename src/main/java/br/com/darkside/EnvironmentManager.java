package br.com.darkside;

import br.com.darkside.entity.Line;
import br.com.darkside.entity.Path;
import br.com.darkside.entity.Variable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EnvironmentManager {	
	private static String BASH_PROFILE ="/home/" + System.getProperty("user.name") + "/.bash_profile";
	private static final String VERSION = "Environment app, Version v0.9.0";
	private static Map<String, Line> lines;
	private static Path path;

	public static void init(String bashFile) {
		lines = new HashMap();
		path = new Path();
		if(bashFile != null) {
			BASH_PROFILE = bashFile;
		}

		FileManager.readFileLineByLine(BASH_PROFILE, new LineListener() {
			int numberOfLine = 0;
			@Override
			public void onLineListener(String l) {
				if(l.startsWith("export")) {
					Line line = new Line(l);
					if(line.getPath() != null) {
						path = line.getPath();
					} else {
						line.setNumberOfLine(numberOfLine);
						if(line.getVariable() != null) {
							lines.put(line.getVariable().getName(), line);
						} else {
							lines.put(line.getContent(), line);
						}
						numberOfLine++;
					}
				}
			}
									
			@Override
			public void onFinish() {
				for (Map.Entry<String, Line> entry : lines.entrySet()) {
					if(path != null && path.getPath() != null) {
						for(String value: path.getPath()){
							if(entry.getValue().getVariable() != null) {							
								if(value.contains("$" + entry.getKey() + File.separator) || value.equals("$" + entry.getKey())
										|| value.contains("${" + entry.getKey() + "}" + File.separator) || value.equals("${" + entry.getKey() + "}")){
									value = value.replace("$" + entry.getKey(), "").replace("${" + entry.getKey() + "}", "");								
									
									if(value.startsWith("/")){
										value = value.substring(1, value.length());
									}
									
									if(!"".equals(value)){
										entry.getValue().getVariable().setAdditionalPath(value);
									}
									entry.getValue().getVariable().setOnPath(true);
								}	
							}						
						}
					}
				}				
			}
		});		
	}	
	
	public static void removeVariableFromPath(Variable variable) {
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

	public static void putVariableOnPath(Variable variable) {
		String value = path.getValue();
						
		if(variable.getAdditionalPath() != null) {
			char[] additionalPath = variable.getAdditionalPath().toCharArray();
			String characterValidation = String.valueOf(additionalPath[0]);
			
			if(additionalPath.length > 0 && characterValidation.equals(File.separator)){
				variable.setAdditionalPath(variable.getAdditionalPath().substring(1, variable.getAdditionalPath().length()));
			}			
			value = value + File.pathSeparator + "$" + variable.getName() + File.separator + variable.getAdditionalPath(); 			
		} else {
			value = value + File.pathSeparator + "$" + variable.getName();			
		}
		
		path.setValue(value);	
	}
	
	public static void generateBashProfile() throws IOException, InterruptedException {	
		String backupBashProfile = FileManager.readFile(BASH_PROFILE);
		FileManager.writeFileWithoutDelete(BASH_PROFILE + "_old", backupBashProfile);
		
		String bashFile = montBashProfile();		
		StringBuilder bashRcWithNewVariables = new StringBuilder();
		if(!bashFile.contains("### DarkSide - system of mananger environment variable ###")) {
			bashRcWithNewVariables.append(System.lineSeparator());
			bashRcWithNewVariables.append("##########################################################");
			bashRcWithNewVariables.append(System.lineSeparator());
			bashRcWithNewVariables.append("### DarkSide - system of mananger environment variable ###");
			bashRcWithNewVariables.append(System.lineSeparator());
			bashRcWithNewVariables.append("##########################################################");
			bashRcWithNewVariables.append(System.lineSeparator());
			bashRcWithNewVariables.append(System.lineSeparator());
		}
		bashRcWithNewVariables.append(bashFile);
				
		FileManager.writeFile(BASH_PROFILE, bashRcWithNewVariables.toString());
	}
	
	public static void sourceBashProfile() throws IOException, InterruptedException {
		try {
			final String command = "source " + BASH_PROFILE;
			System.out.println(command);
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public static boolean createVariable(Variable variable) {
		Line newLine = new Line(); 
		newLine.setNumberOfLine(lines.size());
		newLine.setVariable(variable);
				
		return lines.put(variable.getName(), newLine) == null;	
	} 
	
	public static boolean removeVariable(Variable variable) {
		return lines.remove(variable.getName()) != null;			
	} 
	
	public static boolean existVariableByName(String name) {
		return lines.get(name) != null;
	}
	
	public static Path getPath() {
		return path;
	}
	
	public static void savePath(Path p) {
		path = p;
	}
	
	public static Map<String, Line> listAllVariables() {
		return lines;
	}
	
	public static String version() {
		return VERSION;
	}
	
	private static String montBashProfile() {
		StringBuffer environment = new StringBuffer();			
		LinkedHashSet<Line> noDuplicateLines = new LinkedHashSet<Line>(lines.values());
		List<Line> ordenedLines = new ArrayList<Line>(noDuplicateLines);	
		Collections.sort(ordenedLines);		
		
		for (Line line: ordenedLines) {
			if(line.getVariable() != null) {			
				environment.append("export ");
			    environment.append(line.getVariable().getName());
			    environment.append("=");
			    environment.append("\"");
			    environment.append(line.getVariable().getValue());
			    environment.append("\"");
			    environment.append(System.lineSeparator());
		    } else {
		    	environment.append(line.getContent());
		    	environment.append(System.lineSeparator());
		    }
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
}
