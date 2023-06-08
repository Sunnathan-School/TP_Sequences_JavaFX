package fr.sae201.sae201.models;

import fr.sae201.sae201.models.Pictograms.Pictogram;
import fr.sae201.sae201.models.Pictograms.PictogramSerializableAtrributes;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sequentiel implements Serializable {

    List<PictogramSerializableAtrributes> pictograms;


    public Sequentiel(){
        pictograms = new ArrayList<>();
    }

    public void loadSequentiel() throws FileNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ProjectManager.getProjectPath()));
            pictograms = (List<PictogramSerializableAtrributes>) ois.readObject();
            for (PictogramSerializableAtrributes pictogram : pictograms) {
                System.out.println(pictogram);
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSequentiel(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(ProjectManager.getProjectPath()));
            oos.writeObject(pictograms);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void convertFlowPaneToList(FlowPane flowPane){
        for (Node child : flowPane.getChildren()) {
            PictogramSerializableAtrributes pictogramSerializableAtrributes = ((Pictogram) child).getAtrributes();
            pictograms.add(pictogramSerializableAtrributes);
            //pictograms.set(flowPane.getChildren().indexOf(pictogram), pictogram);
        }
    }

    public List<PictogramSerializableAtrributes> getPictograms() {
        return pictograms;
    }
}
