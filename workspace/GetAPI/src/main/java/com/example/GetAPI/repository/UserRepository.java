package com.example.GetAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.GetAPI.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query( value = "select (nextval('tbl_dtls.tbls_seq'::regclass) || to_char(CURRENT_DATE::timestamp with time zone, 'ddmmyyyy'::text))::bigint",
			nativeQuery = true)
	Long generateTblSequence();
	
	Optional<User> findByUserName(String username);

	Optional<User> findByUserNameAndUsergroupId(String userName, long l);

}
