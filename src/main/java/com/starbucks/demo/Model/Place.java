package com.starbucks.demo.Model;

public record Place(
        String formattedAddress,
        String priceLevel,
        DisplayName displayName
) {}
