package com.example.GetAPI.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "table_row", schema = "tbl_dtls" )
public class TableRow {
	
	@Id
	@Column(name = "tbl_row_id")
	private Long tblRowId;

	@Column(name = "tbl_id")
	private Long tblId;

	@Column(name = "effective_date")
	private Timestamp effectiveDate;

	@Column(name = "tbl_row_sequene")
	private Integer tblRowSequene;

	@Column(name = "col_11")
	private Long col11;

	@Column(name = "col_12")
	private BigDecimal col12;

	@Column(name = "col_13")
	private String col13;

	@Column(name = "col_21")
	private Long col21;

	@Column(name = "col_22")
	private BigDecimal col22;

	@Column(name = "col_23")
	private String col23;

	@Column(name = "col_31")
	private Long col31;

	@Column(name = "col_32")
	private BigDecimal col32;

	@Column(name = "col_33")
	private String col33;

	@Column(name = "col_41")
	private Long col41;

	@Column(name = "col_42")
	private BigDecimal col42;

	@Column(name = "col_43")
	private String col43;

	@Column(name = "col_51")
	private Long col51;

	@Column(name = "col_52")
	private BigDecimal col52;

	@Column(name = "col_53")
	private String col53;

	@Column(name = "col_61")
	private Long col61;

	@Column(name = "col_62")
	private BigDecimal col62;

	@Column(name = "col_63")
	private String col63;

	@Column(name = "col_71")
	private Long col71;

	@Column(name = "col_72")
	private BigDecimal col72;

	@Column(name = "col_73")
	private String col73;

	@Column(name = "col_81")
	private Long col81;

	@Column(name = "col_82")
	private BigDecimal col82;

	@Column(name = "col_83")
	private String col83;

	@Column(name = "col_91")
	private Long col91;

	@Column(name = "col_92")
	private BigDecimal col92;

	@Column(name = "col_93")
	private String col93;

	@Column(name = "col_101")
	private Long col101;

	@Column(name = "col_102")
	private BigDecimal col102;

	@Column(name = "col_103")
	private String col103;
	


}
