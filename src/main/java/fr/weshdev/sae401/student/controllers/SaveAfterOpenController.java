package fr.weshdev.sae401.student.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SaveAfterOpenController implements Initializable{

	@FXML private TextField nom;
	@FXML private TextField prenom;
	@FXML private TextField repertoire;
	@FXML private TextField nomFichier;
	@FXML private Button preEnregistrement;
	
	//Variables pour stocker les informations relatives � l'etudiant
	public static String nomEtudiant;
	public static String prenEtudiant;
	public static String repertoireEtudiant;
	public static String nomExo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Listener, tant qu'un TextField est vide, le bouton n'est pas disponible
		//Pour le TextField du nom de l'�tudiant
		nom.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(nom.getText().isEmpty() || prenom.getText().isEmpty() || repertoire.getText().isEmpty()) {
					preEnregistrement.setDisable(true);
				} else {
					preEnregistrement.setDisable(false);
				}
			}
		});
		
		//Pour le TextField du pr�nom de l'�tudiant
		prenom.textProperty().addListener((arg01, arg11, arg2) -> {
			if(nom.getText().isEmpty() || prenom.getText().isEmpty() || repertoire.getText().isEmpty()) {
				preEnregistrement.setDisable(true);
			} else {
				preEnregistrement.setDisable(false);
			}
		});
		
		//Pour le TextField du repertoire dans lequel sera enregistr� le fichier de l'�tudiant
		repertoire.textProperty().addListener((arg012, arg112, arg2) -> {
			if(nom.getText().isEmpty() || prenom.getText().isEmpty() || repertoire.getText().isEmpty()) {
				preEnregistrement.setDisable(true);
			} else {
				preEnregistrement.setDisable(false);
			}
		});

	}
	
	
	//M�thode qui permet � l'�tudiant de choisir le dossier dans lequel l'�tudiant verra son exercice enregistr�
	@FXML
	public void choixRepertoire() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory;
		directoryChooser.setTitle("Choisissez un r�pertoire pour l'enregistrement de votre exercice");
		selectedDirectory = directoryChooser.showDialog(null);
		if(selectedDirectory != null) {
			repertoire.setText(selectedDirectory.getAbsolutePath());
		}
		
		changeFileName();
	}
	
	//M�thode qui permet de quitter la popUp, un fois les TextFields remplis et de sauvegarder les infos
	@FXML
	public void quitter() {
		nomEtudiant = nom.getText();
		prenEtudiant = prenom.getText();
		repertoireEtudiant = repertoire.getText();
		
		nom.getScene().getWindow().hide();
	}
	
	@FXML public void changeFileName() {
        nomFichier.setText(repertoire.getText() + "\\" + nomExo + "_" + prenom.getText() + "_" + nom.getText() + ".rct");

    }
	

}
