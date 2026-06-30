package com.example.GetAPI.transaction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GetAPI.dao.ColConstMapping;
import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dto.TableColumnDto;
import com.example.GetAPI.enums.ActionMethod;
import com.example.GetAPI.enums.Constraints;
import com.example.GetAPI.repository.ColConstMappingRepository;
import com.example.GetAPI.repository.CustomRepository;
import com.example.GetAPI.repository.TableColumnRepository;
import com.example.GetAPI.repository.TableNameRepository;
import com.example.GetAPI.repository.TableRowRepository;
import com.example.GetAPI.utility.Utility;





@Service
public class CoreTableTransaction {
	
	
	@Autowired
	private TableNameRepository tableNameRepository;
	
	@Autowired
	private TableColumnRepository tableColumnRepository;

	@Autowired
	private TableRowRepository tableRowRepository;
	
	@Autowired
	ColConstMappingRepository colConstMappingRepository;
	
	@Autowired
	private CustomRepository customRepository;
	
	
	
	
	public TableName isTableExists(
			String p_tableName, Long userId) {

		try {
			TableName tableName = tableNameRepository.findByTblNameAndUserId(p_tableName, userId).orElse(null);
			return tableName;
			
		}catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	
	
	public TableName crateTable(
			String p_tableName,
			Long userId
			 ) {

		TableName tableName = null;

		try {
			
			tableName = this.isTableExists(p_tableName, userId);
			if( tableName == null ) {
				
				tableName = new TableName();
				long tableSequence = tableNameRepository.generateTblSequence();
				
				tableName.setTblId(tableSequence);
				tableName.setTblRowCount(0);
				tableName.setTblName(p_tableName);
				tableName.setUserId(userId);
				tableName.setUpdatedBy("SYSTEM");
				
				
			}
			
			tableName = tableNameRepository.save(tableName);
			return tableName;

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	
	
	public TableName createOrUpdateTable(
			List<TableColumnDto> lstTableColumnDto,
			TableName tableName,
			ActionMethod actionMethod ) {

		try {
							
//			tableName = this.crateTable(p_tableName, p_user_id);
//			if( tableName.getLstColumn() == null ) tableName.setLstColumn(new ArrayList<TableColumn>());
			List<TableColumn> lstTableColumns =
					( tableName.getLstColumn() != null ) ? tableName.getLstColumn() : new ArrayList<TableColumn>();
			
			Map<String, TableColumn> columnMap = lstTableColumns.stream()
			        .collect(Collectors.toMap(
			                TableColumn::getTblColName,
			                Function.identity()
			        ));
			
			Map<Integer, Boolean> sequenceMap = lstTableColumns.stream()
			        .collect(Collectors.toMap(
			                TableColumn::getTblColSeq,
			                col -> true,
			                (ele1, ele2) -> ele1
			        ));
			
			AtomicInteger  currentSeq = new AtomicInteger(0);
			List<Integer> missingSequences = IntStream.rangeClosed(1, 10)
			        .filter(i -> !sequenceMap.containsKey(i))
			        .boxed()
			        .collect(Collectors.toList());
			
			
			

	
			lstTableColumnDto.forEach( ( element ) -> {
				
				TableColumn tableColumn = null;
				
				if( !columnMap.containsKey(element.getColumnName()) ) {
					
					tableColumn = new TableColumn();
					Long columnSeq = tableNameRepository.generateTblSequence();
					
					tableColumn.setTblColId(columnSeq);
					tableColumn.setTableName(tableName);
					
					columnMap.put(element.getColumnName(), tableColumn);
					if( tableName.getLstColumn() == null ) {
						tableName.setLstColumn(new ArrayList<TableColumn>());
					}
					tableName.getLstColumn().add(tableColumn);
					
				}
				else if( actionMethod.equals(ActionMethod.ALTER) ) {
					
					tableColumn = columnMap.get(element.getColumnName());
					
				}
				
				for( Method method : element.getClass().getMethods() ) {
					
					if( method.getName().equals("getConstraints") ) {
						
						List<ColConstMapping> lstConsMap = 
								( tableColumn.getLstConstraints() != null ) ? tableColumn.getLstConstraints() : new ArrayList<ColConstMapping>();
						Set<String> existingCodes = lstConsMap.stream()
					            .map(ColConstMapping::getConstCode)
					            .collect(Collectors.toSet());
						
						
						List<Constraints> lstInputConst = 
								(element.getConstraints() != null) ? element.getConstraints() : new ArrayList<Constraints>();

						
						for (Constraints inputConst : lstInputConst) {
					        if (!existingCodes.contains(inputConst.toString())) {
					        	
					            ColConstMapping newMapping = new ColConstMapping();
					            
					            newMapping.setConstCode(inputConst.toString());
					            
					            tableColumn = tableColumnRepository.save(tableColumn);
					            newMapping.setTableColumn(tableColumn);
					            
					            lstConsMap.add(newMapping);
					            if( tableColumn.getLstConstraints() == null ) {
					            	tableColumn.setLstConstraints(new ArrayList<ColConstMapping>());
								}
					            tableColumn.getLstConstraints().add(newMapping);
					            
					            existingCodes.add(inputConst.toString());
					            colConstMappingRepository.save(newMapping);
					        }
					    }

						
						
					}		
					else if( TableColumnDto.DTO_TO_SETTER_MAP.containsKey(method.getName()) ) {
						
						Object dtoValue = Utility.callGetter(element, method.getName());
						
						if(dtoValue != null)
							Utility.callSetter(tableColumn, 
									TableColumnDto.DTO_TO_SETTER_MAP.get(method.getName()), 
									dtoValue);
					}
					
				}
				
				// setting sequence
				if( currentSeq.get() >= missingSequences.size() ) {
					throw new IllegalArgumentException("No of Columns are getting exceeded.");
				}
				tableColumn.setTblColSeq(missingSequences.get(currentSeq.get()));
				currentSeq.set(currentSeq.get()+1);
				
				tableColumn.setUpdatedBy("SYSTEM");
				tableColumnRepository.save(tableColumn);
				
				
				
			} );
			
			
			
//			
//			TableRow tableRow = this.tableDetailsTransaction.getRowById(tableName.getTblId(), primaryId);
			
//			return Utility.TblRowToObjectDto(tableName.getLstColumn(), tableRow);
			tableNameRepository.saveAndFlush(tableName);
			
			return tableName;

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	

}
