package vue;

import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;

import controleur.Controleur.ActionNavigation;
import donnee.ExoplanetesDAO;
import controleur.ControleurExoplanetes;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modele.Exoplanete;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class VueExoplanetes extends Vue{
	protected ControleurExoplanetes controleur;
	protected static VueExoplanetes instance = null; 
	public static VueExoplanetes getInstance() {if(null==instance)instance = new VueExoplanetes();return VueExoplanetes.instance;}; 
	
	TableView tableau = (TableView)lookup("#liste-exoplanetes");
	Label uiModifier = (Label)lookup("#ui-modifier");
	Label uiSupprimer = (Label)lookup("#ui-supprimer");
	Exoplanete selectedExoplanete;
	
	private VueExoplanetes() 
	{
		super("exoplanetes.fxml"); 
		super.controleur = this.controleur = new ControleurExoplanetes();
		Logger.logMsg(Logger.INFO, "new VueExoplanetes()");
	}
		
	public void activerControles()
	{
		super.activerControles();
		
		ExoplanetesDAO exoplaneteDAO = new ExoplanetesDAO();
		
		Button boutonActualiser = (Button)lookup("#bouton-actualiser");
		boutonActualiser.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() 
		{
            @Override public void handle(ActionEvent e) 
            {
            	controleur.rafraichirDonnees();
            	diminiuerOpacityLabel();
            }
        });

		Button boutonAjouter = (Button)lookup("#bouton-ajouter");
		boutonAjouter.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() 
		{
            @Override public void handle(ActionEvent e) 
            {
            	diminiuerOpacityLabel();
            	controleur.navigation(ActionNavigation.AJOUTER);
            }
        });
		
		Button boutonModifier = (Button)lookup("#bouton-modifier");
		boutonModifier.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() 
		{
            @Override public void handle(ActionEvent e) 
            {
        		diminiuerOpacityLabel();
        		controleur.setSelectedExoplanete(selectedExoplanete);
        		System.out.println(selectedExoplanete + " boutonModifier");
        		controleur.navigation(ActionNavigation.MODIFIER); 
            }
        });
		
		Button boutonSupprimer = (Button)lookup("#bouton-supprimer");
		boutonSupprimer.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() 
		{
            @Override public void handle(ActionEvent e) 
            {
              ObservableList<Exoplanete> row , allRows;
      		  allRows = tableau.getItems();
      		  row = tableau.getSelectionModel().getSelectedItems();   
      		  controleur.supprimerExoplanete(row.get(0));

      		  row.forEach(allRows::remove);
          	  
      		  diminiuerOpacityLabel();
            }
        });
		
		tableau.setOnMouseClicked(event -> {
		    // Make sure the user clicked on a populated item
		    if (tableau.getSelectionModel().getSelectedItem() != null) {
		        uiModifier.setOpacity(1);
		        uiSupprimer.setOpacity(1);
		        
		        ObservableList<Exoplanete> row , allRows;
	      		allRows = tableau.getItems();
	      		row = tableau.getSelectionModel().getSelectedItems();   
	      		selectedExoplanete = row.get(0);
		    }
		});
	}
	
	public void afficherExoplanetes(List<Exoplanete> exoplanetes)
	{	
		//Nettoyage des possibles donn�es pr�c�dentes
		tableau.getItems().clear();
		
		// Association des champs de l'objet avec les colonnes du tableau		
		TableColumn colonneNom = (TableColumn) tableau.getColumns().get(0);
		TableColumn colonneEtoile = (TableColumn) tableau.getColumns().get(1);
		TableColumn colonneMasse = (TableColumn) tableau.getColumns().get(2);
		TableColumn colonneRayon = (TableColumn) tableau.getColumns().get(3);
		TableColumn colonneFlux = (TableColumn) tableau.getColumns().get(4);
		TableColumn colonneTemperature = (TableColumn) tableau.getColumns().get(5);
		TableColumn colonnePeriode = (TableColumn) tableau.getColumns().get(6);
		TableColumn colonneDistance = (TableColumn) tableau.getColumns().get(7);
		
		colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		colonneEtoile.setCellValueFactory(new PropertyValueFactory<>("etoile"));
		colonneMasse.setCellValueFactory(new PropertyValueFactory<>("masse"));
		colonneRayon.setCellValueFactory(new PropertyValueFactory<>("rayon"));
		colonneFlux.setCellValueFactory(new PropertyValueFactory<>("flux"));
		colonneTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
		colonnePeriode.setCellValueFactory(new PropertyValueFactory<>("periode"));
		colonneDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
		
		// Ajout des donnees
		for(Exoplanete exoplanete : exoplanetes)
		{ 
			tableau.getItems().add(exoplanete);
		}
	}
	
	public void diminiuerOpacityLabel() {
		uiModifier.setOpacity(0.35);
        uiSupprimer.setOpacity(0.35);
	}
}
