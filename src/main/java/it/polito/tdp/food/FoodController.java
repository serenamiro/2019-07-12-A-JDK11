/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...\n\n");
    	String input = txtPorzioni.getText();
    	try {
    		Integer numPorzioni = Integer.parseInt(input);
    		model.creaGrafo(numPorzioni);
    		txtResult.appendText("Grafo creato\n");
    		boxFood.getItems().addAll(this.model.grafo.vertexSet());
    		txtResult.appendText("vertici: "+model.nVertici()+"\n");
    		txtResult.appendText("archi: "+model.nArchi()+"\n");
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Inserire un numero valido.\n");
    	}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...\n\n");
    	Food ciboScelto = boxFood.getValue();
    	if(ciboScelto==null) {
    		txtResult.appendText("Selezionare un cibo dalla tendina per proseguire.\n");
    		return;
    	}
    	List<Food> best = model.getBestFive(ciboScelto);
    	if(best != null) {
    		txtResult.appendText("Cibi con calorie congiunte massime:\n");
    		for(Food f : best) {
    			txtResult.appendText(f.toString()+"\n");
    		}
    	} else {
    		txtResult.appendText("Non è stato possibile trovare i cibi\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...\n\n");
    	String input = txtK.getText();
    	try {
    		Integer k = Integer.parseInt(input);
    		model.simula(boxFood.getValue(), k);
    		txtResult.appendText("Simulazione effettuata.");
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un numero intero.\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
