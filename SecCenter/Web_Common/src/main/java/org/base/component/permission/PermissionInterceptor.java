package org.base.component.permission;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.base.component.utils.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 权限过滤拦截器
 * 
 * @ClassName: PrivilegeInterceptor
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-4-22 下午2:58:24
 * 
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	/** 平台、供应商端未登录跳转地址 **/
	private static final String notLogin = "/bizmgt/system/main/tologin.ihtml";
	/** 没有URL访问权限跳转地址 **/
	private static final String notPermission = "/bizmgt/system/main/noPrivilege.ihtml";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			HttpSession session = request.getSession();
			String url = request.getServletPath();
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method m = handlerMethod.getMethod();
			Object obj = handlerMethod.getBean();
			Annotation anno = null;
			Privilege p = null;
			for (Annotation annotation : m.getAnnotations()) {
				if ("org.meibaobao.ecos.bizmgt.common.Privilege".equals(annotation
						.annotationType().getName())) {
					anno = annotation;
					p = m.getAnnotation(Privilege.class);
					break;
				}
			}
			if (null == anno) {
				for (Annotation annotation : obj.getClass().getAnnotations()) {
					if ("org.meibaobao.ecos.bizmgt.common.Privilege"
							.equals(annotation.annotationType().getName())) {
						anno = annotation;
						p = obj.getClass().getAnnotation(Privilege.class);
						break;
					}
				}
			}
			if (null != p) {
				boolean pflag = false;
				String pri = (String) request.getSession().getAttribute(
						PermissionConstant.SYSTEM_PRIVILEGE);
				// 用户是否登陆
				if (StringUtils.isExist(pri)
						&& PermissionConstant.PRIVILEGE_LOGIN.equals(pri)) {
					// 判断当前访问路径是否有权限(防恶意访问资源)
					@SuppressWarnings("unchecked")
					List<String> resUrlListStr = (List<String>) session
							.getAttribute(PermissionConstant.USER_RES_URL_LIST);
					if (url.endsWith("mainFrame.ihtml")) {
						return true;
					}
					boolean type = false;
					if (resUrlListStr != null && resUrlListStr.size() > 0) {
						
						for (String resUrl : resUrlListStr) {
							// 判断请求URL是否与资源URL匹配
							if (resUrl.indexOf(url) >= 0) {
								type = true;
							}
						}
						if (!type) {
							response.sendRedirect(request.getContextPath()
									+ notPermission);
							return false;
						}
					} else if (resUrlListStr != null
							&& resUrlListStr.size() == 0) {
						response.sendRedirect(request.getContextPath()
								+ notPermission);
						return false;
					}
					pflag = true;
				}
				if (!pflag) {
					response.sendRedirect(request.getContextPath() + notLogin);
					return false;
				}
			}
		} catch (Exception e) {
			// 异常处理
			return false;
		}
		return true;
	}

}
