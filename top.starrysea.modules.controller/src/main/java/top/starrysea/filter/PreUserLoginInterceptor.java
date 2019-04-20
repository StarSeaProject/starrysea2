package top.starrysea.filter;

import static top.starrysea.common.Const.USER_SESSION_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class PreUserLoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute(USER_SESSION_KEY) == null) {
			int endIndex = request.getRequestURL().length() - request.getRequestURI().length() + 1;
			String url = request.getRequestURL().substring(0, endIndex);
			// 如果当前的请求是https请求,那么就要把url中的http改成https,否则nginx会报400
			if (request.getServerPort() == 443) {
				url = url.replace("http", "https");
			}
			response.sendRedirect(url + "login");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 什么都不做
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 什么都不做
	}
}
