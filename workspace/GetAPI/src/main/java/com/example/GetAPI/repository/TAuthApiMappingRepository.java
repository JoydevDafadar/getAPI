
package com.example.GetAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import com.example.GetAPI.dao.TAuthApiMapping;
import com.example.GetAPI.dao.TAuthApiMappingId;

public interface TAuthApiMappingRepository extends JpaRepository<TAuthApiMapping, TAuthApiMappingId> {

	Optional<TAuthApiMapping> findByUsergroupIdAndApiEpAndApiMethod(long l, String requestUrl, String requestMethod);
	
	@Query(value = """
	            SELECT tbl_dtls.getsequncebycode(
	                CAST(:groupId AS BIGINT),
	                CAST(:seqCode AS VARCHAR)
	            )
	            """,
	        nativeQuery = true)
	Optional<String> getSequenceByCode(Long userIdLong, String seqCode);
	
	
}
