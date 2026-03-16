package com.example.backend1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.backend1.model.CvCompare;

@Repository
public interface CvCompareRepository extends CrudRepository<CvCompare, Long> {

}
