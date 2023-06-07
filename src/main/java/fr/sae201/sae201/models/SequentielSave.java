package fr.sae201.sae201.models;

import fr.sae201.sae201.models.Pictograms.PictogramCoord;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.*;
import java.util.HashMap;

public class SequentielSave implements Serializable {

    HashMap<PictogramCoord, String> pictoMap;
    SequentielSize sequentielSize;
    private String filePath;

    public SequentielSave(String filePath) {
        pictoMap = new HashMap<>();
        this.filePath=filePath;
    }

    public void loadSeqFile() throws FileNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            HashMap<HashMap<PictogramCoord, String>, SequentielSize> hashMap;
            hashMap = (HashMap<HashMap<PictogramCoord, String>, SequentielSize>) ois.readObject();
            for (HashMap<PictogramCoord, String> stringPictogramCoordHashMap : hashMap.keySet()) {
                pictoMap = stringPictogramCoordHashMap;
            }
            sequentielSize = hashMap.get(pictoMap);
            System.out.println(pictoMap);
            System.out.println(sequentielSize);
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<PictogramCoord, String> getPictoMap() {
        return pictoMap;
    }

    public SequentielSize getSequentielSize(){
        return sequentielSize;
    }

    public void saveSeqFile() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
        HashMap<HashMap<PictogramCoord,String>, SequentielSize> hashMap = new HashMap<>();
        hashMap.put(pictoMap, sequentielSize);
        oos.writeObject(hashMap);
        oos.close();
    }

    public void convertHashmap(HashMap<PictogramCoord, ImageView> map, GridPane gridPane){

        for (PictogramCoord coord : map.keySet()) {
            pictoMap.put(coord, map.get(coord).getImage().getUrl());
        }
        sequentielSize = new SequentielSize(gridPane.getColumnCount(), gridPane.getRowCount());
    }


}
