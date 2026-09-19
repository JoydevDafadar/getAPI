package com.example.GetAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GetAPI.dao.TEventTransaction;

@Repository
public interface TEventTransactionRepository extends JpaRepository<TEventTransaction, Long> {

	List<TEventTransaction> findAllByUsergroupIdAndEventCodeAndApiEpAndApiMethodAndActionType(Long userGroupId, String eventCode,
			String url, String method, String actionType);

	
	
}
