package fr.weshdev.sae401;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class MainEnseignant extends Application {

	public static Parent root;

	//Paramètres de taille d'écran
	public static double width;
	public static double height;

	@Override
	public void start(Stage primaryStage) throws Exception{
		System.out.println(getClass());
		FXMLLoader loader = new FXMLLoader();
		FileInputStream fis = new FileInputStream("src/main/resources/templates/teacher/menu.fxml");
		root = loader.load(fis);
		primaryStage.setTitle("Reconstitution - Version Enseignante");
		primaryStage.getIcons().add(new Image("/Image/Logo_Reconstitution.png"));

		//On affiche le plein écran
		primaryStage.setMaximized(true);
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);

		//On récupère la largeur et la hauteur de l'écran
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		width=screenBounds.getWidth();
		height=screenBounds.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().addAll(getClass().getResource("/css/menu_and_buttons.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
