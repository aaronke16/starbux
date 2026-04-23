package com.starbucks.demo.Model;

public record NearMeResponse(
        String formattedAddress,
        String placeId
) {

}
