package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface AppointmentRepo extends CrudRepository<Appointment,Long> {
    Set<Appointment> findAllByDoctorId(int docId);
    Set<Appointment> findAllByDoctorIdAndDateBetweenOrderByTimeAsc(int docId, Date begin, Date end);
    Set<Appointment> findAllByPetIdAndDateBetweenOrderByTimeAsc(int petId,Date begin,Date end);
}
