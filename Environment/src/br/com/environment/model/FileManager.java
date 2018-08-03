package br.com.environment.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public class FileManager {
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

	public static void readFileLineByLine(String fullFileName, LineListener lineListener) {
		BufferedReader br = null;
		try {	
			File file = new File(fullFileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			br = new BufferedReader(new FileReader(fullFileName));
		    String line = br.readLine();
		    while(line != null) {
		    	Variable variable = EnvironmentManager.variableFromLine(line);
		    	
		    	if(variable != null) {	
			    	if(variable.getName().equals(EnvironmentManager.PATH)) {	
			    		Path path = new Path();
			    		path.setName(variable.getName());
			    		path.setValue(variable.getValue());
			    		path.setAdditionalPath(variable.getAdditionalPath());		    		
			    		lineListener.onPathListener(path);	
			    	} else {
			    		lineListener.onLineListener(variable);	
			    	}
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

	@SuppressWarnings("finally")
	public static String readFile(String fullFileName) {
		StringBuffer bodyOfFile = new StringBuffer();
		BufferedReader br = null;		
		try {	
			File file = new File(fullFileName);
			if(!file.exists()){
				file.createNewFile();
				return bodyOfFile.toString();
			}
			
			br = new BufferedReader(new FileReader(fullFileName));
			String line = br.readLine();
		    while(line != null) {
		    	bodyOfFile.append(line); 
		    	bodyOfFile.append(System.lineSeparator());
		        line = br.readLine();
		    }		    
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
		    try {
				br.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		    		    
		    return bodyOfFile.toString();
		}
	}
}
