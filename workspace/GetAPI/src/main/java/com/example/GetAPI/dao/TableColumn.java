package com.example.GetAPI.dao;

import java.sql.Timestamp;
import java.util.List;

import com.example.GetAPI.enums.Datatypes;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="table_column", schema="tbl_dtls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableColumn {
	
	
	@Id
	@Column( name="tbl_col_id" )
	private Long tblColId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "tbl_id" )
	@JsonIgnore
	private TableName tableName;

	@Column( name="tbl_col_name" )
	private String tblColName;
	
	@Column( name="tbl_col_type" )
	@Enumerated(EnumType.STRING)
	private Datatypes tblColType;
	
	@Column( name="tbl_col_default" )
	private String tblColDefault;
	
	@Column( name="tbl_col_nullable" )
	private Boolean tblColNullable;
	
	@Column( name="effective_date" )
	private Timestamp effectiveDate;
	
	@Column( name="created_at" )
	private Timestamp createdAt;
	
	@Column( name="updated_at" )
	private Timestamp updatedAt;
	
	@Column( name="updated_by" )
	private String updatedBy;
	
	@Column( name="tbl_col_seq" )
	private Integer tblColSeq;
	
	@Column( name="tbl_col_length" )
	private Integer tblColLength;
	
	@OneToMany( mappedBy = "tableColumn" )
	private List<ColConstMapping> lstConstraints;
	
	@PrePersist
    protected void onCreate() {
		createdAt = new Timestamp(System.currentTimeMillis());
	    updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }


}
