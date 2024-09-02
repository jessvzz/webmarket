package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.TipologiaUtente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.UtenteImpl;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giulia Di Flamminio
 */
public class ProfiloOrdinante extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Profilo Ordinante");
        res.activate("profilo_ordinante.ftl.html", request, response);
    }
    
     private void action_update(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, ServletException, TemplateManagerException, DataException, NoSuchAlgorithmException, InvalidKeySpecException {
         
        String current = request.getParameter("current-password");
        String newP = request.getParameter("new-password");
        String confirm = request.getParameter("confirm-password");
        
        //tutti i campi devono essere fatti
        if (current == null || current.trim().isEmpty() || newP == null || newP.trim().isEmpty()||confirm == null || confirm.trim().isEmpty()) {
            request.setAttribute("errore", "Tutti i campi devono essere compilati!");
            action_default(request, response);
            return; 
        }
        
        //password devono coincidere
        if (!newP.equals(confirm)) {
        request.setAttribute("errore", "Le password non coincidono.");
        action_default(request, response);
        return;
        }
        
        Utente u = ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(userId);
        
        //controllo su passwrod corrente
        if (!SecurityHelpers.checkPasswordHashPBKDF2(current, u.getPassword())){
          request.setAttribute("errore", "La password corrente è errata");
          action_default(request, response);
          return;  
        }
        
        String hashedPass = SecurityHelpers.getPasswordHashPBKDF2(newP);
        u.setPassword(hashedPass);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(u);
        
        
        response.sendRedirect("homepageordinante"); 
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
        
        int userId = (int) session.getAttribute("userid");
         Utente u = ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(userId);

        String action = request.getParameter("action");
        if (action != null && action.equals("updateProfile")){
            action_update(request, response, userId);
        }else{
            action_default(request, response);
        }
        

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(ProfiloOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ProfiloOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ProfiloOrdinante.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Profilo Ordinante servlet";
    }

}