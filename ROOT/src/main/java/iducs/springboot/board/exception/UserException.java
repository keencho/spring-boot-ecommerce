package iducs.springboot.board.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import iducs.springboot.board.domain.Section;
import iducs.springboot.board.service.SectionService;
import iducs.springboot.board.utils.HttpSessionUtils;

public class UserException extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();

		if ("/404".equals(request.getRequestURI())) {
			return true;
		}
		if (session.getAttribute("user") != null) {
			response.sendRedirect("/404");
			return false;
		}
		return true;
	}
}