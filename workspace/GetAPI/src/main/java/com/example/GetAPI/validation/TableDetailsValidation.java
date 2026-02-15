package com.example.GetAPI.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.GetAPI.dao.ColConstMapping;
import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dao.TableRow;
import com.example.GetAPI.dto.Pagenation;
import com.example.GetAPI.enums.Datatypes;
import com.example.GetAPI.transaction.TableDetailsTransaction;
import com.example.GetAPI.utility.Constants;
import com.example.GetAPI.utility.Utility;

@Service
public class TableDetailsValidation {

	@Autowired
	private TableDetailsTransaction tableDetailsTransaction;

	private final static Long TABLE_USERID_LONG = 12345L;

	private final Logger log = LoggerFactory.getLogger(TableDetailsValidation.class);

	public TableName getColumn(String p_tableName) {

		TableName tableName = null;

		try {
			log.debug("Entering into getTable with - " + p_tableName + " - " + TABLE_USERID_LONG);

			if (p_tableName == null || p_tableName.equals("")) {
				throw new NullPointerException("Table name should not be null or empty");
			}
			if (TABLE_USERID_LONG == null) {
				throw new NullPointerException("User Id name should not be null or empty");
			}

			tableName = this.tableDetailsTransaction.getTableData(p_tableName, TABLE_USERID_LONG);

		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
		return tableName;
	}

	// datatype conversion -- triming -- conversion
	// validation Check based on datatypes and length
	// data operation

	public Object getTableRow(String P_tableName, MultiValueMap<String, String> queryParams) {

		try {

//			Integer pageNumber = 1;
//			Integer rowCount = 10;
			Pagenation pagination = new Pagenation();

			Map<String, String> propertyMap = new HashMap<String, String>();

			TableName tableName = this.tableDetailsTransaction.getTableData(P_tableName, TABLE_USERID_LONG);

			tableName.getLstColumn().forEach((eachColumn) -> {

				String colName = eachColumn.getTblColName();
//				Integer length = eachColumn.getTblColLength();
//				String dataType = eachColumn.getTblColType();

				if (queryParams.containsKey(colName)) {
					String groupString = queryParams.get(colName).stream().map(String::valueOf)
							.collect(Collectors.joining("', '"));

					propertyMap.put(colName, ("( '" + groupString + "' )"));
				}

			});

			Constants.QUERY_PARAM_LIST.forEach((eachParams) -> {

				if (queryParams.containsKey(eachParams)) {

					switch (eachParams) {
					case Constants.PARAM_PAGE_NO: {
						Integer pageNumber = Integer.valueOf(queryParams.get(eachParams).get(0));
						pagination.setPageNumber(pageNumber);
						break;
					}
					case Constants.PARAM_ROW_COUNT: {
						Integer rowCount = Integer.valueOf(queryParams.get(eachParams).get(0));
						pagination.setRowCount(rowCount);
						break;
					}
					case Constants.PARAM_ORDER_BY: {
						String orderBy = String.valueOf(queryParams.get(eachParams));
						boolean isColumnPresent = tableName.getLstColumn().stream()
								.anyMatch(v -> orderBy.equals(v.getTblColName()) );
						
						if( isColumnPresent ) pagination.setOrderBy(orderBy);
						break;
					}

					}
				}

			});

			log.debug("Entering into getTable " + tableName);

			queryParams.forEach((key, value) -> {
				System.out.println(key + " --- --- " + value);
			});

			System.out.println(queryParams);
			Object dataObject = this.tableDetailsTransaction.getTableRowData(propertyMap, pagination, tableName,
					TABLE_USERID_LONG);

			return dataObject;

		} catch (Exception e) {
			log.debug("Exception : /get-table :- " + e);
			return ResponseEntity.status(404).body(e.getMessage());
		}

	}
	
	public Map<String, Object> getTableDataById(String p_tableName, Long primaryId) {

		TableName tableName = null;

		try {
			log.debug("Entering into getTableDataById with - " + p_tableName + " - " + TABLE_USERID_LONG 
					 + " primaryId - " + primaryId );
			
			p_tableName = Utility.isNullOrEmpty(p_tableName, "table-name");
			primaryId = Utility.isNullOrEmpty(primaryId, "primary-id");

			tableName = this.tableDetailsTransaction.getTableData(p_tableName, TABLE_USERID_LONG);
			
			TableRow tableRow = this.tableDetailsTransaction.getRowById(tableName.getTblId(), primaryId);
			
			return Utility.TblRowToObjectDto(tableName.getLstColumn(), tableRow);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	
	//Save
	
	public Object saveTableData(String p_tableName, Map<String, Object> bodyParams) {

		TableName tableName = null;

		try {
			log.debug("Entering into saveTableData with - " + p_tableName + " - " + TABLE_USERID_LONG);

			tableName = this.tableDetailsTransaction.getTableData(p_tableName, TABLE_USERID_LONG);
			
			
			
			// triming bodyParams
			List<String> columnNames = tableName.getLstColumn().stream()
			        .map(TableColumn::getTblColName)
			        .collect(Collectors.toList());

			bodyParams.keySet().removeIf( (ele) -> !columnNames.contains(ele) );
			
			
			
			// Validating input body
			tableName.getLstColumn().forEach((eachColumn) -> {

				String colName = eachColumn.getTblColName();
				Integer length = eachColumn.getTblColLength();
				String dataType = eachColumn.getTblColType();
				
				List<String> constList = eachColumn.getLstConstraints().stream()
						.map(ColConstMapping::getConstCode)
						.collect(Collectors.toList());

				if (bodyParams.containsKey(colName)) {

					Object data = bodyParams.get(colName);
					Datatypes dt = Datatypes.valueOf(dataType);

					switch (dt) {
					case STR: {

						if (!(data instanceof String)) {
							throw new NullPointerException(
									"Expeccted DataType -STR. But " + colName + " is " + data.getClass().getName());
						}
						String trimedString = String.valueOf(data).trim();

						if ( length != null && trimedString.length() > length) {
							throw new NullPointerException("Expeccted Length <" + length + ". But " + colName + " is "
									+ trimedString.length());
						}
						
						bodyParams.put(colName, trimedString);
						break;
					}
					case INT: {

						if (!(data instanceof Integer)) {
							throw new NullPointerException(
									"Expeccted DataType -INT. But " + colName + " is " + data.getClass().getName());
						}
						
						bodyParams.put(colName, data);
						break;
					}
					case FLT: {

						if (!(data instanceof Integer)) {
							throw new NullPointerException(
									"Expeccted DataType -STR. But " + colName + " is " + data.getClass().getName());
						}
						
						bodyParams.put(colName, data);
						break;
					}

					}
				} else if ( !(constList.contains("PKEY") || (eachColumn.getTblColNullable() == true)) ) {
					throw new NullPointerException(colName + " is not present.");
				}

			});
			
			Object obj = this.tableDetailsTransaction.saveTableData(bodyParams, tableName, TABLE_USERID_LONG);
			return obj;

		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	
	// Update 
	
	public Map<String, Object> updateRowById(String p_tableName, Long primaryId, Map<String, Object> pRowData ) {

		try {
			log.debug("Entering into getTableDataById with - " + p_tableName + " - " + TABLE_USERID_LONG 
					 + " primaryId - " + primaryId );
			
			p_tableName = Utility.isNullOrEmpty(p_tableName, "table-name");
			primaryId = Utility.isNullOrEmpty(primaryId, "primary-id");

			TableName tableName = this.tableDetailsTransaction.getTableData(p_tableName, TABLE_USERID_LONG);
			
			tableName.getLstColumn().forEach((eachColumn) -> {

				String colName = eachColumn.getTblColName();
				if (pRowData.containsKey(colName)) {
					
					try {	
						Object data = this.validateInputDatatype(eachColumn, pRowData.get(colName));
						pRowData.put(colName, data);
						
					} catch (Exception e) {
						throw new IllegalArgumentException(e.getMessage());
					}

				}

			});
			
			
			return this.tableDetailsTransaction.updateRow(tableName, primaryId, pRowData );

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Object validateInputDatatype( TableColumn tableColumn, Object data  ) throws Exception {
		
		String colName = tableColumn.getTblColName();
		Integer length = tableColumn.getTblColLength();
		String dataType = tableColumn.getTblColType();

		Datatypes dt = Datatypes.valueOf(dataType);
		
		switch (dt) {
		case STR: {

			if (!(data instanceof String)) {
				throw new NullPointerException(
						"Expeccted DataType -STR. But " + colName + " is " + data.getClass().getName());
			}
			String trimedString = String.valueOf(data).trim();

			if ( length != null && trimedString.length() > length) {
				throw new NullPointerException("Expeccted Length <" + length + ". But " + colName + " is "
						+ trimedString.length());
			}
			
			return trimedString;
		}
		case INT: {

			if (!(data instanceof Integer)) {
				throw new NullPointerException(
						"Expeccted DataType -INT. But " + colName + " is " + data.getClass().getName());
			}
			
			return data;
		}
		case FLT: {

			if (!(data instanceof Integer)) {
				throw new NullPointerException(
						"Expeccted DataType -STR. But " + colName + " is " + data.getClass().getName());
			}
			
			return data;
		}

		}
		
		return null;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
