package org.base.component.utils.dataset;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.unitils.core.UnitilsException;
import org.unitils.dbunit.util.MultiSchemaDataSet;

public class MultiSchemaXlsDataSetReader {
	
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(MultiSchemaXlsDataSetReader.class); 
	
	private String defaultSchemaName;
	
	public MultiSchemaXlsDataSetReader(String defaultSchemaName) {  
		 this.defaultSchemaName = defaultSchemaName;         
	} 
	
	public MultiSchemaDataSet readDataSetXls(File[] dataSetFiles){
		MultiSchemaDataSet multiSchemaDataSet = new MultiSchemaDataSet();
		multiSchemaDataSet.setDataSetForSchema(defaultSchemaName, createXlsDataSetFromFiles(dataSetFiles));
		return multiSchemaDataSet;
		
	}
	
	private IDataSet createXlsDataSetFromFiles(File[] dataSetFiles) {
		
		if (dataSetFiles.length > 1) { 
			throw new IllegalArgumentException("Does not support multiple Excel dataset files yet!");
		}                                  
		
		IDataSet dataSet = null;                 
		
		try {
			dataSet = new XlsDataSet(dataSetFiles[0]);
		}catch (Exception e) {
			throw new UnitilsException(e);
		}
		
		return dataSet;
	}

}
