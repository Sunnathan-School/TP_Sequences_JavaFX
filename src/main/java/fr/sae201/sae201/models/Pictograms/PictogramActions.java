package fr.sae201.sae201.models.Pictograms;

import java.io.Serializable;

public enum PictogramActions implements Serializable {
    PAST("past"),
    NONE(""),
    FUTURE("future");

    private String action;
    PictogramActions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
