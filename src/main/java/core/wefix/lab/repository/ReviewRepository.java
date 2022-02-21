package core.wefix.lab.repository;


import core.wefix.lab.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value= "SELECT avg(a.star) as avg_star FROM Review a WHERE a.userIdReceiveReview =:userIdReceive")
    Double avgStar(Long userIdReceive);

    @Query("SELECT a FROM Review a WHERE a.userIdReceiveReview =:userIdReceive")
    List<Review> findByUserIdReceiveReview (Long userIdReceive);

    @Query("SELECT a FROM Review a")
    List<Review> findAll();
}