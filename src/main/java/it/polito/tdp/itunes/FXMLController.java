/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<String> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {
    	
    	if (this.txtDTOT.getText() == "") {
    		txtResult.setText("Inserisci il valore totale");
    		return;
    	}
    	try {
    		Double.parseDouble(txtDTOT.getText());
    	}
    	catch (Exception e) {
    		txtResult.setText("Inserisci un valore numerico (dTOT)");
    		return;
    	}
    	
    	txtResult.setText("LA MIA PLAYLIST:\n");
    	
    	List<Track> playlist = model.getPlaylist(Double.parseDouble(txtDTOT.getText()));
    	
    	for (Track t : playlist)
    		txtResult.appendText(t.toString() + '\n');

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (this.cmbGenere.getValue() == null) {
    		txtResult.setText("Scegli un genere");
    		return;
    	}
    	
    	if (this.txtMin.getText() == "") {
    		txtResult.setText("Inserisci il valore minimo");
    		return;
    	}
    	try {
    		if (Double.parseDouble(txtMin.getText()) < model.getMin(cmbGenere.getValue())) {
    			txtResult.setText("Inserire un valore minimo > di: " + model.getMin(cmbGenere.getValue()));
    			return;
    		}
    	}
    	catch (Exception e) {
    		txtResult.setText("Inserisci un valore numerico (min)");
    		return;
    	}
    	
    	if (this.txtMax.getText() == "") {
    		txtResult.setText("Inserisci il valore massimo");
    		return;
    	}
    	try {
    		if (Double.parseDouble(txtMax.getText()) > model.getMax(cmbGenere.getValue())) {
    			txtResult.setText("Inserire un valore massimo < di: " + model.getMax(cmbGenere.getValue()));
    			return;
    		}
    	}
    	catch (Exception e) {
    		txtResult.setText("Inserisci un valore numerico (max)");
    		return;
    	}
    	
    	SimpleGraph<Track, DefaultEdge> graph = model.creaGrafo(cmbGenere.getValue(), Double.parseDouble(txtMin.getText()), Double.parseDouble(txtMax.getText()));
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " + graph.edgeSet().size() + " archi.\n\n");
    	
    	List<Set<Track>> connesse = model.connesse();
    	
    	for (Set<Track> set : connesse) {
    		List<Track> list = new ArrayList<>(set);
    		txtResult.appendText("Componente con " + set.size() + " vertici, inseriti in " + list.get(0).getNumPlaylist() + " playlist\n");
    	}
    	
    	this.btnPlaylist.setDisable(false);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbGenere.getItems().addAll(model.getAllGenreName());
    	this.btnPlaylist.setDisable(true);
    }

}
