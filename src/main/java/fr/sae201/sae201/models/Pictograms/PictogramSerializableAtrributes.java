package fr.sae201.sae201.models.Pictograms;

import java.io.Serializable;

public class PictogramSerializableAtrributes implements Serializable {
    private boolean plural = false;
    private boolean color = true;
    private String backgroundColor = "none";
    private PictogramActions action = PictogramActions.NONE;
    private PictogramSkin skin = PictogramSkin.WHITE;

    private String pictoText = "";

    private PictogramHair hair = PictogramHair.BLACK;
    private int pictoId;
    private PictogramTextPosition textPosition = PictogramTextPosition.TOP;

    public PictogramSerializableAtrributes(){

    }


    @Override
    public String toString() {
        return "PictogramSerializableAtrributes{" +
                "plural=" + plural +
                ", color=" + color +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", action=" + action +
                ", skin=" + skin +
                ", hair=" + hair +
                ", pictoId=" + pictoId +
                ", textPosition=" + textPosition +
                '}';
    }

    public void setPictoText(String pictoTest) {
        this.pictoText = pictoTest;
    }

    public String getPictoText() {
        return pictoText;
    }

    public boolean isPlural() {
        return plural;
    }

    public void setPlural(boolean plural) {
        this.plural = plural;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public PictogramActions getAction() {
        return action;
    }

    public void setAction(PictogramActions action) {
        this.action = action;
    }

    public PictogramSkin getSkin() {
        return skin;
    }

    public void setSkin(PictogramSkin skin) {
        this.skin = skin;
    }

    public PictogramHair getHair() {
        return hair;
    }

    public void setHair(PictogramHair hair) {
        this.hair = hair;
    }

    public int getPictoId() {
        return pictoId;
    }

    public void setPictoId(int pictoId) {
        this.pictoId = pictoId;
    }

    public PictogramTextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(PictogramTextPosition textPosition) {
        this.textPosition = textPosition;
    }
}
