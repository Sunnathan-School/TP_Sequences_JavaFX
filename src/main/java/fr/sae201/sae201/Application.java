package fr.sae201.sae201;

import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        showHome(stage);
        //ARASAAC.getKeywords();
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