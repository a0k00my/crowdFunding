package com.intuit.funding.dao;

import com.intuit.funding.entities.Contribution;
import com.intuit.funding.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    @Query("from Contribution as c where c.user.id=:userId")
    public List<Contribution> getContribution(@Param("userId") Long userId);
}
