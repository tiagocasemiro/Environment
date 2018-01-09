package br.com.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
	private static String PATH = "PATH";
	private static String SYSTEM_START_FILE = "/etc/environment";  
	private static Map<String, Variable> variables = new HashMap<String, Variable>();
	private static Path path = new Path();
		
	public static void main(String[] args) {
		Main.readFile(SYSTEM_START_FILE, new LineListener() {			
			@Override
			public void onLineListener(Variable variable) {									
				variables.put(variable.getName(), variable);
			}
									
			@Override
			public void onPathListener(Path path) {
				Main.path = path;
				Main.path.generatePath();
			}
						
			@Override
			public void onFinish() {
				for (Map.Entry<String, Variable> entry : variables.entrySet()) {
					for(String value: path.getPath()){
						if(value.contains(entry.getKey())){
							value = value.replace("$" + entry.getKey() + File.separator, "");
							entry.getValue().setAdditionalPath(value);
						}
					}					
				}				
			}
		});	
		
		if(args.length > 0) {
			Command command = Command.getCommand(args[0]);
			switch (command) {
			case CREATE:{
				Variable variable = new Variable();
				variable.setName(args[1].toUpperCase());
				variable.setValue(args[2]);
				
				Variable result = variables.put(variable.getName(), variable);				
				if(result == null) {
					if(args.length == 3) {					
						writeFile(SYSTEM_START_FILE, montFile(path, variables));
						try {
							generateBashProfile(path, variables);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Ocorreu um erro ao gerar /etc/.environment. Para criar as novas variáveis reinicie o computador");
						}
					} else {
						System.out.println("Parametros inválidos");
					}
				} else {
					System.out.println("Variável já existente");
				}
				break;
			}
			case DELETE:{
				Variable variable = variables.get(args[1].toUpperCase());				
				Variable result = variables.remove(args[1].toUpperCase());
				if(result != null){
					if(args.length == 2) {	
						removeVariableFromPath(path, variable);
						writeFile(SYSTEM_START_FILE, montFile(path, variables));					
					} else {
						System.out.println("Parametros inválidos");
					}
				} else {
					System.out.println("Variável não encontrada");
				}
				break;
			}
			case CREATE_ON_PATH:{
				Variable variable = new Variable();
				variable.setName(args[1].toUpperCase());
				variable.setValue(args[2]);			
				Variable result = variables.put(args[1].toUpperCase(), variable);
				if(result == null) {				
					if(args.length == 3) {
						putVariableOnPath(path, variable);
						writeFile(SYSTEM_START_FILE, montFile(path, variables));					
					} else if(args.length == 4) {	
						variable.setAdditionalPath(args[3]);
						putVariableOnPath(path, variable);
						writeFile(SYSTEM_START_FILE, montFile(path, variables));
					} else {
						System.out.println("Parametros inválidos");
					}
				} else {
					System.out.println("Variável já existente");
				}
				break;
			}	
			default:
				System.out.println("Comando inválido");
				break;
			}
		}else{
			System.out.println("Parametros inválidos");
		}
	}
	
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
		
		path.setValue(finalPath.toString());
	}
		
	public static void putVariableOnPath(Variable path, Variable variable) {
		String value = path.getValue();
		
		if(variable.getAdditionalPath() != null) {
			value = value + File.pathSeparator + "$" + variable.getName() + File.separator + variable.getAdditionalPath(); 			
		} else {
			value = value + ":$" + variable.getName();			
		}
		
		path.setValue(value);
	}
	
	public static void generateBashProfile(Path path, Map<String, Variable> variables) throws IOException, InterruptedException {
		String bash = "/etc/.environment";		
		writeFile(bash, montBashFile(path, variables));		
	}
	
	public static void writeFile(String fullFileName, String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {			
			File file = new File(fullFileName);
			if(file.exists()){
				file.delete();				
			}
			file.createNewFile();
			
			fw = new FileWriter(fullFileName);			
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String montFile(Variable path, Map<String, Variable> variables) {
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
	
	public static String montBashFile(Variable path, Map<String, Variable> variables) {
		StringBuffer environment = new StringBuffer();
		
		environment.append("export ");
		environment.append(path.getName());
	    environment.append("=");
	    environment.append("\"");
	    environment.append(path.getValue());
	    environment.append("\"");
	    environment.append(System.lineSeparator());
						
		for(Map.Entry<String, Variable> entry : variables.entrySet()) {	
			environment.append("export ");
		    environment.append(entry.getKey());
		    environment.append("=");
		    environment.append("\"");
		    environment.append(entry.getValue().getValue());
		    environment.append("\"");
		    environment.append(System.lineSeparator());
		}
		
		return environment.toString();
	}
		
	public static void readFile(String fullFileName, Main.LineListener lineListener) {
		BufferedReader br = null;
		try {	
			File file = new File(fullFileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			br = new BufferedReader(new FileReader(fullFileName));
		    String line = br.readLine();
		    while(line != null) {
		    	Variable variable = variableFromLine(line);
		    	
		    	if(variable.getName().equals(PATH)) {	
		    		Path path = new Path();
		    		path.setName(variable.getName());
		    		path.setValue(variable.getValue());
		    		path.setAdditionalPath(variable.getAdditionalPath());		    		
		    		lineListener.onPathListener(path);	
		    	} else {
		    		lineListener.onLineListener(variable);	
		    	}
		        		        
		        line = br.readLine();
		    }
		    lineListener.onFinish();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
		    try {
				br.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}		
	}
	
	public interface LineListener {
		void onPathListener(Path path);
		void onLineListener(Variable variable);
		void onFinish();
	}
			 		
	public enum Command {
		CREATE("create"),
		DELETE("delete"),
		CREATE_ON_PATH("createOnPath"),
		INVALID_COMMAND(null);
		
		private String value;
		
		Command(String command) {
			value = command;
		}
		
		public String getValue() {
			return value;
		}
		
		public static Command getCommand(String command) {
			if(CREATE.value.equals(command)) {
				return Command.CREATE;
			} else if(DELETE.value.equals(command)) {
				return Command.DELETE;
			} else if(CREATE_ON_PATH.value.equals(command)) {
				return Command.CREATE_ON_PATH;
			}
			
			return INVALID_COMMAND;
		}
	}
}
