package com.example.GetAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableColumnMetadata;

public interface TableColumnRepository extends JpaRepository<TableColumn, Long> {

	
	@Query(value = """
					select
				    c.ordinal_position AS tblColId,
				    c.column_name as tblColName,
				    CASE
				        WHEN c.udt_name IN ('int2', 'int4', 'int8') THEN 'LNG'
				        WHEN c.udt_name IN ('varchar', 'text', 'bpchar') THEN 'STR'
				        WHEN c.udt_name IN ('float4', 'float8', 'numeric') THEN 'FLT'
				        WHEN c.udt_name = 'bool' THEN 'STR'
				        WHEN c.udt_name IN ('date', 'timestamp', 'timestamptz') THEN 'STR'
				        ELSE UPPER(c.udt_name)
				    END AS tblColType,
				    c.character_maximum_length as tblColLength,
				    CASE
				        WHEN c.is_nullable = 'YES' THEN true
				        ELSE false
				    END AS tblColNullable,
				    CASE
				        WHEN tc.constraint_type = 'PRIMARY KEY' THEN 'PKEY'
				    END AS constraint_type
				FROM information_schema.columns c
				LEFT JOIN information_schema.key_column_usage kcu
				    ON c.table_schema = kcu.table_schema
				    AND c.table_name = kcu.table_name
				    AND c.column_name = kcu.column_name
				LEFT JOIN information_schema.table_constraints tc
				    ON kcu.constraint_name = tc.constraint_name
				    AND kcu.table_schema = tc.table_schema
				    AND kcu.table_name = tc.table_name
				WHERE c.table_schema = :schemaName
				  AND c.table_name = :tableName
				ORDER BY c.ordinal_position
		    """,
		    nativeQuery = true)
		List<TableColumnMetadata> getTableColumnMetadata(
		        @Param("schemaName") String schemaName,
		        @Param("tableName") String tableName
		);
	
}
