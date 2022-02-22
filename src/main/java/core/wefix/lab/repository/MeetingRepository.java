package core.wefix.lab.repository;


import core.wefix.lab.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Query("SELECT a FROM Meeting a WHERE a.meetingId =:meetingId")
    Meeting findByIdMeeting(Long meetingId);

    @Query("SELECT a FROM Meeting a WHERE a.userIdCustomerMeeting =:userIdCustomer")
    List<Meeting> findByUserIdCustomer(Long userIdCustomer);

    @Query("SELECT a FROM Meeting a WHERE a.userIdWorkerMeeting =:userIdWorker")
    List<Meeting> findByUserIdWorker(Long userIdWorker);

    @Query("SELECT a FROM Meeting a")
    List<Meeting> findAll();
}