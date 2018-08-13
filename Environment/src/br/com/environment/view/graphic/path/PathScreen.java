package br.com.environment.view.graphic.path;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import br.com.environment.controller.EnvironmentControllerGraphic;
import br.com.environment.model.entity.Path;
import br.com.environment.view.graphic.AlertUtil;
import br.com.environment.view.graphic.ApplicationEnvironment;
import br.com.environment.view.graphic.ApplicationEnvironment.Screen;

public class PathScreen implements Initializable {
	@FXML
    private TextArea value;
	
    private EnvironmentControllerGraphic controller = new EnvironmentControllerGraphic();
    private Path path;
    private String validateMessage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		path = controller.getPath();
		value.setText(path.getValue());		
	}
	
	@SuppressWarnings("static-access")
	@FXML
	void save(ActionEvent event) {
		try {
			if(validate()) {
				path.setValue(value.getText());				
				controller.savePath(path);
				ApplicationEnvironment.openScreen(Screen.MAIN);
			} else {								
				AlertUtil.title("Conteúdo do Path!").content(validateMessage).showAlertErro();
			}
		}catch(Exception e) {						
			AlertUtil.title("Erro na manipulaćão do arquivo!").content("Verifique se a aplicação possui as permissões necessárias").showAlertErro();
		}
	}
	 
	@FXML
	void back(ActionEvent event) {
		ApplicationEnvironment.openScreen(Screen.MAIN);
	}	
	
	private boolean validate(){	    	
    	if(value.getText() == null || value.getText().equals("")){
    		validateMessage = "Por segurança, \nnão é permitido apagar todo o valor do Path!";	
    		return false;
    	}
    	
    	return true;	    
	}
}
