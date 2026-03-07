package com.example.backend1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend1.model.JobSpec;

@Repository
public interface JobSpecRepository extends JpaRepository<JobSpec, Long> {

}
