package com.starbucks.demo.dao;

import com.starbucks.demo.Model.Starbucks;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StarbucksDao {

    private final JdbcTemplate jdbcTemplate;

    public StarbucksDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_STARBUCKS_LOCATION = """
    INSERT INTO starbucks_table
    (place_id, address, bathroom_code, longitude, latitude)
    VALUES
    (?, ?, ?, ?, ?)
    RETURNING id, place_id, address, bathroom_code, longitude, latitude
    """;

    private static final String GET_STARBUCKS_LOCATION_INFO_BY_ID = """
    SELECT id, place_id, address, bathroom_code, longitude, latitude
    FROM starbucks_table
    WHERE id = ?
    """;

    private static final String GET_STARBUCKS_LOCATION_INFO_BY_PLACEID = """
    SELECT id, place_id, address, bathroom_code, longitude, latitude
    FROM starbucks_table
    WHERE place_id = ?
    """;

    private static final String GET_STARBUCKS_LOCATION_INFO_BY_FILTER = """
    SELECT id, place_id, address, bathroom_code, longitude, latitude
    FROM starbucks_table
    """;


    public Starbucks addStarbucksLocation(Starbucks starbucks, String bathroomCode) {
        try{
            var placeId = starbucks.placeId();
            var address = starbucks.address();
            var longitude = starbucks.longitude();
            var latitude = starbucks.latitude();
            return jdbcTemplate.queryForObject(INSERT_STARBUCKS_LOCATION,
                    new StarbucksRowMapper(),
                    placeId, address, bathroomCode,
                    longitude, latitude
            );
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }

    public Starbucks getStarbucksLocationInfoByPlaceId(String placeId) {
        if (placeId == null) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(
                    GET_STARBUCKS_LOCATION_INFO_BY_PLACEID,
                    new StarbucksRowMapper(),
                    placeId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Starbucks> getStarbucksLocationInfo() {
        try {
            List<Starbucks> starbucksList = jdbcTemplate.query(
                    GET_STARBUCKS_LOCATION_INFO_BY_FILTER,
                    (rs, rowNum) -> new Starbucks(
                            rs.getString("id"),
                            rs.getString("bathroom_code"),
                            rs.getString("address"),
                            rs.getString("place_id"),
                            rs.getDouble("longitude"),
                            rs.getDouble("latitude")
                    ));

            return starbucksList;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }

    public Starbucks getStarbucksLocationInfoById(String id) {
        try {
            return jdbcTemplate.queryForObject(GET_STARBUCKS_LOCATION_INFO_BY_ID, new StarbucksRowMapper(), id);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.", e);
        }
    }

    public class StarbucksRowMapper implements RowMapper<Starbucks> {
        @Override
        public Starbucks mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String code = rs.getString("bathroom_code");
            String address = rs.getString("address");
            String placeId = rs.getString("place_id");
            Double longitude = rs.getDouble("longitude");
            Double latitude = rs.getDouble("latitude");
            return Starbucks.builder()
                    .id(id)
                    .bathroomCode(code)
                    .address(address)
                    .placeId(placeId)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
        }
    }
}
