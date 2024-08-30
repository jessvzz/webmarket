package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RichiesteTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int user) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Richieste Tecnico");

        //creo un nuovo dao che contenente una lista di richieste non ancora evase (stato: PRESA_IN_CARICO e tecnico=tecnicoId)
        request.setAttribute("richieste", ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiesteNonEvase(user));

        res.activate("richieste_tecnico.ftl.html", request, response);
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

        // Recupero l'ID del tecnico dalla sessione
        int tecnicoId = (int) session.getAttribute("userid");
        
        action_default(request, response, tecnicoId);

    } catch (IOException | TemplateManagerException ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(RichiesteTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    //Descrizione servlet
    @Override
    public String getServletInfo() {
        return "Servlet per la gestione delle richieste prese in carico dal tecnico ma non ancora evase";
    }
}