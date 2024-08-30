package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.OrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.TipologiaUtente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.UtenteImpl;
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

/**
 *
 * @author Giulia Di Flamminio
 */
public class DetailPropostaTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(n);
        request.setAttribute("proposta", proposta);

        request.setAttribute("page_title", "Detail proposta");
        res.activate("detailproposta_tecnico.ftl.html", request, response);
    }
    
  
    private void action_sendOrdine(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(n);
        Ordine ordine = new OrdineImpl();
        ordine.setProposta(proposta);
        ordine.setStato(StatoOrdine.IN_ATTESA);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordine);
        

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
    try {
        HttpSession session = SecurityHelpers.checkSession(request);
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));

        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action != null && action.equals("inviaOrdine")) {
            action_sendOrdine(request, response, n);
        } else {
            action_default(request, response, n);
        }

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(DetailPropostaTecnico.class.getName()).log(Level.SEVERE, null, ex);
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