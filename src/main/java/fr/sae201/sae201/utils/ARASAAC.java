package fr.sae201.sae201.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sae201.sae201.models.Pictograms.PictogramActions;
import fr.sae201.sae201.models.Pictograms.PictogramHair;
import fr.sae201.sae201.models.Pictograms.PictogramResolution;
import fr.sae201.sae201.models.Pictograms.PictogramSkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ARASAAC {

    private final static String API_URL = "https://api.arasaac.org/api";
    private final static String LANG = "fr";

    public static void searchPicto(String... query){
        StringBuilder url = new StringBuilder(API_URL + "/pictograms/" + LANG + "/search/");
        for (String s : query) {
            url.append(s).append("%20");
        }
        try {
            System.out.println(sendGetRequest(String.valueOf(url)).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getKeywords(){
        try {
            System.out.println(sendGetRequest(API_URL + "/keywords/" + LANG).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode sendGetRequest(String querry) throws IOException {
            System.out.println("[GET] " + querry);
            // Création de l'objet URL
            URL url = new URL(querry);

            // Ouverture de la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Paramètres de la requête
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Lecture de la réponse
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Fermeture de la connexion
            connection.disconnect();
            ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(String.valueOf(response));
        // Traitement de la réponse
            return jsonNode;
    }

    public static void getPictogrammeURL(int pictoId, boolean plural, boolean color,
                                      String backgroundColor, PictogramActions action,
                                      PictogramResolution resolution, PictogramSkin skin,
                                      PictogramHair hair){
        String url = API_URL + "/pictograms/" + pictoId;
        url += "?plural=" + plural;
        url += "&color=" + color;
        url += "&backgroundColor=" + backgroundColor;
        if (action != PictogramActions.NONE){
            url += "&action=" + action.getAction();
        }

        url += "&resolution=" + resolution.getSize();
        url += "&skin=" + skin.getSkin();
        url += "&hair=" + hair.getHair();
        url += "&url=true";

        try {
            System.out.println(sendGetRequest(url).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
