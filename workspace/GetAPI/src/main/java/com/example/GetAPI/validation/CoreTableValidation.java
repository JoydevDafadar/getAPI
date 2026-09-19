package com.example.GetAPI.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dao.TableRow;
import com.example.GetAPI.dto.TableColumnDto;
import com.example.GetAPI.enums.ActionMethod;
import com.example.GetAPI.enums.Constraints;
import com.example.GetAPI.enums.Datatypes;
import com.example.GetAPI.repository.TableNameRepository;
import com.example.GetAPI.transaction.CoreTableTransaction;
import com.example.GetAPI.transaction.TableDetailsTransaction;
import com.example.GetAPI.utility.Constants;
import com.example.GetAPI.utility.Utility;

import jakarta.transaction.Transactional;






@Service
public class CoreTableValidation {
	
	
	@Autowired
	private TableDetailsTransaction tableDetailsTransaction;
	
	@Autowired
	private CoreTableTransaction coreTableTransaction;
	
	private final static Long TABLE_USERID_LONG = 11111L;

	private final Logger log = LoggerFactory.getLogger(CoreTableValidation.class);
	
	
	

	@Transactional
	public TableName createOrUpdateTable(
			String p_tableName,
			 List<TableColumnDto> lstTableColumnDto,
			 ActionMethod actionMethod) {

		TableName tableName = null;

		try {
			log.debug("Entering into crateTable with - " + p_tableName + " - " + TABLE_USERID_LONG );
			
			p_tableName = Utility.isNullOrEmpty(p_tableName, "table-name");
			
			
			tableName = this.coreTableTransaction.isTableExists(p_tableName, TABLE_USERID_LONG);
			
			switch (actionMethod) {
				case CREATE: {
					if( tableName != null ) 
						throw new IllegalArgumentException("Table Already Exists in the SYSTEM");
					break;
				}
				case ALTER: {
					if( tableName == null )
						throw new IllegalArgumentException("No such Table Exists in the SYSTEM");
					break;
				}
			}
				
			tableName = this.coreTableTransaction.crateTable(p_tableName, TABLE_USERID_LONG);
			List<TableColumn> lstTableColumns =
					( tableName.getLstColumn() != null ) ? tableName.getLstColumn() : new ArrayList<TableColumn>();
			
			Map<String, TableColumn> columnMap = lstTableColumns.stream()
			        .collect(Collectors.toMap(
			                TableColumn::getTblColName,
			                Function.identity()
			        ));

			// Checking primaryKey validation
			boolean existsDbPrimaryKey = lstTableColumns.stream()
					.flatMap(col -> col.getLstConstraints().stream())
					.anyMatch(c -> "PKEY".equals(c.getConstCode()));
			
			AtomicBoolean existsPrimaryKey = new AtomicBoolean(existsDbPrimaryKey);
			
			
			
			
			
			lstTableColumnDto.forEach( ( element ) -> {
				
//				String column = element.getColumnName();
				Datatypes datatypes = element.getColumnType();
				
				switch (datatypes) {
					case STR: {
						if( element.getColumnLength() == null ) element.setColumnLength(Constants.DEFAULT_COLUMN_STR_LENGTH);
						break;
					}
					case LNG: {
						if( element.getColumnLength() != null ) element.setColumnLength(null);
						break;
					}
					case FLT: {
						if( element.getColumnLength() != null ) element.setColumnLength(null);
						break;
					}

				}
				
				
				// Constraints Validation
				element.getConstraints().forEach( constEle -> {
					
					Constraints constraints = Constraints.valueOf(constEle.getCode());
					
					switch (constraints) {
						case PKEY: {
							
							if( existsPrimaryKey.get() ) {
								throw new IllegalArgumentException("Multiple PKEY insertion is not allowed.");
							}
							existsPrimaryKey.set(true);
							
							element.setColumnType( Datatypes.LNG );
							element.setColumnLength(null);
							element.setMandatory(true);
							
							break;
						}
						case UNQ: {
							break;
						}
					}

				});
				
				
				
			} );
			
			
			tableName = this.coreTableTransaction.createOrUpdateTable(lstTableColumnDto, tableName, actionMethod);
				
			return tableName;

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	

}
