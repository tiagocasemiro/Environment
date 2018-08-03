package br.com.environment.view.graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ApplicationEnvironment extends Application{
	private static Stage primaryStage;	
	private static Class<?> clazz;

	public static void go(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			ApplicationEnvironment.primaryStage = primaryStage;			
			primaryStage.setTitle("Gerenciador de variáveis de ambiente");									
			clazz = getClass();					
			ApplicationEnvironment.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));  
			
			
			openScreen(Screen.MAIN);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void openScreen(Screen screen) {
		try {
			switch (screen) {
			case MAIN:
				ApplicationEnvironment.primaryStage.setScene(new Scene(FXMLLoader.load(clazz.getResource("main/Main.fxml")), 750, 550));				
				ApplicationEnvironment.primaryStage.centerOnScreen();
				ApplicationEnvironment.primaryStage.show();
				break;
			case CREATE:				
				ApplicationEnvironment.primaryStage.setScene(new Scene(FXMLLoader.load(clazz.getResource("create/Create.fxml")), 750, 550));				
				ApplicationEnvironment.primaryStage.centerOnScreen();
				ApplicationEnvironment.primaryStage.show();				
				break;
			case PATH:
				ApplicationEnvironment.primaryStage.setScene(new Scene(FXMLLoader.load(clazz.getResource("path/Path.fxml")), 750, 550));				
				ApplicationEnvironment.primaryStage.centerOnScreen();
				ApplicationEnvironment.primaryStage.show();	
				break;
			default:
				ApplicationEnvironment.primaryStage.setScene(new Scene(FXMLLoader.load(clazz.getResource("main/Main.fxml")), 750, 550));				
				ApplicationEnvironment.primaryStage.centerOnScreen();
				ApplicationEnvironment.primaryStage.show();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public enum Screen{
		MAIN, CREATE, PATH;
	}
}
