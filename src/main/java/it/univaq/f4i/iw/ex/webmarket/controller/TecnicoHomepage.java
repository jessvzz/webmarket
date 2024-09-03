/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jessviozzi
 */
public class TecnicoHomepage extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Tecnico Dashboard");
        
        boolean r = ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().esisteRichiestaInAttesa();
        request.setAttribute("richieste_in_attesa", r);
        
        
        boolean richiestePreseInCarico = !((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiesteNonEvase(userId).isEmpty();
        request.setAttribute("richieste_prese_in_carico", richiestePreseInCarico);
        
        boolean proposte = !((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getProposteByTecnico(userId).isEmpty();
        request.setAttribute("proposte", proposte);
        
        boolean ordini = !((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdiniByTecnico(userId).isEmpty();
        request.setAttribute("ordini", ordini);


        res.activate("homepagetecnico.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
        HttpSession session = SecurityHelpers.checkSession(request);
        if (session == null) {
            
            // Se la sessione non è valida, torno login
            response.sendRedirect("login");
            return;
        }
        
        // trovo user
        int userId = (int) session.getAttribute("userid");
        Utente u = ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(userId);
        
        if (u != null) {
            request.setAttribute("user", u);
        }

        action_default(request, response, userId);

    } catch (IOException | TemplateManagerException | DataException ex) {
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
        return "Main Newspaper servlet";
    }// </editor-fold>
    
}
