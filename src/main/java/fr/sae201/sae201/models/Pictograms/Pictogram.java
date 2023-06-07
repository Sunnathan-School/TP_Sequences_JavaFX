package fr.sae201.sae201.models.Pictograms;

import fr.sae201.sae201.utils.ARASAAC;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.Serializable;

public class Pictogram extends VBox implements Serializable{

    private boolean plural = false;
    private boolean color = true;
    private String backgroundColor = "none";
    private PictogramActions action = PictogramActions.NONE;
    private PictogramSkin skin = PictogramSkin.WHITE;



    private PictogramHair hair = PictogramHair.BLACK;
    private int pictoId;
    private PictogramTextPosition textPosition = PictogramTextPosition.TOP;

    private ImageView imageView;
    private Label label;

//plural, color, backgroundColor,action,skin,hair
    //public Pictogram(Integer id, boolean plural, boolean color, String backgroundColor, PictogramActions action, PictogramSkin skin, PictogramHair hair, PictogramTextPosition textPosition
    public Pictogram(Integer id){
        super();
        System.out.println("Picto Construct");
        this.pictoId=id;
        //String imgUrl = ARASAAC.getPictogrammeURL(id).get("image").asText();
        String imgUrl = ARASAAC.getPictogrammeURL(getPictoId(), isPlural(), isColor(), getBackgroundColor(),getAction(),getSkin(),getHair()).get("image").asText();
        imageView = new ImageView(new Image(imgUrl));
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        label = new Label();
        setAlignment(Pos.CENTER);


        if (textPosition.equals(PictogramTextPosition.TOP)){
            getChildren().addAll(label, imageView);
        }else {
            getChildren().addAll(imageView,label);
        }

        setOnMouseEntered(event -> {
            setStyle("-fx-background-color: gray;");
        });

        setOnMouseExited(event -> {
            setStyle("-fx-background-color: transparent;");
        });

    }
/*
    public Pictogram(Integer id, boolean plural, boolean color, String backgroundColor, PictogramActions action,
                     PictogramSkin skin, PictogramHair hair, PictogramTextPosition textPosition, String txt){
        this.textPosition = textPosition;
        System.out.println("Picto Construct");
        this.pictoId=id;
        String imgUrl = ARASAAC.getPictogrammeURL(pictoId, plural, color, backgroundColor,action,skin,hair).get("image").asText();
        imageView = new ImageView(new Image(imgUrl));
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        label = new Label(txt);
        setAlignment(Pos.CENTER);


        if (this.textPosition.equals(PictogramTextPosition.TOP)){
            getChildren().add(label);
            getChildren().add(imageView);
        }else {
            getChildren().add(imageView);
            getChildren().add(label);
        }

        setOnMouseEntered(event -> {
            setStyle("-fx-background-color: gray;");
        });

        setOnMouseExited(event -> {
            setStyle("-fx-background-color: transparent;");
        });
    }*/


    public void updatePictogram(){
        System.out.println("Picto Updated");
        String url = ARASAAC.getPictogrammeURL(getPictoId(), isPlural(), isColor(), getBackgroundColor(),getAction(),getSkin(),getHair()).get("image").asText();
        this.imageView.setImage(new Image(url));
        getChildren().clear();
        if (textPosition.equals(PictogramTextPosition.TOP)){
            getChildren().addAll(label, imageView);
        }else {
            getChildren().addAll(imageView,label);
        }
    }

    public Pictogram clonePictogram() {
        Pictogram clonedPictogram = new Pictogram(this.pictoId);  // Créer une nouvelle instance du pictogramme

        // Copier les autres propriétés du pictogramme
        clonedPictogram.setPlural(this.plural);
        clonedPictogram.setColor(this.color);
        clonedPictogram.setBackgroundColor(this.backgroundColor);
        clonedPictogram.setAction(this.action);
        clonedPictogram.setSkin(this.skin);
        clonedPictogram.setHair(this.hair);

        // Copier les éléments visuels (ImageView et Label)
        clonedPictogram.setLabel(new Label(this.label.getText()));
        clonedPictogram.setImageView(this.imageView);
        return clonedPictogram;
    }


    @Override
    public String toString() {
        return "Pictogram{" +
                "plural=" + plural +
                ", color=" + color +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", action=" + action +
                ", skin=" + skin +
                ", hair=" + hair +
                ", pictoId=" + pictoId +
                ", textPosition=" + textPosition +
                ", imageView=" + imageView +
                ", label=" + label +
                '}';
    }
    public ImageView getImageView() {
        return imageView;
    }
    public Label getLabel() {
        return label;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getPictoId() {
        return pictoId;
    }

    public void setSkin(PictogramSkin skin) {
        this.skin = skin;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPlural(boolean plural) {
        this.plural = plural;
    }

    public void setAction(PictogramActions action) {
        this.action = action;
    }

    public void setHair(PictogramHair hair) {
        this.hair = hair;
    }
    public boolean isPlural() {
        return plural;
    }

    public boolean isColor() {
        return color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public PictogramActions getAction() {
        return action;
    }

    public PictogramSkin getSkin() {
        return skin;
    }

    public PictogramHair getHair() {
        return hair;
    }

    public PictogramTextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(PictogramTextPosition textPosition) {
        this.textPosition = textPosition;
    }
}
