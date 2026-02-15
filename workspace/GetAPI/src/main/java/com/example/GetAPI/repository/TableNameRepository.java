package com.example.GetAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GetAPI.dao.TableName;

@Repository
public interface TableNameRepository extends JpaRepository<TableName, Long> {

	List<TableName> findByTblId(long tableId);

	Optional<TableName> findByTblNameAndUserId(String p_tableName, Long p_userId);
	
	
	
}
