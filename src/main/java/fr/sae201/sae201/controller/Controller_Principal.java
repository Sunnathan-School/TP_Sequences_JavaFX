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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller_Principal {
    @FXML
    private MenuItem closeSeqItem;

    @FXML
    private MenuItem exportSeqItem;

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
                setLocked(false);
            }
        }
    }

    //Exportation du séquentiel au format PDF
    @FXML
    void onExportSequentiel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("sequentiel");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF", "*.pdf");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        File file = fileChooser.showSaveDialog(StageManager.homeStage);
        if (file!=null){
            generatePDF(file);
        }
    }

    //ouverture d'un séquentiel existant
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
        StageManager.homeStage.getScene().setCursor(Cursor.WAIT);

        if (!pictoSearchBar.getText().isEmpty() || (!pictoCategories.getSelectionModel().getSelectedItem().equals("..."))) {
            pictoAPIList.getItems().clear();

            // Si searchBar vide ==> on récupère la catégorie
            String searchText = pictoSearchBar.getText();
            String selectedCategory = pictoCategories.getSelectionModel().getSelectedItem();

            Task<List<Pictogram>> searchTask = new Task<List<Pictogram>>() {
                @Override
                protected List<Pictogram> call() throws Exception {
                    List<Pictogram> pictograms = new ArrayList<>();

                    if (searchText.isEmpty()) {
                        JsonNode response = ARASAAC.searchPicto(selectedCategory);
                        for (JsonNode res : response) {
                            Integer pictoID = Integer.parseInt(res.get("_id").toString());
                            Pictogram pictogram = new Pictogram(pictoID);
                            pictograms.add(pictogram);
                        }
                    } else {
                        String[] searchTerms = searchText.split(" ");
                        JsonNode response = ARASAAC.searchPicto(searchTerms);
                        for (JsonNode res : response) {
                            Integer pictoID = Integer.parseInt(res.get("_id").toString());
                            Pictogram pictogram = new Pictogram(pictoID);
                            pictograms.add(pictogram);
                        }
                    }

                    return pictograms;
                }
            };

            searchTask.setOnSucceeded(workerStateEvent -> {
                pictoAPIList.getItems().addAll(searchTask.getValue());
                StageManager.homeStage.getScene().setCursor(Cursor.DEFAULT);
            });

            Thread thread = new Thread(searchTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    void showAboutModal(ActionEvent event) throws IOException {
        Application.showAboutModal();
    }

    @FXML
    void initialize(){
        String[] categories = {"...","Alimentation", "Loisirs", "Place", "Education", "Temps", "Divers", "Mobilité",
                "Religion", "Travail", "Communication", "Documents", "Connaissances", "Objets"};

        for (String category : categories) {
            pictoCategories.getItems().add(category);
        }
        pictoCategories.getSelectionModel().select(0);

        //AUTO COMPLETION
        List<String> rawKeywords = ARASAAC.getKeywords();

        ContextMenu contextMenu = new ContextMenu();
        pictoSearchBar.setContextMenu(contextMenu);

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

        setLocked(true);

        //COntextMenu Picto
        flowPane.getChildren().addListener((ListChangeListener<Node>) change -> {
            for (Node child : flowPane.getChildren()) {
                Pictogram pictogram = (Pictogram) child;
                pictogram.setOnContextMenuRequested(contextMenuEvent -> {
                    ContextMenu menu = new ContextMenu();
                    menu.setAutoHide(true);

                    ImageView personalizeLogo = new ImageView(new Image(String.valueOf(Application.class.getResource("images/editIcon.png"))));
                    ImageView deleteLogo = new ImageView(new Image(String.valueOf(Application.class.getResource("images/deleteIcon.png"))));

                    ImageView leftArrow = new ImageView(new Image(String.valueOf(Application.class.getResource("images/left-arrow.png"))));
                    ImageView rightArrow = new ImageView(new Image(String.valueOf(Application.class.getResource("images/right-arrow.png"))));


                    personalizeLogo.setFitWidth(20);
                    personalizeLogo.setPreserveRatio(true);
                    deleteLogo.setFitWidth(20);
                    deleteLogo.setPreserveRatio(true);

                    leftArrow.setFitWidth(20);
                    leftArrow.setPreserveRatio(true);

                    rightArrow.setFitWidth(20);
                    rightArrow.setPreserveRatio(true);

                    MenuItem moveLeft = new MenuItem("Déplacer a gauche");
                    moveLeft.setGraphic(leftArrow);
                    moveLeft.setOnAction(event -> {
                        System.out.println("Déplacer a gauche");
                        int a = flowPane.getChildren().indexOf(pictogram);
                        if (a != 0){
                            int b = a-1;
                            Pictogram aPicto = ((Pictogram) flowPane.getChildren().get(a));
                            Pictogram bPicto = (Pictogram) flowPane.getChildren().get(b);

                            flowPane.getChildren().set(b, aPicto.clonePictogram());
                            flowPane.getChildren().set(a, bPicto.clonePictogram());

                        }
                    });

                    MenuItem moveRight = new MenuItem("Déplacer a droite");
                    moveRight.setGraphic(rightArrow);
                    moveRight.setOnAction(event -> {
                        System.out.println("Déplacer a droite");
                        int a = flowPane.getChildren().indexOf(pictogram);
                        System.out.println(a + " " + flowPane.getChildren().size());
                        if (a != flowPane.getChildren().size()-1){
                            int b = a+1;
                            Pictogram aPicto = ((Pictogram) flowPane.getChildren().get(a));
                            Pictogram bPicto = (Pictogram) flowPane.getChildren().get(b);

                            flowPane.getChildren().set(b, aPicto.clonePictogram());
                            flowPane.getChildren().set(a, bPicto.clonePictogram());

                        }
                    });



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

                    menu.getItems().addAll(moveLeft, moveRight,personalize, delete);
                    menu.show(pictogram, Side.BOTTOM,25,0);
                });
            }
        });
        //Ajout Picto
        pictoAPIList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2){
                Pictogram pictogram = pictoAPIList.getSelectionModel().getSelectedItem();
                if (pictogram != null){
                    //pictogram.setPrefHeight(100);
                    flowPane.getChildren().add(pictogram.clonePictogram());

                }
            }
        });

        //Picto dans listView
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

    private void setLocked(boolean lock){
        pictoSearchBar.setDisable(lock);
        pictoCategories.setDisable(lock);
        searchBTN.setDisable(lock);
        saveSeqItem.setDisable(lock);
        closeSeqItem.setDisable(lock);
        exportSeqItem.setDisable(lock);
    }
    private void generatePDF(File file) {
        //SAVE AU FORMAT PDF
        PDDocument document = new PDDocument();

        WritableImage img = flowPane.snapshot(new SnapshotParameters(), null);
        PDPage page = new PDPage();

        document.addPage(page);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            File tempFile = File.createTempFile("pictogram", ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", tempFile);

            contentStream.drawImage(
                    PDImageXObject.createFromFileByContent(tempFile, document),
                    (float) 0, (float) 0
            );
            contentStream.close();
            tempFile.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            document.save(file.getAbsolutePath());
            document.close();
            Alerts.showAlertWithoutHeaderText(Alert.AlertType.INFORMATION, "Exportation Terminé", "Votre PDF a été généré avec succès");
        } catch (IOException e) {
            e.printStackTrace();
        }
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