/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import businesslogic.AccountService;
import businesslogic.ResetPasswordService;
import domainmodel.PasswordChangeRequest;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 725899
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/ResetPasswordServlet"})
public class ResetPasswordServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String uuid = (String) request.getParameter("uuid");
        
        //is UUID parameter sent?
        if (uuid != null) {
            ResetPasswordService rps = new ResetPasswordService();
            PasswordChangeRequest pcr = rps.get(uuid);

            //correct UUID entry found in database?
            if (pcr != null) {
                Date date = new Date();
                Long milisec = date.getTime() - pcr.getTime().getTime();

                //is request is within 10 minutes?
                if (milisec < 1000*60*10) {
                    request.setAttribute("uuid", uuid);
                    request.getRequestDispatcher("/WEB-INF/resetNewPassword.jsp").forward(request, response);
                    return;
                }
            }
            
            response.sendRedirect("login?status=4");
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = (String) request.getParameter("action");
        String uuid = (String) request.getParameter("uuid");
        String password = (String) request.getParameter("password");
        int status = 0;
        
        //is "new password" FORM submitted?
        if (action != null && action.equals("newPassword")) {
            //get username by UUID
            ResetPasswordService rps = new ResetPasswordService();
            PasswordChangeRequest pcr = rps.get(uuid);
            String username = pcr.getOwner().getUsername();
            
            //change password
            AccountService as = new AccountService();
            as.changePassword(username, password);
            
            //delete PasswordChangeRequest record
            rps.delete(uuid);
            
            response.sendRedirect("login?status=3");
            return;
        }
        else {
            String email = (String) request.getParameter("email");
            String url = request.getRequestURL().toString();

            //generate UUID and send and email with it
            String path = getServletContext().getRealPath("/WEB-INF");
            AccountService as = new AccountService();
            status = as.resetPassword(email, path, url);
        }
    
        response.sendRedirect("login?status=" + status);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
