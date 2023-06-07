package fr.sae201.sae201.models;

import fr.sae201.sae201.models.Pictograms.PictogramCoord;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.*;
import java.util.HashMap;

public class SequentielSave implements Serializable {

    HashMap<String, PictogramCoord> pictoMap;
    SequentielSize sequentielSize;
    private String filePath;

    public SequentielSave(String filePath) {
        pictoMap = new HashMap<>();
        this.filePath=filePath;
    }

    public void loadSeqFile() throws FileNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            HashMap<HashMap<String, PictogramCoord>, SequentielSize> hashMap;
            hashMap = (HashMap<HashMap<String, PictogramCoord>, SequentielSize>) ois.readObject();
            for (HashMap<String, PictogramCoord> stringPictogramCoordHashMap : hashMap.keySet()) {
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

    public HashMap<String, PictogramCoord> getPictoMap() {
        return pictoMap;
    }

    public SequentielSize getSequentielSize(){
        return sequentielSize;
    }

    public void saveSeqFile() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
        HashMap<HashMap<String, PictogramCoord>, SequentielSize> hashMap = new HashMap<>();
        hashMap.put(pictoMap, sequentielSize);
        oos.writeObject(hashMap);
        oos.close();
    }

    public void convertHashmap(HashMap<ImageView, PictogramCoord> map, GridPane gridPane){
        for (ImageView imageView : map.keySet()) {
            pictoMap.put(imageView.getImage().getUrl(), map.get(imageView));
        }
        sequentielSize = new SequentielSize(gridPane.getColumnCount(), gridPane.getRowCount());
    }


}
