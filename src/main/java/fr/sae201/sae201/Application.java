package fr.sae201.sae201;

import fr.sae201.sae201.controller.Controller_Modif_Picto;
import fr.sae201.sae201.controller.Controller_Principal;
import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        showHome(stage);
    }


    public void showHome(Stage stage) throws IOException {
        StageManager.homeStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vues/Vue_Principal.fxml"));
        StageManager.homeController = fxmlLoader;
        Scene scene = new Scene(fxmlLoader.load());
        //scene.getStylesheets().add(String.valueOf(Application.class.getResource("styles/style.css")));
        stage.setTitle("Séquentiels");
        stage.setScene(scene);
        stage.show();
    }

    public static void showAboutModal() throws IOException {
        Stage aboutModal = new Stage();
        aboutModal.initModality(Modality.WINDOW_MODAL);
        aboutModal.setTitle("A propos");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vues/Vue_About.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        aboutModal.setScene(scene);
        aboutModal.showAndWait();
    }

    public static void showPictoEditModal(Pictogram pictogram){
        Stage modifyPictoStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vues/Vue_Modif_Picto.fxml"));
            Parent root = fxmlLoader.load();

            modifyPictoStage.setScene(new Scene(root));
            modifyPictoStage.setTitle("My modal window");
            modifyPictoStage.initModality(Modality.WINDOW_MODAL);
            modifyPictoStage.initOwner(pictogram.getScene().getWindow());
            modifyPictoStage.show();
            ((Controller_Modif_Picto)fxmlLoader.getController()).setEditedPicto(pictogram, StageManager.homeController.getController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}