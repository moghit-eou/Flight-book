package com.flightbooking.model.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;

@Repository
public class FlightDAOImpl implements FlightDAO {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Object> getFlights(String depIata, String arrIata) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(apiUrl)
            .queryParam("access_key", apiKey);
        if (depIata != null) builder.queryParam("dep_iata", depIata);
        if (arrIata != null) builder.queryParam("arr_iata", arrIata);

        Map response = restTemplate.getForObject(builder.toUriString(), Map.class);
        return (List<Object>) response.get("data");
    }
}