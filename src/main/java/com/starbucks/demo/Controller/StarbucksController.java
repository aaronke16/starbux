package com.starbucks.demo.Controller;

import com.google.maps.model.GeocodingResult;
import com.starbucks.demo.Model.Starbucks;
import com.starbucks.demo.Model.StarbucksFilter;
import com.starbucks.demo.Service.GoogleMapsService;
import com.starbucks.demo.Service.StarbucksService;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/starbux")
public class StarbucksController {

    private final GoogleMapsService googleMapsService;
    private final StarbucksService starbucksService;

    public StarbucksController(GoogleMapsService googleMapsService, StarbucksService starbucksService) {
        this.googleMapsService = googleMapsService;
        this.starbucksService = starbucksService;
    }

    @GetMapping
    public Starbucks getById(@RequestParam String id) throws Exception {
        return starbucksService.getStarbucksLocationInfo(id);
    }

    @GetMapping("/all")
    public List<Starbucks> getAllLocations() throws Exception {
        return starbucksService.getAllStarbucksInfo();
    }

    @PostMapping("/add")
    public Starbucks geocodeAddresss(@RequestBody StarbucksFilter filters) throws Exception {
        return starbucksService.addLocationCode(filters);
    }

    @GetMapping("/formatted-address")
    public GeocodingResult[] geocodeAddressss(@RequestParam StarbucksFilter filters) throws Exception {
        return googleMapsService.geocodeAddress(filters);
    }

    @GetMapping("/near-me")
    public Set<Starbucks> getNearMe() throws Exception {
        return starbucksService.getLocationsNearUser();
    }
}
