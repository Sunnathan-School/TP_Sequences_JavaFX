package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.utils.ARASAAC;
import fr.sae201.sae201.utils.Alerts;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

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
                Pictogram pictogram = new Pictogram();

                Task<ImageView> task = pictogram.generatePictoImage(id);
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

    @FXML
    void createNewSequentiel(ActionEvent event) {
        System.out.println("Create New Seq");
        //Pop-up save si gridPane n'est pas vide
            //Save

    }

    @FXML
    void openExistingSequentiel(ActionEvent event) {
        System.out.println("Open Seq");
        //Ouverture FileExplorer ==> Choix d'un ".seq"
        //Ajout de tous les picto dans le grid Pane
    }

    @FXML
    void saveSequentiel(ActionEvent event) {
        System.out.println("Save Seq");
        //Si chemin spécifier
            //save
        //sinon
            //open Explorer ==> Choix du nom
            //Save
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

                addRowAndColumnToGridPane(mainGrid,columnIndex,rowIndex);

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(mainGrid.getWidth()/mainGrid.getColumnCount());
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(mainGrid.getHeight()/mainGrid.getRowCount());

                mainGrid.add(imageView, columnIndex, rowIndex);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });



    }
    private void addRowAndColumnToGridPane(GridPane gridPane, int columnIndex, int rowIndex) {
        int numRows = gridPane.getRowCount();
        int numCols = gridPane.getColumnCount();




        if (rowIndex == numRows - 1 || numRows == 0) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }

        if (columnIndex == numCols - 1 || numCols == 0) {
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }


        for (ColumnConstraints columnConstraint : gridPane.getColumnConstraints()) {
            columnConstraint.setPrefWidth(gridPane.getWidth()/gridPane.getColumnCount());
            columnConstraint.setMaxWidth(gridPane.getWidth()/gridPane.getColumnCount());
            columnConstraint.setMinWidth(gridPane.getWidth()/gridPane.getColumnCount());
            columnConstraint.setHgrow(Priority.ALWAYS);
            columnConstraint.setFillWidth(true);
            columnConstraint.setHalignment(HPos.CENTER);
        }

        for (RowConstraints rowConstraint : gridPane.getRowConstraints()) {
            rowConstraint.setPrefHeight(gridPane.getHeight()/gridPane.getRowCount());
            rowConstraint.setMaxHeight(gridPane.getHeight()/gridPane.getRowCount());
            rowConstraint.setMinHeight(gridPane.getHeight()/gridPane.getRowCount());
            rowConstraint.setVgrow(Priority.ALWAYS);
            rowConstraint.setFillHeight(true);
            rowConstraint.setValignment(VPos.CENTER);
        }



        for (Node child : gridPane.getChildren()) {
            if (child instanceof ImageView imageView){
                imageView.setFitWidth(mainGrid.getWidth()/mainGrid.getColumnCount());

                imageView.setFitHeight(mainGrid.getHeight()/mainGrid.getRowCount());
            }
        }
    }

}