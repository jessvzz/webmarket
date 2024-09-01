package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
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

public class DettaglioOrdineOrd extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response,int user) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Dettaglio ordine ordinante");
        int ordineId = Integer.parseInt(request.getParameter("n"));
        // Recupero l'ordine dal database utilizzando il DAO
        request.setAttribute("ordine", ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(ordineId));
        System.out.println(ordineId);
        res.activate("dettaglio_ordine_ord.ftl.html", request, response);
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
        String action = request.getParameter("action");
        action_default(request, response, userId);

    } catch (IOException | TemplateManagerException  ex) {
        handleError(ex, request, response);
    }
    catch (DataException ex) {
        Logger.getLogger(Ordini.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Dettaglio ordine ordinante servlet";
    }// </editor-fold>

}