package com.example.GetAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GetAPI.dao.TableColumn;

public interface TableColumnRepository extends JpaRepository<TableColumn, Long> {

}
