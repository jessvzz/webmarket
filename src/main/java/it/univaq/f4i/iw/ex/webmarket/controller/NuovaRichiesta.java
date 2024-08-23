package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class NuovaRichiesta extends BaseController {

    // Metodo per gestire l'azione di default
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Nuova Richiesta Ordine");
        res.activate("nuova_richiesta.ftl.html", request, response);
    }

    // Metodo per processare le richieste
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession session = SecurityHelpers.checkSession(request);
            if (session == null) {
                response.sendRedirect("login");
                return;
            }
            
            String action = request.getParameter("action");
            action_default(request, response);
    
        } catch (IOException | TemplateManagerException /* | DataException */ ex) {
            handleError(ex, request, response);
        }
    }
    @Override
    public String getServletInfo() {
        return "Controller per la pagina Nuova Richiesta Ordine";
    }
}
