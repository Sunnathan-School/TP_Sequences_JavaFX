package fr.sae201.sae201;

import fr.sae201.sae201.models.Pictograms.PictogramActions;
import fr.sae201.sae201.models.Pictograms.PictogramHair;
import fr.sae201.sae201.models.Pictograms.PictogramResolution;
import fr.sae201.sae201.models.Pictograms.PictogramSkin;
import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        //showHome(stage);
        ARASAAC.getPictogrammeURL(31348, false, true, "none", PictogramActions.NONE, PictogramResolution.SMALL, PictogramSkin.WHITE, PictogramHair.BROWN);
    }


    public void showHome(Stage stage) throws IOException {
        StageManager.homeStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}