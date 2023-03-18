package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.MainEnseignant;
import fr.weshdev.sae401.DeplacementFenetre;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

	// Page Apercu
	@FXML
	private TextField texteConsigne;
	@FXML
	private TextArea texteTranscription;
	@FXML
	private TextField texteAide;
	@FXML
	private MediaView MediaViewApercu;
	@FXML
	private Button continueBouton;
	@FXML
	private ImageView imageViewApercu;

	public static String contenuConsigne;
	public static String contenuTranscription;
	public static String contenuAide;

	@FXML
	private CheckMenuItem dark;

	// Gestion du media (son + video)
	@FXML
	private Slider progressBar;
	@FXML
	private Slider sliderSon;
	@FXML
	private ImageView sonIcone;
	Image sonCoupe = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume_cut.png").toExternalForm());
	Image sonPasCoupe = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume.png").toExternalForm());
	Image play = new Image(getClass().getResource("/fr.weshdev.sae401/images/play.png").toExternalForm());
	Image pause = new Image(getClass().getResource("/fr.weshdev.sae401/images/pause.png").toExternalForm());
	@FXML private ImageView playPauseVideo;
	
	MediaPlayer mediaPlayer;
	Media media = ImportRessourceController.contenuMedia.getContent();

	// M�thode d'initialisation de la page
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// On met le media dans la preview
		mediaPlayer = new MediaPlayer(ImportRessourceController.contenuMedia.getContent());
		MediaViewApercu.setMediaPlayer(mediaPlayer);

		// On met l'image dans la preview
		imageViewApercu.setImage(ImportRessourceController.contenuImage.getContent());

		// Si les contenus ne sont pas null (lorsque l'enseignant fait retour), les
		// informations sont conserv�es
		// Consigne
		if (contenuConsigne != null) {
			texteConsigne.setText(contenuConsigne);
		}

		// Transcription
		if (contenuTranscription != null) {
			texteTranscription.setText(contenuTranscription);
		}

		// Aide
		if (contenuAide != null) {
			texteAide.setText(contenuAide);
		}
	}
	
	// Fonction qui permet � l'enseignant de visualiser sa video ou son son
		@FXML
		public void playOrPause() {

			sliderSonChange();
			sliderVideoChange();
			
			if (mediaPlayer.getStatus() == Status.PAUSED || mediaPlayer.getStatus() == Status.READY) {
				mediaPlayer.play();
				playPauseVideo.setImage(pause);
			} else {
				mediaPlayer.pause();
				playPauseVideo.setImage(play);
			}
		}
		
		public void sliderSonChange() {
			// Change le volume sonore selon la valeur du slider
			sliderSon.valueProperty().addListener((o -> {
				mediaPlayer.setVolume(sliderSon.getValue() / 100.0); 

				if(sliderSon.getValue() == 0) {
					sonIcone.setImage(sonCoupe);
				} else {
					sonIcone.setImage(sonPasCoupe);
				}
			}));
		}

		//Fonction qui permet de mute le son
		@FXML
		public void sonCoupe(MouseEvent event) {

			if(mediaPlayer.getVolume() != 0) {
				sonIcone.setImage(sonCoupe);
				mediaPlayer.setVolume(0);
			} else {
				sonIcone.setImage(sonPasCoupe);
				mediaPlayer.setVolume(sliderSon.getValue() / 100);
			}

		}

		//Fonction qui fait avancer le slider en fonction de la video
		public void sliderVideoChange() {

			Duration total = media.getDuration();
			progressBar.setMax(total.toSeconds());

			mediaPlayer.setOnReady(new Runnable() {
				@Override
				public void run() {
					Duration total = media.getDuration();
					progressBar.setMax(total.toSeconds());
				}
			});

			mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
					progressBar.setValue(newValue.toSeconds());
				}
			});

			progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
				}
			});

			progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
				}
			});
			
		}

	// Bouton Quitter qui permet � l'enseignant de quitter l'application (disponible
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
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void pageNouvelExo() throws IOException {

		// R�initialisation des variables
		AccueilController c = new AccueilController();
		c.delete();
		Stage primaryStage = (Stage) continueBouton.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/new_exercise.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	//M�thode qui permet de se rendre au manuel utilisateur == tuto
	@FXML
	public void tuto() throws MalformedURLException, IOException, URISyntaxException {
		
		InputStream is = MainEnseignant.class.getResourceAsStream("fr.weshdev.sae401/pdf/user_manual.pdf");

		File pdf = File.createTempFile("Manuel Utilisateur", ".pdf");
		pdf.deleteOnExit();
        OutputStream out = new FileOutputStream(pdf);

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while (is.available() != 0) {
            bytesRead = is.read(buffer);
            out.write(buffer, 0, bytesRead);
        }
        
        out.close();
        is.close();
        
        Desktop.getDesktop().open(pdf);

	}

	// M�thode pour charger la page d'importation de ressource (bouton retour)
	@FXML
	public void pageImporterRessource(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) continueBouton.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/import_ressource.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		darkModeActivation(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// M�thode pour charger la page des options de l'exercice
	@FXML
	public void pageOptions(ActionEvent event) throws IOException {
		// Quand on passe � la page suivante, on r�ucp�re les informations des
		// TextFields
		contenuConsigne = texteConsigne.getText();
		contenuTranscription = texteTranscription.getText();
		contenuAide = texteAide.getText();

		Stage primaryStage = (Stage) continueBouton.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/options.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	// M�thode pour passer ou non le darkMode
	@FXML
	public void darkMode() {

		if (dark.isSelected()) {
			continueBouton.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			continueBouton.getScene().getStylesheets()
					.addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			AccueilController.isDark = true;
		} else {
			continueBouton.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			continueBouton.getScene().getStylesheets().addAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			AccueilController.isDark = false;
		}

	}

	// M�thode qui regarde si le darkMode est actif et l'applique en cons�quence �
	// la scene
	public void darkModeActivation(Scene scene) {
		if (AccueilController.isDark) {
			scene.getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			scene.getStylesheets()
					.addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			dark.setSelected(true);
		} else {
			scene.getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			scene.getStylesheets().addAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			dark.setSelected(false);
		}
	}

}
