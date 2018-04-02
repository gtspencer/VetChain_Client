package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.RecentActivity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecentActivityRepo extends CrudRepository<RecentActivity,Long> {
    public List<RecentActivity> findAllByDateLessThan(Date date);
    public List<RecentActivity> findAll();
}
