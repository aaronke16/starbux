package com.starbucks.demo.Model;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record Starbucks(
    String id,
    String bathroomCode,
    String address,
    String placeId,
    Double longitude,
    Double latitude
) {
}