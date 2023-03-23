package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.MainEnseignant;
import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.model.DarkModeManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApercuController implements Initializable {

	private static final int ENCRYPT_OFFSET = 3;
	public BorderPane mainPain;

	@FXML
	private TextField instructionText;
	@FXML
	private TextArea transcriptionText;
	@FXML
	private TextField helpText;
	@FXML
	private MediaView apercuMediaView;

	// Bouton "continuer"
	@FXML
	private Button nextPageButton;
	@FXML
	private ImageView apercuImageView;

	public static String instructionContent;
	public static String transcriptionContent;
	public static String helpContent;

	DarkModeManager darkModeManager = new DarkModeManager();

	@FXML
	private CheckMenuItem darkModeMenuSelection;
	@FXML
	private Slider videoProgressBar;
	@FXML
	private Slider soundSlider;
	@FXML
	private ImageView soundImage;
	Image cutSongImage = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume_cut.png").toExternalForm());
	Image activeSoundImage = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume.png").toExternalForm());
	Image playImage = new Image(getClass().getResource("/fr.weshdev.sae401/images/play.png").toExternalForm());
	Image pauseImage = new Image(getClass().getResource("/fr.weshdev.sae401/images/pause.png").toExternalForm());
	@FXML private ImageView videoStatusImageView;
	
	MediaPlayer mediaPlayer;
	Media media = ImportRessourceController.getContenuMedia();

	// M�thode d'initialisation de la page
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			MenuBar header = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/header.fxml"));
			mainPain.setTop(header);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// On met le media dans la preview
		mediaPlayer = new MediaPlayer(ImportRessourceController.getContenuMedia());
		apercuMediaView.setMediaPlayer(mediaPlayer);

		// On met l'image dans la preview
		apercuImageView.setImage(ImportRessourceController.getContenuImage());

		// Si les contenus ne sont pas null (lorsque l'enseignant fait retour), les
		// informations sont conserv�es
		// Consigne
		if (instructionContent != null) {
			instructionText.setText(instructionContent);
		}

		// Transcription
		if (transcriptionContent != null) {
			transcriptionText.setText(transcriptionContent);
		}

		// Aide
		if (helpContent != null) {
			helpText.setText(helpContent);
		}
	}
	
	// Fonction qui permet � l'enseignant de visualiser sa video ou soundImage soundImage
		@FXML
		public void playOrPause() {

			sliderSonChange();
			sliderVideoChange();
			
			if (mediaPlayer.getStatus() == Status.PAUSED || mediaPlayer.getStatus() == Status.READY) {
				mediaPlayer.play();
				videoStatusImageView.setImage(pauseImage);
			} else {
				mediaPlayer.pause();
				videoStatusImageView.setImage(playImage);
			}
		}
		
		public void sliderSonChange() {
			// Change le volume sonore selon la valeur du slider
			soundSlider.valueProperty().addListener((o -> {
				mediaPlayer.setVolume(soundSlider.getValue() / 100.0);

				if(soundSlider.getValue() == 0) {
					soundImage.setImage(cutSongImage);
				} else {
					soundImage.setImage(activeSoundImage);
				}
			}));
		}

		//Fonction qui permet de mute le soundImage
		@FXML
		public void sonCoupe(MouseEvent event) {

			if(mediaPlayer.getVolume() != 0) {
				soundImage.setImage(cutSongImage);
				mediaPlayer.setVolume(0);
			} else {
				soundImage.setImage(activeSoundImage);
				mediaPlayer.setVolume(soundSlider.getValue() / 100);
			}

		}

		//Fonction qui fait avancer le slider en fonction de la video
		public void sliderVideoChange() {

			Duration total = media.getDuration();
			videoProgressBar.setMax(total.toSeconds());

			mediaPlayer.setOnReady(new Runnable() {
				@Override
				public void run() {
					Duration total = media.getDuration();
					videoProgressBar.setMax(total.toSeconds());
				}
			});

			mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
					videoProgressBar.setValue(newValue.toSeconds());
				}
			});

			videoProgressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(videoProgressBar.getValue()));
				}
			});

			videoProgressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(videoProgressBar.getValue()));
				}
			});
			
		}

	// Bouton Quitter qui permet � l'enseignant de loadQuittingPage l'application (disponible
	// sur toutes les pages)
	@FXML
	public void quitter(ActionEvent event) throws IOException {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader
				.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/confirm_quit.fxml"));
		Scene scene = new Scene(root, 400, 200);
		// On bloque sur cette fen�tre
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);

		// Bordure
		Rectangle rect = new Rectangle(400, 200);
		rect.setArcHeight(20.0);
		rect.setArcWidth(20.0);
		root.setClip(rect);

		DeplacementFenetre.deplacementFenetre((Pane) root, primaryStage);
		primaryStage.setScene(scene);
		if(darkModeManager.isDarkMode()){
			darkModeManager.darkModeActivation(scene);
		}
		else {
			darkModeManager.darkModeDesactivation(scene);
		}
		primaryStage.show();
	}



	//M�thode qui permet de se rendre au manuel utilisateur == loadUserManual


	// M�thode pour charger la page d'importation de ressource (bouton retour)
	@FXML
	public void pageImporterRessource(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) nextPageButton.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/import_ressource.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		if(darkModeManager.isDarkMode()){
			darkModeManager.darkModeActivation(scene);
		}
		else {
			darkModeManager.darkModeDesactivation(scene);
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// M�thode pour charger la page des options de l'exercice
	@FXML
	public void pageOptions(ActionEvent event) throws IOException {
		// Quand on passe � la page suivante, on r�ucp�re les informations des
		// TextFields.
		instructionContent = instructionText.getText();
		transcriptionContent = transcriptionText.getText();
		helpContent = helpText.getText();

		Stage primaryStage = (Stage) nextPageButton.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/options.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		if(darkModeManager.isDarkMode()){
			darkModeManager.darkModeActivation(scene);
		}
		else {
			darkModeManager.darkModeDesactivation(scene);
		}
		primaryStage.show();
	}

	// M�thode pour passer ou non le setDarkMode


	// M�thode qui regarde si le setDarkMode est actif et l'applique en cons�quence �
	// la scene

	public static void reset(){
		helpContent = null;
		instructionContent = null;
		transcriptionContent = null;
	}

}
