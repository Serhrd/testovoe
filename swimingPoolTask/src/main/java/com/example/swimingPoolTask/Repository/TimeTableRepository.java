package com.example.swimingPoolTask.Repository;

import com.example.swimingPoolTask.Entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {
    Optional<TimeTable> findByTime(LocalDateTime time);

}
