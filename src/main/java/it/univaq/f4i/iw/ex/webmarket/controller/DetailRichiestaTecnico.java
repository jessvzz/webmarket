package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
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
public class DetailRichiestaTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int user) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        
        String source = request.getParameter("source");
        request.setAttribute("source", source);

        request.setAttribute("page_title", "Detail richiesta");

        //Recupero la key passata come parametro
        int richiesta_key = Integer.parseInt(request.getParameter("n"));

        //Recupero la richiesta usando la key
        RichiestaOrdine richiesta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiestaOrdine(richiesta_key);
        request.setAttribute("richiesta", richiesta);
        
        List<CaratteristicaRichiesta> caratteristiche = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCaratteristicaRichiestaDAO().getCaratteristicaRichiestaByRichiesta(richiesta_key);
                request.setAttribute("caratteristiche", caratteristiche);
        
        res.activate("detailrichiesta_tecnico.ftl.html", request, response);

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
        action_default(request, response, userId);

    } catch (IOException | TemplateManagerException ex) {
        handleError(ex, request, response);
    } catch (DataException ex) {
            Logger.getLogger(DetailRichiestaTecnico.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    @Override
    public String getServletInfo() {
        return "Servlet per la visualizzazione dei dettagli di una richiesta da evadere";
    }

}