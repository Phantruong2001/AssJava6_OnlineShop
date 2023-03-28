package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.User;
import com.example.service.CookieService;
import com.example.service.SessionService;

@Service
public class AuthInterceptor implements HandlerInterceptor {
	@Autowired
	SessionService session;

	@Autowired
	CookieService cookie;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("---------------------preHandle---------------------");
		String uri = request.getRequestURI();
		User user = session.get("user");
		String error = "";
		if (user == null)
			error = "Please login!";
		else if (!user.isRole())
			error = "Access denied!";
		System.out.println(uri + "+" + user + "+" + error);
		if (error.length() > 0) {
			session.set("security-uri", uri);
			response.sendRedirect("/accountpage/login?error=" + error);
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView mv)
			throws Exception {
		System.out.println("---------------------postHandle---------------------");
		System.out.println(cookie.getValue("email") + "+" + cookie.getValue("role") + "+" + session.get("user"));
	}
}
