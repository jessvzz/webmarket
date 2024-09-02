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
import java.sql.Date;
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
 * @author Gea Viozzi
 */
public class DetailStoricoTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Detail storico");
        
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);
        System.out.println("proposta: " + ordine.getProposta().getCodice());
        
        request.setAttribute("ordine", ordine);
        
        res.activate("detailstorico_tecnico.ftl.html", request, response);
    }

     private void action_sendOrdine(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);
        
        String email = ordine.getProposta().getRichiestaOrdine().getUtente().getEmail();

        ordine.setStato(StatoOrdine.IN_ATTESA);
        ordine.setData(new Date(System.currentTimeMillis()));
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordine);
        
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
        String text = "Gentile Utente, la informiamo che è stato effettuato un nuovo ordine per la sua proposta numero " + ordine.getProposta().getCodice() +"\n\n In allegato trova i dettagli del suo ordine.";
        // genero PDF
        String tipo = "OrdineProposta_";
        String messaggio = "\n Dettagli dell'ordine effettuato per la proposta numero: "+ ordine.getProposta().getCodice()+"\n\n";
        String pdfFilePath = "OrdineProposta_" + ordine.getProposta().getCodice() + ".pdf";

        try {
            EmailSender.createPDF(tipo, messaggio, ordine.getProposta());

           
            EmailSender.sendEmailWithAttachment(session, email, "Notifica Ordine", text, pdfFilePath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("storico_tecnico"); 
        

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
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));
        
        String action = request.getParameter("action");
       if (action != null && action.equals("reinviaOrdine")) {
            action_sendOrdine(request, response, n);
        } else {
            action_default(request, response, n);
        }

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(DetailStoricoTecnico.class.getName()).log(Level.SEVERE, null, ex);
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