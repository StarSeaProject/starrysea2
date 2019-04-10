package top.starrysea.filter;

import static top.starrysea.common.Const.INFO;
import static top.starrysea.common.Const.USER_SESSION_KEY;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import top.starrysea.common.Common;

public class PreUserLoginForAjaxInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Map<String, Object> theResult = new HashMap<>();
		if (session.getAttribute(USER_SESSION_KEY) == null) {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			theResult.put(INFO, "您还未登录，请登录");// 暂用于测试，之后再修改
			out.print(Common.toJson(theResult));
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
