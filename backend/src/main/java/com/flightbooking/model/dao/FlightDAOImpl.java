package com.flightbooking.model.dao;

import com.flightbooking.model.entity.Flight;
import com.flightbooking.model.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDate;

import java.util.*;

@Repository
public class FlightDAOImpl implements FlightDAO {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final FlightRepository flightRepository;

    public FlightDAOImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
        
        // Configuration de timeouts de 4 secondes pour éviter que l'API externe ne bloque le thread indéfiniment
        org.springframework.http.client.SimpleClientHttpRequestFactory factory = 
            new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(10000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getFlights(String depIata, String arrIata) {
        String dep = depIata != null ? depIata.trim().toUpperCase() : "";
        String arr = arrIata != null ? arrIata.trim().toUpperCase() : "";

        if (dep.isEmpty() && arr.isEmpty()) {
            System.out.println("Recherche sans paramètres. Retour de tous les vols en cache.");
            return getFlightsFromCache("", "");
        }

        boolean isDepFull = dep.length() == 3;
        boolean isArrFull = arr.length() == 3;
        boolean hasDep = !dep.isEmpty();
        boolean hasArr = !arr.isEmpty();

        // Si l'un des paramètres saisis est incomplet (longueur < 3),
        // on effectue UNIQUEMENT une recherche de préfixe locale (SANS appel à l'API externe AviationStack).
        if ((hasDep && !isDepFull) || (hasArr && !isArrFull)) {
            System.out.println("Recherche partielle détectée (longueur < 3). Recherche locale par préfixe uniquement.");
            List<Flight> cachedFlights;
            if (hasDep && hasArr) {
                cachedFlights = flightRepository.findByDepIataStartingWithIgnoreCaseAndArrIataStartingWithIgnoreCase(dep, arr);
            } else if (hasDep) {
                cachedFlights = flightRepository.findByDepIataStartingWithIgnoreCase(dep);
            } else {
                cachedFlights = flightRepository.findByArrIataStartingWithIgnoreCase(arr);
            }

            List<Object> result = new ArrayList<>();
            for (Flight flight : cachedFlights) {
                result.add(convertToMap(flight));
            }
            return result;
        }

        // 1. Recherche par correspondance exacte dans le cache local (PostgreSQL)
        List<Object> cachedFlights = getFlightsFromCache(dep, arr);
        if (!cachedFlights.isEmpty()) {
            System.out.println("Vols trouvés dans le cache local (PostgreSQL). Utilisation du cache pour préserver le quota API.");
            return cachedFlights;
        }

        System.out.println("Aucun vol en cache dans PostgreSQL. Appel à l'API externe AviationStack...");
        try {
            // 2. Interroger l'API externe AviationStack uniquement si le cache local exact est vide
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("access_key", apiKey);
            if (hasDep) builder.queryParam("dep_iata", dep);
            if (hasArr) builder.queryParam("arr_iata", arr);

            Map response = restTemplate.getForObject(builder.toUriString(), Map.class);
            if (response != null && response.get("data") != null) {
                List<Object> apiFlights = (List<Object>) response.get("data");
                
                // Sauvegarder dans le cache local
                saveFlightsToCache(apiFlights);
                
                return apiFlights;
            }
        } catch (Exception e) {
            System.err.println("Échec de la récupération des vols via l'API externe. Erreur: " + e.getMessage());
        }

        // 3. Repli : Renvoyer le cache (vide ou existant)
        return cachedFlights;
    }

    @SuppressWarnings("unchecked")
    private void saveFlightsToCache(List<Object> apiFlights) {
        if (apiFlights == null) return;
        for (Object obj : apiFlights) {
            if (!(obj instanceof Map)) continue;
            try {
                Map<String, Object> fMap = (Map<String, Object>) obj;
                
                String flightDate = (String) fMap.get("flight_date");
                String flightStatus = (String) fMap.get("flight_status");
                
                Map<String, Object> departure = (Map<String, Object>) fMap.get("departure");
                String depIata = departure != null ? (String) departure.get("iata") : null;
                String depAirport = departure != null ? (String) departure.get("airport") : null;
                String depScheduled = departure != null ? (String) departure.get("scheduled") : null;
                
                Map<String, Object> arrival = (Map<String, Object>) fMap.get("arrival");
                String arrIata = arrival != null ? (String) arrival.get("iata") : null;
                String arrAirport = arrival != null ? (String) arrival.get("airport") : null;
                String arrScheduled = arrival != null ? (String) arrival.get("scheduled") : null;
                
                Map<String, Object> airline = (Map<String, Object>) fMap.get("airline");
                String airlineName = airline != null ? (String) airline.get("name") : null;
                
                Map<String, Object> flightInfo = (Map<String, Object>) fMap.get("flight");
                String flightIata = flightInfo != null ? (String) flightInfo.get("iata") : null;
                
                if (depIata == null || arrIata == null) continue;
                
                // Recherche par code de vol et date de vol pour éviter les doublons                
                Optional<Flight> existingOpt = flightRepository.findByFlightIataAndFlightDate(flightIata, flightDate);

                Flight flight;
                if (existingOpt.isPresent()) {
                    // Update existing ->  setters still work thanks to @Setter
                    flight = existingOpt.get();
                    flight.setFlightDate(flightDate);
                    flight.setFlightStatus(flightStatus);
                    flight.setDepIata(depIata.toUpperCase());
                    flight.setArrIata(arrIata.toUpperCase());
                    flight.setDepAirport(depAirport);
                    flight.setArrAirport(arrAirport);
                    flight.setDepScheduled(depScheduled);
                    flight.setArrScheduled(arrScheduled);
                    flight.setAirlineName(airlineName);
                    flight.setFlightIata(flightIata);
                } else {
                    // New flight ->  use the Builder
                    flight = Flight.builder()
                        .flightDate(flightDate)
                        .flightStatus(flightStatus)
                        .depIata(depIata.toUpperCase())
                        .arrIata(arrIata.toUpperCase())
                        .depAirport(depAirport)
                        .arrAirport(arrAirport)
                        .depScheduled(depScheduled)
                        .arrScheduled(arrScheduled)
                        .airlineName(airlineName)
                        .flightIata(flightIata)
                        .build();
                }
                
                flightRepository.save(flight);
            } catch (Exception e) {
                System.err.println("Erreur d'enregistrement d'un vol en cache: " + e.getMessage());
            }
        }
    }

    private List<Object> getFlightsFromCache(String depIata, String arrIata) {
        List<Flight> cachedFlights;
        boolean hasDep = depIata != null && !depIata.trim().isEmpty();
        boolean hasArr = arrIata != null && !arrIata.trim().isEmpty();

        if (hasDep && hasArr) {
            cachedFlights = flightRepository.findByDepIataAndArrIata(depIata.toUpperCase().trim(), arrIata.toUpperCase().trim());
        } else if (hasDep) {
            cachedFlights = flightRepository.findByDepIata(depIata.toUpperCase().trim());
        } else if (hasArr) {
            cachedFlights = flightRepository.findByArrIata(arrIata.toUpperCase().trim());
        } else {
            cachedFlights = flightRepository.findAll();
        }

        String today = LocalDate.now().toString();

        List<Object> result = new ArrayList<>();
        for (Flight flight : cachedFlights) {
            if (flight.getFlightDate() != null && flight.getFlightDate().compareTo(today) >= 0) {
                result.add(convertToMap(flight));
            }
        }
        return result;
    }

    private Map<String, Object> convertToMap(Flight flight) {
        Map<String, Object> map = new HashMap<>();
        map.put("flight_date", flight.getFlightDate());
        map.put("flight_status", flight.getFlightStatus());

        Map<String, Object> departure = new HashMap<>();
        departure.put("iata", flight.getDepIata());
        departure.put("airport", flight.getDepAirport());
        departure.put("scheduled", flight.getDepScheduled());
        map.put("departure", departure);

        Map<String, Object> arrival = new HashMap<>();
        arrival.put("iata", flight.getArrIata());
        arrival.put("airport", flight.getArrAirport());
        arrival.put("scheduled", flight.getArrScheduled());
        map.put("arrival", arrival);

        Map<String, Object> airline = new HashMap<>();
        airline.put("name", flight.getAirlineName());
        map.put("airline", airline);

        Map<String, Object> flightInfo = new HashMap<>();
        flightInfo.put("iata", flight.getFlightIata());
        map.put("flight", flightInfo);

        return map;
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void clearCache() {
        flightRepository.deleteUnreferencedFlights();
        System.out.println("Flight cache cleared.");

        // Immediately repopulate with fresh flights from AviationStack
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("access_key", apiKey);

            Map response = restTemplate.getForObject(builder.toUriString(), Map.class);
            if (response != null && response.get("data") != null) {
                List<Object> apiFlights = (List<Object>) response.get("data");
                saveFlightsToCache(apiFlights);
                System.out.println("Cache repopulated with " + apiFlights.size() + " fresh flights.");
            }
        } catch (Exception e) {
            System.err.println("Failed to repopulate cache: " + e.getMessage());
        }
    }

}