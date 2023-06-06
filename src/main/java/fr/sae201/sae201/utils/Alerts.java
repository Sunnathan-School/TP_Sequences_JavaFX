package fr.sae201.sae201.utils;

import javafx.scene.control.Alert;

public class Alerts {

    public static void showAlertWithoutHeaderText(Alert.AlertType alertType, String title, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
