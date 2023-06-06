package fr.sae201.sae201.models.Pictograms;

import fr.sae201.sae201.utils.ARASAAC;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pictogram {

    private boolean plural;

    private boolean color;

    private String backgroundColor;
    private String action;
    private Integer resolution;
    private String skin;
    private String hair;
    //URL = true

    private String pictoImage;

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
