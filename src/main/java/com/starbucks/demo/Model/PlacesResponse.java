package com.starbucks.demo.Model;

import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
public record PlacesResponse(Set<Place> places) {}

