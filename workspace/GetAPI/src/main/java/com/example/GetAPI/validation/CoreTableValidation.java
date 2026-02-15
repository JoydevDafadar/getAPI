package com.example.GetAPI.validation;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dao.TableRow;
import com.example.GetAPI.transaction.TableDetailsTransaction;
import com.example.GetAPI.utility.Utility;






@Service
public class CoreTableValidation {
	
	
	
	@Autowired
	private TableDetailsTransaction tableDetailsTransaction;
	
	
	
	private final static Long TABLE_USERID_LONG = 12345L;

	private final Logger log = LoggerFactory.getLogger(CoreTableValidation.class);
	
	
	

	public Map<String, Object> getTableDataById(String p_tableName, Long primaryId) {

		TableName tableName = null;

		try {
			log.debug("Entering into getTableDataById with - " + p_tableName + " - " + TABLE_USERID_LONG 
					 + " primaryId - " + primaryId );
			
			p_tableName = Utility.isNullOrEmpty(p_tableName, "table-name");
			primaryId = Utility.isNullOrEmpty(primaryId, "primary-id");

//			tableName = this.tableDetailsTransaction.getTableData(p_tableName, TABLE_USERID_LONG);
//			
//			TableRow tableRow = this.tableDetailsTransaction.getRowById(tableName.getTblId(), primaryId);
			
//			return Utility.TblRowToObjectDto(tableName.getLstColumn(), tableRow);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}
	

}
