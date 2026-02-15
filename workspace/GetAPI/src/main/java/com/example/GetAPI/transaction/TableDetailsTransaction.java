package com.example.GetAPI.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties.Datatype;
import org.springframework.stereotype.Service;

import com.example.GetAPI.dao.ColConstMapping;
import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dao.TableRow;
import com.example.GetAPI.dto.Pagenation;
import com.example.GetAPI.enums.Constraints;
import com.example.GetAPI.enums.Datatypes;
import com.example.GetAPI.repository.CustomRepository;
import com.example.GetAPI.repository.TableNameRepository;
import com.example.GetAPI.repository.TableRowRepository;
import com.example.GetAPI.utility.Utility;

@Service
public class TableDetailsTransaction {

	@Autowired
	private TableNameRepository tableNameRepository;

	@Autowired
	private TableRowRepository tableRowRepository;
	
	@Autowired
	private CustomRepository customRepository;
	

	public TableName getTableData(String p_tableName, Long p_userId) {

		TableName tableName = null;
		try {

			tableName = tableNameRepository.findByTblNameAndUserId(p_tableName, p_userId)
					.orElseThrow( () -> {
						throw new NoSuchElementException("Table not found by this name - " + p_tableName );
					});

		} catch (NoSuchElementException e) {
			throw new NoSuchElementException(e.getMessage());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return tableName;
	}

	
	
	public List<Map<String, Object>> getTableRowData(Map<String, String> propertyMap, Pagenation pagenation, TableName tableName,
			Long userId) {

		try {
			List<Map<String, Object>> dtoObject = new ArrayList<Map<String, Object>>(); 
			List<String> columnList = new ArrayList<String>(); 
			
			List<String> headerList = new ArrayList<String>();
			StringBuilder queryHeader = new StringBuilder("");
			StringBuilder queryCondition = new StringBuilder("");


			for (TableColumn eachColumn : tableName.getLstColumn()) {
				
				StringBuilder tempString =  new StringBuilder("( tr.col_");

				String colName = eachColumn.getTblColName();
		
				tempString.append( Utility.getColInd(eachColumn) );
				tempString.append( " ) \"" + colName + "\"");

				headerList.add(tempString.toString());
				columnList.add(colName);
				
				// Adding condition 
				if( propertyMap.containsKey(colName) ) {
					queryCondition.append(" AND AA.\"" + colName + "\""
							+ " in " + propertyMap.get(colName));
				}
				
			}
			queryHeader.append(String.join(", ", headerList));
			
			
			// Pagination Concept
			Integer skipRow = (pagenation.getPageNumber() - 1) * pagenation.getRowCount();
			Integer selectRow = pagenation.getRowCount();
			
			queryCondition.append(" ORDER BY AA.\"" + pagenation.getOrderBy() + "\" ASC");
			queryCondition.append(" LIMIT " + selectRow  + " OFFSET " + skipRow );
			
			
			List<Object[]> data =  customRepository.findTableRows(queryHeader.toString(), userId, tableName.getTblId(), queryCondition.toString());
			
			// Binding rough object with DTO
			for( int ind = 0; ind<data.size(); ind++ ) {
				
				Map<String, Object> JOSNDto = new HashMap<String, Object>();
				Object[] element = data.get(ind);
				
				for( int i = 0; i<columnList.size(); i++ ) {
					JOSNDto.put(columnList.get(i), element[i+1]);
				}
				
				dtoObject.add(JOSNDto);	
			}
			
			return dtoObject;

		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}

	}
	
	
	public Map<String, Object> saveTableData( Map<String, Object> bodyParams, TableName tableName, Long userId ) {

		try {
			
			TableRow tableRow = new TableRow();
			Long seqLong = tableRowRepository.generateRowSequence();
			
			tableRow.setTblId(tableName.getTblId());
			tableRow.setTblRowId(seqLong);
			tableRow.setTblRowSequene(tableName.getTblRowCount()+1);
			tableName.setTblRowCount(tableName.getTblRowCount()+1);


			for (TableColumn eachColumn : tableName.getLstColumn()) {
				
				List<String> constList = eachColumn.getLstConstraints().stream()
						.map(ColConstMapping::getConstCode)
						.collect(Collectors.toList());
				
				
				// adding data
				StringBuilder tempString =  new StringBuilder("setCol");

				String colName = eachColumn.getTblColName();
				String dataType = eachColumn.getTblColType();
				int sequence = eachColumn.getTnlColSeq();
				
			
				Datatypes dt = Datatypes.valueOf(dataType);
				tempString.append( sequence );
				tempString.append( dt.getCode() );
				
				
				constList.forEach( ( element ) -> {
					
					Constraints ct = Constraints.valueOf(element);
					
					switch (ct) {
						case PKEY : {
							bodyParams.put(colName, Math.toIntExact(seqLong) );
							break;
						}
						case UNQ : {
							
							String dbColName = "col_" + Utility.getColInd(eachColumn);
							Boolean isPresent = customRepository.isDataPresent(tableName.getTblId(), dbColName,
									"'" + bodyParams.get(colName) + "'" );
							
							if( isPresent ) {
								throw new IllegalArgumentException( "Expeccted " + colName + " to be - UNIQUE. But " 
											+ bodyParams.get(colName) + " present in system. " );
							}
							
							break;
						}
					}
					
				});
				
				
				
				Utility.callSetter(tableRow, tempString.toString(), bodyParams.get(colName));
				
			}
			tableRowRepository.saveAndFlush(tableRow);
			
			return Utility.TblRowToObjectDto(tableName.getLstColumn(), tableRow);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}

	}
	
	
	public TableRow getRowById(Long p_tableId, Long p_tableRow_Id) {

		try {
			
			TableRow tableRow = this.tableRowRepository.findByTblRowIdAndTblId( p_tableRow_Id, p_tableId )
					.orElseThrow( () -> {
						throw new NoSuchElementException("Row not found by this Id - " + p_tableRow_Id );
					});
			
			return tableRow;
			
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	
	
	public Map<String, Object> updateRow( TableName tableName, Long primaryId, Map<String, Object> pRowData ){
		
		try {
			
			TableRow tableRow = this.getRowById(tableName.getTblId(), primaryId);
			
			tableName.getLstColumn().forEach((eachColumn) -> {
				
				String colName = eachColumn.getTblColName();
				
				if ( pRowData.containsKey(colName) ) {
					
					String column = "setCol" + Utility.getColInd(eachColumn);
					Object value = pRowData.get(colName);
					
					List<String> constList = eachColumn.getLstConstraints().stream()
							.map(ColConstMapping::getConstCode)
							.collect(Collectors.toList());
					
					
					constList.forEach( ( element ) -> {
						
						Constraints ct = Constraints.valueOf(element);
						
						switch (ct) {
							case PKEY : {
								break;
							}
							case UNQ : {
								
								String dbColName = "col_" + Utility.getColInd(eachColumn);
								Boolean isPresent = customRepository.isDataPresent(tableName.getTblId(), dbColName,
										"'" + value + "'" );
								
								if( isPresent ) {
									throw new IllegalArgumentException( "Expeccted " + colName + " to be - UNIQUE. But " 
												+ value + " present in system. " );
								}
								
								break;
							}
						}
						
					});
					
					Utility.callSetter(tableRow, column, value);
				}

			});
			
			tableRowRepository.saveAndFlush(tableRow);
			
			return Utility.TblRowToObjectDto(tableName.getLstColumn(), tableRow);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	

}
