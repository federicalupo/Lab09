
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
	

    @FXML
    private ComboBox<Country> cmbStato;

    @FXML
    private Button btnVicini;


	@FXML
	void doCalcolaConfini(ActionEvent event) {

		this.txtResult.clear();
		
		try {
			int anno = Integer.parseInt(this.txtAnno.getText());

			if (anno >= 1816 && anno <= 2016) {
				model.creaGrafo(anno);//!!!!

				txtResult.appendText("Elenco stati:\n\n");
				for(Country c: this.model.vertici()) {
					
					this.txtResult.appendText(c.getStateNme()+"--- Numero stati confinanti: "+model.nConfinanti(c)+"\n");
				}
				
				
				this.txtResult.appendText("\nNumero componenti connesse: "+ model.nComponentiConnesse());
				
				
				this.cmbStato.getItems().addAll(model.vertici());
			
				
			}
			else {
				this.txtResult.clear();
				this.cmbStato.getItems().clear();
				this.txtResult.appendText("Inserire anno nell'intervallo 1816-2016");
			}
		} catch (NumberFormatException nfe) {

			this.txtResult.appendText("Inserire anno nell'intervallo 1816-2016");
		}

	}
	

    @FXML
    void trovaVicini(ActionEvent event) {
    		
    	this.txtResult.clear();
    	
    	Country stato = this.cmbStato.getValue();
    	
    	if(stato != null) {
    	
    	//List<Country> vicini = model.trovaViciniAmpiezza(stato);
    	//List<Country> vicini = model.trovaViciniProfondita(stato);
    	//List<Country> vicini = model.trovaViciniRicorsiva(stato);
    	List<Country> vicini = model.trovaViciniIterativa(stato);
    	
    	this.txtResult.appendText("Gli stati raggiungibili dallo stato selezionato:\n\n");
    	for(Country c: vicini) {
    		
    		this.txtResult.appendText(c.toString()+"\n");
    		}
    	}else {
    		
    		this.txtResult.appendText("Nessuno stato selezionato");
    	}
    	
    	
    	
    }

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbStato != null : "fx:id=\"cmbStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVicini != null : "fx:id=\"btnVicini\" was not injected: check your FXML file 'Scene.fxml'.";

		
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
