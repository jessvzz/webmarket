package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.OrdineDAO;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
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
public class StoricoTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int tecnico) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Storico Tecnico");

        // Recupera gli ordini associati al tecnico loggato
        OrdineDAO ordineDAO = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO();
        List<Ordine> ordini = ordineDAO.getOrdiniByTecnico(tecnico);
        request.setAttribute("ordini", ordini);

        res.activate("storico_tecnico.ftl.html", request, response);
    }

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
    }    catch (DataException ex) {
            Logger.getLogger(StoricoTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    @Override
    public String getServletInfo() {
        return "Servlet per la visualizzazione e gestione dello storico ordini del tecnico";
    }

}