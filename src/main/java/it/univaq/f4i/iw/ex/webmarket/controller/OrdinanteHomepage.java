package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class OrdinanteHomepage extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Ordinante Dashboard");
        
        //notifiche per proposte
        boolean proposte = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().notificaProposteOrd(userId);
        System.out.println(proposte);
        request.setAttribute("proposte", proposte);
        
        //notifiche per ordini 
        boolean ordini = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().notificaOrdineOrd(userId);
        request.setAttribute("ordini", ordini);

        
        res.activate("homepageordinante.ftl.html", request, response);
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
            request.setAttribute("user", u);
         }
        
         action_default(request, response, userId);

         }
        
        catch (IOException | TemplateManagerException | DataException ex) {
            handleError(ex, request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Ordinante Homepage servlet";
    }

}