package fr.sae201.sae201.models.Pictograms;

import java.io.Serializable;

public class PictogramCoord implements Serializable {
    private int x;
    private int y;

    public PictogramCoord(int x, int y){
        this.x=x;
        this.y=y;

    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "PictogramCoord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
