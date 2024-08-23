package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
// import it.univaq.f4i.iw.ex.webmarket.data.model.Notifica;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class NotificheOrdinante extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Notifiche Utente");
        res.activate("notifiche_ordinante.ftl.html", request, response);
    }
        @Override
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException {
    
            try {
                HttpSession session = SecurityHelpers.checkSession(request);
            if (session == null) {
                
                response.sendRedirect("login");
                return;
    
            } 
             // Recupero l'ID dell'utente dalla sessione
             int userId = (int) session.getAttribute("userid");
             Utente u = ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(userId);
            
             if (u != null) {
              // Passa l'utente alla vista
                            request.setAttribute("user", u);
             }
            
             action_default(request, response);
    
             }
            
            catch (IOException | TemplateManagerException | DataException ex) {
                handleError(ex, request, response);
            }
        }
    
      
    @Override
    public String getServletInfo() {
        return "Notifiche Utente servlet";
    }
}
