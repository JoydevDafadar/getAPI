package com.example.GetAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dao.TableRow;

@Repository
public interface TableRowRepository extends JpaRepository<TableRow, Long> {
	
//	@Query( value = "select * from\r\n"
//			+ "(select \r\n"
//			+ "\r\n"
//			+ " ?1 "
//			+ "\r\n"
//			+ "from tbl_dtls.table_row tr,\r\n"
//			+ "tbl_dtls.table_name tn\r\n"
//			+ "\r\n"
//			+ "where tn.user_id = ?2 \r\n"
//			+ "and tn.tbl_id = ?3 \r\n"
//			+ "and tr.tbl_id = tn.tbl_id) AA\r\n"
//			+ "where 1=1"
//			+ "" , nativeQuery = true )
//	 List<Object> findTableRows(String queryHeader, Long userId, Long tableId);
	
	@Query( value = "select (nextval('tbl_dtls.tbls_row_seq'::regclass) || to_char(CURRENT_DATE::timestamp with time zone, 'ddmmyyyy'::text))::bigint",
			nativeQuery = true)
	Long generateRowSequence();

	Optional<TableRow> findByTblRowIdAndTblId(Long p_tableId, Long p_tableRow_Id);

	long deleteByTblRowIdAndTblId(Long p_tableRow_Id, Long p_tableId);

}
