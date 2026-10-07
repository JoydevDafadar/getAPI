package com.example.GetAPI.dao;

import com.example.GetAPI.enums.Datatypes;

public interface TableColumnMetadata {

    Long getTblColId();

    String getTblColName();

    Datatypes getTblColType();

    Integer getTblColLength();

    Boolean getTblColNullable();

    String getConstraintType();
    
}