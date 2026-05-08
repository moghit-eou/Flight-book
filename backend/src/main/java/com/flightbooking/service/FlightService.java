package com.flightbooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FlightService {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Object getFlights(String depIata, String arrIata) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(apiUrl)
            .queryParam("access_key", apiKey);

        if (depIata != null) builder.queryParam("dep_iata", depIata);
        if (arrIata != null) builder.queryParam("arr_iata", arrIata);

        return restTemplate.getForObject(builder.toUriString(), Object.class);
    }
}