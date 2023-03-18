package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SaveAfterOpenController implements Initializable{

	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField directory;
	@FXML private TextField fileName;
	@FXML private Button validateRegisterButton;
	
	//Variables pour stocker les informations relatives � l'etudiant
	public static String studentLastName;
	public static String studentFirstName;
	public static String studentDirectory;
	public static String exerciceName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Listener, tant qu'un TextField est vide, le bouton n'est pas disponible
		//Pour le TextField du nom de l'�tudiant
		firstName.textProperty().addListener(event -> {
			if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || directory.getText().isEmpty()) {
				validateRegisterButton.setDisable(true);
			} else {
				validateRegisterButton.setDisable(false);
			}
		});
		
		//Pour le TextField du pr�nom de l'�tudiant
		lastName.textProperty().addListener(event -> {
			if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || directory.getText().isEmpty()) {
				validateRegisterButton.setDisable(true);
			} else {
				validateRegisterButton.setDisable(false);
			}
		});
		
		//Pour le TextField du repertoire dans lequel sera enregistr� le fichier de l'�tudiant
		directory.textProperty().addListener(event -> {
			if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || directory.getText().isEmpty()) {
				validateRegisterButton.setDisable(true);
			} else {
				validateRegisterButton.setDisable(false);
			}
		});

	}
	
	
	//M�thode qui permet � l'�tudiant de choisir le dossier dans lequel l'�tudiant verra son exercice enregistr�
	@FXML
	public void registerLocationFileExplorer() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory;
		directoryChooser.setTitle("Choisissez un r�pertoire pour l'enregistrement de votre exercice");
		selectedDirectory = directoryChooser.showDialog(null);
		if(selectedDirectory != null) {
			directory.setText(selectedDirectory.getAbsolutePath());
		}
		
		changeFileName();
	}
	
	//M�thode qui permet de quitter la popUp, un fois les TextFields remplis et de sauvegarder les infos
	@FXML
	public void exitRegisterExercicePopUp() {
		studentLastName = firstName.getText();
		studentFirstName = lastName.getText();
		studentDirectory = directory.getText();
		
		firstName.getScene().getWindow().hide();
	}
	
	@FXML public void changeFileName() {
        fileName.setText(directory.getText() + "\\" + exerciceName + "_" + lastName.getText() + "_" + firstName.getText() + ".rct");

    }
	

}
