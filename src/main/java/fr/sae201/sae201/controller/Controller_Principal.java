package fr.sae201.sae201.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fr.sae201.sae201.Application;
import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.models.Pictograms.PictogramSerializableAtrributes;
import fr.sae201.sae201.models.ProjectManager;
import fr.sae201.sae201.models.Sequentiel;
import fr.sae201.sae201.models.StageManager;
import fr.sae201.sae201.utils.ARASAAC;
import fr.sae201.sae201.utils.Alerts;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
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
    private ComboBox<String> pictoCategories;

    @FXML
    private FlowPane flowPane;
    @FXML
    private Button searchBTN;

    //fermeture du séquentiel
    @FXML
    void closeExistingSequentiel(ActionEvent event) {
        setLocked(true);
        ProjectManager.setProjectPath("");
        ProjectManager.updateTitle();
        pictoAPIList.getItems().clear();
        flowPane.getChildren().clear();
        pictoSearchBar.setText("");
    }

    //création du séquentiel
    @FXML
    void createNewSequentiel(ActionEvent event) {
        if (flowPane.getChildren().size() > 1){  //Pop-up save si gridPane n'est pas vide
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.INFORMATION, "Attention", "Vous avez déjà un projet en cours, merci de le fermer avant !");
        }else {//ouverture popUp
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("sequentiel");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Séquentiel (.seq)", "*.seq");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            File file = fileChooser.showSaveDialog(StageManager.homeStage);
            if (file != null) {
                ProjectManager.setProjectPath(file.getAbsolutePath());
                ProjectManager.updateTitle();
                setLocked(false);
            }
        }
    }

    //Exportation du séquentiel au format PDF
    @FXML
    void onExportSequentiel(ActionEvent event) {

    }

    @FXML
    void openExistingSequentiel(ActionEvent event) {
        if (flowPane.getChildren().size() > 1){  //Pop-up save si gridPane n'est pas vide
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.INFORMATION, "Attention", "Vous avez déjà un projet en cours, merci de le fermer avant !");
        }else {//ouverture popUp
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("sequentiel");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Séquentiel (.seq)", "*.seq");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            flowPane.getChildren().clear();
            File file = fileChooser.showOpenDialog(StageManager.homeStage);
            if (file != null) {
                ProjectManager.setProjectPath(file.getAbsolutePath());
                ProjectManager.updateTitle();
                setLocked(false);
                Sequentiel seq = new Sequentiel();
                try {
                    seq.loadSequentiel();
                    for (PictogramSerializableAtrributes pictogramAttributes : seq.getPictograms()) {
                        Pictogram picto = new Pictogram(pictogramAttributes.getPictoId());
                        picto.setAtrributes(pictogramAttributes);
                        picto.updatePictogram();
                        flowPane.getChildren().add(picto);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    //sauvegarde d'un séquentiel
    @FXML
    void saveSequentiel(ActionEvent event) {
        Sequentiel seq = new Sequentiel();
        seq.convertFlowPaneToList(flowPane);
        seq.saveSequentiel();
    }

    //méthode de recherche d'un pictogramme dans le champ de texte pour 
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
        setLocked(true);
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

    private void setLocked(boolean lock){
        pictoSearchBar.setDisable(lock);
        pictoCategories.setDisable(lock);
        searchBTN.setDisable(lock);
        saveSeqItem.setDisable(lock);
        closeSeqItem.setDisable(lock);
    }

    public void updatePicto(Pictogram source, Pictogram newPicto){
        for (Node child : flowPane.getChildren()) {
            System.out.println(child);
        }
        System.out.println("To remove" + source);
        int index = flowPane.getChildren().indexOf(source);
        System.out.println(index);
        flowPane.getChildren().remove(source);

        flowPane.getChildren().add(index, newPicto);

    }
}