package com.intuit.funding.dao;

import com.intuit.funding.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("from Project as p where p.user.id=:userId")
    public Page<Project> findProjectByUser(@Param("userId") long userId, Pageable pageable);
}
