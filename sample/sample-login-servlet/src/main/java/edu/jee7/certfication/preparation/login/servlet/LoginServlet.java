package edu.jee7.certfication.preparation.login.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("login");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print("hallo");
		response.getWriter().close();
	}
}
