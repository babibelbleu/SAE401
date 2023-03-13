package fr.weshdev.sae401;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainEnseignant extends Application {

	public static Parent root;

	//Paramètres de taille d'écran
	public static double width;
	public static double height;

	@Override
	public void start(Stage primaryStage) throws Exception{
		System.out.println(getClass());
		root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/menu.fxml"));
		primaryStage.setTitle("Reconstitution - Version Enseignante");
		//primaryStage.getIcons().add(new Image("path:icons/logo.png"));

		//On affiche le plein écran
		primaryStage.setMaximized(true);
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);

		//On récupère la largeur et la hauteur de l'écran
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		width=screenBounds.getWidth();
		height=screenBounds.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
