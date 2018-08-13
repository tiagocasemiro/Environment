package br.com.environment.view.graphic.create;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import br.com.environment.controller.EnvironmentControllerGraphic;
import br.com.environment.model.entity.Variable;
import br.com.environment.view.graphic.AlertUtil;
import br.com.environment.view.graphic.ApplicationEnvironment;
import br.com.environment.view.graphic.ApplicationEnvironment.Screen;

public class CreateScreen implements Initializable{	
    @FXML
    private TextField name;
    @FXML
    private TextArea value;
    @FXML
    private CheckBox addToPath;
    @FXML
    private TextField complement;
    @FXML
    private Label variableOnPathResult;   
    @FXML
    private Label labelOnPath;
    @FXML
    private Label labelAddToPath;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    
    private EnvironmentControllerGraphic controller = new EnvironmentControllerGraphic();  
    private Variable variable = null;
    private String validateMessage = "";
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		labelAddToPath.setVisible(false);
		labelOnPath.setVisible(false);
		variableOnPathResult.setVisible(false);
		complement.setVisible(false);   
		
    	name.textProperty().addListener((observableTextField, oldValueTextField, newValueTextField) -> {    		
    		newValueTextField = newValueTextField.toUpperCase();
    		name.setText(newValueTextField);    		    		
    		if(addToPath.isSelected()) {
	    		if(complement.getText() != null && !"".equals(complement.getText())) {
	        		if(complement.getText().startsWith("/")) {
	        			variableOnPathResult.setText(":$" + newValueTextField + complement.getText());
	        		} else {
	        			variableOnPathResult.setText(":$" + newValueTextField + File.separator + complement.getText());
	
	        		}		        		
			    } else {
			    	variableOnPathResult.setText(":$" + newValueTextField);
			    }
    		}
		});	
    	    	
    	complement.textProperty().addListener((observableTextField, oldValueTextField, newValueTextField) -> {	
    		if("/".equals(newValueTextField)){
        		complement.setText("");
        		newValueTextField = "";
        	}
    		
        	if(newValueTextField != null && !"".equals(newValueTextField)) {
        		if(newValueTextField.startsWith("/")) {
        			variableOnPathResult.setText(":$" + name.getText() + newValueTextField);
        		} else {
        			variableOnPathResult.setText(":$" + name.getText() + File.separator + newValueTextField);

        		}		        		
		    } else {
		    	variableOnPathResult.setText(":$" + name.getText());
		    }
		});		
    	
		addToPath.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue){		    		
		    		labelAddToPath.setVisible(true);
		    		labelOnPath.setVisible(true);
		    		variableOnPathResult.setVisible(true);
		    		complement.setVisible(true);
		    		
		    		if(complement.getText().equals("")) {
		    			variableOnPathResult.setText(":$" + name.getText());
		    		} else {
		    			variableOnPathResult.setText(":$" + name.getText() + File.separator + complement.getText());
		    		}
		    	} else {		    		
		    		labelAddToPath.setVisible(false);
		    		labelOnPath.setVisible(false);
		    		variableOnPathResult.setVisible(false);
		    		complement.setVisible(false);
		    	}
		    }
		});
    }    
    
    @FXML
    void cancel(ActionEvent event) {
    	ApplicationEnvironment.openScreen(Screen.MAIN);
    }
        
    @SuppressWarnings("static-access")
	@FXML
    void save(ActionEvent event) {
		try {
			if(valedate()) {
				loadVariable();	    			
				controller.create(variable);					
				if(variable.isOnPath()) {
					controller.addToPath(variable);
				}
				ApplicationEnvironment.openScreen(Screen.MAIN);
			} else {							
				AlertUtil.title("Erro na validação").content(validateMessage).showAlertErro();
			}
		} catch (Exception e) {					
			e.printStackTrace();
		}
    }
    
    private boolean valedate() {
    	if(name.getText() == null || name.getText().equals("")){
    		validateMessage = "Campo nome deve ser preenchido!";	
    		return false;
    	}
    	if(value.getText() == null || value.getText().equals("")){
    		validateMessage = "Campo valor deve ser preenchido!";	
    		return false;
    	}
    	return true;
    }
      			
    private void loadVariable() {
    	variable = new Variable();    	
    	variable.setName(name.getText());
    	variable.setValue(value.getText());
    	variable.setOnPath(addToPath.isSelected());
    	if(addToPath.isSelected() && complement.getText() != null && !"".equals(complement.getText())) {    		
    		variable.setAdditionalPath(complement.getText());    		
    	} else {
    		variable.setAdditionalPath(null);
    	}    	
    }	
}
