package fr.sae201.sae201.models.Pictograms;

public enum PictogramHair {
    BLONDE("blonde"),
    BROWN("brown"),
    DARK_BROWN("darkBrown"),
    GRAY("gray"),
    DARK_GRAY("darkGray"),
    RED("red"),
    BLACK("black");

    private String hair;
    PictogramHair(String hair) {
        this.hair=hair;
    }

    public String getHair() {
        return hair;
    }
}
