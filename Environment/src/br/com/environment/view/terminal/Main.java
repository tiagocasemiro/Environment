package br.com.environment.view.terminal;

import java.io.IOException;

import br.com.environment.controller.EnvironmentController;
import br.com.environment.model.EnvironmentManager;
import br.com.environment.model.entity.Variable;

public class Main {
	private static EnvironmentController controller = new EnvironmentController(); 
		
	public static void main(String[] args) {
		if(args.length > 0) {
			Command command = Command.getCommand(args[0]);
			switch (command) {
			case CREATE: {		
				if(args.length == 3) {		
					Variable variable = new Variable();
					variable.setName(args[1].toUpperCase());
					variable.setValue(args[2]);
					
					try {
						printOut(controller.create(variable));						
					} catch (IOException e) {
						printOut("Ocorreu um erro ao gerar /etc/.environment. Para criar as novas variáveis reinicie o computador");
					} catch (InterruptedException e) {
						printOut("Ocorreu um erro ao gerar /etc/.environment. Para criar as novas variáveis reinicie o computador");
					} catch(Exception e){
						printOut("Ocorreu um erro inesperado. Verifique os arquivos envolvidos na operação");
					} 
				} else {
					printOut("Número de parametros inválidos");
				}
								
				break;
			}
			case DELETE:{								
				if(args.length == 2) {	
					Variable variable = EnvironmentManager.variables.get(args[1].toUpperCase());	
					try {
						printOut(controller.delete(variable));
					} catch(Exception e){
						printOut("Ocorreu um erro inesperado. Verifique os arquivos envolvidos na operação");
					}  
				} else {
					printOut("Parametros inválidos");
				}
				
				break;
			}
			case CREATE_ON_PATH:{
				Variable variable = new Variable();
				variable.setName(args[1].toUpperCase());
				variable.setValue(args[2]);			
									
				if(args.length >= 3 && args.length <= 4 ) {					
					if(args.length == 4){	
						variable.setAdditionalPath(args[3]);
					}
					try{
						printOut(controller.createOnPath(variable));						
					} catch(IOException e) {	
						printOut("Ocorreu um erro ao gerar /etc/.environment. Para criar as novas variáveis reinicie o computador");
					} catch(InterruptedException e) {	
						printOut("Ocorreu um erro ao gerar /etc/.environment. Para criar as novas variáveis reinicie o computador");
					} catch(Exception e) {
						printOut("Ocorreu um erro inesperado. Verifique os arquivos envolvidos na operação");
					}
				} else {
					printOut("Parametros inválidos");
				}
				
				break;
			}
			case LIST:{		
				try {
					printOut(controller.list());					
				} catch(Exception e) {
					printOut("Ocorreu um erro inesperado. Verifique os arquivos envolvidos na operação");
				}
				break;
			}
			case VERSION:{				
				printOut(controller.version());				
				break;
			}
			default:
				printOut("Comando inválido");
				break;
			}
		}else{
			printOut("Erro, não foi passado parametro de comando.");
		}
	}		
	
	private static void printOut(String content){
		if(content != null && !content.equals("")){
			printOut(content);
		}
	}
}
