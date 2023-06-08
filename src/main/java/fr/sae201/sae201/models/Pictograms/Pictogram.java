package fr.sae201.sae201.models.Pictograms;

import fr.sae201.sae201.utils.ARASAAC;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Pictogram extends VBox{

    PictogramSerializableAtrributes atrributes = new PictogramSerializableAtrributes();

    private ImageView imageView;
    private Label label;

    public Pictogram(Integer id){
        super();
        System.out.println("Picto Construct");
        atrributes.setPictoId(id);
        //String imgUrl = ARASAAC.getPictogrammeURL(id).get("image").asText();
        String imgUrl = ARASAAC.getPictogrammeURL(atrributes.getPictoId(), atrributes.isPlural(), atrributes.isColor(), atrributes.getBackgroundColor(),atrributes.getAction(),atrributes.getSkin(),atrributes.getHair()).get("image").asText();
        imageView = new ImageView(new Image(imgUrl));
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        label = new Label();
        setAlignment(Pos.CENTER);


        if (atrributes.getTextPosition().equals(PictogramTextPosition.TOP)){
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

    public void setAtrributes(PictogramSerializableAtrributes atrributes) {
        this.atrributes = atrributes;
    }

    public PictogramSerializableAtrributes getAtrributes() {
        return atrributes;
    }

       public void updatePictogram(){
        System.out.println("Picto Updated");
        String url = ARASAAC.getPictogrammeURL(atrributes.getPictoId(), atrributes.isPlural(), atrributes.isColor(), atrributes.getBackgroundColor(),atrributes.getAction(),atrributes.getSkin(),atrributes.getHair()).get("image").asText();
        this.imageView.setImage(new Image(url));
        getChildren().clear();
        if (atrributes.getTextPosition().equals(PictogramTextPosition.TOP)){
            getChildren().addAll(label, imageView);
        }else {
            getChildren().addAll(imageView,label);
        }
    }

    public Pictogram clonePictogram() {
        Pictogram clonedPictogram = new Pictogram(atrributes.getPictoId());  // Créer une nouvelle instance du pictogramme

        // Copier les autres propriétés du pictogramme
        clonedPictogram.atrributes.setPlural(atrributes.isPlural());
        clonedPictogram.atrributes.setColor(atrributes.isColor());
        clonedPictogram.atrributes.setBackgroundColor(atrributes.getBackgroundColor());
        clonedPictogram.atrributes.setAction(atrributes.getAction());
        clonedPictogram.atrributes.setSkin(atrributes.getSkin());
        clonedPictogram.atrributes.setHair(atrributes.getHair());

        // Copier les éléments visuels (ImageView et Label)
        clonedPictogram.setLabel(new Label(this.label.getText()));
        clonedPictogram.setImageView(this.imageView);
        return clonedPictogram;
    }


    @Override
    public String toString() {
        return "Pictogram{" +
                "plural=" + atrributes.isPlural() +
                ", color=" + atrributes.isColor() +
                ", backgroundColor='" + atrributes.getBackgroundColor() + '\'' +
                ", action=" + atrributes.getAction() +
                ", skin=" + atrributes.getSkin() +
                ", hair=" + atrributes.getHair() +
                ", pictoId=" + atrributes.getPictoId() +
                ", textPosition=" + atrributes.getTextPosition() +
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

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
