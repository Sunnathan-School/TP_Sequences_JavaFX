package fr.sae201.sae201.models.Pictograms;

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

    private void generatePictoImage(){

    }



    public Pictogram(boolean plural, boolean color, String backgroundColor, String action, Integer resolution, String skin, String hair) {
        this.plural = plural;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.action = action;
        this.resolution = resolution;
        this.skin = skin;
        this.hair = hair;
    }
}
