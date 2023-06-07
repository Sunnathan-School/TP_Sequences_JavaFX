package fr.sae201.sae201.models.Pictograms;

public enum PictogramSkin {
    WHITE("white"),
    BLACK("black"),
    ASSIAN("assian"),
    MULATTO("mulatto"),
    AZTEC("aztec");

    private String skin;
    PictogramSkin(String skin) {
        this.skin=skin;
    }

    public String getSkin() {
        return skin;
    }
}
