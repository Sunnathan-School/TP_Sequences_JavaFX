package fr.sae201.sae201;

import fr.sae201.sae201.models.StageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        showHome(stage);

        //Récupère le pictogramme
        //JsonNode res = ARASAAC.getPictogrammeURL(31348, false, true, "none", PictogramActions.NONE, PictogramResolution.SMALL, PictogramSkin.WHITE, PictogramHair.BROWN);
        //System.out.println(res.get("image")); ==> lien .png

    }


    public void showHome(Stage stage) throws IOException {
        StageManager.homeStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Vue_Principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Séquentiels");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}