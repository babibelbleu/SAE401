package fr.weshdev.sae401.student.controllers;

import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.MainEtudiant;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseController implements Initializable {

	//Variables qui vont contenir les diff�rentes informations sur l'exercice
	//Informations textuelles
	public static String transcriptionContent;
	public static String instructionContent;
	public static Media mediaContent;
	public static String hidddenChar;

	//Options de l'exercice
	public static String nbMin;

	public static Image imageContent;

	//TextFields et autre composants qui contiennent les informations de l'exercice
	@FXML private TextArea transcription;
	@FXML private TextArea instruction;
	@FXML private ImageView thumbnailContainerMp3;
	@FXML private MediaView mediaContainer;
	@FXML private Text time;
	@FXML private Label titleTime;
	@FXML private TextField userPropositionWord;

	//Ce qui concerne le media
	@FXML private ImageView launchVideoIcon;
	@FXML private ImageView playOrPauseImageContainer;
	MediaPlayer mediaPlayer = new MediaPlayer(mediaContent);
	Image playIcon = new Image(getClass().getResource("/fr.weshdev.sae401/images/play.png").toExternalForm());
	Image pauseIcon = new Image(getClass().getResource("/fr.weshdev.sae401/images/pause.png").toExternalForm());
	Image mutedLoudSpeakerIcon = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume_cut.png").toExternalForm());
	Image loudspeakerIcon = new Image(getClass().getResource("/fr.weshdev.sae401/images/volume.png").toExternalForm());
	@FXML private Slider sliderSound;
	@FXML private Slider sliderVideo;
	@FXML private ImageView soundContainerState;

	//Gestion du timer

	private Integer sec = 0;
	private Integer min;
	private boolean isTimerRunning = false;

	//Autres boutons
	@FXML private Button helpButton;
	@FXML private Button solutionButton;
	@FXML private ImageView alertSolution;



	private final ArrayList<String> wordExerciceList = new ArrayList<>();
	private final ArrayList<String> sensivityCaseWordList = new ArrayList<>();
	private final ArrayList<Integer> discoveredWordList = new ArrayList<>();
	private String encryptedText;
	
	public String getEncryptedText() {
		return encryptedText;
	}

	public String getClearText() {
		return clearText;
	}


	private final String clearText = transcriptionContent;
	public int numberPartialReplacement;

	//Tout ce qui concerne la barre de progression
	@FXML private ProgressBar progressBar;
	@FXML private Label rateDiscoveredWords;
	@FXML private Label discoveredWordsLabel;
	private final float nbDiscoveredWord = 0;
	private float totalNbWord;

	//Tooltip
	@FXML private ImageView helpInstruction;
	@FXML private ImageView helpTranscription;
	@FXML private ImageView helpProposition;

	@FXML private CheckMenuItem darkMode;
	@FXML private Button validateButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MenuController.getOptions();


		encryptedText = encryptText();

		//On fait en sorte � ce que le texte ne d�passe pas du cadre
		transcription.setWrapText(true);



		//On initialise la liste discoveredWordList
		for(String w : wordExerciceList) {
			if(checkStringContainsPunctuation(w)) {
				discoveredWordList.add(1);
			} else {
				discoveredWordList.add(0);
			}
		}

		//On passe les mots comparatifs en minuscule dans une autre liste
		for(String word : wordExerciceList) {
			sensivityCaseWordList.add(word.toLowerCase());

			if(!checkStringContainsPunctuation(word)) {
				totalNbWord++;
			}
		}

		//On load la instruction
		if(instructionContent != null) {
			instruction.setText(instructionContent);
		}

		//On load le media
		if(mediaContent != null) {
			mediaContainer.setMediaPlayer(mediaPlayer);
		}

		//On load l'image quand il s'agit d'un mp3
		if(imageContent != null) {
			thumbnailContainerMp3.setImage(imageContent);
		}

		//On load le temps n�cessaire si c'est en mode Evaluation
		if(MenuController.getOptions().get("evaluationOption").isActive() == true) {
			evalutationModeLoader();
		} 
		//Sinon cela veut dire que l'on est en mode Entrainement
		else {

			try {
				trainingModeLoader();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		//On fait appara�tre une fen�tre pour que l'�tudiant rentre son nom et pr�nom en vue du futur enregistrement
		//Note : Seulement si l'exercice est en mode Entrainement

		sliderSoundChange();
		sliderVideoChange();

		validateButton.setOnAction(ActionEvent -> {
			try {
				verify(userPropositionWord.getText());
				userPropositionWord.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		transcription.setText(encryptedText);

	}

	private void trainingModeLoader() throws IOException {
		titleTime.setText("Temps Ecoul�");
		min = 00;
		time.setText("00:00");

		//Si l'enseignant n'a pas souhait� autoriser l'affichage de la solution
		if(MenuController.getOptions().get("solutionShowOption").isActive() ==false) {
			solutionButton.setVisible(false);
			alertSolution.setVisible(false);
		}

		//Si l'enseignant n'a pas souhait� l'affichage de mots d�couverts en temps r�el
		if(MenuController.getOptions().get("discoveredWordRateProgressBarOption").isActive() == false) {
			progressBar.setVisible(false);
			rateDiscoveredWords.setVisible(false);
			discoveredWordsLabel.setVisible(false);
		}

		if(MenuController.getOptions().get("incompletedWordWithTwoLettersOption").isActive() == true) {
			numberPartialReplacement = 2;
		} else if(MenuController.getOptions().get("incompletedWordWithThreeLettersOption").isActive() == true){
			numberPartialReplacement = 3;
		} else {
			numberPartialReplacement = 0;
		}
		popUpUserRegister();
	}

	private void evalutationModeLoader() {
		min = Integer.parseInt(nbMin);
		time.setText(min + ":" + sec);

		//On masque les boutons qui ne sont pr�sent que ne mode entrainement
		helpButton.setVisible(false);
		solutionButton.setVisible(false);
		alertSolution.setVisible(false);
		//Si l'enseignant n'a pas souhait� l'affichage de mots d�couverts en temps r�el
		progressBar.setVisible(false);
		rateDiscoveredWords.setVisible(false);
		discoveredWordsLabel.setVisible(false);
	}

	private String encryptText() {
		String constructString = "";
		for (String string : clearText.split(""))
			if (string.matches("[a-zA-Z]") || (string.matches("[0-9]"))) {
				constructString += hidddenChar;
			} else {
				constructString += string;
			}
		return constructString;
	}

	public void sliderSoundChange() {
		// Change le volume sonore selon la valeur du slider
		sliderSound.valueProperty().addListener((o -> {
			mediaPlayer.setVolume(sliderSound.getValue() / 100.0);

			if(sliderSound.getValue() == 0) {
				soundContainerState.setImage(mutedLoudSpeakerIcon);
			} else {
				soundContainerState.setImage(loudspeakerIcon);
			}
		}));
	}

	//Fonction qui fait avancer le slider en fonction de la video
	public void sliderVideoChange() {

		mediaPlayer.setOnReady(() -> sliderVideo.setMax(mediaPlayer.getTotalDuration().toSeconds()));

		// Ecoute sur le slider. Quand il est modifi�, modifie le temps du media player.
		InvalidationListener sliderChangeListener = o -> {
			Duration seekTo = Duration.seconds(sliderVideo.getValue());
			mediaPlayer.seek(seekTo);
		};
		sliderVideo.valueProperty().addListener(sliderChangeListener);

		// Lie le temps du media player au slider
		mediaPlayer.currentTimeProperty().addListener(l -> {

			sliderVideo.valueProperty().removeListener(sliderChangeListener);

			// Met a jour la valeur de temps du m�dia avec la position du slider.
			Duration currentTime = mediaPlayer.getCurrentTime();
			sliderVideo.setValue(currentTime.toSeconds());

			// R�activation de l'�coute du slider
			sliderVideo.valueProperty().addListener(sliderChangeListener);

		});
	}

	//Fonction qui permet de mute le son
	@FXML
	public void mute() {

		if(mediaPlayer.getVolume() != 0) {
			soundContainerState.setImage(mutedLoudSpeakerIcon);
			mediaPlayer.setVolume(0);
		} else {
			soundContainerState.setImage(loudspeakerIcon);
			mediaPlayer.setVolume(sliderSound.getValue() / 100);
		}

	}

	//Fonction qui lance le media pour la premiere fois 
	@FXML
	public void firstPlay() {

		mediaPlayer.play();
		setKeyboardShortcut();
		

		if(!isTimerRunning) {
			gestionTimer();
			isTimerRunning = true;
		}

		launchVideoIcon.setVisible(false);
	}

	//Fonction qui play / pause le media
	@FXML
	public void playOrPauseClicked() {

		if(mediaPlayer.getStatus() == Status.PLAYING) {
			mediaPlayer.pause();
			playOrPauseImageContainer.setImage(playIcon);
		}

		if(mediaPlayer.getStatus() == Status.PAUSED) {
			mediaPlayer.play();
			playOrPauseImageContainer.setImage(pauseIcon);
		}

	}


	//Fonction qui regarde si le mot contient un caract�re de ponctuation
	private boolean checkStringContainsPunctuation(String mot) {

		for(int i = 0; i < mot.length(); i++) {
			if((mot.charAt(i) + "").matches("[.,;!?:]")) {
				return true;
			}
		}
		return false;
	}

	//M�thode qui permet de se rendre au manuel utilisateur == tuto
	@FXML
	public void tuto() throws IOException {

		InputStream is = MainEtudiant.class.getResourceAsStream("Manuel_Utilisateur.pdf");

		File pdf = File.createTempFile("Manuel Utilisateur", ".pdf");
		pdf.deleteOnExit();

		try (OutputStream out = new FileOutputStream(pdf)) {

			byte[] buffer = new byte[4096];
			int bytesRead = 0;

			while (is.available() != 0) {
				bytesRead = is.read(buffer);
				out.write(buffer, 0, bytesRead);
			}

			out.close();
		}
		is.close();

		Desktop.getDesktop().open(pdf);

	}

	//M�thode qui fait appara�tre la popUp pour que l'�tudiant rentre ses infos pour l'enregistrement
	public void popUpUserRegister() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/save_after_open.fxml"));
		Stage stage = new Stage();
		Rectangle rect = new Rectangle(900,500);
		rect.setArcHeight(20.0);
		rect.setArcWidth(20.0);
		root.setClip(rect);

		//On bloque sur cette fen�tre
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(root, 900, 500);
		scene.setFill(Color.TRANSPARENT);
		darkModeActivation(scene);

		//On bloque le resize
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		DeplacementFenetre.deplacementFenetre((Pane) root, stage);
	}

	//M�thode qui regarde si le darkMode est actif et l'applique en cons�quence � la scene
	public void darkModeActivation(Scene scene) {
		if(MenuController.isInDarkMode) {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			darkMode.setSelected(true);
		} else {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			darkMode.setSelected(false);
		}
	}

	//M�thode pur afficher l'aide propos�e par l'enseignant
	@FXML
	public void HelpShow() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/hints.fxml"));
		Stage stage = new Stage();
		Rectangle rect = new Rectangle(400,600);
		rect.setArcHeight(20.0);
		rect.setArcWidth(20.0);
		root.setClip(rect);

		//On bloque sur cette fen�tre
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(root, 400, 600);
		scene.setFill(Color.TRANSPARENT);
		darkModeActivation(scene);

		stage.setScene(scene);
		stage.show();
		DeplacementFenetre.deplacementFenetre((Pane) root, stage);
	}

	//M�thode pour afficher la solution
	@FXML
	public void solutionShowed() throws IOException {

		backToMenuClicked();

		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/solution.fxml"));
		Stage stage = new Stage();
		Rectangle rect = new Rectangle(600,400);
		rect.setArcHeight(20.0);
		rect.setArcWidth(20.0);
		root.setClip(rect);

		//On bloque sur cette fen�tre
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(root, 600, 400);
		scene.setFill(Color.TRANSPARENT);
		darkModeActivation(scene);

		stage.setScene(scene);
		stage.show();
		DeplacementFenetre.deplacementFenetre((Pane) root, stage);

	}

	private void verify(String text) throws IOException {
		if (text == null) {
			return;
		}
		String[] encrypted = encryptedText.split("[ \\t\\n\\x0B\\f\\r]");
		String[] clear = clearText.split("[ \\t\\n\\x0B\\f\\r]");
		Pattern punctionLessPattern = Pattern.compile("[^\\p{Punct}&&[^'-]]*");
		Matcher clearMatcher;
		for (int i = 0; i < clear.length; i++) {
			clearMatcher = punctionLessPattern.matcher(clear[i]);
			if (clearMatcher.find() && clearMatcher.group(0).toLowerCase().equals(text.toLowerCase())) {
				if (MenuController.getOptions().get("caseSensitiveOption").isActive() == true && !clearMatcher.group(0).equals(text))
				{
					continue;
				}
				encrypted[i] = clear[i];
			}

			Pattern numberCharPattern = Pattern.compile(".{4,}");
			Matcher numberCharMatcher = numberCharPattern.matcher(clear[i]);
			if (numberCharMatcher.find() && numberPartialReplacement > 0 && text.length() >= numberPartialReplacement 
					&& encrypted[i].substring(0,text.length()).contains(""+ hidddenChar)
					&& numberCharMatcher.group().startsWith(text)) {

				encrypted[i] = numberCharMatcher.group(0).substring(0,text.length());
				for (int j = text.length(); j < clearMatcher.group(0).length(); j++) {
					encrypted[i] += hidddenChar;
				}
				encrypted[i] += clear[i].substring(clearMatcher.group(0).length());
			}
		}
		encryptedText = "";
		int length =0;
		for (int i = 0; i < encrypted.length; i++) {
			encryptedText += encrypted[i];
			if (length + clear[i].length() < clearText.length()) {
				length += clear[i].length();
			}
			if (Character.isWhitespace(clearText.charAt(length)) || Character.isSpaceChar(clearText.charAt(length))) {
				encryptedText += clearText.charAt(length);
			}
			length++;
		}

		int ok = 0;

		if(MenuController.getOptions().get("discoveredWordRateProgressBarOption").isActive()==true) {
			int numberWord = clear.length;
			int numberFoundWord = 0;
			for (String string : encrypted) {
				if (!string.contains(hidddenChar)) {
					numberFoundWord++;
				}
			}
			progressBar.setProgress( (double) numberFoundWord / (double) numberWord);
			rateDiscoveredWords.setText(Math.round(( (double) numberFoundWord / (double) numberWord) * 100)  + "%");

			if (Math.round(( (double) numberFoundWord / (double) numberWord) * 100) == 100){
				ok = 1;
			}
		}

		if(ok == 1) {

			//Si c'est le cas, on enregistre son exercice, puis on load une popUp
			backToMenuClicked();

			if(MenuController.getOptions().get("evaluationOption").isActive() == true) {
				finExercice();
				registerExercice();
			}
		}

		transcription.setText(encryptedText);
	}


	//M�thode pour quitter l'application
	@FXML
	public void quitter() {
		Platform.exit();
	}


	//M�thode qui regarde si l'�tudiant a fini l'exercice

	//M�thode qui va load le temps �coul� pour le mode �valuation
	public void finExercice() throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/save_validation.fxml"));
		//On bloque sur cette fen�tre
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		DeplacementFenetre.deplacementFenetre((Pane) root, stage);
		Scene scene = new Scene(root, 350, 180);
		stage.setScene(scene);
		darkModeActivation(scene);
		stage.show();
	}


	//M�thode permettant de cr�er un timer pour que l'�tudiant voit le temps qui d�file en mode Evaluation
	public void gestionTimer() {
  Timeline timer;
		timer = new Timeline();
		timer.setCycleCount(Animation.INDEFINITE);

		if(MenuController.getOptions().get("evaluationOption").isActive() == true) {
			// KeyFrame event handler
			timer.getKeyFrames().add(
					new KeyFrame(Duration.seconds(1),
							arg0 -> {
								sec--;
								if (sec < 0) {
									min--;
									sec=59;
								}
								// update timerLabel
								time.setText(min +":"+ sec +"s");

								//S'il ne reste plus de temps, on load la fenetre d'enregistrement
								if (sec <= 0 && min<=0) {
									timer.stop();
									try {
										backToMenuClicked();
										loadEnregistrement();
										registerExercice();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

							}));
			timer.playFromStart();
		}

		if(MenuController.getOptions().get("evaluationOption").isActive() == true) {
			// KeyFrame event handler
			timer.getKeyFrames().add(
					new KeyFrame(Duration.seconds(1),
							arg0 -> {
								sec++;
								if (sec > 59) {
									min++;
									sec = 00;
								}

								// update timerLabel
								time.setText(min +":"+ sec +"s");

							}));
			timer.playFromStart();
		}
	}

	//M�thode qui survient lorsque le timer est �coul� en mode Evaluation
	public void loadEnregistrement() throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/time_is_up.fxml"));
		//On bloque sur cette fen�tre
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		DeplacementFenetre.deplacementFenetre((Pane) root, stage);
		//On bloque le resize
		stage.setResizable(false);
		Scene scene = new Scene(root, 500, 300);
		stage.setScene(scene);
		darkModeActivation(scene);
		stage.show();
	}

	//M�thode qui va enregistrer l'exercice de l'�tudiant
	public void registerExercice() throws IOException {

		File file = new File(SaveAfterOpenController.studentDirectory + "\\" + SaveAfterOpenController.exerciceName
				+ "_" + SaveAfterOpenController.studentLastName + "_" + SaveAfterOpenController.studentFirstName + ".rct");
		FileWriter fwrite = new FileWriter(file);
		try (BufferedWriter buffer = new BufferedWriter(fwrite)) {

			buffer.write(transcription.getText());
			buffer.newLine();
			buffer.write(Double.toString(Math.round((nbDiscoveredWord / totalNbWord) * 100)) + '%');

		}
		fwrite.close();
	}

	//M�thode pour retourner au menu
	public void backToMenuClicked() throws IOException {
		Stage stage = (Stage) alertSolution.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
		Scene scene = new Scene(root,  MainEtudiant.width, MainEtudiant.height - 60);
		stage.setScene(scene);
		darkModeActivation(scene);
		stage.show();
	}
	@FXML
	public void tipsInstruction() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Lisez bien la consigne du professeur ");
		tooltip.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 15px;");
		tooltip.setShowDelay(Duration.millis(0));
		tooltip.setShowDuration(Duration.millis(5000));
		Tooltip.install(helpInstruction, tooltip);
	}
	public void tipsTranscription() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Transcrivez le texte en respectant la ponctuation et les majuscules");
		tooltip.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 15px;");
		tooltip.setShowDelay(Duration.millis(0));
		tooltip.setShowDuration(Duration.millis(5000));
		Tooltip.install(helpTranscription, tooltip);

	}
	public void tipsProposition(){
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Proposez des mots qui correspondent au texte");
		tooltip.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 15px;");
		tooltip.setShowDelay(Duration.millis(0));
		tooltip.setShowDuration(Duration.millis(5000));
		Tooltip.install(helpProposition, tooltip);
	}



	@FXML
	public  void darkMode() {

		if(darkMode.isSelected()) {
			helpButton.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			helpButton.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			MenuController.isInDarkMode = true;
		} else {
			helpButton.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			helpButton.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			MenuController.isInDarkMode = false;
		}
	}


	private void setKeyboardShortcut() {
		helpButton.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handle);
		
		helpButton.getScene().addEventFilter(KeyEvent.KEY_RELEASED, event -> {
			 if ((helpButton.getScene().focusOwnerProperty().get() instanceof TextField)) {
					if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
							userPropositionWord.setText("");
					}
				}
		});
	}

	private void handle(KeyEvent event) {

		if ((helpButton.getScene().focusOwnerProperty().get() instanceof TextField)) {
			if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER && (!userPropositionWord.getText().isEmpty())) {
				try {
					verify(userPropositionWord.getText());
					userPropositionWord.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else if (event.getCode() == KeyCode.SPACE) {
			if (mediaContainer.getMediaPlayer().getStatus() == Status.PAUSED) {
				mediaContainer.getMediaPlayer().play();
				playOrPauseImageContainer.setImage(pauseIcon);
			}
			if (mediaContainer.getMediaPlayer().getStatus() == Status.PLAYING) {
				mediaContainer.getMediaPlayer().pause();
				playOrPauseImageContainer.setImage(playIcon);
			}

		}
		if (event.getCode() == KeyCode.RIGHT && mediaContainer.getMediaPlayer().getTotalDuration().greaterThan(mediaContainer.getMediaPlayer().getCurrentTime().add(new Duration(5000)))) {
			mediaContainer.getMediaPlayer().seek(mediaContainer.getMediaPlayer().getCurrentTime().add(new Duration(5000)));
		}
		if (event.getCode() == KeyCode.LEFT && new Duration(0).lessThan(mediaContainer.getMediaPlayer().getCurrentTime().subtract(new Duration(5000)))) {
			mediaContainer.getMediaPlayer().seek(mediaContainer.getMediaPlayer().getCurrentTime().subtract(new Duration(5000)));
		}
		if (event.getCode() == KeyCode.UP) {
			sliderSound.setValue(sliderSound.getValue() + 3);
		}
		if (event.getCode() == KeyCode.DOWN) {
			sliderSound.setValue(sliderSound.getValue() - 3);
		}
	}
}
