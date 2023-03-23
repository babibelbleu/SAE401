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
    Screen screeen = Screen.getPrimary();
	
	public static Parent root;

	private static double width;
	private static double height;
	
	@Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
        primaryStage.setTitle("Reconstitution - Version Etudiante");
        primaryStage.getIcons().add(new Image(getClass().getResource("/fr.weshdev.sae401/icons/logo.png").toExternalForm()));

        primaryStage.setMaximized(true);
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        width=screenBounds.getWidth();
        height=screenBounds.getHeight();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    public static double getWidth() {
    	return width;
    }

    public static double getHeight() {
    	return height;
    }
}
