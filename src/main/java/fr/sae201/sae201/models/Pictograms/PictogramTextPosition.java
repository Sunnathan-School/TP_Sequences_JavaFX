package fr.sae201.sae201.models.Pictograms;

import java.io.Serializable;

public enum PictogramTextPosition implements Serializable {

    TOP("Haut"),
    BOTTOM("Bas");

    private String position;

    PictogramTextPosition(String position){
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
