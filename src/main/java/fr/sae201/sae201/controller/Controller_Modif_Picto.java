package fr.sae201.sae201.controller;

import fr.sae201.sae201.models.Pictograms.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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

    public void setEditedPicto(Pictogram editedPicto) {
        this.editedPicto = editedPicto;
        previewPicto = editedPicto.clonePictogram();
        previewVbox.getChildren().add(previewPicto);


        skinColorChoiceBox.setValue(editedPicto.getSkin());
        hairColorChoiceBox.setValue(editedPicto.getHair());
        actionChoiceBox.setValue(editedPicto.getAction());
        pluralCheckBox.setSelected(editedPicto.isPlural());
        colorCheckBox.setSelected(editedPicto.isColor());
        pictoText.setText(editedPicto.getLabel().getText());
        pictoTextPosition.setValue(editedPicto.getTextPosition());

        pictoTextPosition.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        actionChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        hairColorChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        skinColorChoiceBox.valueProperty().addListener((observableValue, position, t1) -> updatePicto());
        backgroundColorPicker.valueProperty().addListener((observableValue, color, t1) -> updatePicto());

    }



    @FXML
    void applyEdit(ActionEvent event) {

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

        /*skinColorChoiceBox.setValue(editedPicto.getSkin());
        hairColorChoiceBox.setValue(editedPicto.getHair());
        actionChoiceBox.setValue(editedPicto.getAction());
        pluralCheckBox.setSelected(editedPicto.isPlural());
        colorCheckBox.setSelected(editedPicto.isColor());
        pictoText.setText(editedPicto.getLabel().getText());
        pictoTextPosition.setValue(editedPicto.getTextPosition());
        * */
        Pictogram pictogram = new Pictogram(previewPicto.getPictoId(),
                pluralCheckBox.isSelected(),colorCheckBox.isSelected(),(backgroundColorPicker.getValue().toString().substring(2,8)),
                actionChoiceBox.getValue(),skinColorChoiceBox.getValue(),hairColorChoiceBox.getValue(),pictoTextPosition.getValue(), pictoText.getText());
        /*previewPicto.setSkin(skinColorChoiceBox.getValue());
        previewPicto.setHair(hairColorChoiceBox.getValue());
        previewPicto.setAction(actionChoiceBox.getValue());
        previewPicto.setPlural(pluralCheckBox.isSelected());
        previewPicto.setColor(colorCheckBox.isSelected());
        previewPicto.setLabel(new Label(pictoText.getText()));
        previewPicto.setTextPosition(pictoTextPosition.getValue());*/


        //previewPicto = previewPicto.updatePictogram();
        previewVbox.getChildren().clear();
        System.out.println("[DBG] " + pictogram);
        previewVbox.getChildren().add(pictogram);

    }
}
