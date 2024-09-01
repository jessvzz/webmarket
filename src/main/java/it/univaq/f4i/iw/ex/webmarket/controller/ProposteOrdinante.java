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

public class ProposteOrdinante extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int user) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Proposte Ordinante");

        
        //creo un nuovo dao che contenente una lista di proposte ricevute dall'ordinante
        request.setAttribute("proposte", ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getProposteByUtente(user));

        res.activate("proposte_ordinante.ftl.html", request, response);
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
        int userId = (int) session.getAttribute("userid");
        
        //ho aggiunto id perchè dobbiamo filtrare le richieste che ha fatto l'utente interessato
        action_default(request, response, userId);

    } catch (IOException | TemplateManagerException ex) {
        handleError(ex, request, response);
    }    catch (DataException ex) {
            Logger.getLogger(RichiesteOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    // Descrizione della servlet
    @Override
    public String getServletInfo() {
        return "Servlet per le proposte ricevute dall'ordinante";
    }

}