package com;

import com.pmc.controller.ZoneController;
import com.pmc.model.Density;
import com.pmc.model.Zone;
import com.util.Position;

import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.*;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Huber on 04/05/2015.
 */

public class Trafic {

    private static void createZones() throws Exception {

        // Acceder à la base, données trafic
        String json = catchData();

        // Récupérer la liste des rues bouchées
        getBlockedStreets(json);

        // Parcourir les rues bouchées et les pilloner de zones rouges
        ArrayList<String> rues = new ArrayList<String>();
        drawZonesStreet(rues);
    }

    public static String catchData() {

        String jsonResult = null;

        try {
            URL remoteUrl = new URL("https://download.data.grandlyon.com/ws/rdata/pvo_patrimoine_voirie.pvotrafic/all.json?maxfeatures=10000");

            HttpsURLConnection remoteServer = (HttpsURLConnection) remoteUrl.openConnection();
            remoteServer.setDoInput(true);
            remoteServer.setRequestMethod("POST");
            //remoteServer.setRequestProperty("Content-type", "text/html");
            remoteServer.setRequestProperty("user-agent", "LIRIS_DATA Platform");
            remoteServer.setRequestProperty("Authorization", "Basic TWFyaWFuLlNjdXR1cmljaUBpbnNhLWx5b24uZnI6YmFiYTIxNDM=");
            remoteServer.connect();
            String ligne = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(remoteServer.getInputStream()));
            while ((ligne = reader.readLine()) != null) {
                jsonResult += ligne.trim() + "\n";
            }
        } catch (Exception e) {
            //on s'occupe des exceptions

        }
        return jsonResult;
    }

    private static ArrayList<String> getBlockedStreets(String json) {

            ArrayList<String> blockedStreetsList = new ArrayList<String>();
            JSONObject jObj = new JSONObject(json);
            JSONArray values = jObj.getJSONArray("values");
            for (int i = 0 ; i < values.length() ; ++i) {
                JSONArray item = values.getJSONArray(i);
                String etat = item.getString(8);
                if (etat.equals("R")){
                    blockedStreetsList.add(item.getString(2));
                }
            }

        return blockedStreetsList;
    }


    private static Position adressToPosition(String adresse) throws Exception {

        String url = "https://maps.googleapis.com/maps/api/geocode/json";
        adresse = URLEncoder.encode(adresse, "UTF-8");

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        String urlParameters = "address="+adresse+"&key=AIzaSyDbx1glkOCe9r7WvHCCzCHC4yBxdusY-is";

        //add request header
        con.setRequestMethod("POST");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        JSONObject jObj = new JSONObject(response.toString());
        JSONArray arr = jObj.getJSONArray("results");
        JSONObject item = arr.getJSONObject(0);
        JSONObject geometry = item.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");

        double latitude = location.getDouble("lat");
        double longitude = location.getDouble("lng");

        Position positionCorrespondante = new Position();
        positionCorrespondante.setLatitude(latitude);
        positionCorrespondante.setLongitude(longitude);

        return positionCorrespondante;
    }

    private static String positionToAdress(Position position) throws Exception {

        String url = "https://maps.googleapis.com/maps/api/geocode/json";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        double latitude = position.getLatitude();
        double longitude = position.getLongitude();

        String urlParameters = "latlng=" + latitude + "," + longitude + "&key=AIzaSyDbx1glkOCe9r7WvHCCzCHC4yBxdusY-is";

        //add request header
        con.setRequestMethod("POST");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        JSONObject jObj = new JSONObject(response.toString());
        JSONArray arr = jObj.getJSONArray("results");
        JSONObject item = arr.getJSONObject(0);
        String adresse = item.getString("formatted_address");

        return adresse;
    }

    private static void drawZonesStreet(ArrayList<String> rues) throws Exception {

        for (String rue : rues) {

            // On parcourt la rue pour placer des zones tout le long
            boolean outOfBounds = false;
            int i = 0;
            while (!outOfBounds) {
                Position temp = adressToPosition(i + " " + rue);
                Zone zone = new Zone();
                zone.setLongitude(temp.getLongitude());
                zone.setLatitude(temp.getLatitude());
                zone.setDate(new DateTime());
                zone.setDensity(Density.HIGH);
                zone.setIntensity(1);

                // Persister la zone
                ZoneController zc = new ZoneController();
                zc.indicateZone(zone);

                i += 20;

                // TODO : A Modifier : Trouver le retour de l'API qui dit que le numero n'existe pas dans cette rue et remplacer le true
                if (true) {
                    outOfBounds = true;
                }
            }
        }
    }
}
