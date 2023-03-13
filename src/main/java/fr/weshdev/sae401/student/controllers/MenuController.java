package fr.weshdev.sae401.student.controllers;

import fr.weshdev.sae401.MainEtudiant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

	// Menu
	@FXML
	private Text recupScene;
	@FXML
	private ImageView handicap;

	// A propos
	@FXML
	private Label recuperation;

	@FXML
	private CheckMenuItem dark;

	public static boolean isDark = false;

	private static ExerciseController c;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	// Fonction pour quitter l'application
	@FXML
	public void quitter(ActionEvent event) {
		Platform.exit();
	}

	//M�thode qui permet de se rendre au manuel utilisateur == tuto
	@FXML
	public void tuto() throws MalformedURLException, IOException, URISyntaxException {
		
		InputStream is = MainEtudiant.class.getResourceAsStream("/fr.weshdev.sae401/pdf/user_manual.pdf");

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

	// Fonction qui permet � l'�tudiant d'ouvrir un exercice (t�l�charg�
	// au pr�alable)
	@FXML
	public void ouvrir(ActionEvent event) throws IOException {

		FileChooser fileChooser = new FileChooser();
		File selectedFile = new File("");
		fileChooser.setTitle("Ouvrez votre exercice");

		selectedFile = fileChooser.showOpenDialog(null);
		SaveAfterOpenController.nomExo = stripExtension(selectedFile);
		decrypte(selectedFile);

		// On load la page o� il y a l'exercice
		loadExo();
	}
	
	//M�thode qui strip une extension
	public static String stripExtension(File file) {
        if (file == null) {
            return null;
        }
        String name = file.getName();

        int posPoint = name.lastIndexOf(".");

        if (posPoint == -1) {
            return name;
        }

        return name.substring(0, posPoint);
    }

	// Fonction qui permet d'aller � la page o� se trouve l'exercice
	public void loadExo() throws IOException {
		Stage primaryStage = (Stage) recupScene.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.weshdev.sae401/templates/student/exercise.fxml"));
		Parent root = loader.load();
		c = loader.getController();
		primaryStage.setMaximized(true);
		Scene scene = new Scene(root, MainEtudiant.width, MainEtudiant.height);

		darkModeActivation(scene);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static ExerciseController getC() {
		return c;
	}

	// Fonction qui converti des bytes en String
	public String chaine(byte[] bytes) {
		// Variable qui contiendra la chaine
		String chaine = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
		return chaine;
	}

	// Fonction qui va load les informations du fichier s�lectionn� dans les
	// diff�rents TextField...
	public void decrypte(File file) throws IOException {

		// Variables pour r�cup�rer les informations du fichier
		String consigne, aide, transcription, caraOccul, nbMin;
		int nombreOctetALire, sensiCasse, mode, solution, motsDecouverts, motsIncomplets, lettre, extension;
		File tmpFile;

		// On ouvre le fichier en lecture
		FileInputStream fin = new FileInputStream(file);

		// On r�cup�re la longueur de la consigne + la consigne
		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		consigne = chaine(readNBytes(fin, nombreOctetALire));
		// On met la consigne dans la textField associ�
		ExerciseController.contenuConsigne = consigne;

		// On r�cup�re la longueur de la transcription + la transcription
		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		transcription = chaine(readNBytes(fin, nombreOctetALire));
		// On met la transcription dans le textField associ�
		ExerciseController.contenuTranscription = transcription;

		// On r�cup�re la longueur de l'aide + l'aide
		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		aide = chaine(readNBytes(fin, nombreOctetALire));
		// On met les aides dans le textField associ�
		HintsController.contenuAide = aide;

		// On r�cup�re le caract�re d'occultation
		caraOccul = chaine(readNBytes(fin, 1));
		// On met le caract�re dans le texField associ�
		ExerciseController.caractereOccul = caraOccul;

		// On r�cup�re la reponse de sensiCasse 0 = false, 1 = true
		sensiCasse = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		// On met la variable associ�e en fonction de la r�ponse
		if (sensiCasse == 1) {
			ExerciseController.sensiCasse = true;
		} else {
			ExerciseController.sensiCasse = false;
		}

		// On r�cup�re le mode choisi par l'enseignant 0 = entrainement, 1 =
		// evaluation
		mode = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		// On met la variable associ�e en fonction de la r�ponse
		// Mode Evaluation
		if (mode == 1) {
			ExerciseController.evaluation = true;
			ExerciseController.entrainement = false;

			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
			nbMin = chaine(readNBytes(fin, nombreOctetALire));

			ExerciseController.nbMin = nbMin;

			// Mode Entrainement
		} else {
			ExerciseController.evaluation = false;
			ExerciseController.entrainement = true;

			// On r�cup�re la reponse de l'affiche de la solution 0 = false, 1 = true
			solution = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			// On met la variable associ�e en fonction de la r�ponse
			if (solution == 1) {
				ExerciseController.solution = true;
			} else {
				ExerciseController.solution = false;
			}

			// On r�cup�re la reponse de l'affiche du nombre de mots d�couverts en
			// temps
			// r�el 0 = false, 1 = true
			motsDecouverts = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			// On met la variable associ�e en fonction de la r�ponse
			if (motsDecouverts == 1) {
				ExerciseController.motDecouverts = true;
			} else {
				ExerciseController.motDecouverts = false;
			}

			// On r�cup�re la reponse de l'autorisation du nb min de lettre pour
			// d�couvrir
			// le mot 0 = false, 1 = true
			motsIncomplets = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			// On met la variable associ�e en fonction de la r�ponse
			if (motsIncomplets == 1) {
				ExerciseController.motIncomplet = true;

				// On r�cup�re la reponse du nb min de lettre pour d�couvrir le mot 2 = 2
				// lettres, 3 = 3 lettres
				lettre = ByteBuffer.wrap(readNBytes(fin, 1)).get();

				// On met la variable associ�e en fonction de la r�ponse
				if (lettre == 2) {
					ExerciseController.lettres_2 = true;
					ExerciseController.lettres_3 = false;
				} else {
					ExerciseController.lettres_2 = false;
					ExerciseController.lettres_3 = true;
				}

			} else {
				ExerciseController.motIncomplet = false;
				ExerciseController.lettres_2 = false;
				ExerciseController.lettres_3 = false;
			}
		}

		// On regarde l'extension du media
		extension = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		// Si c'est un mp3, on doit d�chiffrer l'image
		if (extension == 0) {

			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 8)).getInt();

			File tmpFileImage = File.createTempFile("data", ".png");
			FileOutputStream ecritureFileImage = new FileOutputStream(tmpFileImage);
			ecritureFileImage.write(readNBytes(fin, nombreOctetALire));
			ecritureFileImage.close();

			ExerciseController.contenuImage = new Image(tmpFileImage.toURI().toString());

			// On efface le fichier temporaire
			tmpFileImage.deleteOnExit();

			// On lit le mp3
			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 8)).getInt();

			tmpFile = File.createTempFile("data", ".mp3");

		}
		// Sinon c'est un mp4
		else {

			// On r�cup�re ensuite le media
			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 8)).getInt();

			tmpFile = File.createTempFile("data", ".mp4");

		}

		FileOutputStream ecritureFile = new FileOutputStream(tmpFile);
		ecritureFile.write(readAllBytes(fin));
		ecritureFile.close();

		ExerciseController.contenuMedia = new Media(tmpFile.toURI().toString());

		// On efface le fichier temporaire
		tmpFile.deleteOnExit();

		// Fermeture du fichier
		fin.close();
	}

	@FXML
	public void aPropos(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) recupScene.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/about.fxml"));
		Scene scene = new Scene(root, MainEtudiant.width, MainEtudiant.height - 60);
		primaryStage.setScene(scene);

		darkModeActivation(scene);

		primaryStage.setMaximized(true);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	@FXML
	public void retourMenu(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) recuperation.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
		Scene scene = new Scene(root, MainEtudiant.width, MainEtudiant.height);
		primaryStage.setScene(scene);

		darkModeActivation(scene);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	// M�thode pour passer ou non le darkMode
	@FXML
	public void darkMode() {

		if (dark.isSelected()) {
			recupScene.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			recupScene.getScene().getStylesheets()
					.addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			isDark = true;
		} else {
			recupScene.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			recupScene.getScene().getStylesheets().addAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			isDark = false;
		}
	}

	// M�thode qui regarde si le darkMode est actif et l'applique en cons�quence
	// � la scene
	public void darkModeActivation(Scene scene) {
		if (isDark) {
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

	// M�thode qui va lire n bytes (ne marche pas sous java 1.8 donc on la remet ici
	// telle quel
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

	public static byte[] readNBytes(FileInputStream fin, int len) throws IOException {
		if (len < 0) {
			throw new IllegalArgumentException("len < 0");
		}

		List<byte[]> bufs = null;
		byte[] result = null;
		int total = 0;
		int remaining = len;
		int n;
		do {
			byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
			int nread = 0;

			// read to EOF which may read more or less than buffer size
			while ((n = fin.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
				nread += n;
				remaining -= n;
			}

			if (nread > 0) {
				if (MAX_BUFFER_SIZE - total < nread) {
					throw new OutOfMemoryError("Required array size too large");
				}
				total += nread;
				if (result == null) {
					result = buf;
				} else {
					if (bufs == null) {
						bufs = new ArrayList<>();
						bufs.add(result);
					}
					bufs.add(buf);
				}
			}
			// if the last call to read returned -1 or the number of bytes
			// requested have been read then break
		} while (n >= 0 && remaining > 0);

		if (bufs == null) {
			if (result == null) {
				return new byte[0];
			}
			return result.length == total ? result : Arrays.copyOf(result, total);
		}

		result = new byte[total];
		int offset = 0;
		remaining = total;
		for (byte[] b : bufs) {
			int count = Math.min(b.length, remaining);
			System.arraycopy(b, 0, result, offset, count);
			offset += count;
			remaining -= count;
		}

		return result;
	}

	public static byte[] readAllBytes(FileInputStream fin) throws IOException {
		return readNBytes(fin, Integer.MAX_VALUE);
	}
}
