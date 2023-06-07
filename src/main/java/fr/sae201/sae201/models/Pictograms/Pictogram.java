package fr.sae201.sae201.models.Pictograms;

import fr.sae201.sae201.utils.ARASAAC;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Pictogram implements Serializable {

    private boolean plural = false;

    private boolean color = true;

    private String backgroundColor = null;
    private PictogramActions action = null;
    private PictogramResolution resolution = null;
    private PictogramSkin skin = null;
    private PictogramHair hair = null;

    private int pictoId;

    public Task<ImageView> generatePictoImage(int id){
        return new Task<>() {
            @Override
            protected ImageView call() {
                String imageUrl = ARASAAC.getPictogrammeURL(id).get("image").asText();
                Image image = new Image(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setId(String.valueOf(id));

                System.out.println(image.toString());
                return imageView;
            }
        };
    }



    public Pictogram(boolean plural, boolean color, String backgroundColor, PictogramActions action, PictogramResolution resolution, PictogramSkin skin, PictogramHair hair) {
        super();
        this.plural = plural;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.action = action;
        this.resolution = resolution;
        this.skin = skin;
        this.hair = hair;
    }

    public Pictogram(){
        super();
    }
}
