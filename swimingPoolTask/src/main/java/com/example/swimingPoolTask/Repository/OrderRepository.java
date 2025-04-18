package com.example.swimingPoolTask.Repository;

import com.example.swimingPoolTask.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByClientId(Long clientId);
    boolean existsByClientIdAndTimeBetween(Long clientId, LocalDateTime start, LocalDateTime end);
}
