package it.univaq.f4i.iw.ex.webmarket.controller;

import com.itextpdf.text.DocumentException;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.OrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.TipologiaUtente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.UtenteImpl;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DettaglioPropostaOrd extends BaseController {
    private void action_default(HttpServletRequest request, HttpServletResponse response, int user) throws IOException, ServletException, TemplateManagerException,DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Dettaglio proposta ordinante");

        int proposta_key = Integer.parseInt(request.getParameter("n"));
        System.out.println("ID Proposta: " + proposta_key);

        // request.setAttribute("proposta", ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(proposta_key));
        // System.out.println("Proposta Recuperata");

        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(proposta_key);
        System.out.println("Proposta Recuperata: " + proposta);
        request.setAttribute("proposta", proposta);

        res.activate("dettaglio_proposta_ord.ftl.html", request, response);
    }
    
    private void action_accettaProp(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException,DataException {
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));
        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(n);
        proposta.setStatoProposta(StatoProposta.ACCETTATO);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().storePropostaAcquisto(proposta);
        String email = proposta.getRichiestaOrdine().getTecnico().getEmail();
        String username = proposta.getRichiestaOrdine().getTecnico().getUsername();

        
        //gestione email
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.outlook.com"); 
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("webmarket.univaq@outlook.com", "geagiuliasamanta1");
            }
        });
        String text = "Ciao "+username+",\n La informiamo che la sua proposta numero " + proposta.getCodice() +"è stata ACCETTATA. In allegato trova i dettagli della proposta.";
        // genero PDF
        String messaggio = "\n Dettagli dell'ordine effettuato per la proposta numero: "+ proposta.getCodice()+"\n\n";
        String pdfFilePath = "OrdineProposta_" + proposta.getCodice() + ".pdf";

        try {
            EmailSender.createPDF(messaggio, proposta);

           
            EmailSender.sendEmailWithAttachment(session, email, "Notifica Proposta", text, pdfFilePath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("proposte_ordinante"); 
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
  //ho aggiunto id perchè dobbiamo filtrare le richieste che ha fatto l'utente interessato
  //non ti serve l'id, da modificare
  
  String action = request.getParameter("action");
        if (action != null && action.equals("accettaProposta")) {
            action_accettaProp(request, response);
        } else{
   
        action_default(request, response, userId);
        }
    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    } catch (DataException ex) {
            Logger.getLogger(RichiesteOrdinante.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Dettaglio proposta ordinante servlet";
    }// </editor-fold>

}