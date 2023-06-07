package fr.sae201.sae201.models;

import java.io.Serializable;

public class SequentielSize implements Serializable {
    private int x;
    private int y;

    public SequentielSize(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "SequentielSize{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
