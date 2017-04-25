package org.base.component.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * 文件操作工具类
 *  
 */

public class FileUtils {
	
	private static Log log = LogFactory.getLog(FileUtils.class);
	
	/** 
	 * 写文件到磁盘
	 * @param dirPath
	 * @param fileName
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(String dirPath, String fileName, byte[] fileData) {
		File dir = new File(dirPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return writeFileToDisc(dir, fileName, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param dir
	 * @param fileName
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(File dir, String fileName, byte[] fileData) {
		File file = new File(dir, fileName);

		return writeToFileOutputStream(file, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param fileFullPath
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(String fileFullPath, byte[] fileData) {
		File file = new File(fileFullPath);
		return writeFileToDisc(file, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param file
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(File file, byte[] fileData) {
		return writeToFileOutputStream(file, fileData);
	}
	
	/** 
	 * 写数据到文件输出流
	 * @param file
	 * @param buf
	 * @return  
	 * @throws 
	 */
	private static boolean writeToFileOutputStream(File file, byte[] buf) {
		boolean res = false;

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(buf);
			
			res = true;
		} catch (FileNotFoundException e) {
			log.error(e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		
		return res;
	}
	
}
