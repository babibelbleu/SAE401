package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.MainEnseignant;
import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.model.DarkModeManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewExerciseController implements Initializable{
	@FXML private TextField repertoire;


	@FXML private TextField nomExo;
	@FXML private Button okNouvelExo;
	
	private static String directoryPath;
	private static String exerciseName;

	DarkModeManager darkModeManager = new DarkModeManager();
	
	@FXML private CheckMenuItem dark;
	@FXML
	private BorderPane mainPain;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////			INITIALISATION		////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Méthode d'initialisation de la page
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			MenuBar header = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/header.fxml"));
			mainPain.setTop(header);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//On rempli les champs s'il ne sont pas null (si l'enseignant revient en arrière)
		if(directoryPath != null) {
			repertoire.setText(directoryPath);
		}
		
		if(exerciseName != null) {
			nomExo.setText(exerciseName);
		}
		
		//Si les deux champs sont remplis, on met le bouton cliquable
		if(directoryPath != null && nomExo != null) {
			okNouvelExo.setDisable(false);
		}
		
		//On regarde si le textField du repertoire est vide ou non
		repertoire.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newvalue) {
				if(!repertoire.getText().isEmpty() && !nomExo.getText().isEmpty()) {
					okNouvelExo.setDisable(false);
				} else {
					okNouvelExo.setDisable(true);
				}
			}
			
		});
		
		//On regarde si le textField du Nomd e l'exercice est vide ou non
		nomExo.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newvalue) {
				if(!nomExo.getText().isEmpty() && !repertoire.getText().isEmpty()) {
					okNouvelExo.setDisable(false);
				} else {
					okNouvelExo.setDisable(true);
				}
			}
			
		});
		
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////			METHDOES GENERALES		////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Bouton Quitter qui permet à l'enseignant de loadQuittingPage l'application (disponible sur toutes les pages)

	
	//Méthode qui permet de se rendre au manuel utilisateur == loadUserManual

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////		METHODES SPECIFIQUES A LA PAGE		////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Bouton Nouveau qui permet de créer un nouvel exercice

	
	//Méthode pour choisir le répertoire dans lequel l'enseignant enregistrera son fichier
	@FXML
	public void choisirRepertoire(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory;
		directoryChooser.setTitle("Choisissez un répertoire pour l'enregistrement");
		selectedDirectory = directoryChooser.showDialog(null);
		if(selectedDirectory != null) {
			repertoire.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	//Méthode pour aller sur la page d'importation de la ressource
	@FXML
	public void pageImportationRessource(ActionEvent event) throws IOException {
		
		//Au moment d'aller sur la page d'après, on récupère le contenu des TextFields
		directoryPath = repertoire.getText();
		exerciseName = nomExo.getText();
		
		Stage primaryStage = (Stage) repertoire.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/import_ressource.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		primaryStage.show();
		if(darkModeManager.isDarkMode()) {
			darkModeManager.darkModeActivation(scene);
		}
		else{
			darkModeManager.darkModeDesactivation(scene);
		}
	}
	
	//Méthode pour passer ou non le setDarkMode


	//Méthode qui regarde si le setDarkMode est actif et l'applique en conséquence à la scene


	public static String getDirectoryPath() {
		return directoryPath;
	}

	public static String getExerciseName() {
		return exerciseName;
	}

	//setters
	public static void setDirectoryPath(String newDirectoryPath) {
		directoryPath = newDirectoryPath;
	}

	public static void setExerciseName(String newExerciseName) {
		exerciseName = newExerciseName;
	}

	public static void delete() {
		directoryPath = null;
		exerciseName = null;
	}
}
