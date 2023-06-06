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
    private String action = null;
    private Integer resolution = null;
    private String skin = null;
    private String hair = null;
    //URL = true

    private int pictoId;

    public Task<ImageView> generatePictoImage(int id){
        return new Task<>() {
            @Override
            protected ImageView call() throws Exception {
                String imageUrl = ARASAAC.getPictogrammeURL(id).get("image").asText();
                Image image = new Image(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setId(String.valueOf(id));
                return imageView;
            }
        };
    }



    public Pictogram(boolean plural, boolean color, String backgroundColor, String action, Integer resolution, String skin, String hair) {
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
