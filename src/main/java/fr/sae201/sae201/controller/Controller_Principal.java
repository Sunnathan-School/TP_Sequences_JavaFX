package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.utils.ARASAAC;
import fr.sae201.sae201.utils.Alerts;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller_Principal {
    @FXML
    private TextField pictoSearchBar;
    @FXML
    private GridPane mainGrid;
    @FXML
    private ListView<ImageView> pictoAPIList;

    @FXML
    private Button searchBTN;

    private List<Task<ImageView>> imageLoadingTasks;
    @FXML
    void searchPicto(ActionEvent event) {
        pictoAPIList.getItems().clear();
        Scene scene =((Button)event.getSource()).getScene();
        scene.setCursor(Cursor.WAIT);
        if (!pictoSearchBar.getText().isEmpty()){
            JsonNode res = ARASAAC.searchPicto(pictoSearchBar.getText().split(" "));
            if (res == null) {
                Alerts.showAlertWithoutHeaderText(Alert.AlertType.ERROR, "Erreur","Aucun pictogramme n'a été trouvé");
                scene.setCursor(Cursor.DEFAULT);
                return;
            }
            imageLoadingTasks = new ArrayList<>();
            for (JsonNode jsonNode : res) {
                int id = Integer.parseInt(String.valueOf(jsonNode.get("_id")));
                Task<ImageView> task = createImageLoadingTask(id);
                imageLoadingTasks.add(task);
                task.setOnSucceeded(e -> {
                    ImageView imageView = task.getValue();
                    pictoAPIList.getItems().add(imageView);

                });
                task.setOnFailed(e -> {
                    Alerts.showAlertWithoutHeaderText(Alert.AlertType.ERROR,"Erreur","Erreur lors du chargement de l'image : " + e.getSource().getException().getMessage());
                });
                new Thread(task).start();
            }

        }else {
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.ERROR,"Erreur","Aucun pictogramme n'a été trouvé");
        }
        scene.setCursor(Cursor.DEFAULT);


    }


    private Task<ImageView> createImageLoadingTask(int id) {
        return new Task<>() {
            @Override
            protected ImageView call() throws Exception {
                String imageUrl = ARASAAC.getPictogrammeURL(id).get("image").asText();
                Image image = new Image(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setId(String.valueOf(id));
                return imageView;
            }
        };
    }

    @FXML
    void initialize(){
        pictoSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBTN.fire();
            }
        });

        initDragAndDrop();





    }

    private int getColumnIndex(double x) {
        double totalWidth = mainGrid.getWidth();
        double columnWidth = totalWidth / mainGrid.getColumnCount();
        return (int) (x / columnWidth);
    }

    private int getRowIndex(double y) {
        double totalHeight = mainGrid.getHeight();
        double rowHeight = totalHeight / mainGrid.getRowCount();
        return (int) (y / rowHeight);
    }


    private void initDragAndDrop() {
        // Gestion du glisser-déposer depuis la ListView
        pictoAPIList.setOnDragDetected(event -> {
            ImageView selectedImageView = pictoAPIList.getSelectionModel().getSelectedItem();

            if (selectedImageView != null) {
                Dragboard dragboard = pictoAPIList.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(selectedImageView.getImage());
                dragboard.setContent(content);
            }

            event.consume();
        });

        // Gestion du survol de la case dans le GridPane
        mainGrid.setOnDragOver(event -> {
            if (event.getGestureSource() != mainGrid && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Gestion du largage dans la case du GridPane
        mainGrid.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasImage()) {
                Image image = dragboard.getImage();
                int columnIndex = getColumnIndex(event.getX());
                int rowIndex = getRowIndex(event.getY());

                ImageView imageView = new ImageView(image);

                mainGrid.add(imageView, columnIndex, rowIndex);
               addRowAndColumnToGridPane(mainGrid);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void addRowAndColumnToGridPane(GridPane gridPane) {
        gridPane.addRow(1, new Pane());
    }




}