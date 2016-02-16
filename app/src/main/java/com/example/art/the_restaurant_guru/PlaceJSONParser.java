package com.example.art.the_restaurant_guru;

/**
 * Created by art on 4/1/2015.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {

    /** Receives a JSONObject and returns a list */
    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }


    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        int price=0;
        String type = "";
        String jsonObject=jPlace.toString();

        try {
            // Extracting Place name, if available
            if(!jPlace.isNull("name")){
                placeName = jPlace.getString("name");
            }

            // Extracting Place Vicinity, if available
            if(!jPlace.isNull("vicinity")){
                vicinity = jPlace.getString("vicinity");
            }
            // Extracting Place Price, if available
            if(!jPlace.isNull("price_level")){
                price = jPlace.getInt("price_level");
            }


            JSONArray the_types = jPlace.getJSONArray("types");
            type = the_types.toString();

            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");


            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("price",price+"");
            place.put("type",type);
            place.put("temp",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
