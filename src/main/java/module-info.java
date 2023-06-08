module fr.sae201.sae201 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jackson.databind;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires javafx.swing;


    opens fr.sae201.sae201 to javafx.fxml;
    exports fr.sae201.sae201;
    exports fr.sae201.sae201.models;
    opens fr.sae201.sae201.models to javafx.fxml;
    exports fr.sae201.sae201.controller;
    opens fr.sae201.sae201.controller to javafx.fxml;
    exports fr.sae201.sae201.models.Pictograms;
    opens fr.sae201.sae201.models.Pictograms to javafx.fxml;
}