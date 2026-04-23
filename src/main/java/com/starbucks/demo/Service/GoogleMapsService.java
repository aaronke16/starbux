package com.starbucks.demo.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.starbucks.demo.Client.GoogleMapsClient;
import com.starbucks.demo.Model.Starbucks;
import com.starbucks.demo.Model.StarbucksFilter;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleMapsService {

    private final GeoApiContext context;
    private final GoogleMapsClient googleMapsClient;

    public GoogleMapsService(GeoApiContext context, GoogleMapsClient googleMapsClient) {
        this.context = context;
        this.googleMapsClient = googleMapsClient;
    }

    public GeocodingResult[] geocodeAddress(StarbucksFilter filters) throws Exception {
        return GeocodingApi.geocode(context, filters.address()).await();
    }

    public Starbucks extractAddressInfo(String address) throws Exception {
        try {
            var addressRepresentation = GeocodingApi.geocode(context, address).await();
            if (!Arrays.stream(addressRepresentation).toList().isEmpty()) {
                var addressRepresentationList = Arrays.stream(addressRepresentation).toList();
                var formattedAddress = addressRepresentationList.get(0).formattedAddress;
                var placeId = addressRepresentationList.get(0).placeId;
                var lon = addressRepresentationList.get(0).geometry.location.lng;
                var lat = addressRepresentationList.get(0).geometry.location.lat;
                return Starbucks.builder().address(formattedAddress).placeId(placeId).longitude(lon).latitude(lat).build();
            } else {
                return Starbucks.builder().build();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

//    public String getLocationsNearUser() throws Exception {
//        try {
//            return googleMapsClient.searchQuery();
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}