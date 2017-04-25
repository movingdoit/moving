package org.base.component.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (c) ZOSHOW NETWORK TECHNOLOGY CO., LTD.
 * 
 * @createDate: 2011-11-15 下午05:20:55
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @version 1.0
 */
public class IPUtils {

	/**
	 * 获得客户端真实IP
	 * @param request
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-11-1上午09:54:13
	 */
	public static String getClientIp(HttpServletRequest request) {
		if (request == null) { // 如果request为空的话，返回本机IP，
			return "127.0.0.1";
		}

		String ipStr = request.getHeader("X-Cluster-Client-Ip");
		if (ipStr == null || ipStr.trim().length() == 0 || "unknown".equalsIgnoreCase(ipStr)) {
			ipStr = request.getHeader("X-Forwarded-For");
		}

		if (ipStr != null && !"".equals(ipStr.trim())) {

			if (ipStr.indexOf(",") < 0) {
				return ipStr;
			}
			String[] ips = ipStr.split(",");
			for (String ip : ips) { // 第一个IP就是反向代理的真实客户端IP
				if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
					continue;
				}
				return ip;
			}
		}
		return request.getRemoteAddr(); // 没有代理则返回
	}
	
	// ----------------------------------获取本机IP----------------------------------
	public static Collection<InetAddress> getAllHostAddress() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			Collection<InetAddress> addresses = new ArrayList<InetAddress>();

			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					addresses.add(inetAddress);
				}
			}

			return addresses;
		} catch (SocketException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static Collection<String> getAllNoLoopbackAddresses() {
		Collection<String> noLoopbackAddresses = new ArrayList<String>();
		Collection<InetAddress> allInetAddresses = getAllHostAddress();

		for (InetAddress address : allInetAddresses) {
			if (!address.isLoopbackAddress()) {
				noLoopbackAddresses.add(address.getHostAddress());
			}
		}

		return noLoopbackAddresses;
	}

	public static String getFirstNoLoopbackAddress() {
		Collection<String> allNoLoopbackAddresses = getAllNoLoopbackAddresses();
		// Assert.isTrue(!allNoLoopbackAddresses.isEmpty(),
		// " Sorry, seems you don't have a network card :( ");
		return allNoLoopbackAddresses.iterator().next();
	}

	/**
	 * 获取本机地址
	 * 
	 * @return
	 */
	public static String getLocalIp() throws Exception {
		if (isWindowsOS()) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				String ipstr = addr.getHostAddress().toString();// 获得本机IP
				return ipstr;
			} catch (UnknownHostException e) {
				System.out.println(e);
				throw new Exception(e);
			}
		} else {
			Collection<String> ips = getAllNoLoopbackAddresses();
			if (ips != null && !ips.isEmpty()) {
				return ips.iterator().next();
			} else {
				throw new Exception("取不到IP");
			}
		}
	}

	/*
	 * @return true---是Windows操作系统
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
	
	public static void main(String[] args) throws Exception{
		
		Collection<InetAddress> allHostAddress = getAllHostAddress();
		System.out.println(getLocalIp());
	}
}
