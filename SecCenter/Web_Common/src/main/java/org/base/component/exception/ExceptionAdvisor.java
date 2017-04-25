package org.base.component.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @ClassName: ExceptionAdvisor 
 * @Description: 由Spring AOP调用 输出异常信息，把程序异常抛向业务异常
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-4-7 下午1:01:29
 *
 */
public class ExceptionAdvisor implements ThrowsAdvice {
	public void afterThrowing(Method method, Object[] args, Object target, Exception exception) throws Throwable {
		
		Locale zhLoc=new Locale("zh","CN");//表示中文地区
		ResourceBundle resourceBundle = ResourceBundle.getBundle("friendlyErrMsg",zhLoc);
		
		outputLog(method, args, target, exception);
		exceptionJudgment(exception, resourceBundle);

	}
	
	/**
	 * @Title: outputLog 
	 * @Description: 在后台中输出错误异常异常信息，通过log4j输出。
	 * @param method
	 * @param args
	 * @param target
	 * @param exception  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-4-7下午2:04:38
	 */
	private void outputLog(Method method, Object[] args, Object target, Exception exception){
		Logger log = Logger.getLogger(target.getClass());
		log.info("**************************************************************");
		log.info("Error happened in class: " + target.getClass().getName());
		log.info("Error happened in method: " + method.getName());
		for (int i = 0; i < args.length; i++) {
			log.info("arg[" + i + "]: " + args[i]);
		}
		log.info("Exception class: " + exception.getClass().getName());
		log.info("exception.getMessage():" + exception.getMessage());
		exception.printStackTrace();
		log.info("**************************************************************");
	}
	
	/**
	 * @Title: exceptionJudgment 
	 * @Description: 在这里判断异常，根据不同的异常返回错误。
	 * @param exception
	 * @param resourceBundle  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-4-7下午1:07:00
	 */
	private void exceptionJudgment(Exception exception, ResourceBundle resourceBundle){
		if (exception.getClass().equals(DataAccessException.class)) {
			exception.printStackTrace();
			throw new BusinessException("data.access.exception",resourceBundle);
		} else if (exception.getClass().toString().equals(NullPointerException.class.toString())) {
			exception.printStackTrace();
			throw new BusinessException("null.pointer.exception",resourceBundle);
		} else if (exception.getClass().equals(IOException.class)) {
			exception.printStackTrace();
			throw new BusinessException("IO.exception",resourceBundle);
		} else if (exception.getClass().equals(ClassNotFoundException.class)) {
			exception.printStackTrace();
			throw new BusinessException("class.notFound.exception",resourceBundle);
		} else if (exception.getClass().equals(ArithmeticException.class)) {
			exception.printStackTrace();
			throw new BusinessException("arithmetic.exception",resourceBundle);
		} else if (exception.getClass().equals(ArrayIndexOutOfBoundsException.class)) {
			exception.printStackTrace();
			throw new BusinessException("arrayIndexception.outOfBounds.exception",resourceBundle);
		} else if (exception.getClass().equals(IllegalArgumentException.class)) {
			exception.printStackTrace();
			throw new BusinessException("illegal.argument.exception", resourceBundle);
		} else if (exception.getClass().equals(ClassCastException.class)) {
			exception.printStackTrace();
			throw new BusinessException("class.cast.exception",resourceBundle);
		} else if (exception.getClass().equals(SecurityException.class)) {
			exception.printStackTrace();
			throw new BusinessException("security.exception",resourceBundle);
		} else if (exception.getClass().equals(SQLException.class)) {
			exception.printStackTrace();
			throw new BusinessException("class.notFound.exception",resourceBundle);
		} else if (exception.getClass().equals(NoSuchMethodError.class)) {
			exception.printStackTrace();
			throw new BusinessException("nosuch.method.error",resourceBundle);
		} else if (exception.getClass().equals(InternalError.class)) {
			exception.printStackTrace();
			throw new BusinessException("internal.error",resourceBundle);
		} else {
			exception.printStackTrace();
			throw new BusinessException("program.internal.error"+ exception.getMessage(),resourceBundle);
		}
	}
}
