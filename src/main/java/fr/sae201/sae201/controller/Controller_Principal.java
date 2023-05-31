package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Controller_Principal {
    @FXML
    private VBox pictoScrollPane;
    @FXML
    private TextField pictoSearchBar;


    @FXML
    void searchPicto(ActionEvent event) {
        pictoScrollPane.getChildren().clear();
        Scene scene =((Button)event.getSource()).getScene();
        scene.setCursor(Cursor.WAIT);
        if (!pictoSearchBar.getText().isEmpty()){
            for (JsonNode jsonNode : ARASAAC.searchPicto(pictoSearchBar.getText())) {
                int id = Integer.parseInt(String.valueOf(jsonNode.get("_id")));
                System.out.println(ARASAAC.getPictogrammeURL(id).get("image").asText());
                ImageView imageView = new ImageView(String.valueOf(ARASAAC.getPictogrammeURL(id).get("image").asText()));
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                pictoScrollPane.getChildren().add(imageView);
            }
        }
        scene.setCursor(Cursor.DEFAULT);

    }


}