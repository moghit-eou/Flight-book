package com.flightbooking.model.repository;
import com.flightbooking.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepIataAndArrIata(String depIata, String arrIata);
    List<Flight> findByDepIata(String depIata);
    List<Flight> findByArrIata(String arrIata);
    Optional<Flight> findByFlightIataAndFlightDate(String flightIata, String flightDate);
    Optional<Flight> findByFlightIata(String flightIata);
    Optional<Flight> findFirstByFlightIata(String flightIata);
    


    List<Flight> findByDepIataStartingWithIgnoreCaseAndArrIataStartingWithIgnoreCase(String depIata, String arrIata);
    List<Flight> findByDepIataStartingWithIgnoreCase(String depIata);
    List<Flight> findByArrIataStartingWithIgnoreCase(String arrIata);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM flights WHERE id NOT IN (SELECT DISTINCT flight_id FROM bookings WHERE flight_id IS NOT NULL) AND id NOT IN (SELECT DISTINCT flight_id FROM reviews WHERE flight_id IS NOT NULL)", nativeQuery = true)
    void deleteUnreferencedFlights();
}
