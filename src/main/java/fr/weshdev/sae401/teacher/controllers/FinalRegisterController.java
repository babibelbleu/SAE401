package fr.weshdev.sae401.teacher.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class FinalRegisterController implements Initializable {

	@FXML
	private Text recupScene;

	// M�thode d'initialisation de la page
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Conteneurs en octets de la consigne
		byte[] contenuConsigne = ApercuController.contenuConsigne.getBytes();
		byte[] longueurConsigne = getLongueur(ApercuController.contenuConsigne);

		// Conteneurs en octets de la transcription
		byte[] contenuTranscription = ApercuController.contenuTranscription.getBytes();
		byte[] longueurTranscription = getLongueur(ApercuController.contenuTranscription);

		// Conteneurs en octets de l'aide
		byte[] contenuAide = ApercuController.contenuAide.getBytes();
		byte[] longueurAide = getLongueur(ApercuController.contenuAide);

		// Caract�re d'occultation
		byte[] caraOccul = OptionsController.caraOccul.getBytes();

		// Conteneurs du media
		byte[] contenuMedia = null;
		byte[] longueurMedia = null;

		//Conteneur de l'image (pour mp3)
		byte[] contenuImage = null;
		byte[] longueurImage = null;

		// On r�cup�re le media / image et on lui demande sa taille
		try {
			
			FileInputStream fus = new FileInputStream(ImportRessourceController.cheminVideo);
			contenuMedia = readAllBytes(fus);
			longueurMedia = ByteBuffer.allocate(8).putInt(contenuMedia.length).array();

			if(ImportRessourceController.contenuImage != null) {
				FileInputStream fusImg = new FileInputStream(ImportRessourceController.cheminImg);
				contenuImage = readAllBytes(fusImg);
				longueurImage = ByteBuffer.allocate(8).putInt(contenuImage.length).array();
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			// On ouvre un fichier o� on va enregistrer les informations
			// On lui donne l'endroit o� il doit �tre enregistr� et le nom
			File file = new File(NewExerciseController.contenuRepertoire, NewExerciseController.contenuNomExo + ".rct");
			FileOutputStream out = new FileOutputStream(file);

			// On y �crit la consigne
			out.write(longueurConsigne);
			out.write(contenuConsigne);

			// On y �crit la transcription
			out.write(longueurTranscription);
			out.write(contenuTranscription);

			// On y �crit les aides
			out.write(longueurAide);
			out.write(contenuAide);

			// On y �crit le caract�re d'occultation
			out.write(caraOccul);

			// Si la sensibilit� � la casse est activ�e ou non
			if (OptionsController.sensiCasse == true) {
				out.write(1);
			} else {
				out.write(0);
			}

			// On y �crit le mode (sur 1 octet)
			// Si c'est le mode entrainement
			if (OptionsController.entrainement == true) {
				out.write(0);

				// Si l'affichage de la solution est autoris�
				if (OptionsController.solution == true) {
					out.write(1);
				} else {
					out.write(0);
				}

				// Si l'affiche du nombre de mots d�couverts en temps r�el est autoris�
				if (OptionsController.motDecouverts == true) {
					out.write(1);
				} else {
					out.write(0);
				}

				// Si les mots incomplets sont autoris�s
				if (OptionsController.motIncomplet == true) {
					out.write(1);

					//On pr�cise le nombre de lettres autoris�es
					if (OptionsController.lettres_2 == true) {
						out.write(2);
					} else {
						out.write(3);
					}

				} else {
					out.write(0);
				}
			}

			// Sinon il s'agit du mode evaluation
			else {

				// Limite de temps (pour le mode Evaluation)
				byte[] nbMin = OptionsController.nbMin.getBytes();
				byte[] longueurNbMin = getLongueur(OptionsController.nbMin);

				out.write(1);
				// On �crit la limite de temps
				out.write(longueurNbMin);
				out.write(nbMin);
			}

			//S'il s'agit d'une extension mp3
			if(getExtension(ImportRessourceController.contenuMedia.getSource()).compareTo(".mp3") == 0) {
				out.write(0);
				out.write(longueurImage);
				out.write(contenuImage);
			}
			//S'il s'agit d'une extension mp4
			else {
				out.write(1);
			}

			// On y �crit les donn�es du media
			out.write(longueurMedia);
			out.write(contenuMedia);

			// Fermeture du fichier
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


		//On remet toutes les variables statiques � null
		NewExerciseController.contenuNomExo = null;
		NewExerciseController.contenuRepertoire = null;
		ImportRessourceController.contenuMedia = null;
		ImportRessourceController.contenuImage = null;
		ApercuController.contenuAide = null;
		ApercuController.contenuConsigne = null;
		ApercuController.contenuTranscription = null;
		OptionsController.caraOccul = null;
		OptionsController.sensiCasse = false;
		OptionsController.entrainement = false;
		OptionsController.evaluation = false;
		OptionsController.lettres_2 = false;
		OptionsController.lettres_3 = false;
		OptionsController.motDecouverts = false;
		OptionsController.motIncomplet = false;
		OptionsController.solution = false;
		OptionsController.nbMin = null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////       METHODES GENERALES         /////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

	// Bouton Quitter qui permet � l'enseignant de quitter l'application (disponible
	// sur toutes les pages)
	@FXML
	public void quitter(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	public void okay() {
		recupScene.getScene().getWindow().hide();
	}

	//M�thode qui r�cup�re la lonngueur en byte d'une chaine de caract�re
	private byte[] getLongueur(String chaine) {

		int nbCara = 0;

		for (int i = 0; i < chaine.length(); i++) {
			nbCara++;
		}
		return ByteBuffer.allocate(4).putInt(nbCara).array();
	}

	private String getExtension(String filePath) {
		if (filePath == null) {
			return null;
		}
		int posPoint = filePath.lastIndexOf(".");

		if (posPoint == -1) {
			return null;
		}

		return filePath.substring(posPoint);
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