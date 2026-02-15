package com.example.GetAPI.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="table_name", schema="tbl_dtls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableName {
	
	@Id
	@Column( name = "tbl_id")
	private Long tblId;

	@Column( name = "user_id")
	private Long userId;

	@Column( name = "tbl_name")
	private String tblName; 
	
	@Column( name = "tbl_row_count")
	private Integer tblRowCount;

	@Column( name = "effective_date")
	private Timestamp effectiveDate;

	@Column( name = "created_at")
	private Timestamp createdAt;

	@Column( name = "updated_at")
	private Timestamp updatedAt;

	@Column( name = "updated_by")
	private String updatedBy;
	
	@OneToMany( mappedBy = "tableName" )
	List<TableColumn> lstColumn;


}
