package com.example.personaltrainerapp.services;

import com.example.personaltrainerapp.model.Fruit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to connect the FruityVice API endpoint to the app
 */
public class FruityViceAPIService implements FruitApiService {
    /** API URL Constant*/
    private static final String BASE_URL = "https://www.fruityvice.com/api/fruit";
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Fetches all the JSON data from the API
     * @return a String representing the body of the request
     * @throws Exception if failed
     */
    public List<Fruit> fetchAll() throws Exception {
        String body = get(BASE_URL + "/all");
        return parseFruits(new JSONArray(body));
    }

    /**
     * Helper function to create a GET request to the API
     * @param url - URL of the API
     * @return the body response from the API as a String
     * @throws Exception if failed
     */
    private String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API returned status " + response.statusCode() + " for: " + url);
        }
        return response.body();
    }

    /**
     * Parse the JSON Array into a list of Fruit instances
     * @param array - JSON array from the API
     * @return a list of fruit
     */
    private List<Fruit> parseFruits(JSONArray array) {
        List<Fruit> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            results.add(parseFruit(array.getJSONObject(i)));
        }
        return results;
    }

    /**
     * Parse a single JSON object into a fruit
     * @param obj - the Json object
     * @return a Fruit instance representing the JSON object
     */
    private Fruit parseFruit(JSONObject obj) {
        JSONObject n = obj.getJSONObject("nutritions");
        return new Fruit(
                obj.getString("name"),
                obj.getString("family"),
                obj.getString("genus"),
                n.optDouble("calories", 0),
                n.optDouble("fat", 0),
                n.optDouble("sugar", 0),
                n.optDouble("carbohydrates", 0),
                n.optDouble("protein", 0)
        );
    }
}
