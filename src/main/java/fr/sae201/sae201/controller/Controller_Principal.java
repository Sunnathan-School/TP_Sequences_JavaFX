package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.Application;
import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.utils.ARASAAC;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Controller_Principal {
    @FXML
    private Menu aboutMenu;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem closeSeqItem;

    @FXML
    private MenuItem exportSeqItem;

    @FXML
    private SplitPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newSeqItem;

    @FXML
    private MenuItem openSeqItem;

    @FXML
    private ListView<Pictogram> pictoAPIList;

    @FXML
    private TextField pictoSearchBar;

    @FXML
    private MenuItem saveSeqItem;

    @FXML
    private FlowPane flowPane;
    @FXML
    private Button searchBTN;

    @FXML
    void closeExistingSequentiel(ActionEvent event) {

    }

    @FXML
    void createNewSequentiel(ActionEvent event) {

    }

    @FXML
    void onExportSequentiel(ActionEvent event) {

    }

    @FXML
    void openExistingSequentiel(ActionEvent event) {

    }

    @FXML
    void saveSequentiel(ActionEvent event) {

    }

    @FXML
    void searchPicto(ActionEvent event) {
        //TODO: ajout d'un Cursor WAIT
        if (!pictoSearchBar.getText().equals("")){
            pictoAPIList.getItems().clear();

            JsonNode response = ARASAAC.searchPicto(pictoSearchBar.getText().split(" "));
            for (JsonNode res : response) {
                //System.out.println(re.get("_id"));
                Integer pictoID = Integer.parseInt(res.get("_id").toString());
                Pictogram pictogram = new Pictogram(pictoID);
                Task<Pictogram> task = new Task<Pictogram>() {
                    @Override
                    protected Pictogram call() throws Exception {
                        Pictogram pictogram = new Pictogram(pictoID);
                        return pictogram;
                    }
                };
                Thread thread = new Thread(task);

                thread.setDaemon(true);
                thread.start();

                task.setOnSucceeded(workerStateEvent -> {
                    pictoAPIList.getItems().add(task.getValue());
                    System.out.println("Ajout");
                });

            }
        }
    }

    @FXML
    void showAboutModal(ActionEvent event) throws IOException {
        Application.showAboutModal();
    }

    @FXML
    void initialize(){
        flowPane.getChildren().addListener((ListChangeListener<Node>) change -> {
            for (Node child : flowPane.getChildren()) {
                Pictogram pictogram = (Pictogram) child;
                pictogram.setOnContextMenuRequested(contextMenuEvent -> {
                    ContextMenu menu = new ContextMenu();
                    menu.setAutoHide(true);

                    ImageView personalizeLogo = new ImageView(new Image(String.valueOf(Application.class.getResource("images/editIcon.png"))));
                    ImageView deleteLogo = new ImageView(new Image(String.valueOf(Application.class.getResource("images/deleteIcon.png"))));
                    personalizeLogo.setFitWidth(20);
                    personalizeLogo.setPreserveRatio(true);
                    deleteLogo.setFitWidth(20);
                    deleteLogo.setPreserveRatio(true);

                    MenuItem personalize = new MenuItem("Personnaliser");

                    personalize.setGraphic(personalizeLogo);
                    personalize.setOnAction(onDelete -> {

                        Application.showPictoEditModal(pictogram);
                        System.out.println("edit");
                    });

                    MenuItem delete = new MenuItem("Supprimer");

                    delete.setGraphic(deleteLogo);
                    delete.setOnAction(onDelete -> {
                        flowPane.getChildren().remove(pictogram);
                        System.out.println("delete");
                    });

                    menu.getItems().addAll(personalize, delete);
                    menu.show(pictogram, Side.BOTTOM,25,0);
                });
            }
        });
        pictoAPIList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2){
                Pictogram pictogram = pictoAPIList.getSelectionModel().getSelectedItem();
                if (pictogram != null){
                    //pictogram.setPrefHeight(100);
                    flowPane.getChildren().add(pictogram.clonePictogram());

                }
            }
        });
        pictoAPIList.setCellFactory(param -> new ListCell<Pictogram>(){
            private final VBox vbox = new VBox();
            private final ImageView imageView = new ImageView();
            {
                vbox.getChildren().addAll(imageView);
                vbox.setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(Pictogram pictogram, boolean empty) {
                super.updateItem(pictogram, empty);
                if (empty || pictogram == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(pictogram.getImageView().getImage().getUrl()));
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(100);

                    setGraphic(vbox);
                }
            }
        });
    }
    private static final DataFormat PICTOGRAM_FORMAT = new DataFormat("application/x-pictogram");

    @FXML
    void ListViewDragDetected(MouseEvent event) {

    }

    @FXML
    void flowPaneDragDropped(DragEvent event) {

    }

    @FXML
    void flowPaneDragOver(DragEvent event) {

    }
}