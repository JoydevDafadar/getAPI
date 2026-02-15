package com.example.GetAPI.dao;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="col_const_mapping", schema="tbl_dtls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ColConstMapping {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "col_const_id")
	private Integer colConstId;
	

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "tbl_col_id")
	@JsonIgnore
	private TableColumn tableColumn;

	@Column( name = "const_code")
	private String constCode;

}
