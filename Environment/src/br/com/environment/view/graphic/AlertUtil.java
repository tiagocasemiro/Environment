package br.com.environment.view.graphic;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.util.Pair;

public class AlertUtil {
	private static AlertUtil instance = new AlertUtil();	
	private String title = "";
	private  String content = "";
	private static Class<?> clazz;
	
	
	
	public AlertUtil() {
		clazz = getClass();
	}

	public static AlertUtil title(String title){
		instance.title = title;
		return instance;
	}
	
	public static AlertUtil content(String content){
		instance.content = content;
		return instance;
	}
	
	public static void showAlertErro(){
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(instance.title);		
		dialog.setContentText(instance.content);	
		
		Label content = new Label();
		content.setText(instance.content);
		content.setWrapText(true);				
		dialog.getDialogPane().setContent(content);
					
		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton);
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(clazz.getResource("Alert.css").toExternalForm());
		dialogPane.setPrefSize(400, 200);
		
		dialog.showAndWait();		
	}
	
	
}
