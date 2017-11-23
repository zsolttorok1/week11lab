/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import businesslogic.AccountService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String status = request.getParameter("status");
        
        if (action != null && action.equals("resetPassword")) {
            response.sendRedirect("resetPassword");
            return;
        }
        
        if (request.getParameter("logout") != null) {
            HttpSession session = request.getSession();
            session.invalidate();
            request.setAttribute("errormessage", "logged out");
        }
        
        //status 0 = default
        //status 1 = OK: user's email found in database and email sent.
        //status 2 = Error: email not found in database.
        //status 3 = OK: password successfuly changed.
        //status 4 = Error: request expired
     
        if (status != null && status.equals("1"))
            request.setAttribute("errormessage", "Password reset request link sent to the provided email address.");
        else if (status != null && status.equals("2"))
            request.setAttribute("errormessage", "Error: provided email address not found in database.");
        else if (status != null && status.equals("3"))
            request.setAttribute("errormessage", "Password successfuly changed! You may login with the new one.");
        else if (status != null && status.equals("4"))
            request.setAttribute("errormessage", "Error: This password reset request is expired or never existed. You may make a new one.");
                       
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("errormessager", "fill in both fields, please");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp/").forward(request, response);
        }
        
        AccountService as = new AccountService();
        
        String path = getServletContext().getRealPath("/WEB-INF");
        
        if (as.checkLogin(username, password, path) != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect("home");
        } else {
            request.setAttribute("errormessager", "invalid login");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp/").forward(request, response);
        }
    }
}
