package org.base.component.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.base.component.utils.Message;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.alibaba.fastjson.JSONObject;

/**
 * 异常拦截器
 * 
 * @author: wangbh
 * @date:
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {
	private Properties jsonExceptionMappings;

	/**
	 * json类返回操作，异常，指定异常信息
	 */
	public void setJsonExceptionMappings(Properties jsonExceptionMappings) {
		this.jsonExceptionMappings = jsonExceptionMappings;
	}

	/**
	 * 当请求是ajax请求是，返回json错误信息。当为页面请求时，使用SimpleMappingExceptionResolver的处理机制
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error(ex.getMessage(), ex);
		if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Class returnType = method.getReturnType();
			if (!returnType.equals(ModelAndView.class)) {
				return handleResponseBody(response, ex);
			}
		}
		return super.doResolveException(request, response, handler, ex);
	}

	/**
	 * 当请求是ajax请求是，返回json错误信息
	 */
	protected ModelAndView handleResponseBody(HttpServletResponse response, Exception ex) {
		String jsonMsg = getJsonMsg(ex);
		try {
			response.getWriter().write(jsonMsg);
			response.getWriter().close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(e,"返回错误信息出错");
		}
		return null;
	}

	/**
	 * 获取错误提示信息
	 */
	protected String getJsonMsg(Exception ex) {
		Message<Object> msg = new Message<Object>();
		msg.setCode("-1");
		// 如果异常为系统自定义异常,则返回异常信息
		if (ex instanceof BusinessException) {
			msg.setMsg(ex.getMessage());
		} else {
			String errorMessage = findMatchingErrorMessage(ex);
			// 如果没有指定异常提示信息,则提示异常自带的信息,如果指定了，则返回指定的异常信息
			if (errorMessage != null) {
				msg.setMsg(ex.getMessage());
			} else {
				msg.setMsg(ex.getMessage());
			}
		}
		return JSONObject.toJSONString(msg);
	}

	/**
	 * 查找xml配置文件中指定的该异常的提示错误信息,如果没指定，则返回null
	 */
	protected String findMatchingErrorMessage(Exception ex) {
		if (jsonExceptionMappings != null) {
			return findMatchingViewName(jsonExceptionMappings, ex);
		} else {
			return null;
		}

	}
}
