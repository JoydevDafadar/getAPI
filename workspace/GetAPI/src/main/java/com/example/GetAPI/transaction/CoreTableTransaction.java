package com.example.GetAPI.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GetAPI.repository.CustomRepository;
import com.example.GetAPI.repository.TableNameRepository;
import com.example.GetAPI.repository.TableRowRepository;





@Service
public class CoreTableTransaction {
	
	
	@Autowired
	private TableNameRepository tableNameRepository;

	@Autowired
	private TableRowRepository tableRowRepository;
	
	@Autowired
	private CustomRepository customRepository;
	
	
	
	
	
	
	

}
