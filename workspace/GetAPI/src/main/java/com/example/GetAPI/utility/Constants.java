package com.example.GetAPI.utility;

import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public final static Integer DEFAULT_COLUMN_STR_LENGTH = 200;
	
	public final static String PARAM_PAGE_NO = "PageNo";
	public final static String PARAM_ROW_COUNT = "RowCount";
	public final static String PARAM_ORDER_BY = "OrderBy";
	
	public final static List<String> QUERY_PARAM_LIST = Arrays.asList(PARAM_PAGE_NO, PARAM_ROW_COUNT);
	
	
	public final static String PLACEHOLDER_ST_SYMBOL = "`${";
	public final static String PLACEHOLDER_END_SYMBOL = "}$`";
	
	public final static String PLACEHOLDER_REGEX = "`\\$\\{(.*?)\\}\\$`";

	
}
