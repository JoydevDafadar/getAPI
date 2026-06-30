package com.example.GetAPI.dao;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table( name = "t_user", 
schema = "tbl_dtls",
uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_t_user_usergroupid_username",
			columnNames = {"usergroupid", "usergroupid"}
		)
})
public class User implements UserDetails {

	@Id
	@Column( name = "userid")
	private Long userId;
	
	@Column( name = "usergroupid")
	private Long usergroupId;

	@Column( name = "username")
	private String userName;
	
	@Column( name = "password")
	private String password;
	
	@Column( name = "useremail")
	private String userEmail;
	
	@Column( name = "userrole")
	private String userRole;
	
	@Column( name = "createdate")
	private Timestamp createDate;
	
	@Column( name = "effectivedate")
	private Timestamp effectiveDate;
	
	@PrePersist
    protected void onCreate() {
		createDate = new Timestamp(System.currentTimeMillis());
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of();
	}

	@Override
	public String getUsername() {
		return this.userName;
	}
	
}
