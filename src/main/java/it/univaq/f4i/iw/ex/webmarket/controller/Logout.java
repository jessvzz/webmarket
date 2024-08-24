package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class Logout extends BaseController {

    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityHelpers.disposeSession(request);
        response.sendRedirect("login");
 
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_logout(request, response);
        } catch (IOException ex) {
            handleError(ex, request, response);
        }
    }    
}
