package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
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
import java.util.List;

public class Ordini extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response,int user) throws IOException, ServletException, TemplateManagerException,DataException  {
      
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Storico Ordini");

        //creo un nuovo dao che continete una lista di ordini
         request.setAttribute("ordini", ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdiniByUtente(user));
         request.setAttribute("proposte", ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getProposteByUtente(user)); 
         res.activate("ordini.ftl.html", request, response);
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
            
        action_default(request, response, userId);

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
        catch (DataException ex) {
            Logger.getLogger(RichiesteOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Storico Ordini servlet";
    }
}
