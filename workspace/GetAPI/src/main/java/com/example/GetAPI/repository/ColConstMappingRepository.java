package com.example.GetAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GetAPI.dao.ColConstMapping;

@Repository
public interface ColConstMappingRepository extends JpaRepository<ColConstMapping, Integer> {
	
	

}
