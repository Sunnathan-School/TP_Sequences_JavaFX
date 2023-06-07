package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.Application;
import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.models.Pictograms.PictogramCoord;
import fr.sae201.sae201.models.SaveManager;
import fr.sae201.sae201.models.SequentielSave;
import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import fr.sae201.sae201.utils.Alerts;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller_Principal {
    @FXML
    private TextField pictoSearchBar;
    @FXML
    private GridPane mainGrid;
    @FXML
    private Menu aboutMenu;
    @FXML
    private MenuItem closeSeqItem;
    @FXML
    private ListView<ImageView> pictoAPIList;

    private String[] keywordsArray;

    @FXML
    private SplitPane mainPane;

    @FXML
    private Button searchBTN;
    @FXML
    private MenuItem exportSeqItem;
    @FXML
    private MenuItem openSeqItem;
    @FXML
    private MenuItem saveSeqItem;

    private List<Task<ImageView>> imageLoadingTasks;

    private ImageView currentImageView;
    private HashMap<PictogramCoord, ImageView> coordHashMap = new HashMap<>();
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
        if (mainGrid.getChildren().size() > 1){  //Pop-up save si gridPane n'est pas vide
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.INFORMATION, "Attention", "Vous avez déjà un projet en cours, merci de le fermer avant !");
        }else {//ouverture popUp
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("sequentiel");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Séquentiel (.seq)", "*.seq");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            File file = fileChooser.showSaveDialog(StageManager.homeStage);
            if (file != null){
//                mainGrid.getChildren().clear();
//                mainGrid.getColumnConstraints().clear();
//                mainGrid.getRowConstraints().clear();
//                mainGrid.getStyleClass().add("grid-pane");
//                mainGrid.setAlignment(Pos.CENTER);
//                mainGrid.setGridLinesVisible(true);

//                ColumnConstraints columnConstraints = new ColumnConstraints();
//                columnConstraints.setHgrow(Priority.SOMETIMES);
//                columnConstraints.setMinWidth(10);
//                columnConstraints.setPrefWidth(100);
//                mainGrid.getColumnConstraints().add(columnConstraints);
//
//                RowConstraints rowConstraints = new RowConstraints();
//                rowConstraints.setMinHeight(10);
//                rowConstraints.setPrefHeight(30);
//                rowConstraints.setVgrow(Priority.SOMETIMES);
//                mainGrid.getRowConstraints().add(rowConstraints);
                //mainGrid.getChildren().clear();
                //mainGrid.getColumnConstraints().clear();
                //mainGrid.getRowConstraints().clear();
                SaveManager.setProjectPath(file.getAbsolutePath());
                SaveManager.updateTitle();
                lockPane(false);

                mainGrid.setGridLinesVisible(true);
                //addRowAndColumnToGridPane(mainGrid, 0,0);
                autoResizeGridPane(mainGrid);
            }

        }
    }

    @FXML
    void openExistingSequentiel(ActionEvent event) {

    }

    @FXML
    void saveSequentiel(ActionEvent event) {
        System.out.println("Save Seq");
        SequentielSave sequentielSave = new SequentielSave(SaveManager.getProjectPath());
        sequentielSave.convertHashmap(coordHashMap,mainGrid);
        try {
            sequentielSave.saveSeqFile();
        } catch (IOException e) {
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.ERROR, "Erreur", "Nous avons rencontré une erreur dans la sauvegarde.");
            throw new RuntimeException(e);
        }
    }

    @FXML
    void closeExistingSequentiel(ActionEvent event) {
        SaveManager.setProjectPath("");
        SaveManager.updateTitle();
        pictoAPIList.getItems().clear();
        pictoSearchBar.clear();
        Node node = mainGrid.getChildren().get(0);

        mainGrid.getChildren().clear();
        mainGrid.getChildren().add(0,node);
        System.out.println(node.toString());
        Group t = (Group) node;
        mainGrid.setGridLinesVisible(false);
        for (Node child : t.getChildren()) {
            System.out.println("Line False" + child.toString());
        }
        t.getChildren().clear();
        mainGrid.setGridLinesVisible(true);

        for (Node child : t.getChildren()) {
            System.out.println("Line True" + child.toString());
        }
        lockPane(true);
    }
    @FXML
    void showAboutModal(ActionEvent event) throws IOException {
        Application.showAboutModal();
    }


    @FXML
    void initialize(){
        mainGrid.getStyleClass().add("gridpane");
        pictoSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBTN.fire();
            }
        });
        initDragAndDrop();

        if (!SaveManager.isProjectPathDefined()){
            lockPane(true);
        }

        mainPane.setOnMouseClicked(mouseEvent -> {
            if (!SaveManager.isProjectPathDefined()){
                Alerts.showAlertWithoutHeaderText(Alert.AlertType.ERROR, "Vous n'avez pas créer de projet", "Merci de créer un nouveau Séquentiel avant !");
            }
        });


        List<String> rawKeywords = ARASAAC.getKeywords();

        ContextMenu contextMenu = new ContextMenu();
        pictoSearchBar.setContextMenu(contextMenu);

        // Configuration de l'autocomplétion
        pictoSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            contextMenu.getItems().clear();
            if (!newValue.isEmpty()) {
                int count = 0;
                for (String keyword : rawKeywords) {
                    if (keyword.toLowerCase().startsWith(newValue.toLowerCase())) {
                        MenuItem menuItem = new MenuItem(keyword);
                        menuItem.setOnAction(event -> {
                            pictoSearchBar.setText(keyword);
                            contextMenu.hide();
                        });
                        contextMenu.getItems().add(menuItem);
                        count++;
                        if (count >= 10) break; // Limite le nombre d'éléments à 10.
                    }
                }
                if (!contextMenu.getItems().isEmpty()) {
                    contextMenu.show(pictoSearchBar, Side.BOTTOM, 0, 0);
                } else {
                    contextMenu.hide();
                }
            } else {
                contextMenu.hide();
            }
        });


        // Configuration des touches pressées
        pictoSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && contextMenu.isShowing()) {
                // Met l'accent sur le premier élément du menu lorsque l'utilisateur appuie sur la touche bas
                contextMenu.getSkin().getNode().requestFocus();
                ((MenuItem)contextMenu.getItems().get(0)).fire();
            }
        });
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
                System.out.println(selectedImageView.getImage().getUrl());
                currentImageView=selectedImageView;
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
                autoResizeGridPane(mainGrid);

                ImageView imageView = new ImageView(image);
                //Pane pane = new Pane();
                //pane.getChildren().add(imageView);
                imageView.setFitWidth(mainGrid.getWidth()/mainGrid.getColumnCount());
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(mainGrid.getHeight()/mainGrid.getRowCount());
                mainGrid.add(imageView, columnIndex, rowIndex);
                coordHashMap.put(new PictogramCoord(columnIndex,rowIndex), currentImageView);
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
    }

    private void autoResizeGridPane(GridPane gridPane){
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

    private void lockPane(boolean lock){
        pictoSearchBar.setDisable(lock);
        mainGrid.setDisable(lock);
        pictoAPIList.setDisable(lock);
        searchBTN.setDisable(lock);
        saveSeqItem.setDisable(lock);
        exportSeqItem.setDisable(lock);
        closeSeqItem.setDisable(lock);
    }

}