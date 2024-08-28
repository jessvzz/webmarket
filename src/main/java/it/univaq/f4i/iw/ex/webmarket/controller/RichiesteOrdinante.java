package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RichiesteOrdinante extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int user) throws IOException, ServletException, TemplateManagerException, DataException {
        
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Richieste Ordinante");
        
        //creo un nuovo dao che continete una lista di richieste
        request.setAttribute("richieste", ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiesteByUtente(user));

        res.activate("richieste_ordinante.ftl.html", request, response);
    }


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
        
        //ho aggiunto id perchè dobbiamo filtrare le richieste che ha fatto l'utente interessato
        action_default(request, response, userId);

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(RichiesteOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        }
}
   

    // Descrizione della servlet
    @Override
    public String getServletInfo() {
        return "Servlet per la gestione delle richieste effettuate dall'ordinante";
    }
}
