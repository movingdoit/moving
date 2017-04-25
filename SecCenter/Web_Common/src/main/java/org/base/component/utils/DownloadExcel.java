package org.base.component.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
/**
 * 文件下载工具
 * @author lyc
 * @date 2016年1月14日
 */
public class DownloadExcel {
	//下载导出在服务器上的Excel到本地
	/**
	 * 导出的文件名exportFileName最好为英文
	 * @param response
	 * @param fileName 服务器源文件文件名
	 * @param url 服务器源文件地址
	 * @param exportFileName 保存再本地的文件名
	 * @throws IOException
	 */
	public static void download(HttpServletResponse response,String fileName,String url,String exportFileName) throws IOException{
		OutputStream os = response.getOutputStream();
		try {
			response.reset();
			response.setHeader("Content-Disposition","attachment; filename="+exportFileName+".xls");
			response.setContentType("application/octet-stream; charset=utf-8");
			os.write(FileUtils.readFileToByteArray(new File(url+fileName)));
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
