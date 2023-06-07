package fr.sae201.sae201.models.Pictograms;

import java.io.Serializable;

public enum PictogramResolution implements Serializable {
    SMALL(500),
    BIG(2500);

    private Integer size;
    PictogramResolution(int size){
        this.size=size;
    }

    public Integer getSize() {
        return size;
    }
}
