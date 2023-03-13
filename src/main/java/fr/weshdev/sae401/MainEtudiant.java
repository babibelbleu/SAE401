package fr.weshdev.sae401;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainEtudiant extends Application{
	
	public static Parent root;
	
	//Param�tres de taille d'�cran
	public static double width;
	public static double height;
	
	@Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
        primaryStage.setTitle("Reconstitution - Version Etudiante");
        primaryStage.getIcons().add(new Image(getClass().getResource("/fr.weshdev.sae401/icons/logo.png").toExternalForm()));
        
        //On affiche le plein �cran
        primaryStage.setMaximized(true);
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);
        
        //On r�cup�re la largeur et la hauteur de l'�cran
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
