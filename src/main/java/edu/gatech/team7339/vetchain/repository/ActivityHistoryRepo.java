package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.ActivityHistory;
import edu.gatech.team7339.vetchain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface ActivityHistoryRepo extends CrudRepository<ActivityHistory,Long> {
    Set<ActivityHistory> findAllByUserAndTimestampBetweenOrderByTimestampDesc(User user,Date begin, Date end);
}
