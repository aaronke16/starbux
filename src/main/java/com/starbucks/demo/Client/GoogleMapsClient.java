package com.starbucks.demo.Client;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

@Service
public class GoogleMapsClient {

//    private final HttpRequest request;
//    private final HttpClient client;
//    private final Gson gson;

    public GoogleMapsClient() {
//        this.request = request;
//        this.client = client;
//        this.gson = gson;
    }

    @Value("${google.maps.api.key}")
    private String googleApiKey;

    public String searchQuery() throws Exception {
        var url = "https://places.googleapis.com/v1/places:searchText";

        String payload = """
        {
          "textQuery": "Starbucks near me"
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers("Content-Type", "application/json")
                .headers("Accept", "application/json")
                .headers("X-Goog-Api-Key", googleApiKey)
                .headers("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.priceLevel")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        return response.body();
    }
}
