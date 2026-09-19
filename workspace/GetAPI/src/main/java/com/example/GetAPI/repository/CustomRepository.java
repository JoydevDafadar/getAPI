package com.example.GetAPI.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

@Repository
public class CustomRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Object[]> findTableRows( String queryHeader, Long userId, Long tableId, String queryCondition ) {
		
	    
 	    String sql = "SELECT * FROM (" +
	                 "SELECT ( tr.tbl_row_id ) \"RID\", " + 
	                 queryHeader + " " +
	                 "FROM tbl_dtls.table_row tr, " +
	                 "tbl_dtls.table_name tn " +
	                 "WHERE tn.user_id = :userId " +
	                 "AND tn.tbl_id = :tableId " +
	                 "AND tr.tbl_id = tn.tbl_id" +
	                 ") AA " +
	                 "WHERE 1=1"
	                 + queryCondition;
	    
	    Query query = entityManager.createNativeQuery(sql);
	    query.setParameter("userId", userId);
	    query.setParameter("tableId", tableId);
	    
	   return query.getResultList();
	    
	}
	
	public List<Tuple> executeQuery( String paramQuery ) {
		
 	    String sql = paramQuery;
	    Query query = entityManager.createNativeQuery(sql, Tuple.class);
	    
	   return query.getResultList();
	    
	}
	
	public int executeUpdate( String paramQuery ) {
		
 	    String sql = paramQuery;
	    Query query = entityManager.createNativeQuery(sql);
	    
	   return query.executeUpdate();
	    
	}
	
	public Boolean isDataPresent ( Long tableId, String column, String value ) {
		
	    
	    String sql = "select count(*) from tbl_dtls.table_row tr "
	    			+ "where tr.tbl_id = :tableId "
	    			+ "and tr." + column + " = " + value ;
	    
	    Query query = entityManager.createNativeQuery(sql);
	    query.setParameter("tableId", tableId);
	    
	    Object data = query.getResultList().get(0);
	    
	    return ((Long)data) > 0 ;


	}
	

}
