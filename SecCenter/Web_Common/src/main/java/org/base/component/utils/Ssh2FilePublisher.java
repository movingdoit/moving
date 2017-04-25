package org.base.component.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 */
public class Ssh2FilePublisher {

	private Logger log = Logger.getLogger(this.getClass());

	private static String SFTPKEY = "sftp";

	private ChannelSftp sftpClient;

	private Session session;

	private String host;

	private int port;

	private String userName;

	private String password;

	private String uploadPath;

	@SuppressWarnings("unused")
	private Ssh2FilePublisher() {
	}

	public Ssh2FilePublisher(String host, int port, String userName,
			String password, String uploadPath) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.uploadPath = uploadPath;
	}

	/**
	 * 获取连接
	 */
	public void getConnection() {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, host, port);
			MyUserInfo ui = new MyUserInfo();
			ui.setPassword(password);
			session.setUserInfo(ui);
			session.connect();
			Channel channel = session.openChannel(SFTPKEY);
			channel.connect();
			sftpClient = (ChannelSftp) channel;
//			try {
//				sftpClient.chmod(777, uploadPath);
//			} catch (SftpException e) {
//				e.printStackTrace();
//			}
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开连接
	 */
	public void closeConnection() {
		if (sftpClient != null && sftpClient.isConnected()) {
			sftpClient.disconnect();
		}
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * 上传单个文件
	 * 
	 * @param file
	 *            要上传的文件路径
	 * @param remotePath
	 *            上传文件的存放目录
	 */
	public void upload(String file, String remoteDir) {
		try {
			createDir(remoteDir);
			this.sftpClient.cd(remoteDir);
			log.info("start file publish");
			log.info("srcFile:"+file);
			log.info("remoteFile:"+remoteDir);
			this.sftpClient.put(file, remoteDir);
			log.info("end file publish!");
		} catch (SftpException e) {
			log.info("file publish fail!");
			e.printStackTrace();
		}
	}

	/**
	 * 上传单个文件
	 * 
	 * @param file
	 *            要上传的文件
	 * @param remoteFile
	 *            上传到服务器上要保存的完整文件名路径
	 */
	public void upload(File file, String remoteFile) {
		try {
			createDir(remoteFile.substring(0, remoteFile.lastIndexOf("/")));
			this.sftpClient.put(new FileInputStream(file), remoteFile);
			log.info("upload remoteFile:"+remoteFile);
		} catch (FileNotFoundException e) {
			log.info("file publish fail!", e);
			e.printStackTrace();
		} catch (SftpException e) {
			log.info("file publish fail!", e);
			e.printStackTrace();
		}
	}

	/**
	 * 批量上传文件
	 * 
	 * @param absolutePath
	 *            上传文件的路径
	 * @param remotePath
	 *            上传文件的存放路径
	 * @throws SftpException
	 */
	public void uploadFiles(String localDir, String remoteDir)
			throws SftpException {
		File file = new File(localDir);
		if (file.exists()) {
			if (file.isFile()) {

				upload(file, remoteDir);
			} else if (file.isDirectory()) {

				createDir(remoteDir);

				File[] files = file.listFiles();
				if (files != null && files.length > 0) {
					for (File f : files) {

						if (f != null && f.exists()) {
							uploadFiles(f.getAbsolutePath(), remoteDir + "/"
									+ f.getName());
						}

					}
				}

			}
		} else {
			log.info("File path not exists!");
		}
	}

	/**
	 * 创建文件路径
	 * 
	 * @param filepath
	 * @throws SftpException
	 */
	private void createDir(String filepath) throws SftpException {
		log.info("=========== createDir ==========" +filepath);
		String[] tokens = filepath.split("/");
		tokens[1] = "/" + tokens[1];
		for (int i = 1; i < tokens.length; i++) {
			if ("" != tokens[i] && tokens[i].length() > 0) {
				try {
					sftpClient.cd(tokens[i]);
				} catch (SftpException e) {
					sftpClient.mkdir(tokens[i]);
					sftpClient.cd(tokens[i]);
				}
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径
	 * @return
	 */
	public boolean deleteFile(String filePath) {
		try {
			sftpClient.rm(filePath);
			return true;
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除空文件夹
	 * 
	 * @param dirPath
	 * @return
	 */
	public boolean deleteDir(String dirPath) {
		try {
			sftpClient.rmdir(dirPath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 下载单个文件
	 * 
	 * @param remotePath
	 *            要下载的文件路径
	 * @param filePath
	 *            指定文件存在路径
	 */
	public void download(String remotePath, String filePath) {
		try {
			sftpClient.get(remotePath, filePath);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量下载文件(未完成)
	 * 
	 * @param remotePath
	 * @param filePath
	 */
	@SuppressWarnings("unchecked")
	public void downloadFiles(String remotePath, String filePath) {
		try {
			Vector v = sftpClient.ls(remotePath);
			Iterator it = v.iterator();
			while (it.hasNext()) {

			}
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	private class MyUserInfo implements UserInfo {

		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return password;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public boolean promptYesNo(String message) {
			return true;
		}

		public void showMessage(String message) {
			// nothing
		}

		public void setPassword(String password) {
			this.password = password;
		}

		private String password;

	}

}