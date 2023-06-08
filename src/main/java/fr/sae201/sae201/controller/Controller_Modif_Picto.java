package fr.sae201.sae201.controller;

import fr.sae201.sae201.Application;
import fr.sae201.sae201.models.Pictograms.*;
import fr.sae201.sae201.models.StageManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Modif_Picto {

    @FXML
    private ChoiceBox<PictogramActions> actionChoiceBox;

    @FXML
    private ColorPicker backgroundColorPicker;

    @FXML
    private CheckBox colorCheckBox;

    @FXML
    private ChoiceBox<PictogramHair> hairColorChoiceBox;

    @FXML
    private VBox previewVbox;
    @FXML
    private CheckBox pluralCheckBox;
    @FXML
    private TextField pictoText;

    @FXML
    private ChoiceBox<PictogramTextPosition> pictoTextPosition;

    @FXML
    private ChoiceBox<PictogramSkin> skinColorChoiceBox;

    Controller_Principal controllerPrincipal;

    private Pictogram editedPicto;
    private Pictogram previewPicto;


    @FXML
    void initialize() {

        for(PictogramActions action: PictogramActions.values()) {
            actionChoiceBox.getItems().add(action);
        }

        for(PictogramHair hair: PictogramHair.values()) {
            hairColorChoiceBox.getItems().add(hair);
        }

        for(PictogramSkin skin: PictogramSkin.values()) {
            skinColorChoiceBox.getItems().add(skin);
        }
        for (PictogramTextPosition position : PictogramTextPosition.values()) {
            pictoTextPosition.getItems().add(position);
        }



    }

    public void setEditedPicto(Pictogram editedPicto, Controller_Principal controllerPrincipal) {
        this.editedPicto = editedPicto;
        previewPicto = editedPicto.clonePictogram();
        previewVbox.getChildren().add(previewPicto);
        this.controllerPrincipal = controllerPrincipal;

        skinColorChoiceBox.setValue(editedPicto.getAtrributes().getSkin());
        hairColorChoiceBox.setValue(editedPicto.getAtrributes().getHair());
        actionChoiceBox.setValue(editedPicto.getAtrributes().getAction());
        pluralCheckBox.setSelected(editedPicto.getAtrributes().isPlural());
        colorCheckBox.setSelected(editedPicto.getAtrributes().isColor());
        pictoText.setText(editedPicto.getLabel().getText());
        pictoTextPosition.setValue(editedPicto.getAtrributes().getTextPosition());

        pictoTextPosition.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        actionChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        hairColorChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        skinColorChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        backgroundColorPicker.valueProperty().addListener((observableValue, color, t1) -> updatePicto());

    }



    @FXML
    void applyEdit(ActionEvent event) {
        System.out.println("Tentative de changement de editedPicto");

        Scene scene = (Scene) (((Node) event.getSource()).getScene());
        controllerPrincipal.updatePicto(editedPicto, previewPicto);
        ((Stage)scene.getWindow()).close();

    }

    @FXML
    void cancelEdit(ActionEvent event) {
        Stage stage = (Stage)(((Node) event.getSource()).getScene().getWindow());
        stage.close();
    }
    @FXML
    void onUpdateController(Event event) {
        updatePicto();
    }

    private void updatePicto(){
        System.out.println("Rebuild Picto");
        previewPicto.getAtrributes().setSkin(skinColorChoiceBox.getValue());
        previewPicto.getAtrributes().setHair(hairColorChoiceBox.getValue());
        previewPicto.getAtrributes().setAction(actionChoiceBox.getValue());
        previewPicto.getAtrributes().setPlural(pluralCheckBox.isSelected());
        previewPicto.getAtrributes().setColor(colorCheckBox.isSelected());
        previewPicto.setLabel(new Label(pictoText.getText()));
        previewPicto.getAtrributes().setTextPosition(pictoTextPosition.getValue());


        previewPicto.updatePictogram();
        previewVbox.getChildren().clear();
        System.out.println("[DBG] " + previewPicto);
        System.out.println("[DBG] " + previewPicto.getImageView().getImage().getUrl());
        previewVbox.getChildren().add(previewPicto);

    }
}
