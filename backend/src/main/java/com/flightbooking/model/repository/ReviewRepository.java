package com.flightbooking.model.repository;

import com.flightbooking.model.entity.Flight;
import com.flightbooking.model.entity.Review;
import com.flightbooking.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFlight(Flight flight);
    List<Review> findByUser(User user);
}