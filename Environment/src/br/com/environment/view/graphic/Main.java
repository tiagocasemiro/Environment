package br.com.environment.view.graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
    //http://jfoenix.com/  >>> biblioteca visual com material design
	//https://github.com/toastkidjp/javafx_css_generator >>> gerador de css para esta biblioteca
	
	//http://code.makery.ch/library/javafx-8-tutorial/pt/part1/  >>> tutorial javafx
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = FXMLLoader.load(getClass().getResource("Principal.fxml"));
		Scene scene = new Scene(root, 700, 500);
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
}
