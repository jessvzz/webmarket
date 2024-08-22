/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


/**
 *
 * @author jessviozzi
 */
public class GestioneUtenti extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Gestione Utenti");
        res.activate("gestione_utenti.ftl.html", request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
    try {
        HttpSession session = SecurityHelpers.checkSession(request);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action != null && action.equals("createUser")) {
            action_createUser(request, response);
        } else {
            action_default(request, response);
        }

    } catch (IOException | TemplateManagerException | DataException ex) {
        handleError(ex, request, response);
    }
}

    private void action_createUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataException, TemplateManagerException {
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String password = request.getParameter("temp-password");
    String confirmPassword = request.getParameter("confirm-password");
    String roleParam = request.getParameter("role");
    System.out.println("user: " + username);
    
    if (username == null || email == null || password == null || confirmPassword == null || roleParam == null) {
        request.setAttribute("error", "Tutti i campi sono obbligatori.");
        action_default(request, response);
        return;
    }

    if (!password.equals(confirmPassword)) {
        request.setAttribute("error", "Le password non coincidono.");
        action_default(request, response);
        return;
    }

    TipologiaUtente role = TipologiaUtente.valueOf(roleParam.toUpperCase());

    Utente nuovoUtente = new UtenteImpl();
    nuovoUtente.setUsername(username);
    nuovoUtente.setEmail(email);
    nuovoUtente.setPassword(password);
    nuovoUtente.setTipologiaUtente(role);

    ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(nuovoUtente);
    try {
              String subject = "Benvenuto in WebMarket";
              String body = "Ciao " + username + ",\n\n" +
                            "Ecco le tue credenziali per accedere:\n" +
                            "Username: " + username + "\n" +
                            "Password temporanea: " + password + "\n\n" +
                            "Ti consigliamo di cambiare la tua password al primo accesso.\n\n";
              EmailSender.sendEmail(email, subject, body);
              request.setAttribute("success", "Utente creato con successo e email inviata!");
          } catch (Exception e) {
              request.setAttribute("error", "Utente creato con successo, ma si è verificato un problema durante l'invio dell'email.");
              e.printStackTrace();
          }
    request.setAttribute("success", "Utente creato con successo!");
    action_default(request, response);
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