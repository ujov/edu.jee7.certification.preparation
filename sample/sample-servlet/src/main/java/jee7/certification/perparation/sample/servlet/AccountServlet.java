package jee7.certification.perparation.sample.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/account", "/accountNg"}, initParams = {@WebInitParam(name=AccountServlet.PARAMTER_ACCOUNT_NUMBER, value="1")})
public class AccountServlet extends HttpServlet {
	
	protected static final String PARAMTER_ACCOUNT_NUMBER = "accountNumber";
	
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AccountServlet() {

    }
    
    @Override
    public void init(ServletConfig config) {
    	System.out.println("Default-" + PARAMTER_ACCOUNT_NUMBER + ": ");
        System.out.println(config.getInitParameter(PARAMTER_ACCOUNT_NUMBER));
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath()).append("Account number: ").append(request.getParameter(PARAMTER_ACCOUNT_NUMBER));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
