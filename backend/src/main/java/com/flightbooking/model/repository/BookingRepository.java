package com.flightbooking.model.repository;

import com.flightbooking.model.entity.Booking;
import com.flightbooking.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByUserId(Long userId);
}
