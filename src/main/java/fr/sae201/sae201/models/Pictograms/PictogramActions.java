package fr.sae201.sae201.models.Pictograms;

public enum PictogramActions {
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
