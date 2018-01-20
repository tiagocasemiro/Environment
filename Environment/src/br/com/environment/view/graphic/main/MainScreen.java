package br.com.environment.view.graphic.main;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import br.com.environment.controller.EnvironmentController;
import br.com.environment.model.entity.Variable;
import br.com.environment.view.graphic.AlertUtil;
import br.com.environment.view.graphic.ApplicationEnvironment;
import br.com.environment.view.graphic.ApplicationEnvironment.Screen;

public class MainScreen implements Initializable {
	@FXML
    private ListView<String> variablesListView;
    @FXML
    private Label name;
    @FXML
    private TextArea value;
    @FXML
    private CheckBox addToPath;
    @FXML
    private TextField complement;
    @FXML
    private Label variableOnPathResult;
    @FXML
    private Line line;
    @FXML
    private Label labelOnPath;
    @FXML
    private Label labelAddToPath;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    
    private EnvironmentController controller = new EnvironmentController();
    private Map<String, Variable> variables;     
    private Variable variableOpened = null;
    private String validateMessage = "";
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		variables = controller.list();	
		cleanFields();
		hideForm();
		
		this.variablesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {	
		    	showForm();
		    	variableOpened = variables.get(newValue);
		        name.setText(variableOpened.getName());
		        value.setText(variableOpened.getValue());		        
		        
		        complement.textProperty().addListener((observableTextField, oldValueTextField, newValueTextField) -> {	
		        	if("/".equals(newValueTextField)){
		        		complement.setText("");
		        		newValueTextField = "";
		        	}
		        	
		        	if(newValueTextField != null && !"".equals(newValueTextField)) {
		        		if(newValueTextField.startsWith("/")) {
		        			variableOnPathResult.setText(":$" + variableOpened.getName() + newValueTextField);
		        		} else {
		        			variableOnPathResult.setText(":$" + variableOpened.getName() + File.separator + newValueTextField);

		        		}		        		
				    } else {
				    	variableOnPathResult.setText(":$" + variableOpened.getName());
				    }
				});		        
		        
		        if(variableOpened.isOnPath()){
		        	addToPath.setSelected(true);		        			        	
		        	if(variableOpened.getAdditionalPath() != null) {
			        	complement.setText(File.separator + variableOpened.getAdditionalPath());			        
			        	variableOnPathResult.setText(":$" + variableOpened.getName() + File.separator + variableOpened.getAdditionalPath());
			        } else {
			        	complement.setText("");	
			        	variableOnPathResult.setText(":$" + variableOpened.getName());
			        }	
		        	
		        	line.setVisible(true);
		    		labelAddToPath.setVisible(true);
		    		labelOnPath.setVisible(true);
		    		variableOnPathResult.setVisible(true);
		    		complement.setVisible(true);
		        }else{
		        	addToPath.setSelected(false);
		        	complement.setText("");		        	
		        	variableOnPathResult.setText("");
		        	
		        	line.setVisible(false);
		    		labelAddToPath.setVisible(false);
		    		labelOnPath.setVisible(false);
		    		variableOnPathResult.setVisible(false);
		    		complement.setVisible(false);
		        }		                		        
		    }
		});
	
		this.addToPath.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue){
		    		line.setVisible(true);
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
		    		line.setVisible(false);
		    		labelAddToPath.setVisible(false);
		    		labelOnPath.setVisible(false);
		    		variableOnPathResult.setVisible(false);
		    		complement.setVisible(false);
		    	}
		    }
		});		
    }    
    
    @FXML
    void create(ActionEvent event) {
    	ApplicationEnvironment.openScreen(Screen.CREATE);
    }
    
    @FXML
    void delete(ActionEvent event) {
    	if(name.getText() != null && !name.getText().equals("")) {	
    		try {
    			controller.delete(variableOpened);
    			variables = controller.list();	
    			variablesListView.getItems().remove(variableOpened.getName());
    			cleanFields();
    			hideForm();
    			ApplicationEnvironment.openScreen(Screen.MAIN);
			} catch (Exception e) {					
				e.printStackTrace();
			}	    	
    	}
    }
    
    @SuppressWarnings("static-access")
	@FXML
    void save(ActionEvent event) {
		if(valedate()){ 
    		try {
    			updateVariableOpened();	    			
    			controller.update(variableOpened);
    			controller.removeFromPath(variableOpened);
    			if(variableOpened.isOnPath()){
    				controller.addToPath(variableOpened);
    			}	    			
    			variables = controller.list();	
    			cleanFields();
    			hideForm();
    			ApplicationEnvironment.openScreen(Screen.MAIN);
			} catch (Exception e) {					
				e.printStackTrace();
			}	    	 	
		} else {						
			AlertUtil.title("Erro na validação").content(validateMessage).showAlertErro();
		}    	
    }
        
    @FXML
    void path(ActionEvent event) {
    	ApplicationEnvironment.openScreen(Screen.PATH);
    }
			
    private void updateVariableOpened() {
    	variableOpened.setValue(value.getText());
    	variableOpened.setOnPath(addToPath.isSelected());
    	if(addToPath.isSelected() && complement.getText() != null && !"".equals(complement.getText())) {    		
    		variableOpened.setAdditionalPath(complement.getText());    		
    	} else {
    		variableOpened.setAdditionalPath(null);
    	}    	
    }
    
	private void showForm() {
		name.setVisible(true);
		value.setVisible(true);
		addToPath.setVisible(true);
		
		line.setVisible(true);
		labelAddToPath.setVisible(true);
		labelOnPath.setVisible(true);
		variableOnPathResult.setVisible(true);
		complement.setVisible(true);
		deleteButton.setVisible(true);
		saveButton.setVisible(true);
	}
	
	private void hideForm() {
		name.setVisible(false);
		value.setVisible(false);
		addToPath.setVisible(false);
		line.setVisible(false);
		labelAddToPath.setVisible(false);
		labelOnPath.setVisible(false);
		variableOnPathResult.setVisible(false);
		complement.setVisible(false);
		deleteButton.setVisible(false);
		saveButton.setVisible(false);
	}
	
	private void cleanFields() {
		name.setText("");
        value.setText("");		        
        addToPath.setSelected(false);
        complement.setText("");  
        variableOnPathResult.setText("");        
        variablesListView.getItems().clear();  
               
         for(Map.Entry<String, Variable> entry : variables.entrySet()) {	
			this.variablesListView.getItems().add(entry.getValue().getName());					
		}        
           
        variablesListView.refresh();
	}
	
	private boolean valedate() {
    	if(value.getText() == null || value.getText().equals("")){
    		validateMessage = "Campo valor deve ser preenchido!";	
    		return false;
    	}
    	return true;
    }
}
