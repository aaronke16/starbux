package com.starbucks.demo.Service;

import com.starbucks.demo.Client.GoogleMapsClient;
import com.starbucks.demo.Model.Place;
import com.starbucks.demo.Model.PlacesResponse;
import com.starbucks.demo.Model.Starbucks;
import com.starbucks.demo.Model.StarbucksFilter;
import com.starbucks.demo.dao.StarbucksDao;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class StarbucksService {

    private final GoogleMapsService service;
    private final StarbucksDao dao;
    private final GoogleMapsClient googleMapsClient;

    public StarbucksService(GoogleMapsService service, StarbucksDao dao, GoogleMapsClient googleMapsClient) {
        this.service = service;
        this.dao = dao;
        this.googleMapsClient = googleMapsClient;
    }

    public Starbucks addLocationCode(StarbucksFilter filters) throws Exception {
        var address = filters.address();
        var code = filters.code();
        var extractedAddress = service.extractAddressInfo(address);
        var formattedAddress = extractedAddress.address();
        var placeId = extractedAddress.placeId();
        var longitude = extractedAddress.longitude();
        var latitude = extractedAddress.latitude();
        var starbucksObject = Starbucks.builder().address(formattedAddress).placeId(placeId).bathroomCode(code).longitude(longitude).latitude(latitude).build();
        return dao.addStarbucksLocation(starbucksObject, code);
    }

    public Starbucks getStarbucksLocationInfo(String id) throws Exception {
        return dao.getStarbucksLocationInfoById(id);
    }

    public List<Starbucks> getAllStarbucksInfo() throws Exception {
        return dao.getStarbucksLocationInfo();
    }

    public Set<Starbucks> getLocationsNearUser() throws Exception {
        try {
            Set<Starbucks> starbucksList = new HashSet<>();
            var json = googleMapsClient.searchQuery();
            ObjectMapper mapper = new ObjectMapper();
            PlacesResponse responseObj = mapper.readValue(json, PlacesResponse.class);
            System.out.println(responseObj);

            for (Place places : responseObj.places()) {
                var res = service.extractAddressInfo(places.formattedAddress());
                var locs = dao.getStarbucksLocationInfoByPlaceId(res.placeId());
                if (locs != null && locs.placeId() != null) {
                    starbucksList.add(locs);
                } else {
                starbucksList.add(res);
                }
            }

            return starbucksList;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
