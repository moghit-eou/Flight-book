package com.flightbooking.model.repository;

import com.flightbooking.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepIataAndArrIata(String depIata, String arrIata);
    List<Flight> findByDepIata(String depIata);
    List<Flight> findByArrIata(String arrIata);
    Optional<Flight> findByFlightIataAndFlightDate(String flightIata, String flightDate);

    // Méthodes pour la recherche partielle dans le cache (ex: l'utilisateur tape "C" ou "CM")
    List<Flight> findByDepIataStartingWithIgnoreCaseAndArrIataStartingWithIgnoreCase(String depIata, String arrIata);
    List<Flight> findByDepIataStartingWithIgnoreCase(String depIata);
    List<Flight> findByArrIataStartingWithIgnoreCase(String arrIata);
}
